package electricexpansion.common.itemblocks;

import electricexpansion.common.blocks.BlockTransformer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTransformer extends ItemBlock
{
	public ItemBlockTransformer(int par1)
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
		if (j >= 0 && j < 4)
			name = "60v";
		if (j >= 4 && j < 8)
			name = "120v";
		if (j >= 8 && j < 12)
			name = "240v";
		return i.getItem().getItemName() + "." + name;
	}
}

