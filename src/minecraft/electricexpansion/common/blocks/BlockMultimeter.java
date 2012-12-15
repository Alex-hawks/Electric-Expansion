package electricexpansion.common.blocks;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IRedstoneProvider;
import electricexpansion.common.EECommonProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityMultimeter;

public class BlockMultimeter extends BlockMachine
{
	public BlockMultimeter(int id, int textureIndex)
	{
		super("Multimeter", id, UniversalElectricity.machine, UETab.INSTANCE);
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
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.blockIndexInTexture;
		}

		// If it is the front side
		if (side == ForgeDirection.getOrientation(metadata).getOpposite().ordinal()) { return this.blockIndexInTexture + 2; }


		// If it is the front side
		if (side == metadata + 2)
		{
			return this.blockIndexInTexture + 3;
		}
/*		// If it is the back side
		 if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) 
		 {
			 return this.blockIndexInTexture + 5;
		 }*/
			
		 return this.blockIndexInTexture + 5;

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
					par1World.setBlockMetadataWithNotify(x, y, z, 3);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, 1);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, 2);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, 0);
					break;
			}
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int change = 0;

		

		// Reorient the block
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
		par1World.setBlockMetadataWithNotify(x, y, z, change);
		((TileEntityAdvancedBatteryBox) par1World.getBlockTileEntity(x, y, z)).initiate();

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
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityMultimeter();
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!par1World.isRemote)
		{
			TileEntityMultimeter tileEntity = (TileEntityMultimeter) par1World.getBlockTileEntity(x, y, z);

			par5EntityPlayer.openGui(ElectricExpansion.instance, 1, par1World, x, y, z);

		}

		return true;
	}
}
