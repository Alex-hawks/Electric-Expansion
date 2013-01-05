package electricexpansion.common.blocks;

import java.util.List;

import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityDistribution;

public class BlockDistribution extends BlockMachine
{
	public BlockDistribution(int id)
	{
		super(id, Material.iron);
		this.setBlockName("Distribution");
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setCreativeTab(EETab.INSTANCE);
		this.blockIndexInTexture = 16;
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
		return ElectricExpansion.MATT_BLOCK_TEXTURE_FILE;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		// If it is the front side
		if (side == metadata + 2) { return this.blockIndexInTexture + 3; }

		// If it is the back side
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 2; }

		return this.blockIndexInTexture + 0;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityDistribution();
	}

	/**
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
	
	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 3;

		switch (angle)
		{
			case 0:
				par1World.setBlockMetadata(x, y, z, 3);
				break;
			case 1:
				par1World.setBlockMetadata(x, y, z, 1);
				break;
			case 2:
				par1World.setBlockMetadata(x, y, z, 2);
				break;
			case 3:
				par1World.setBlockMetadata(x, y, z, 0);
				break;
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}
	
	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int change = 0;

		// Re-orient the block
		switch (metadata)
		{
			case 0:
				change = 3;
				break;
			case 3:
				change = 1;
				break;
			case 1:
				change = 2;
				break;
			case 2:
				change = 0;
				break;
		}

		par1World.setBlockMetadata(x, y, z, change);

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

		return true;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = idPicked(world, x, y, z);

		if (id == 0) { return null; }

		Item item = Item.itemsList[id];
		if (item == null) { return null; }

		return new ItemStack(id, 1, 0);
	}
}