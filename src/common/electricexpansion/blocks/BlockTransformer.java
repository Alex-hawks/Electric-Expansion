package electricexpansion.blocks;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IRedstoneProvider;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.EEClientProxy;
import electricexpansion.tile.TileEntityTransformer;

public class BlockTransformer extends BlockMachine
{
	public static final int meta = 0;


	public BlockTransformer(int id, int textureIndex)
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
    	else if(metadata >= meta)
    	{
    		metadata -= meta;
    		
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
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 3;

		if (metadata >= meta)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, meta + 1);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, meta + 2);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, meta + 0);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, meta + 3);
					break;
			}
		}
	
	}


	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		 if (metadata >= meta)
		{
			original -= meta;
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

		 if (metadata >= meta)
		{
			change += meta;
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
			return new TileEntityTransformer();
	}


    /**
     * Called when the block is right clicked by the player
     */
    @Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!par1World.isRemote)
        {
        	TileEntityTransformer tileEntity = (TileEntityTransformer)par1World.getBlockTileEntity(x, y, z);

        	System.out.println("Voltage: " + tileEntity.getVoltage());
        	System.out.println("Incoming: " + tileEntity.elecPack.voltage);
        	System.out.println(tileEntity.voltageAdd + " Added Voltage");

            par5EntityPlayer.openGui(ElectricExpansion.instance, 3, par1World, x, y, z);

        }

        return true;
    }
    
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	@Override
	public int getRenderType()
	{
	return EEClientProxy.RENDER_ID;
	}
}
