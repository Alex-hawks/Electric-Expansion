package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;

public class BlockSwitchWireBlock extends BlockConductor
{
	public BlockSwitchWireBlock(int id, int meta)
	{
		super(id, Material.rock);
		this.setBlockName("SwitchWireBlock");
		this.setStepSound(soundStoneFootstep);
		this.setResistance(0.2F);
		this.setRequiresSelfNotify();
		this.setHardness(1.5F);
		this.setResistance(10.0F);
//		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return true;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@Override
	public int damageDropped(int i)
	{
		return i;
	}

	@Override
	public int getRenderType()
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntitySwitchWireBlock();
	}

	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		int texture = 255;
		if (side == 1)
		{
			if (meta == 0)
				texture = 16;
			if (meta == 1)
				texture = 17;
			if (meta == 2)
				texture = 18;
			if (meta == 3)
				texture = 19;
			if (meta == 4)
				texture = 20;
		}
		else if (side == 0)
		{
			if (meta == 0)
				texture = 32;
			if (meta == 1)
				texture = 33;
			if (meta == 2)
				texture = 34;
			if (meta == 3)
				texture = 35;
			if (meta == 4)
				texture = 36;
		}
		else
		{
			if (meta == 0)
				texture = 0;
			if (meta == 1)
				texture = 1;
			if (meta == 2)
				texture = 2;
			if (meta == 3)
				texture = 3;
			if (meta == 4)
				texture = 4;
		}
		if (texture != 255)
			texture = texture + 16;
		return texture;
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.BLOCK_FILE;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
}