package electricexpansion.common.blocks;

import java.util.List;

import electricexpansion.common.ElectricExpansion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.UETab;

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

