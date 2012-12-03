package electricexpansion.mattredsox;

import ic2.api.IElectricItem;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import universalelectricity.core.implement.IItemElectric;
import electricexpansion.mattredsox.tileentities.TileEntityVoltDetector;

public class ContainerVoltDetector extends Container
{
    private TileEntityVoltDetector tileEntity;

    public ContainerVoltDetector(TileEntityVoltDetector batteryBox)
    {
        this.tileEntity = batteryBox;
    }
    
    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
		super.onCraftGuiClosed(entityplayer);
    }

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return false;
	}



}
