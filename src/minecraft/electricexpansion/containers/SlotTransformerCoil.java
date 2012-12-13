package electricexpansion.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import electricexpansion.items.ItemTransformerCoil;

public class SlotTransformerCoil extends Slot
{
	public SlotTransformerCoil(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItem() instanceof ItemTransformerCoil;
	}
}
