package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntitySwitchWire extends TileEntityConductorBase
{
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }
}