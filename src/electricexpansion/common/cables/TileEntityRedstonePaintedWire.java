package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.api.IRedstoneNetAccessor;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase implements IRedstoneNetAccessor
{
    private boolean isRegistered = false;
    @Override
    public void initiate()
    {
        super.initiate();
        if (this.smartNetwork != null)
        {
            this.smartNetwork.addRsInterfacer(this);
            this.isRegistered = true;
        }
        else {}
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!this.isRegistered && this.smartNetwork != null)
        {
            this.smartNetwork.addRsInterfacer(this);
            this.isRegistered = true;
        }
    }
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public int getRsSignalFromBlock()
    {
        int i = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
        {
            i = Math.max(i, this.worldObj.getBlockPowerInput(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ));
        }
        return i;
    }
}
