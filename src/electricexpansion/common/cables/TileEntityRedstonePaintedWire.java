package electricexpansion.common.cables;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase
{

    public short redstoneLevel = 0;
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return true;
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        int cableNeighbor = 0;
        int rsNeighbor = this.worldObj.getStrongestIndirectPower(this.xCoord, this.yCoord, this.zCoord) * 17;
        
        for (TileEntity neighbor : this.connectedBlocks)
        {
            if (neighbor != null && neighbor instanceof TileEntityRedstonePaintedWire)
            {
                TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) neighbor;
                if (te.redstoneLevel > cableNeighbor + 1)
                {
                    cableNeighbor = te.redstoneLevel - 1;
                }
            }
        }
        
        if (this.redstoneLevel != Math.max(cableNeighbor, rsNeighbor))
        {
            this.redstoneLevel = (short) Math.max(cableNeighbor, rsNeighbor);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }
}
