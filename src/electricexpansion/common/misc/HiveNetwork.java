package electricexpansion.common.misc;

import java.util.HashSet;
import java.util.Set;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.grid.IElectricityNetwork;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveController;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;

public class HiveNetwork implements IHiveNetwork 
{
    private Set<IElectricityNetwork> networks = new HashSet<IElectricityNetwork>();
    private IHiveController controller;
    
    private Set<IHiveConductor> conductors = new HashSet<IHiveConductor>();
    private Set<IHiveMachine> machines = new HashSet<IHiveMachine>();
    
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
    
}