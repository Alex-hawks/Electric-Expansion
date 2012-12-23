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
import universalelectricity.prefab.tile.TileEntityAdvanced;
import electricexpansion.common.CommonProxy;
import electricexpansion.common.tile.TileEntityMultimeter;

public class BlockMultimeter extends BlockMachine
{
	public BlockMultimeter(int id, int textureIndex)
	{
		super("multimeter", id, UniversalElectricity.machine, UETab.INSTANCE);
		this.blockIndexInTexture = textureIndex;
		this.setStepSound(this.soundMetalFootstep);
		this.setTextureFile(CommonProxy.MattBLOCK_TEXTURE_FILE);
		this.setCreativeTab(UETab.INSTANCE);
	}

/*	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1) { return this.blockIndexInTexture; }

		// If it is the front side
		if (side == ForgeDirection.getOrientation(metadata).ordinal())
		{
			return this.blockIndexInTexture + 5;
		}
		if (side == ForgeDirection.OPPOSITES[metadata])		{ 
			return this.blockIndexInTexture + 2; 
		}

		return this.blockIndexInTexture;

	}*/
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1) { return this.blockIndexInTexture; }



			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 2;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 5; }
			
			return blockIndexInTexture;
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
					par1World.setBlockMetadata(x, y, z, 1);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, 2);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, 0);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, 3);
					break;
			}
		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}
	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int original = world.getBlockMetadata(x, y, z);
		int change = 2;

		switch (original)
		{
			case 2:
				change = 5;
				break;
			case 5:
				change = 4;
				break;
			case 4:
				change = 3;
				break;
			case 3:
				change = 2;
				break;
		}

		world.setBlockMetadata(x, y, z, change);
		((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
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
}
