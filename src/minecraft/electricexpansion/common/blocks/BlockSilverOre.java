package electricexpansion.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import universalelectricity.prefab.UETab;
import electricexpansion.common.ElectricExpansion;

public class BlockSilverOre extends Block
{
	public BlockSilverOre(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(UETab.INSTANCE);
		this.setBlockName("silverore");
		this.setHardness(2f);
		this.blockIndexInTexture = 6;
	}


	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.MATT_BLOCK_TEXTURE_FILE;
	}
	
}

