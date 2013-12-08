package electricexpansion.common.cables;

import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import electricexpansion.api.ElectricExpansionItems;
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
    protected int getID()
    {
        return ElectricExpansionItems.blockInsulatedWire.blockID;
    }
}