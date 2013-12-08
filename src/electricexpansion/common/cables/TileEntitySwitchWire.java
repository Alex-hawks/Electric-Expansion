package electricexpansion.common.cables;

import electricexpansion.api.ElectricExpansionItems;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySwitchWire extends TileEntityInsulatedWire
{
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    protected int getID()
    {
        return ElectricExpansionItems.blockSwitchWire.blockID;
    }
}