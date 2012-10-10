package electricexpansion.alex_hawks.blocks;

import java.util.logging.Logger;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

public class ItemBlockRawWire extends ItemBlock 
{
	public ItemBlockRawWire(int par1, Block mainBlock)
	{
	super(par1);
	setHasSubtypes(true);
	}
	public int getMetadata(int par1)
    {
        return par1;
    }
	public String getItemNameIS(ItemStack i) 
	{
		String name = null;
		int j = i.getItemDamage();
		if (j != -1)
		{
			if(j == 0)
				name = "Copper";
			else if(j == 1)
				name = "Tin"; 
			else if(j == 2)
				name = "Silver";
			else if(j == 3)
				name = "HV";
			//expandable
			else name = "Unknown";
		}
		return i.getItem().getItemName() + "." + name;
	}
	public int getIconFromDamage(int i)
	{
		return i;
	}
}
