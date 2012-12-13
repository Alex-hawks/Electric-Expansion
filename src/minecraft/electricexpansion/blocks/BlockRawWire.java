package electricexpansion.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.BlockConductor;
import universalelectricity.prefab.UETab;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import electricexpansion.EECommonProxy;
import electricexpansion.cables.TileEntityRawWire;

public class BlockRawWire extends BlockConductor
{
	public BlockRawWire(int id, int meta)
	{
		super(id, Material.cloth);
		this.setBlockName("RawWire");
		this.setStepSound(soundClothFootstep);
		this.setResistance(0.2F);
		this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
		this.setRequiresSelfNotify();
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int damageDropped(int i)
	{
		return i;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityRawWire();
	}

	@Override
	public String getTextureFile()
	{
		return EECommonProxy.AITEMS;
	}

	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		// I have relocated this to the TE...
		// And now I can use a range...
		/*
		 * if(par1World.getBlockMetadata(par2, par3, par4) == 0)
		 * par5Entity.attackEntityFrom(UEDamageSource.electrocution, 3);
		 * if(par1World.getBlockMetadata(par2, par3, par4) == 1)
		 * par5Entity.attackEntityFrom(UEDamageSource.electrocution, 2);
		 * if(par1World.getBlockMetadata(par2, par3, par4) == 2)
		 * par5Entity.attackEntityFrom(UEDamageSource.electrocution, 1);
		 * if(par1World.getBlockMetadata(par2, par3, par4) == 3)
		 * par5Entity.attackEntityFrom(UEDamageSource.electrocution, 8);
		 */}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}
}