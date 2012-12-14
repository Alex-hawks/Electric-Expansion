package electricexpansion.common.blocks;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IRedstoneProvider;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import electricexpansion.common.EECommonProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

public class BlockAdvancedBatteryBox extends BlockMachine
{
	public static final int BATTERY_BOX_METADATA = 0;

	public BlockAdvancedBatteryBox(int id, int textureIndex)
	{
		super("Bat Box", id, UniversalElectricity.machine, UETab.INSTANCE);
		this.blockIndexInTexture = textureIndex;
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
	}

	@Override
	public String getTextureFile()
	{
		return EECommonProxy.MattBLOCK_TEXTURE_FILE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		int metadata = iBlockAccess.getBlockMetadata(x, y, z);
		TileEntityAdvancedBatteryBox tileEntity = (TileEntityAdvancedBatteryBox) iBlockAccess.getBlockTileEntity(x, y, z);

		if (side == 0 || side == 1)
		{
			return this.blockIndexInTexture;
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			metadata -= BATTERY_BOX_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 3;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 2; }
			// Tier 1
			if (tileEntity.maxJoules <= 6000000 && tileEntity.maxJoules >= 3000000) { return this.blockIndexInTexture + 6; }

			// Tier 2
			if (tileEntity.maxJoules > 6000000 && tileEntity.maxJoules <= 9000000) { return this.blockIndexInTexture + 4; }

			// Tier 3
			if (tileEntity.maxJoules > 9000000) { return this.blockIndexInTexture + 7; }

		}
		return this.blockIndexInTexture + 1;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.blockIndexInTexture;
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			metadata -= BATTERY_BOX_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 3;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 2; }
			return this.blockIndexInTexture + 4;
		}

		return this.blockIndexInTexture + 1;
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

		if (metadata >= BATTERY_BOX_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 3);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 1);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 2);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 0);
					break;
			}
		}

		par1World.notifyBlockChange(x, y, z, this.blockID);
		((TileEntityAdvancedBatteryBox) par1World.getBlockTileEntity(x, y, z)).initiate();
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= BATTERY_BOX_METADATA)
		{
			original -= BATTERY_BOX_METADATA;
		}

		// Reorient the block
		switch (original)
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

		if (metadata >= BATTERY_BOX_METADATA)
		{
			change += BATTERY_BOX_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change);

		return true;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!par1World.isRemote)
		{
			TileEntityAdvancedBatteryBox tileEntity = (TileEntityAdvancedBatteryBox) par1World.getBlockTileEntity(x, y, z);

//issues with this, They can only open the GUI if their hand ISN'T empty, if this if() is called
//			if (par5EntityPlayer.inventory.getCurrentItem() != null) 
			{
				par5EntityPlayer.openGui(ElectricExpansion.instance, 0, par1World, x, y, z);
				return true;
			}

		}
		return true;
	}

	/**
	 * Is this block powering the block on the specified side
	 */
	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider) { return ((IRedstoneProvider) tileEntity).isPoweringTo(ForgeDirection.getOrientation(side)); }

		return false;
	}

	/**
	 * Is this block indirectly powering the block on the specified side
	 */
	@Override
	public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider) { return ((IRedstoneProvider) tileEntity).isIndirectlyPoweringTo(ForgeDirection.getOrientation(side)); }

		return false;
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
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityAdvancedBatteryBox();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(ElectricExpansion.blockAdvBatteryBox);
	}
}
