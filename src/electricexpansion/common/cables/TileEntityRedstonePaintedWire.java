package electricexpansion.common.cables;

import net.minecraftforge.common.ForgeDirection;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase
{
    public boolean input = false;
    
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
        
    }
    
}
