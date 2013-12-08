package electricexpansion.api.hive;

import universalelectricity.core.block.IConnector;
import universalelectricity.core.grid.IElectricityNetwork;
/**
 * @author Alex_hawks
 * Implement this on your TileEntities
 */
public interface IHiveNetworkMember extends IConnector
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
     * If your TileEntity is a {@link IHiveSignalIO}, then register the IO to the new network here, 
     * after ensuring that it isn't registered to any network
     * @return true if successful. DO NOT reassign your TileEntity's HiveNetwork unless mustOverride is true or it is null
     */
    public boolean setHiveNetwork(IHiveNetwork hiveNetwork, boolean mustOverride);
}
