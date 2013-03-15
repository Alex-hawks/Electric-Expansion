package electricexpansion.common.blocks;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityMultimeter;

public class BlockMultimeter extends BlockAdvanced
{
	private HashMap<String, Icon> icons = new HashMap<String, Icon>();
	
	public BlockMultimeter(int id, int textureIndex)
	{
		super(id, UniversalElectricity.machine);
		this.setStepSound(this.soundMetalFootstep);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName("multimeter");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 3)
			return this.icons.get("front");
		else
			return this.icons.get("top");
	}
	
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
    	int metadata = par1IBlockAccess.getBlockMetadata(x, y, z);
    	
		if (side == 0 || side == 1)
			return this.icons.get("top");
		else if (side == metadata)
			return this.icons.get("output");
		else
			return this.icons.get("machine");
    }

	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister par1IconRegister)
	{
		this.icons.put("top", par1IconRegister.func_94245_a(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineTop"));
		this.icons.put("output", par1IconRegister.func_94245_a(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineOutput"));
		this.icons.put("machine", par1IconRegister.func_94245_a(ElectricExpansion.TEXTURE_NAME_PREFIX + "machine"));
		this.icons.put("front", par1IconRegister.func_94245_a(ElectricExpansion.TEXTURE_NAME_PREFIX + "multimeter"));
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
	{
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 2;

		switch (angle)
		{
			case 0:
				change = 2;
				break;
			case 1:
				change = 5;
				break;
			case 2:
				change = 3;
				break;
			case 3:
				change = 4;
				break;

		}
		world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, change, 0);
		((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
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

		world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, change, 0);
		((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		return true;
	}

	/**
	 * Is this block powering the block on the specified side
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider)
			return ((IRedstoneProvider) tileEntity).isPoweringTo(ForgeDirection.getOrientation(side)) ? 15 : 0;

		return 0;
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
	public TileEntity createTileEntity(World var1, int metadata)
	{
		return new TileEntityMultimeter();
	}
}
