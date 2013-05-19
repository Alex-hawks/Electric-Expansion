package electricexpansion.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

public class ItemMultimeter extends ItemElectric
{
    public final int JOULES_PER_USE = 5000;
    
    public ItemMultimeter(int par1)
    {
        super(par1);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("Multimeter");
        this.setMaxDamage(200);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!worldObj.isRemote && this.onUse(stack))
        {
            TileEntity te = worldObj.getBlockTileEntity(x, y, z);
            
            if (te instanceof IConductor)
            {
                IConductor wireTile = (IConductor) te;
                
                ElectricityPack getProduced = wireTile.getNetwork().getProduced();
                
                player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", "
                        + ElectricityDisplay.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", " + ElectricityDisplay.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
                
                if (ElectricExpansion.debugRecipes)
                    player.addChatMessage(wireTile.getNetwork().toString());
                
                return true;
            }
            else
            {
                if (te instanceof IElectricityStorage)
                {
                    IElectricityStorage tileStorage = (IElectricityStorage) te;
                    player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(tileStorage.getJoules(), ElectricUnit.JOULES) + "/"
                            + ElectricityDisplay.getDisplay(tileStorage.getMaxJoules(), ElectricUnit.JOULES));
                }
                if (te instanceof IVoltage)
                {
                    player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(((IVoltage) te).getVoltage(), ElectricUnit.VOLTAGE));
                }
                
                if (te instanceof TileEntityAdvancedBatteryBox && ElectricExpansion.debugRecipes)
                {
                    TileEntityAdvancedBatteryBox te2 = (TileEntityAdvancedBatteryBox) te;
                    player.addChatMessage("Electric Expansion: Input;  " + te2.getInputMode());
                    player.addChatMessage("Electric Expansion: Output; " + te2.getOutputMode());
                }
                
                return true;
            }
        }
        
        return false;
    }
    
    private boolean onUse(ItemStack itemStack)
    {
        if (this.getJoules(itemStack) >= this.JOULES_PER_USE)
        {
            this.setJoules(this.getJoules(itemStack) - this.JOULES_PER_USE, itemStack);
            return true;
        }
        else
            return false;
    }
    
    @Override
    public double getMaxJoules(ItemStack itemStack)
    {
        return 1000000;
    }
    
    @Override
    public double getVoltage(ItemStack itemStack)
    {
        return 35;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
    }
    
    @Override
    public void setJoules(double joules, ItemStack itemStack)
    {
        // Saves the frequency in the ItemStack
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        
        double electricityStored = Math.max(Math.min(joules, this.getMaxJoules(itemStack)), 0);
        itemStack.getTagCompound().setDouble("electricity", electricityStored);
        
        /**
         * Sets the damage as a percentage to render the bar properly.
         */
        itemStack.setItemDamage((int) (this.getMaxDamage() - electricityStored / this.getMaxJoules(itemStack) * this.getMaxDamage()));
    }
    
    @Override
    public double getJoules(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
            return 0;
        
        double electricityStored = itemStack.getTagCompound().getDouble("electricity");
        
        /**
         * Sets the damage as a percentage to render the bar properly.
         */
        itemStack.setItemDamage((int) (this.getMaxDamage() - electricityStored / this.getMaxJoules(itemStack) * this.getMaxDamage()));
        return electricityStored;
    }
}
