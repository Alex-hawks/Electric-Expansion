package electricexpansion.mattredsox;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.prefab.SlotElectricItem;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.modifier.SlotModifier;
import electricexpansion.mattredsox.tileentities.TileEntityMultimeter;

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
