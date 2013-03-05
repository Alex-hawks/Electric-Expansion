package electricexpansion.common.containers;

import electricexpansion.common.misc.SlotFuse;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerFuseBox extends Container
{
	private TileEntityFuseBox tileEntity;
	
	public ContainerFuseBox(InventoryPlayer par1InventoryPlayer, TileEntityFuseBox tileEntity)
	{
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new SlotFuse(tileEntity, 0, 74, 10));
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}

}
