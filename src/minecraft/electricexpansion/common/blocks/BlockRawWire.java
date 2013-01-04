package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.BlockConductor;
import universalelectricity.prefab.UEDamageSource;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityTransformerT2;

public class BlockRawWire extends BlockConductor
{
	public BlockRawWire(int id, int meta)
	{
		super(id, Material.cloth);
		this.setBlockName("RawWire");
		this.setStepSound(soundClothFootstep);
		this.setResistance(0.2F);
		this.setHardness(0.1F);
		this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
		this.setRequiresSelfNotify();
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
	public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityLiving)
		{
			TileEntityRawWire tileEntity = (TileEntityRawWire) par1World.getBlockTileEntity(x, y, z);

			if (tileEntity.getNetwork().getProduced().getWatts() > 0)
			{
				((EntityLiving) entity).attackEntityFrom(UEDamageSource.electrocution, EnumWireMaterial.values()[par1World.getBlockMetadata(x, y, z)].electrocutionDamage);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityRawWire();
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.ALEX_ITEMS_TEXTURE_FILE;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; ++var4)
			par3List.add(new ItemStack(par1, 1, var4));
	}
}