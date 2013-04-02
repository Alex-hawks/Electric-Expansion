package electricexpansion.api;

import net.minecraft.item.ItemStack;

public interface IItemFuse
{
    /**
     * @param itemStack
     *            The ItemStack, to provide the metadata to the method.
     * @return The maximum amperage. If the amperage is above this,
     *         {@link IItemFuse#onFuseTrip} will be called. </br>
     *         {@link IItemFuse#onFuseTrip} will not be called if the amps does
     *         not go above this.
     */
    public double getMaxVolts(ItemStack itemStack);
    
    /**
     * Called when the fuse is tripped by amperage.
     * 
     * @param itemStack
     *            The ItemStack, to provide the metadata to the method.
     * @return The ItemStack that the good fuse will be replaced with.
     */
    public ItemStack onFuseTrip(ItemStack itemStack);
    
    /**
     * @param itemStack
     *            The ItemStack, to provide the metadata to the method.
     * @return false if this is a bad fuse. Useful for circuit-breakers, so that
     *         you only need one ID
     */
    public boolean isValidFuse(ItemStack itemStack);
    
    /**
     * @param itemStack
     *            The ItemStack, to provide the metadata to the method.
     * @return true if there should be a reset button in the GUI of the Fuse
     *         Box. </br> If true, clicking the "Reset" button will call
     *         {@link IItemFuse#onReset}
     */
    public boolean canReset(ItemStack itemStack);
    
    /**
     * Called when the "Reset" button is clicked in the Fuse Box GUI
     * 
     * @param itemStack
     *            The ItemStack, to provide the metadata to the method.
     * @return The ItemStack that the bad fuse will be replaced with.
     */
    public ItemStack onReset(ItemStack itemStack);
    
    /**
     * This is a copy of
     * {@link net.minecraft.item.Item#getUnlocalizedName(ItemStack)} </br> It is
     * just for convenience in my code.
     */
    public String getUnlocalizedName(ItemStack itemStack);
}
