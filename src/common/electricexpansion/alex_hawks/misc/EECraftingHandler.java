package electricexpansion.alex_hawks.misc;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import electricexpansion.ElectricExpansion;

public class EECraftingHandler implements ICraftingHandler 
{
	private Item[] hammers = {ElectricExpansion.itemHammerStone, ElectricExpansion.itemHammerIron, ElectricExpansion.itemHammerDiamond};
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory inv) 
	{
		for(int i=0; i < inv.getSizeInventory(); i++)
			if(inv.getStackInSlot(i) != null)
				for(Item checkedItem : hammers)
				{
					ItemStack j = inv.getStackInSlot(i);
					if(j.getItem() != null && j.getItem() == checkedItem)
					{
						ItemStack k = new ItemStack(checkedItem, 2, (j.getItemDamage() + 1));
						if(k.getItemDamage() >= k.getMaxDamage())
							k = null;
						inv.setInventorySlotContents(i, k);
					}
				}
	}
	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {}
}
