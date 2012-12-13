package electricexpansion.common.cables;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.prefab.UEDamageSource;
import electricexpansion.common.helpers.TileEntityCableHelper;

public class TileEntityRawWire extends TileEntityCableHelper
{
	private byte ticks = 0;

	@Override
	public double getResistance()
	// Values will NOT be actual values or precise relative values. But if x is meant to be greater
	// than y, it will be.
	// Maybe by 10^10 or 10^-10. But the one meant to be greater, will be.
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch (meta)
		{
			case 0:
				return 0.10;
			case 1:
				return 0.08;
			case 2:
				return 0.04;
			case 3:
				return 0.40;
			case 5:
				return 0.01;
			default:
				return 0.10;
		}
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void updateEntity()
	{
		this.ticks++;
		if (this.ticks == 20)
		{
			this.damagePlayers();
			this.ticks = 0;
		}
	}

	public void damagePlayers()
	{
		if (!worldObj.isRemote)
		{
			if (this.getNetwork().getProduced().getWatts() > 0)
			{
				for (Object player : this.worldObj.playerEntities)
				{
					if (player instanceof EntityPlayer)
					{
						if (((EntityPlayer) player).getPlayerCoordinates().getDistanceSquared(this.xCoord, this.yCoord, this.zCoord) <= 9)
							((EntityPlayer) player).attackEntityFrom(UEDamageSource.electrocution, this.getDamageFromMeta(this.getBlockMetadata()));
					}
				}
			}
		}
	}

	private int getDamageFromMeta(int meta)
	{
		switch (meta)
		{
			case 0:
				return 3;
			case 1:
				return 2;
			case 2:
				return 1;
			case 3:
				return 8;
			case 4:
				return 2;
			default:
				return 4;
		}
	}
}