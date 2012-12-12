package electricexpansion.mattredsox.items;

import com.google.common.io.ByteArrayDataInput;

import universalelectricity.prefab.network.IPacketReceiver;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Item;
import net.minecraft.src.Packet250CustomPayload;

public class ItemMultimeter extends Item implements IPacketReceiver {

	public ItemMultimeter(int par1) {
		super(par1);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType,
			Packet250CustomPayload packet, EntityPlayer player,
			ByteArrayDataInput dataStream) {
		
	}

}
