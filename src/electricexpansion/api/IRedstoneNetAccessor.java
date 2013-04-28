package electricexpansion.api;

import universalelectricity.core.block.INetworkProvider;

/**
 * tip: implement this in the TileEntity... Class
 * @author Alex_hawks
 *
 */
public interface IRedstoneNetAccessor extends INetworkProvider
{
    public int getRsSignalFromBlock();
}
