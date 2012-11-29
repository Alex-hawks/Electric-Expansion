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
import electricexpansion.mattredsox.tileentities.TileEntityVoltDetector;

public class BlockVoltDetector extends BlockMachine
{
	public static final int VOLT_DET_METADATA = 0;


	public BlockVoltDetector(int id, int textureIndex)
	{
		super("Voltage Detector", id, UniversalElectricity.machine, UETab.INSTANCE);
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
    	else if(metadata >= VOLT_DET_METADATA)
    	{
    		metadata -= VOLT_DET_METADATA;
    		
    		//If it is the front side
            if (side == metadata+2)
            {
                return this.blockIndexInTexture + 3;
            }
            //If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata+2).getOpposite().ordinal())
            {
                return this.blockIndexInTexture + 2;
            }
      //      else if (side == 3)
      //      {
       //     	return this.blockIndexInTexture + 1;
        //    }

            return this.blockIndexInTexture + 5;
    	}
    	else
    	{
    		 //If it is the front side
            if (side == metadata+2)
            {
            	return this.blockIndexInTexture + 3;
            }
            //If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata+2).getOpposite().ordinal())
            {
            	return this.blockIndexInTexture + 5;
            }
    	}
    	
        return this.blockIndexInTexture + 1;
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
		
	 if (metadata >= VOLT_DET_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, VOLT_DET_METADATA + 3);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, VOLT_DET_METADATA + 1);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, VOLT_DET_METADATA + 2);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, VOLT_DET_METADATA + 0);
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

		 if (metadata >= VOLT_DET_METADATA)
		{
			original -= VOLT_DET_METADATA;
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

		 if (metadata >= VOLT_DET_METADATA)
		{
			change += VOLT_DET_METADATA;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change);

		return true;
	}

	/**
	 * Is this block powering the block on the
	 * specified side
	 */
	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
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
			return new TileEntityVoltDetector();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(ElectricExpansion.blockVoltDet);
	}

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        if (!par1World.isRemote)
        {
            TileEntityVoltDetector tileEntity = (TileEntityVoltDetector)par1World.getBlockTileEntity(x, y, z);

            par5EntityPlayer.openGui(ElectricExpansion.instance, 1, par1World, x, y, z);

        }

        return true;
    }
}
