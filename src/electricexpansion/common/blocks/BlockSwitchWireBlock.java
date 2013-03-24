package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockSwitchWireBlock extends BlockConductor
{
	public BlockSwitchWireBlock(int id, int meta)
	{
		super(id, Material.rock);
		this.setUnlocalizedName("SwitchWireBlock");
		this.setStepSound(soundStoneFootstep);
		this.setResistance(0.2F);
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setCreativeTab(EETab.INSTANCE);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
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
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntitySwitchWireBlock();
	}
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		return (((TileEntityConductorBase)par1IBlockAccess.getBlockTileEntity(x, y, z)).textureItemStack == null) ? this.blockIcon : ((TileEntityConductorBase)par1IBlockAccess.getBlockTileEntity(x, y, z)).textureItemStack.getIconIndex();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityConductorBase)
		{
			if (player.inventory.getCurrentItem().itemID != this.blockID)
			{
				((TileEntityConductorBase)world.getBlockTileEntity(x, y, z)).textureItemStack = player.inventory.getCurrentItem();
				world.markBlockForRenderUpdate(x, y, z);
				return true;
			}
			else
			{
				((TileEntityConductorBase)world.getBlockTileEntity(x, y, z)).textureItemStack = null;
				world.markBlockForRenderUpdate(x, y, z);
				return true;	
			}
		}
		else return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "CamoWire");
	}
}