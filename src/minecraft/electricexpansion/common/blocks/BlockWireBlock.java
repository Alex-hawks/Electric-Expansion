package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.BlockConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityWireBlock;

public class BlockWireBlock extends BlockConductor
{
	public BlockWireBlock(int id, int meta)
	{
		super(id, Material.rock);
		this.setBlockName("HiddenWire");
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
	public int damageDropped(int i)
	{
		return i;
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

	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityWireBlock();
	}

	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		int texture = 0;
		texture = meta + 0;
		return texture;
	}

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
}
