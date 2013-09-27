package electricexpansion.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.item.ItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

public class ItemMultimeter extends ItemElectric
{
    public final float JOULES_PER_USE = 5;
    
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
                
//                ElectricityPack getProduced = wireTile.getNetwork().getProduced();
//                
//                player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", "
//                        + ElectricityDisplay.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", " + ElectricityDisplay.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
                
                if (ElectricExpansion.debugRecipes)
                    player.addChatMessage(wireTile.getNetwork().toString());
                
                return true;
            }
            else
            {
                if (te instanceof IElectricalStorage)
                {
                    IElectricalStorage tileStorage = (IElectricalStorage) te;
                    player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(tileStorage.getEnergyStored(), ElectricUnit.JOULES) + "/"
                            + ElectricityDisplay.getDisplay(tileStorage.getMaxEnergyStored(), ElectricUnit.JOULES));
                }
                if (te instanceof IElectrical)
                {
                    player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(((IElectrical) te).getVoltage(), ElectricUnit.VOLTAGE));
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
        if (this.getElectricityStored(itemStack) >= this.JOULES_PER_USE)
        {
            this.setElectricity(itemStack, this.getElectricityStored(itemStack) - this.JOULES_PER_USE);
            return true;
        }
        else
            return false;
    }
    
    @Override
    public float getMaxElectricityStored(ItemStack itemStack)
    {
        return 1000000;
    }
    
    @Override
    public float getVoltage(ItemStack itemStack)
    {
        return 35;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.PREFIX));
    }
    
    @Override
    public void setElectricity(ItemStack itemStack, float joules)
    {
        // Saves the frequency in the ItemStack
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        
        double electricityStored = Math.max(Math.min(joules, this.getMaxElectricityStored(itemStack)), 0);
        itemStack.getTagCompound().setDouble("electricity", electricityStored);
        
        /**
         * Sets the damage as a percentage to render the bar properly.
         */
        itemStack.setItemDamage((int) (this.getMaxDamage() - electricityStored / this.getMaxElectricityStored(itemStack) * this.getMaxDamage()));
    }
    
    @Override
    public float getElectricityStored(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
            return 0;
        
        float electricityStored = itemStack.getTagCompound().getFloat("electricity");
        
        /**
         * Sets the damage as a percentage to render the bar properly.
         */
        itemStack.setItemDamage((int) (this.getMaxDamage() - electricityStored / this.getMaxElectricityStored(itemStack) * this.getMaxDamage()));
        return electricityStored;
    }
}
