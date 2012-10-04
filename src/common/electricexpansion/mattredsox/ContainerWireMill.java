package electricexpansion.mattredsox;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.*;

public class ContainerWireMill extends Container
{
/** The crafting matrix inventory (3x3). */
public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
public IInventory craftResult = new InventoryCraftResult();
private World worldObj;
private int posX;
private int posY;
private int posZ;
public ContainerWireMill(InventoryPlayer inventory, World world, int x, int y, int z)
{
         this.worldObj = world;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
         this.addSlotToContainer(new SlotCrafting(inventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
         int var6;
         int var7;
         for (var6 = 0; var6 < 3; ++var6)
         {
                 for (var7 = 0; var7 < 3; ++var7)
                 {
                         this.addSlotToContainer(new Slot(this.craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
                 }
         }
         for (var6 = 0; var6 < 3; ++var6)
         {
                 for (var7 = 0; var7 < 9; ++var7)
                 {
                         this.addSlotToContainer(new Slot(inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
                 }
         }
         for (var6 = 0; var6 < 9; ++var6)
         {
                 this.addSlotToContainer(new Slot(inventory, var6, 8 + var6 * 18, 142));
         }
         this.onCraftMatrixChanged(this.craftMatrix);
}
/**
         * Callback for when the crafting matrix is changed.
         */
public void onCraftMatrixChanged(IInventory inventory)
{
         this.craftResult.setInventorySlotContents(0, CraftingEtcher.getInstance().findMatchingRecipe(this.craftMatrix));
}
/**
         * Callback for when the crafting gui is closed.
         */
public void onCraftGuiClosed(EntityPlayer player)
{
         super.onCraftGuiClosed(player);
         if (!this.worldObj.isRemote)
         {
                 for (int var2 = 0; var2 < 9; ++var2)
                 {
                         ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);
                         if (var3 != null)
                         {
                                 player.dropPlayerItem(var3);
                         }
                 }
         }
}
public boolean canInteractWith(EntityPlayer player)
{
         return this.worldObj.getBlockId(this.posX, this.posY, this.posZ) != ElectricExpansion.blockWireMill.blockID ? false : player.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
}

/**
         * Called to transfer a stack from one inventory to the other eg. when shift clicking.
         */
public ItemStack transferStackInSlot(int slotNumber)
{
         ItemStack itemstack2 = null;
         Slot slot = (Slot)this.inventorySlots.get(slotNumber);
         if (slot != null && slot.getHasStack())
         {
                 ItemStack itemstack = slot.getStack();
                 itemstack2 = itemstack.copy();
                 if (slotNumber == 0)
                 {
                         if (!this.mergeItemStack(itemstack, 10, 46, true))
                         {
                                 return null;
                         }
                         slot.onSlotChange(itemstack, itemstack2);
                 }
                 else if (slotNumber >= 10 && slotNumber < 37)
                 {
                         if (!this.mergeItemStack(itemstack, 37, 46, false))
                         {
                                 return null;
                         }
                 }
                 else if (slotNumber >= 37 && slotNumber < 46)
                 {
                         if (!this.mergeItemStack(itemstack, 10, 37, false))
                         {
                                 return null;
                         }
                 }
                 else if (!this.mergeItemStack(itemstack, 10, 46, false))
                 {
                         return null;
                 }
                 if (itemstack.stackSize == 0)
                 {
                         slot.putStack((ItemStack)null);
                 }
                 else
                 {
                         slot.onSlotChanged();
                 }
                 if (itemstack.stackSize == itemstack2.stackSize)
                 {
                         return null;
                 }
                 slot.onPickupFromSlot(itemstack);
         }
         return itemstack2;
}
}