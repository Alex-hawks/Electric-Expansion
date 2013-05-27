package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;

public class TileEntitySwitchWire extends TileEntityInsulatedWire
{
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
    }
}