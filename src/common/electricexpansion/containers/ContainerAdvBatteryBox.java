package electricexpansion.mattredsox;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;
import universalelectricity.prefab.SlotElectricItem;
import universalelectricity.prefab.modifier.SlotModifier;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;

public class ContainerAdvBatteryBox extends Container
{
    private TileEntityAdvBatteryBox tileEntity;

    public ContainerAdvBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvBatteryBox AdvBatteryBox)
    {
        this.tileEntity = AdvBatteryBox;
        this.addSlotToContainer(new SlotElectricItem(AdvBatteryBox, 0, 11, 24)); //Top slot
        this.addSlotToContainer(new SlotElectricItem(AdvBatteryBox, 1, 11, 48)); //Bottom slot
        
        this.addSlotToContainer(new SlotModifier(AdvBatteryBox, 2, 149, 7)); //1st Upgrade slot
        this.addSlotToContainer(new SlotModifier(AdvBatteryBox, 3, 149, 31)); //2nd Upgrade slot
        this.addSlotToContainer(new SlotModifier(AdvBatteryBox, 4, 149, 55)); //3rd Upgrade slot

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
        }
        
        tileEntity.openChest();
    }
    
    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

}
