package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.api.hive.IRedstoneNetAccessor;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase implements IRedstoneNetAccessor
{
    private byte rsLevel = 0;
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public int getRsSignalFromBlock()
    {
        if (this.mode)
        {
            int i = 0;
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
                i = Math.max(i, this.worldObj.getBlockPowerInput(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ));
            
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return i;
        }
        else
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return 0;
        }
    }

    public byte getRsLevel()
    {
        return this.rsLevel;
    }

    @Override
    public void setRsLevel(byte level)
    {
        this.rsLevel = level;
    }
}
