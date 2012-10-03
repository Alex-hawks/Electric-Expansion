package electricexpansion.alex_hawks.cables;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.TileEntity;

public class TileEntitySwitchWireBlockOff extends TileEntity 
{
	@Override
	public boolean canUpdate()
	{
		return true;
	}
	@Override
	public void updateEntity()
	{
		if(this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.onSwitchWireBlock, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
	}
}
