package electricexpansion.mattredsox;

import electricexpansion.mattredsox.items.ItemTransformerCoil;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotTransformerCoil extends Slot
{
	public SlotTransformerCoil(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItem() instanceof ItemTransformerCoil;
	}
}
