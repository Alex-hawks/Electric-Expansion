package electricexpansion.common.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.grid.IElectricityNetwork;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveController;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveSignalIO;

public class HiveNetwork implements IHiveNetwork 
{
    private Set<IElectricityNetwork> networks = new HashSet<IElectricityNetwork>();
    private IHiveController controller;
    
    private Set<IHiveConductor> conductors = new HashSet<IHiveConductor>();
    private Set<IHiveMachine> machines = new HashSet<IHiveMachine>();
    
    private Map<Byte, Map<Byte, IHiveSignalIO>> signalIOs = new HashMap<>();
    
    @Override
    public Set<IElectricityNetwork> getElectricNetworks()
    {
        return networks;
    }
    
    @Override
    public void addNetwork(IElectricityNetwork network)
    {
        if (networks.add(network))
        {
            for (IConductor c : network.getConductors())
            {
                if (c instanceof IHiveConductor)
                {
                    IHiveConductor conductor = (IHiveConductor) c;
                    
                    conductor.setHiveNetwork(this, false);
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
        for (short s = 0; s < 256; s++)
        {
            if (!this.signalIOs.get(s).isEmpty())
            {
                Map<Byte, IHiveSignalIO> map = this.signalIOs.get(s);
                for (short s2 = 0; s2 < 256; s2++)
                {
                    IHiveSignalIO io = map.get(s2);
                    io.processData(data);
                }
            }
        }
    }
    
    @Override
    public Byte registerIO(IHiveSignalIO io)
    {
        byte ioID = io.getDeviceTypeID();
        
        for (short s = 0; s < 256; s++)
        {
            if (!this.signalIOs.get(ioID).keySet().contains((byte) s))
            {
                this.signalIOs.get(ioID).put((byte) s, io);
                return (byte) s;
            }
        }
        return null;
    }
    
}