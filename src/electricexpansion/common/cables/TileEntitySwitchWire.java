package electricexpansion.common.cables;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntitySwitchWire extends TileEntityConductorBase
{
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
	}
}