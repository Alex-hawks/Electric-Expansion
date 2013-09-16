package electricexpansion.common.cables;

import net.minecraft.network.packet.Packet;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityInsulatedWire extends TileEntityConductorBase

{
    @Override
    public void initiate()
    {
        super.initiate();
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockInsulatedWire.blockID);
        PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3],
                this.visuallyConnected[4], this.visuallyConnected[5], this.frequency.getIndex());
    }
}