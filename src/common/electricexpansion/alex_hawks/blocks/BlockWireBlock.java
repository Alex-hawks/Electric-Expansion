package electricexpansion.alex_hawks.blocks;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.prefab.BlockConductor;
import universalelectricity.prefab.UETab;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.cables.TileEntityWireBlock;

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
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public boolean isOpaqueCube()
	{return true;}

	@Override
	public int damageDropped(int i)
	{return i;}

	@Override
	public boolean renderAsNormalBlock()
	{return true;}

	@Override
	public int getRenderType()
	{return 0;}

	public TileEntity createNewTileEntity(World var1)
	{return new TileEntityWireBlock();}

	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		int texture = 255;
		texture = meta + 0;
		return texture;
	}

	public String getTextureFile()
	{return EECommonProxy.ABLOCK;}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}
}
