package electricexpansion.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.EECommonProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.tile.TileEntityWireMill;

public class BlockWireMill extends BlockMachine
{
	public static final int metaWireMill = 0;
	public static final int metaSuper = 4;

	public boolean isWireMill = false;
	public boolean isSuperMaker = false;

	public BlockWireMill(int par1)
	{
		super("blockEtcher", par1, Material.iron);
		this.setTextureFile(EECommonProxy.MattBLOCK_TEXTURE_FILE);
		this.setCreativeTab(UETab.INSTANCE);
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
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= metaWireMill)
		{
			original -= metaWireMill;
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

		if (metadata >= metaWireMill)
		{
			change += metaWireMill;
		}

		par1World.markBlockForRenderUpdate(x, y, y);
		par1World.setBlockMetadata(x, y, z, change);

		return true;
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

		if (metadata >= metaWireMill)
		{
			this.isWireMill = true;
			// Unnecessary but just in case something weird happens
			this.isSuperMaker = false;
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, metaWireMill + 1);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, metaWireMill + 2);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, metaWireMill + 0);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, metaWireMill + 3);
					break;
			}
		}
		// Super Conductor Machine Meta
		else
		{
			this.isSuperMaker = true;
			// Unnecessary but just in case something weird happens
			this.isWireMill = false;
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, metaSuper + 1);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, metaSuper + 2);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, metaSuper + 0);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, metaSuper + 3);
					break;
			}
		}
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		// if (metadata >= metaWireMill)
		// {
		return new TileEntityWireMill();
		// }
		// else
		// {
		// return new TileEntitySuperMaker();
		// }

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
		if (this.isSuperMaker) { return 0; }
		return ClientProxy.RENDER_ID;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = idPicked(world, x, y, z);

		if (id == 0) { return null; }

		Item item = Item.itemsList[id];
		if (item == null) { return null; }

		int metadata = getDamageValue(world, x, y, z);

		if (metadata >= metaWireMill)
		{
			metadata = metaWireMill;
		}
		else
		{
			metadata = metaSuper;
		}

		return new ItemStack(id, 1, metadata);
	}
}