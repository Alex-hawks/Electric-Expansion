package electricexpansion.api.hive;

import universalelectricity.core.block.INetworkProvider;

/**
 * tip: implement this in the TileEntity... Class
 * 
 * @author Alex_hawks
 * 
 */
public interface IRedstoneNetAccessor extends INetworkProvider, IHiveNetworkMember
{
    public int getRsSignalFromBlock();
    
    public byte getRsLevel();
    
    public void setRsLevel(byte level);
}
