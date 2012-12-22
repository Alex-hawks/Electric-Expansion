package electricexpansion.common.blocks;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.CommonProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityTransformer;

public class BlockTransformer extends BlockMachine
{
	// public static final int meta = 0;

	public BlockTransformer(int id, int textureIndex)
	{
		super("Transformer", id, UniversalElectricity.machine, UETab.INSTANCE);
		this.blockIndexInTexture = textureIndex;
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
	}

	@Override
	public String getTextureFile()
	{
		return CommonProxy.MattBLOCK_TEXTURE_FILE;
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
	
	/**
	 * Called when a player uses a wrench on the machine while sneaking
	 * 
	 * @return True if some happens
	 */
	public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		TileEntityTransformer tileEntity = (TileEntityTransformer) par1World.getBlockTileEntity(x, y, z);

	if(tileEntity.stepUp == true)
		tileEntity.stepUp = false;
		
	if(tileEntity.stepUp == false)
		tileEntity.stepUp = true;
	
	System.out.println(tileEntity.stepUp);
		return true;
	}


	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityTransformer();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		TileEntityTransformer tileEntity = (TileEntityTransformer) par1World.getBlockTileEntity(x, y, z);

	if(tileEntity.stepUp == true)
		tileEntity.stepUp = false;
		
	if(tileEntity.stepUp == false)
		tileEntity.stepUp = true;
	
	System.out.println(tileEntity.stepUp);
		return true;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	@Override
	public int getRenderType()
	{
		return ClientProxy.RENDER_ID;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(ElectricExpansion.blockTransformer);
	}
}
