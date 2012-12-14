package electricexpansion.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import electricexpansion.common.tile.TileEntityMultimeter;

public class ContainerMultimeter extends Container
{
	private TileEntityMultimeter tileEntity;

	public ContainerMultimeter(TileEntityMultimeter multiMeter)
	{
		this.tileEntity = multiMeter;

	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

}
