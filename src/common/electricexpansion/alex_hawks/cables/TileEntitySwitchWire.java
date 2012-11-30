package electricexpansion.alex_hawks.cables;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySwitchWire extends TileEntityCableHelper
{
	@Override
	public boolean canConnect(ForgeDirection side)
	{
		boolean connect = false;
		if(this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord));
			connect = super.canConnect(side);
		return connect;
	}
}