package electricexpansion.common.cables;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityCableHelper;

public class TileEntitySwitchWire extends TileEntityCableHelper
{
<<<<<<< HEAD
/*	@Override
	public boolean canConnect(ForgeDirection side)
=======
	@Override
	protected boolean canConnectToThisType(TileEntity neighbour)
>>>>>>> fc62a7f8897fcef915239302fa87469403fe39e4
	{
		boolean connect = false;
		if (this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			;
		connect = super.canConnectToThisType(neighbour);
		return connect;
	}*/
}