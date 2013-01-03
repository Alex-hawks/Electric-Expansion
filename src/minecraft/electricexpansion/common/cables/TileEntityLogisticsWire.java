package electricexpansion.common.cables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityLogisticsWire extends TileEntityConductorBase implements IRedstoneProvider
{
	public boolean buttonStatus0 = false;
	public boolean buttonStatus1 = false;
	public boolean buttonStatus2 = false;

	private int playersUsing = 0;

	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				int id = dataStream.readInt();

				if (id == 5)
				{
					this.visuallyConnected[0] = dataStream.readBoolean();
					this.visuallyConnected[1] = dataStream.readBoolean();
					this.visuallyConnected[2] = dataStream.readBoolean();
					this.visuallyConnected[3] = dataStream.readBoolean();
					this.visuallyConnected[4] = dataStream.readBoolean();
					this.visuallyConnected[5] = dataStream.readBoolean();
				}

				if (id == 3)
				{
					this.buttonStatus0 = dataStream.readBoolean();
					this.buttonStatus1 = dataStream.readBoolean();
					this.buttonStatus2 = dataStream.readBoolean();
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			try
			{
				int id = dataStream.readInt();
				if (id == -1)
				{
					this.buttonStatus0 = dataStream.readBoolean();
				}
				if (id == 0)
				{
					this.buttonStatus1 = dataStream.readBoolean();
				}
				if (id == 1)
				{
					this.buttonStatus2 = dataStream.readBoolean();
				}
				if (id == 7)
				{
					if (dataStream.readBoolean() == true)
					{
						this.playersUsing++;
					}
					else
					{
						playersUsing--;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateConnection(TileEntity tileEntity, ForgeDirection side)
	{
		if (!this.worldObj.isRemote)
		{
			if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
			{
				this.connectedBlocks[side.ordinal()] = tileEntity;
				this.visuallyConnected[side.ordinal()] = true;

				if (tileEntity instanceof IConductor)
				{
					Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
				}

				return;
			}

			if (this.connectedBlocks[side.ordinal()] != null)
			{
				if (this.connectedBlocks[side.ordinal()] instanceof IConductor)
				{
					Electricity.instance.splitConnection(this, (IConductor) this.getConnectedBlocks()[side.ordinal()]);
				}

				this.getNetwork().stopProducing(this.connectedBlocks[side.ordinal()]);
				this.getNetwork().stopRequesting(this.connectedBlocks[side.ordinal()]);
			}

			this.connectedBlocks[side.ordinal()] = null;
			this.visuallyConnected[side.ordinal()] = false;
		}
	}

	@Override
	public void updateConnectionWithoutSplit(TileEntity tileEntity, ForgeDirection side)
	{
		if (!this.worldObj.isRemote)
		{
			if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
			{
				this.connectedBlocks[side.ordinal()] = tileEntity;
				this.visuallyConnected[side.ordinal()] = true;

				if (tileEntity instanceof IConductor)
				{
					Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
				}

				return;

			}

			this.connectedBlocks[side.ordinal()] = null;
			this.visuallyConnected[side.ordinal()] = false;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.buttonStatus0 = nbt.getBoolean("buttonStatus0");
		this.buttonStatus1 = nbt.getBoolean("buttonStatus1");
		this.buttonStatus2 = nbt.getBoolean("buttonStatus2");

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("buttonStatus0", this.buttonStatus0);
		nbt.setBoolean("buttonStatus1", this.buttonStatus1);
		nbt.setBoolean("buttonStatus2", this.buttonStatus2);

	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(this.channel, this, (int) 5, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5]);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 15 == 0)
			{
				if (this.getNetwork().getProduced().getWatts() > 0)
				{
					this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, ElectricExpansion.blockLogisticsWire.blockID);
					PacketManager.sendPacketToClients(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 3, this.buttonStatus0, this.buttonStatus1, this.buttonStatus2), this.worldObj, new Vector3(this), 12);
				}
			}
		}

	}

	@Override
	public boolean isPoweringTo(ForgeDirection side)
	{
		if (this.buttonStatus0 && this.getNetwork().getProduced().getWatts() > 0)
			return true;

		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		return isPoweringTo(side);
	}
}