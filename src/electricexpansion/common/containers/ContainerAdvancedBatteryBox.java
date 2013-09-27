package electricexpansion.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import electricexpansion.api.IModifier;
import electricexpansion.common.items.ItemLinkCard;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

public class ContainerAdvancedBatteryBox extends Container
{
    private TileEntityAdvancedBatteryBox tileEntity;
    
    public ContainerAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox AdvBatteryBox)
    {
        this.tileEntity = AdvBatteryBox;
        this.addSlotToContainer(new SlotUniversalElectricItem(AdvBatteryBox, 0, -11, 24));
        this.addSlotToContainer(new SlotUniversalElectricItem(AdvBatteryBox, 1, -11, 48));
        //  Link slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 2, 129, 31, ItemLinkCard.class));
        // 1st Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 3, 129, 9, IModifier.class));
        // 2nd Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 4, 151, 9, IModifier.class));
        // 3rd Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 5, 173, 9, IModifier.class));
        
        int var3;
        
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, var4 * 18 - 14, 84 + var3 * 18));
            }
        }
        
        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, var3 * 18 - 14, 142));
        }
        
        this.tileEntity.playersUsing.add(par1InventoryPlayer.player);
    }
    
    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        this.tileEntity.playersUsing.remove(entityplayer);
        super.onContainerClosed(entityplayer);
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }
    
    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot) this.inventorySlots.get(par1);
        
        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            
            if (par1 > 5)
            {
                if (var4.getItem() instanceof IItemElectric)
                {
                    float max = ((IItemElectric) var4.getItem()).getMaxElectricityStored(var2);
                    if (((IItemElectric) var4.getItem()).discharge(var2, max / 2, false) > 0)
                    {
                        if (!this.mergeItemStack(var4, 1, 1, false))
                            return null;
                    }
                    else
                    {
                        if (!this.mergeItemStack(var4, 0, 0, false))
                            return null;
                    }
                }
                else if (var4.getItem() instanceof IModifier)
                {
                    if (!this.mergeItemStack(var4, 2, 4, false))
                        return null;
                }
                else if (var4.getItem() instanceof ItemLinkCard)
                    if (!this.mergeItemStack(var4, 5, 5, false))
                        return null;
            }
            else if (!this.mergeItemStack(var4, 6, 38, false))
                return null;
            
            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack) null);
            }
            else
            {
                var3.onSlotChanged();
            }
            
            if (var4.stackSize == var2.stackSize)
                return null;
            
            var3.onPickupFromSlot(par1EntityPlayer, var4);
        }
        
        return var2;
    }
    
}
