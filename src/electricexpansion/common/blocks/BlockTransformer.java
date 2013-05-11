package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityTransformer;

public class BlockTransformer extends BlockAdvanced
{
	public BlockTransformer(int id)
	{
		super(id, UniversalElectricity.machine);
		this.setStepSound(soundMetalFootstep);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName("transformer");
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int tierStart = metadata - (metadata & 3);

		int angle = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		switch (angle)
		{
			case 0:
				par1World.setBlock(x, y, z, this.blockID, tierStart + 3, 0);
				break;
			case 1:
				par1World.setBlock(x, y, z, this.blockID, tierStart + 1, 0);
				break;
			case 2:
				par1World.setBlock(x, y, z, this.blockID, tierStart + 2, 0);
				break;
			case 3:
				par1World.setBlock(x, y, z, this.blockID, tierStart + 0, 0);
				break;
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int tierStart = metadata - (metadata & 3);
		int original = metadata & 3;

		int change = 0;

		// Re-orient the block
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

		par1World.setBlock(x, y, z, this.blockID, change + tierStart, 0);
		par1World.markBlockForRenderUpdate(x, y, z);

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

		return true;
	}

	@Override
	public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!par1World.isRemote)
		{
			TileEntityTransformer tileEntity = (TileEntityTransformer) par1World.getBlockTileEntity(x, y, z);

			tileEntity.stepUp = !tileEntity.stepUp;
			return true;
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return side.equals(ForgeDirection.DOWN);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{
		return ClientProxy.RENDER_ID;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createTileEntity(World var1, int metadata)
	{
		return new TileEntityTransformer();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 9; i += 4)
		{
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = this.idPicked(world, x, y, z);

		if (id == 0)
			return null;

		Item item = Item.itemsList[id];
		if (item == null)
			return null;

		int metadata = world.getBlockMetadata(x, y, z);
		int tierStart = metadata - (metadata & 3);

		return new ItemStack(id, 1, tierStart);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "darkMachine");
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata - (metadata & 3);
	}

}