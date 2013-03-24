package electricexpansion.common.blocks;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityWireMill;

public class BlockWireMill extends BlockAdvanced
{
	public BlockWireMill(int id)
	{
		super(id, UniversalElectricity.machine);
		this.setStepSound(soundMetalFootstep);
		this.setUnlocalizedName("wiremill");
		this.setCreativeTab(EETab.INSTANCE);
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
	{
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		switch (angle)
		{
			case 0:
				par1World.setBlock(x, y, z, this.blockID, 1, 0);
				break;
			case 1:
				par1World.setBlock(x, y, z, this.blockID, 2, 0);
				break;
			case 2:
				par1World.setBlock(x, y, z, this.blockID, 0, 0);
				break;
			case 3:
				par1World.setBlock(x, y, z, this.blockID, 3, 0);
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

		par1World.setBlock(x, y, z, this.blockID, change, 0);
		par1World.markBlockForRenderUpdate(x, y, z);

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

		return true;
	}

	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(ElectricExpansion.instance, 2, par1World, x, y, z);
			return true;
		}

		return true;
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
		return new TileEntityWireMill();

	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = idPicked(world, x, y, z);

		if (id == 0) { return null; }

		Item item = Item.itemsList[id];
		if (item == null) { return null; }

		return new ItemStack(id, 1, 0);
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType()
	{
		return ClientProxy.RENDER_ID;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		
	}

}