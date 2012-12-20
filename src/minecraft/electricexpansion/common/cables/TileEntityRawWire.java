package electricexpansion.common.cables;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.prefab.UEDamageSource;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRawWire extends TileEntityConductorBase
{
	/**
	 * Values will NOT be actual values or precise relative values. But if x is meant to be greater
	 * than y, it will be. Maybe by 10^10 or 10^-10. But the one meant to be greater, will be.
	 */
	@Override
	public double getResistance()
	{
		int meta = this.getBlockMetadata();

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
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 60 == 0)
		{
			this.electrocuteEntities();
		}
	}

	/**
	 * Electrocute entities around it.
	 */
	public void electrocuteEntities()
	{
		if (!worldObj.isRemote)
		{
			if (this.getNetwork().getProduced().getWatts() > 0)
			{
				int radius = 2;
				List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord + radius, this.yCoord + radius, this.zCoord + radius, this.xCoord - radius, this.yCoord - radius, this.zCoord - radius));
				for (EntityPlayer entity : entities)
				{
					entity.attackEntityFrom(UEDamageSource.electrocution, this.getDamageFromMeta(this.getBlockMetadata()));
				}
			}
		}
	}

	/**
	 * Gets the electrocution damage based on the metadata.
	 */
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