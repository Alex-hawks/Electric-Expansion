package electricexpansion.common.cables;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntitySwitchWire extends TileEntityConductorBase
{
	@Override
	protected boolean canConnectToThisType(TileEntity neighbour)
	{
		boolean connect = false;
		if (this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			;
		connect = super.canConnectToThisType(neighbour);
		return connect;
	}
}