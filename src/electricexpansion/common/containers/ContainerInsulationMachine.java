package electricexpansion.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.tile.TileEntityInsulatingMachine;

public class ContainerInsulationMachine extends Container
{
    private TileEntityInsulatingMachine tileEntity;
    
    public ContainerInsulationMachine(InventoryPlayer par1InventoryPlayer, TileEntityInsulatingMachine tileEntity)
    {
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotUniversalElectricItem(tileEntity, 0, 55, 49)); // Electric
                                                                                       // Input
                                                                                       // Slot
        this.addSlotToContainer(new Slot(tileEntity, 1, 55, 25)); // To be
                                                                  // processed
        this.addSlotToContainer(new SlotSpecific(tileEntity, 2, 108, 25, (ItemStack) null)); // Process
                                                                                             // result
        
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
    
    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeChest();
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
            
            if (par1 == 2)
            {
                if (!this.mergeItemStack(var4, 3, 39, true))
                    return null;
                
                var3.onSlotChange(var4, var2);
            }
            else if (par1 != 1 && par1 != 0)
            {
                if (var4.getItem() instanceof IItemElectric)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                        return null;
                }
                else if (InsulationRecipes.INSTANCE.getProcessResult(var4) > 0)
                {
                    if (!this.mergeItemStack(var4, 1, 2, false))
                        return null;
                }
                else if (par1 >= 3 && par1 < 30)
                {
                    if (!this.mergeItemStack(var4, 30, 39, false))
                        return null;
                }
                else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
                    return null;
            }
            else if (!this.mergeItemStack(var4, 3, 39, false))
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
