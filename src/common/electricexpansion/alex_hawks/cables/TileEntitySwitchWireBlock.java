package electricexpansion.alex_hawks.cables;

import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySwitchWireBlock extends TileEntityCableHelper 
{
	@Override
	public boolean canConnect(int side)
	{
		if(this.getWorld().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			return super.canConnect(side);
		else return false;
	}
}