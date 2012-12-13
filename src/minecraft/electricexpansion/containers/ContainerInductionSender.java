package electricexpansion.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import electricexpansion.tile.TileEntityInductionSender;

public class ContainerInductionSender extends Container
{
	private TileEntityInductionSender tileEntity;

	public ContainerInductionSender(InventoryPlayer par1InventoryPlayer, TileEntityInductionSender tileEntity)
	{
		this.tileEntity = tileEntity;

		int var3;
		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));

		tileEntity.openChest();
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		tileEntity.closeChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		return null;
	}
}
