package electricexpansion.api;

import net.minecraft.world.World;
import universalelectricity.core.block.INetworkProvider;

/**
 * tip: implement this in the TileEntity... Class
 * @author Alex_hawks
 *
 */
public interface IRedstoneNetAccessor extends INetworkProvider
{
    public boolean getIsAcceptingRsSignal();
    
    public int getX();
    public int getY();
    public int getZ();
    
    public World getWorld();
}
