package electricexpansion.mattredsox.blocks;

import java.util.List;

import net.minecraft.src.CreativeTabs;
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
import basiccomponents.tile.TileEntityBatteryBox;

import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.mattredsox.items.ItemUpgrade;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;

public class BlockAdvBatteryBox extends BlockMachine
{
	public static final int BATTERY_BOX_METADATA = 0;


	public BlockAdvBatteryBox(int id, int textureIndex)
	{
		super("Adv Bat Box", id, UniversalElectricity.machine, UETab.INSTANCE);
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
    	else if(metadata >= BATTERY_BOX_METADATA)
    	{
    		metadata -= BATTERY_BOX_METADATA;
    		
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

            return this.blockIndexInTexture + 4;
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
		
	 if (metadata >= BATTERY_BOX_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, BATTERY_BOX_METADATA + 3);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, BATTERY_BOX_METADATA + 1);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, BATTERY_BOX_METADATA + 2);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, BATTERY_BOX_METADATA + 0);
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
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        if (!par1World.isRemote)
        {
            TileEntityAdvBatteryBox tileEntity = (TileEntityAdvBatteryBox)par1World.getBlockTileEntity(x, y, z);

            // NEED WAY OF DETERMINING IF THE ITEM HELD WHEN RIGHT CLICKING IS NOT AN INSTANCE OF THE ITEMUPGRADE.CLASS
          
			if(!(par5EntityPlayer.inventory.getCurrentItem().getItem() instanceof ItemUpgrade))
 			{
	            par5EntityPlayer.openGui(ElectricExpansion.instance, 0, par1World, x, y, z);
	            return true;
 			}

            
            if(par5EntityPlayer.inventory.getCurrentItem() == null)
        	{
            par5EntityPlayer.openGui(ElectricExpansion.instance, 0, par1World, x, y, z);
            return true;
       	}
            
            if(par5EntityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ElectricExpansion.itemUpgrade, 1, 0)) && tileEntity.hasT1Capacity == false)
            {
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                tileEntity.hasT1Capacity = true;
                return true;
            }
            
            if(par5EntityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ElectricExpansion.itemUpgrade, 1, 1)) && tileEntity.hasT2Capacity == false && tileEntity.hasT1Capacity == true)
            {
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                tileEntity.hasT2Capacity = true;
                return true;
                
            } 
            
            if(par5EntityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ElectricExpansion.itemUpgrade, 1, 2)) && tileEntity.hasT3Capacity == false && tileEntity.hasT2Capacity == true)
            {
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                tileEntity.hasT3Capacity = true;
                return true;
            }

            if(par5EntityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ElectricExpansion.itemUpgrade, 1, 3)) && tileEntity.hasBCComp == false)
            {
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                tileEntity.hasBCComp = true;
                return true;
            }
            
            if(par5EntityPlayer.inventory.getCurrentItem().isItemEqual(new ItemStack(ElectricExpansion.itemUpgrade, 1, 4)) && tileEntity.hasIC2Comp == false)
            {
                par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
                tileEntity.hasIC2Comp = true;
                return true;
            }
        }

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
			return new TileEntityAdvBatteryBox();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(ElectricExpansion.blockBigBatteryBox);
	}
}
