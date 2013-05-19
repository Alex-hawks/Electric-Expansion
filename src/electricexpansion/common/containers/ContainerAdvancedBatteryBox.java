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
        // 1st Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 2, 151, 10, IModifier.class));
        // 2nd Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 3, 151, 34, IModifier.class));
        // 3rd Upgrade slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 4, 151, 58, IModifier.class));
        // 3rd Mode slot
        this.addSlotToContainer(new SlotSpecific(AdvBatteryBox, 5, 175, 58, ItemLinkCard.class));
        
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
    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        this.tileEntity.playersUsing.remove(entityplayer);
        super.onCraftGuiClosed(entityplayer);
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
            
            if (par1 > 4)
            {
                if (var4.getItem() instanceof IItemElectric)
                {
                    if (((IItemElectric) var4.getItem()).getProvideRequest(var2).getWatts() > 0)
                    {
                        if (!this.mergeItemStack(var4, 1, 2, false))
                            return null;
                    }
                    else
                    {
                        if (!this.mergeItemStack(var4, 0, 1, false))
                            return null;
                    }
                }
                
                else if (!this.mergeItemStack(var4, 2, 4, false))
                    return null;
            }
            else if (!this.mergeItemStack(var4, 5, 38, false))
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
