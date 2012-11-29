package electricexpansion.alex_hawks.cables;

import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySwitchWire extends TileEntityCableHelper 
{
	@Override
	public boolean canConnect(int side)
	{
		boolean connect = false;
		if(this.getWorld().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			connect = super.canConnect(side);
		return connect;
	}
}