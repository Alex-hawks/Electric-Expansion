package electricexpansion.common.cables;

import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityLogisticsWire extends TileEntityConductorBase
{
	// everything is in the helper class.
	// this class MUST remain existent...
	
	public boolean buttonStatus0 = false;
	public boolean buttonStatus1 = false;
	public boolean buttonStatus2 = false;

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			int id = dataStream.readInt();
			if(id == -1) 
			{
				this.buttonStatus0 = dataStream.readBoolean();			
			}
			if(id == 0)
			{
				this.buttonStatus1 = dataStream.readBoolean();			
			}
			if(id == 1)
			{
				this.buttonStatus2 = dataStream.readBoolean();			
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateConnection(TileEntity tileEntity, ForgeDirection side)
	{
		if (!this.worldObj.isRemote)
		{
			if (tileEntity != null && this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
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
			if (tileEntity != null && this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
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
			}

			this.connectedBlocks[side.ordinal()] = null;
			this.visuallyConnected[side.ordinal()] = false;
		}
	}
}