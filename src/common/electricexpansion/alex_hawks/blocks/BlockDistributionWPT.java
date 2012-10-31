package electricexpansion.alex_hawks.blocks;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.machines.TileEntityWPTDistribution;

public class BlockDistributionWPT extends BlockContainer
{

	public BlockDistributionWPT(int id, int meta) 
	{
        super(id, Material.iron);
        this.setBlockName("QuantumBattBox");
        this.setStepSound(soundMetalFootstep);
        this.setResistance(0.2F);
        this.setRequiresSelfNotify();
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{return new TileEntityWPTDistribution();}
	
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

	public String getTextureFile()
	{return EECommonProxy.ABLOCK;}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 4; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}

}
