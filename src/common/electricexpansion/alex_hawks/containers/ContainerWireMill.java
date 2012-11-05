package electricexpansion.alex_hawks.containers;

import ic2.api.IElectricItem;
import universalelectricity.implement.IItemElectric;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;

public class ContainerWireMill extends Container
{
	private TileEntityWireMill tileEntity;

	public ContainerWireMill(InventoryPlayer par1InventoryPlayer, TileEntityWireMill tileEntity)
	{
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new universalelectricity.prefab.SlotElectricItem(tileEntity, 0, 55, 49)); //Electric Input Slot
		this.addSlotToContainer(new Slot(tileEntity, 1, 55, 25)); //To be drawn into wire
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 108, 25)); //Drawing result
		
		int var3;

		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		tileEntity.openChest();
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{tileEntity.closeChest();}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{return this.tileEntity.isUseableByPlayer(par1EntityPlayer);}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot)this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 != 0 && par1 != 1)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (((IItemElectric)var4.getItem()).canProduceElectricity())
					{
						if (!this.mergeItemStack(var4, 1, 2, false))
						{
							return null;
						}
					}
					else
					{
						if (!this.mergeItemStack(var4, 0, 1, false))
						{
							return null;
						}
					}
				}
				else if(var4.getItem() instanceof IElectricItem)
				{
					if(((IElectricItem)var4.getItem()).canProvideEnergy())
					{
						if(!mergeItemStack(var4, 1, 2, false))
						{
							return null;
						}
					}
					else {
						if(!mergeItemStack(var4, 0, 1, false))
						{
							return null;
						}
					}
				}
				else if (par1 >= 30 && par1 < 38 && !this.mergeItemStack(var4, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 3, 38, false))
			{
				return null;
			}

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack)null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}

}
