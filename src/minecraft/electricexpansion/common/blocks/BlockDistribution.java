package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityDistribution;

public class BlockDistribution extends BlockContainer
{
	public BlockDistribution(int id, int meta)
	{
		super(id, Material.iron);
		this.setBlockName("Distribution");
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDistribution();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}

	@Override
	public int damageDropped(int i)
	{
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@Override
	public int getRenderType()
	{
		return 0;
	}

	public String getTextureFile()
	{
		return ElectricExpansion.ALEX_BLOCK_TEXTURE_FILE;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}

	/*
	 * Called when the block is right clicked by the player
	 */

	@Override 
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) 
	{
		if (!par1World.isRemote) 
		{
			par5EntityPlayer.openGui(ElectricExpansion.instance, 4, par1World, x, y, z); 
			return true;
		}
		return true; 
	}

}