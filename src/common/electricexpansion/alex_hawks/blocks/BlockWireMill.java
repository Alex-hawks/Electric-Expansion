package electricexpansion.alex_hawks.blocks;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.implement.IRedstoneProvider;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;

public class BlockWireMill extends BlockMachine
{
	public BlockWireMill(int par1)
	{
		super("WireMill", par1, Material.iron);
		this.setTextureFile(EECommonProxy.MattBLOCK_TEXTURE_FILE);
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0) { return 7; }
		if (side == 1) { return 6; }

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 2;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 8; }
		
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

		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, + 1);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, + 2);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, + 0);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, + 3);
					break;
			}
		}
	}
	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;
		int change = 0;

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
		par1World.setBlockMetadataWithNotify(x, y, z, change);

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
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityWireMill();
	}
	
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(ElectricExpansion.instance, 2, par1World, x, y, z);
			return true;
		}

		return true;
	}
}
