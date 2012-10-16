package electricexpansion.alex_hawks.misc;

import net.minecraft.src.Packet;
import universalelectricity.basiccomponents.UELoader;
import universalelectricity.network.PacketManager;
import universalelectricity.prefab.TileEntityConductor;

/**
 * 
 * 	@author Alex_hawks
 *	Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityCableHelper extends TileEntityConductor
{
	@Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(UELoader.CHANNEL, this);
    }
}
