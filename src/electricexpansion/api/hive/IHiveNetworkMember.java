package electricexpansion.api.hive;

import universalelectricity.core.electricity.IElectricityNetwork;
/**
 * @author Alex_hawks
 * Implement this on your TileEntities
 */
public interface IHiveNetworkMember 
{
    /**
     * @return EVERY Electricity network that the TileEntity is connected to
     */
    public IElectricityNetwork[] getNetworks();
    
    /**
     * @return The Hive network that this is a member of.
     *      return null if it is null, don't create one...
     */
    public IHiveNetwork getHiveNetwork();
    
    /**
     * @return true if successful. DO NOT reassign your TileEntity's HiveNetwork unless mustOverride is true
     */
    public boolean setHiveNetwork(IHiveNetwork hiveNetwork, boolean mustOverride);
}
