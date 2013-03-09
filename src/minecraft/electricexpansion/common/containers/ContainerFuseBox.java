package electricexpansion.common.containers;

import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import electricexpansion.api.IItemFuse;
import electricexpansion.common.misc.WireMillRecipes;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFuseBox extends Container
{
	private TileEntityFuseBox tileEntity;
	
	public ContainerFuseBox(InventoryPlayer par1InventoryPlayer, TileEntityFuseBox tileEntity)
	{
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 8, 16, IItemFuse.class));
		
		int var3, var4;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		}
		
		tileEntity.openChest();
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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		System.out.println("Slot: " + par1);
		ItemStack var2 = null, var4 = null, var5 = null;
		Slot var3 = (Slot) this.inventorySlots.get(par1);
		Slot var6 = (Slot) this.inventorySlots.get(0);
		
		if (var3 != null && var3.getHasStack())
		{
			var2 = var3.getStack();
			var4 = var2.copy();
			var5 = var2.copy();
			var5.stackSize = var5.stackSize - 1;
			var4.stackSize = 1;
			System.out.println("StackSize: " + var5.stackSize);
			
			if (par1 == 0)
			{
				if (!this.mergeItemStack(var4, 1, 37, true)) { return var5; }
				
				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 0 && !var6.getHasStack())
			{
				if (var4.getItem() instanceof IItemFuse)
				{
					if (!this.mergeItemStack(var4, 0, 1, false)) { return var5; }
				}
				else if (par1 >= 28 && par1 < 37 && !this.mergeItemStack(var4, 1, 28, false)) { return var5; }
			}
			else if (!this.mergeItemStack(var4, 1, 37, false)) { return var5; }
			
			if (var5.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}
			
			if (var4.stackSize == var2.stackSize) { return null; }
			
			var3.onPickupFromSlot(par1EntityPlayer, var2);
		}
		
		return var2;
	}
}
