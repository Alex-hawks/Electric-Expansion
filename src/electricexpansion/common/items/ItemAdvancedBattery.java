package electricexpansion.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemAdvancedBattery extends ItemElectric
{
    public ItemAdvancedBattery(int par1)
    {
        super(par1);
        this.setUnlocalizedName("AdvancedBattery");
        this.setCreativeTab(EETab.INSTANCE);
    }
    
    @Override
    public float getMaxElectricityStored(ItemStack itemStack)
    {
        return 2000000;
    }
    
    @Override
    public float getVoltage(ItemStack itemStack)
    {
        return 30;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.PREFIX));
    }
}
