package electricexpansion.containers;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import electricexpansion.tile.TileEntityMultimeter;

public class ContainerMultimeter extends Container
{
    private TileEntityMultimeter tileEntity;

    public ContainerMultimeter(TileEntityMultimeter multiMeter)
    {
        this.tileEntity = multiMeter;
       
    }
    


    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

}
