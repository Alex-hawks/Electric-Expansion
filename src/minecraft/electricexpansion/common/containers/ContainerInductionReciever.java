package electricexpansion.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import electricexpansion.common.tile.TileEntityInductionReciever;

public class ContainerInductionReciever extends Container
{
	private TileEntityInductionReciever tileEntity;

	public ContainerInductionReciever(InventoryPlayer par1InventoryPlayer, TileEntityInductionReciever tileEntity2)
	{
		this.tileEntity = tileEntity2;

		int var3;
		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));

		tileEntity2.openChest();
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
