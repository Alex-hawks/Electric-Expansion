package electricexpansion.alex_hawks.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySwitchWireBlock extends TileEntityCableHelper 
{
	@Override
	public boolean canConnect(ForgeDirection side)
	{
		if(this.getWorld().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.getWorld().isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			return super.canConnect(side);
		else return false;
	}
}