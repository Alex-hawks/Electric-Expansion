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
import electricexpansion.alex_hawks.machines.TileEntityWireMill;

public class BlockWireMill extends BlockMachine
{
		public BlockWireMill(int par1)
		{
			super("WireMill", par1, Material.iron);
			this.setTextureFile(EECommonProxy.MattBLOCK_TEXTURE_FILE);
		}
		

	/*@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player)
	{
		if (!world.isRemote && !super.onMachineActivated(world, x, y, z, player))
		{
			player.openGui(BASEMOD.instance(), 0, world, x, y, z);
		}

		return true;

	}
*/
	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
			case 2: world.setBlockMetadataWithNotify(x, y, z, 4); break;
			case 5: world.setBlockMetadataWithNotify(x, y, z, 2); break;
			case 3: world.setBlockMetadataWithNotify(x, y, z, 5); break;
			case 4: world.setBlockMetadataWithNotify(x, y, z, 3); break;
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity)
	{
		int direction = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int newMetadata = 3;

		switch (direction)
		{
			case 0: newMetadata = 2; break;
			case 1: newMetadata = 5; break;
			case 2: newMetadata = 3; break;
			case 3: newMetadata = 4; break;
		}

		world.setBlockMetadataWithNotify(x, y, z, newMetadata);

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
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

}