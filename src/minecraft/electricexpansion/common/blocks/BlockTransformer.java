package electricexpansion.common.blocks;

import java.util.List;

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
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityTransformer;

public class BlockTransformer extends BlockMachine
{
	public static final int TIER_1_META = 0;
	public static final int TIER_2_META = 4;
	public static final int TIER_3_META = 8;

	public BlockTransformer(int id)
	{
		super("transformer", id, UniversalElectricity.machine, EETab.INSTANCE);
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
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

		if (metadata >= TIER_3_META)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, TIER_3_META + 3);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, TIER_3_META + 1);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, TIER_3_META + 2);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, TIER_3_META + 0);
					break;
			}
		}
		else if (metadata >= TIER_2_META)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, TIER_2_META + 3);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, TIER_2_META + 1);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, TIER_2_META + 2);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, TIER_2_META + 0);
					break;
			}
		}
		else
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, TIER_1_META + 3);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, TIER_1_META + 1);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, TIER_1_META + 2);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, TIER_1_META + 0);
					break;
			}
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= TIER_3_META)
		{
			original -= TIER_3_META;
		}
		else if (metadata >= TIER_2_META)
		{
			original -= TIER_2_META;
		}

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

		if (metadata >= TIER_3_META)
		{
			change += TIER_3_META;
		}
		else if (metadata >= TIER_2_META)
		{
			change += TIER_2_META;
		}

		par1World.setBlockMetadata(x, y, z, change);

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

			if (tileEntity.stepUp)
				par5EntityPlayer.sendChatToPlayer("Electric Expansion: Transformer is now up converting");

			if (!tileEntity.stepUp)
				par5EntityPlayer.sendChatToPlayer("Electric Expansion: Transformer is now down converting");

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
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
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
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		return new TileEntityTransformer();
	}

	public ItemStack getTier1()
	{
		return new ItemStack(this.blockID, 1, TIER_1_META);
	}

	public ItemStack getTier2()
	{
		return new ItemStack(this.blockID, 1, TIER_2_META);
	}

	public ItemStack getTier3()
	{
		return new ItemStack(this.blockID, 1, TIER_3_META);
	}

	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getTier1());
		par3List.add(this.getTier2());
		par3List.add(this.getTier3());
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = idPicked(world, x, y, z);

		if (id == 0) { return null; }

		Item item = Item.itemsList[id];
		if (item == null) { return null; }

		int metadata = getDamageValue(world, x, y, z);

		if (metadata >= TIER_3_META)
		{
			metadata = TIER_3_META;
		}
		else if (metadata >= TIER_2_META)
		{
			metadata = TIER_2_META;
		}
		else
		{
			metadata = TIER_1_META;
		}

		return new ItemStack(id, 1, metadata);
	}

}