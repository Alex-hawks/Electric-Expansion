package electricexpansion.common.cables;

import com.google.common.io.ByteArrayDataInput;
import electricexpansion.common.helpers.TileEntityConductorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import universalelectricity.prefab.network.PacketManager;

public class TileEntityWireBlock extends TileEntityConductorBase
{
    // everything is in the helper class.
    // this class MUST remain existent...

    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
    {
        super.handlePacketData(network, type, packet, player, dataStream);
        if (this.worldObj.isRemote)
        {
            if (dataStream.readBoolean()) {
                int par1 = dataStream.readInt();
                int par2 = dataStream.readInt();
                int par3 = dataStream.readInt();
                this.textureItemStack = new ItemStack(par1, par2, par3);
            }
        }

    }

    @Override
    public Packet getDescriptionPacket() 
    {
        ItemStack itemStack = this.textureItemStack;
        if (itemStack != null) 
        {
            return PacketManager.getPacket(CHANNEL, this, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5],
                    true, itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage());
        } 
        else 
        {
            return PacketManager.getPacket(CHANNEL, this, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5],
                    false);
        }
    }

}