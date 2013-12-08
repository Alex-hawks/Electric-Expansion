package electricexpansion.common.cables;

import electricexpansion.api.ElectricExpansionItems;
import net.minecraftforge.common.ForgeDirection;

public class TileEntitySwitchWireBlock extends TileEntityWireBlock
{
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    protected int getID()
    {
        return ElectricExpansionItems.blockSwitchWireBlock.blockID;
    }
}