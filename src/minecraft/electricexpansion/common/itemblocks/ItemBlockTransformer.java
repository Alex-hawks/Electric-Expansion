package electricexpansion.common.itemblocks;

import electricexpansion.common.blocks.BlockTransformer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTransformer extends ItemBlock
{
	public ItemBlockTransformer(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		int metadata = 0;

		if (itemstack.getItemDamage() >= BlockTransformer.TIER_3_META)
		{
			metadata = 2;
		}
		else if (itemstack.getItemDamage() >= BlockTransformer.TIER_2_META)
		{
			metadata = 1;
		}

		return Block.blocksList[this.getBlockID()].getBlockName() + "." + metadata;
	}

	@Override
	public String getItemName()
	{
		return Block.blocksList[this.getBlockID()].getBlockName() + ".0";
	}
}