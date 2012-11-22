package electricexpansion.alex_hawks.blocks;

import java.util.Random;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.BlockMachine;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;

public class BlockWireMill extends BlockMachine
{
		public BlockWireMill(int par1)
		{
			super("WireMill", par1, Material.iron);
			this.setTextureFile(EECommonProxy.MattBLOCK_TEXTURE_FILE);
		}	

		@Override
		public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
		{
			if (!par1World.isRemote)
			{
				par5EntityPlayer.openGui(ElectricExpansion.instance, 2, par1World, x, y, z);
				return true;
			}

			return true;
		}

		@Override
		public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
		{
			System.out.println("WRENCH!");
			int metadata = par1World.getBlockMetadata(x, y, z);
			int original = metadata;

			int change = 0;

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

			par1World.setBlockMetadataWithNotify(x, y, z, change);

			return true;
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
		
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadataWithNotify(x, y, z, 3);
					break;
				case 1:
					par1World.setBlockMetadataWithNotify(x, y, z, 1);
					break;
				case 2:
					par1World.setBlockMetadataWithNotify(x, y, z, 2);
					break;
				case 3:
					par1World.setBlockMetadataWithNotify(x, y, z, 0);
					break;
			}
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityWireMill();
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
		return -1;
	}

}