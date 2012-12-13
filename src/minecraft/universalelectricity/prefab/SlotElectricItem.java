package universalelectricity.prefab;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;

/**
 * This slot should be used by any container that needs the slot for an electric items only.
 * 
 * @author Calclavia
 * 
 */
public class SlotElectricItem extends Slot
{
	public SlotElectricItem(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItem() instanceof IItemElectric;
	}
}
