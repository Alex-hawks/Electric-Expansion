package electricexpansion.common.misc;

import java.util.HashSet;
import java.util.Set;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.grid.IElectricityNetwork;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveController;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveNetworkMember;
import electricexpansion.api.hive.IHiveSignalIO;

public class HiveNetwork implements IHiveNetwork 
{
    public class ThreadData extends Thread
    {
        private HiveNetwork network;
        private byte[] data;
        
        public ThreadData(HiveNetwork net, byte[] data )
        {
            this.network = net;
            this.data = data;
        }
        
        @Override
        public void run()
        {
            for (IHiveSignalIO[] ioArray : network.signalIOs)
            {
                for (IHiveSignalIO io : ioArray)
                {
                    if (io != null)
                    {
//                        log(Level.SEVERE, "Processing data in IO: " + io);
                        io.processData(data);
                    }
                }
            }
        }
    }
    
    public static final short byteTrick = 0b11111111;
    
    private Set<IElectricityNetwork> networks = new HashSet<IElectricityNetwork>();
    private IHiveController controller;
    
    private Set<IHiveConductor> conductors = new HashSet<IHiveConductor>();
    private Set<IHiveMachine> machines = new HashSet<IHiveMachine>();
    
    /**
     * The string is IO's type ID + ":" + IO's Network Unique ID
     * eg. 2:10
     */
    IHiveSignalIO[][] signalIOs = new IHiveSignalIO[256][256];
    
    Set<IHiveSignalIO> allRegisteredIOs = new HashSet<IHiveSignalIO>();
    
    @Override
    public Set<IElectricityNetwork> getElectricNetworks()
    {
        return networks;
    }
    
    @Override
    public void addNetwork(IElectricityNetwork network)
    {
        if (network != null && networks.add(network))
        {
            for (IConductor c : network.getConductors())
            {
                if (c instanceof IHiveConductor)
                {
                    IHiveConductor conductor = (IHiveConductor) c;
                    
                    conductor.setHiveNetwork(this, true);
                    this.addConductor(conductor);
                }
            }
        }
    }
    
    @Override
    public IHiveController getController()
    {
        return controller;
    }
    
    @Override
    public boolean setController(IHiveController newController)
    {
        if (this.controller == null)
        {
            this.controller = newController;
            return true;
        }
        else 
            return this.controller == newController;
    }
    
    @Override
    public void addConductor(IHiveConductor conductor)
    {
        this.conductors.add(conductor);
    }
    
    @Override
    public void removeConductor(IHiveConductor conductor)
    {
        this.conductors.remove(conductor);
    }
    
    @Override
    public void addMachine(IHiveMachine machine)
    {
        this.machines.add(machine);
    }
    
    @Override
    public void removeMachine(IHiveMachine machine)
    {
        this.machines.remove(machine);
    }
    
    @Override
    public Set<IHiveMachine> getMachines()
    {
        return machines;
    }
    
    @Override
    public Set<IHiveConductor> getConductors()
    {
        return conductors;
    }
    
    @Override
    public void sendData(byte[] data)
    {
        new ThreadData(this, data).start();
    }
    
    @Override
    public Byte registerIO(IHiveSignalIO io)
    {
        synchronized (this.signalIOs)
        {
            byte ioID = io.getDeviceTypeID();
            
            for (byte b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++)
            {
                if (this.signalIOs[ioID & byteTrick][b & byteTrick] == null)
                {
                    this.signalIOs[ioID & byteTrick][b & byteTrick] = io;
                    this.allRegisteredIOs.add(io);
                    
                    return b;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Byte unregisterIO(IHiveSignalIO io)
    {
        if (this.allRegisteredIOs.contains(io))
        {
            if (this.signalIOs[io.getDeviceTypeID() & byteTrick][io.getNetworkUniqueID() & byteTrick] == io)
            {
                this.signalIOs[io.getDeviceTypeID() & byteTrick][io.getNetworkUniqueID() & byteTrick] = null;
                this.allRegisteredIOs.remove(io);
            }
        }
        
        return null;
    }
    
    @Override
    public void clear()
    {
        for (IHiveNetworkMember c : this.conductors)
            c.setHiveNetwork(null, true);
        
        for (IHiveNetworkMember m : this.machines)
            m.setHiveNetwork(null, true);
        
        this.conductors.clear();
        this.machines.clear();
        this.networks.clear();
    }
    
    @Override
    public synchronized void merge(IHiveNetwork otherNetwork)
    {
        for (IHiveConductor c : otherNetwork.getConductors())
        {
            c.setHiveNetwork(this, true);
            this.addConductor(c);
        }
        
        for (IHiveMachine m : otherNetwork.getMachines())
        {
            m.setHiveNetwork(this, true);
            this.addMachine(m);
        }
        
    }
    
}