package electricexpansion.common.cables;

import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityInsulatedWire extends TileEntityConductorBase

{
	/*
	 * -1 = none (default) 0 - black 1 - red 2 - dark green 3 - brown 4 - blue 5 - purple 6 - cyan 7
	 * - light gray 8 - dark gray 9 - pink 10 - light green 11 - yellow 12 - light blue 13 - magenta
	 * 14 - orange 15 - white
	 */

	public double colordouble;

	// everything is in the helper class.
	// this class MUST remain existent...

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				int id = dataStream.readInt();

				
				if (id == 1)
				{
					this.visuallyConnected[0] = dataStream.readBoolean();
					this.visuallyConnected[1] = dataStream.readBoolean();
					this.visuallyConnected[2] = dataStream.readBoolean();
					this.visuallyConnected[3] = dataStream.readBoolean();
					this.visuallyConnected[4] = dataStream.readBoolean();
					this.visuallyConnected[5] = dataStream.readBoolean();
				}

				if (id == 0)
				{
					this.colordouble = dataStream.readDouble();
					System.out.println("Recieved Dye Color Packet! " + this.colordouble);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 1, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5], this.colordouble);
	}

}