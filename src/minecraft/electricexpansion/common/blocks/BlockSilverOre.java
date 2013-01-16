package electricexpansion.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class BlockSilverOre extends Block
{
	public BlockSilverOre(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(EETab.INSTANCE);
		this.setBlockName("silverore");
		this.setHardness(2f);
		this.blockIndexInTexture = 8;
		this.setTextureFile(ElectricExpansion.BLOCK_FILE);
	}

}
