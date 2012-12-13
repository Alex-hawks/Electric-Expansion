package electricexpansion.itemblocks;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockWPT extends ItemBlock
{
	public ItemBlockWPT(int par1) 
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	public int getMetadata(int par1)
    {
        return par1;
    }
	public String getItemNameIS(ItemStack i) 
	{
		String name = null;
		int j = i.getItemDamage();
		if(j >= 0 && j < 4)
			name = "Distribution";
		if(j >= 4 && j < 8)
			name = "InductionSender";
		if(j >= 8 && j < 12)
			name = "InductionReciever";
		return i.getItem().getItemName() + "." + name;
	}
}
