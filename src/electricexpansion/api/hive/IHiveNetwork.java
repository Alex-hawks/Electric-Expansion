package electricexpansion.api.hive;

import java.util.Set;

import universalelectricity.core.grid.IElectricityNetwork;

/**
 * @author Alex_hawks
 * A Logic network used to make EE cables and machines special...
 * Use this interface only to add better compatibility with Electric Expansion
 */
public interface IHiveNetwork
{
    /**
     * @return All the Electricity networks that this Hive encompasses.
     */
    public Set<IElectricityNetwork> getElectricNetworks();
    
    public void addNetwork(IElectricityNetwork network);
    
    public IHiveController getController();
    
    /** 
     * @return true if successful 
     */
    public boolean setController(IHiveController newController);
    
    public void addConductor(IHiveConductor conductor);
    public void removeConductor(IHiveConductor conductor);
    
    public void addMachine(IHiveMachine machine);
    public void removeMachine(IHiveMachine machine);
    
    public Set<IHiveMachine> getMachines();
    public Set<IHiveConductor> getConductors();
}
