package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockSwitchWire extends BlockConductor
{
	public BlockSwitchWire(int id, int meta)
	{
		super(id, Material.cloth);
		this.setUnlocalizedName("SwitchWire");
		this.setStepSound(soundClothFootstep);
		this.setResistance(0.2F);
		this.setHardness(0.1F);
		this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
		this.setCreativeTab(EETab.INSTANCE);
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
		return new TileEntitySwitchWire();
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityConductorBase)
		{
			TileEntityConductorBase te = (TileEntityConductorBase) tileEntity;
			this.minX = (te.connectedBlocks[4] != null)? 0F 	: 	0.3F;
			this.minY = (te.connectedBlocks[0] != null)? 0F 	: 	0.3F;
			this.minZ = (te.connectedBlocks[2] != null)? 0F 	: 	0.3F;
			this.maxX = (te.connectedBlocks[5] != null)? 1F 	: 	0.7F;
			this.maxY = (te.connectedBlocks[1] != null)? 1F 	: 	0.7F;
			this.maxZ = (te.connectedBlocks[3] != null)? 1F 	: 	0.7F;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		
	}

}
