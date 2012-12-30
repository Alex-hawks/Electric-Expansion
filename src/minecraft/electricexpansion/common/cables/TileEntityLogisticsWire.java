package electricexpansion.common.cables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

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
}