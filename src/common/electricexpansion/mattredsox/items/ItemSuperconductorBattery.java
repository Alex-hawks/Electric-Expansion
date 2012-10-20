package electricexpansion.mattredsox.items;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import universalelectricity.BasicComponents;
import universalelectricity.basiccomponents.ItemBattery;
import universalelectricity.prefab.ItemElectric;

/**
 *
 * @author Matt
 */
public class ItemSuperconductorBattery extends ItemBattery
{
    public ItemSuperconductorBattery(int par1, int par2)
    {
        super(par1, par2);
        this.iconIndex = par2;
        this.setItemName("Superconductor Magnet Battery");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, List par2List)
    {
        super.addInformation(par1ItemStack, par2List);
    }

    @Override
    public double getMaxJoules()
    {
        return 3000000;
    }

    @Override
    public boolean canProduceElectricity()
    {
        return true;
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.ITEM_TEXTURE_FILE;
    }

    @Override
    public double getVoltage()
    {
        return 50;
    }
}
