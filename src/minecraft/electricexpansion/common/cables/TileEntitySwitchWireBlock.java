package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityCableHelper;

public class TileEntitySwitchWireBlock extends TileEntityCableHelper
{
	@Override
	public boolean canConnect(ForgeDirection side)
	{
		boolean connect = false;
		if (this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			connect = super.canConnect(side);
		return connect;
	}
}