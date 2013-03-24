package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityMultimeter extends TileEntityElectrical 
	implements IPacketReceiver, IRotatable
{
	public ElectricityPack electricityReading = new ElectricityPack();
	private ElectricityPack lastReading = new ElectricityPack();

	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 20L == 0L)
		{
			this.lastReading = this.electricityReading;

			if (!this.worldObj.isRemote)
			{
				if (!isDisabled())
				{
					ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
					TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

					if (inputTile != null)
					{
						if ((inputTile instanceof IConductor))
						{
							this.electricityReading = ((IConductor)inputTile).getNetwork().getProduced(new TileEntity[0]);
							this.electricityReading.amperes *= 20.0D;
						}
						else
						{
							this.electricityReading = new ElectricityPack();
						}
					}
					else
					{
						this.electricityReading = new ElectricityPack();
					}
				}

				if (this.electricityReading.amperes != this.lastReading.amperes)
				{
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 20.0D);
				}
			}
		}
	}

	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("ElecEx", this, new Object[] { Double.valueOf(this.electricityReading.amperes), Double.valueOf(this.electricityReading.voltage) });
	}

	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				this.electricityReading.amperes = dataStream.readDouble();
				this.electricityReading.voltage = dataStream.readDouble();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public String getInvName()
	{
		return StatCollector.translateToLocal("tile.multimeter.name");
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction.ordinal() - 2 == this.getBlockMetadata();
	}

	@Override
	public void setDirection(World world, int x, int y, int z, ForgeDirection facingDirection)
	{
		this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, facingDirection.ordinal(), 0x02);
	}

	@Override
	public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
	}
}
