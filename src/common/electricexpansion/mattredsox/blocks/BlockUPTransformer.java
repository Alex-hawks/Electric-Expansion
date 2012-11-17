package electricexpansion.mattredsox.blocks;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IRedstoneProvider;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.mattredsox.tileentities.TileEntityUPTransformer;

public class BlockUPTransformer extends BlockMachine
{
	public static final int UP_TRANS_METADATA = 0;


	public BlockUPTransformer(int id, int textureIndex)
	{
		super("UP Transformer", id, UniversalElectricity.machine, UETab.INSTANCE);
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
    		  return this.blockIndexInTexture + 17;        
    	}
    	if(metadata >= UP_TRANS_METADATA)
    	{
    		metadata -= UP_TRANS_METADATA;
    		
    		//If it is the front side
            if (side == metadata+2)
            {
                return this.blockIndexInTexture + 19;      
            }
            //If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata+2).getOpposite().ordinal())
            {
                return this.blockIndexInTexture + 18;
            }

            return this.blockIndexInTexture + 20;
            }
    	
		return metadata;
    }

	/**
	 * Called when the block is placed in the
	 * world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 3;
		
	 if (metadata >= UP_TRANS_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, UP_TRANS_METADATA + 3);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, UP_TRANS_METADATA + 1);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, UP_TRANS_METADATA + 2);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, UP_TRANS_METADATA + 0);
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

		 if (metadata >= UP_TRANS_METADATA)
		{
			original -= UP_TRANS_METADATA;
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

		 if (metadata >= UP_TRANS_METADATA)
		{
			change += UP_TRANS_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change);

		return true;
	}

	/**
	 * Is this block powering the block on the
	 * specified side
	 */
	@Override
	public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider) { return ((IRedstoneProvider) tileEntity).isPoweringTo(ForgeDirection.getOrientation(side)); }

		return false;
	}

	/**
	 * Is this block indirectly powering the block
	 * on the specified side
	 */
	@Override
	public boolean isIndirectlyPoweringTo(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
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
			return new TileEntityUPTransformer();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(ElectricExpansion.blockUPTransformer);
	}
}
