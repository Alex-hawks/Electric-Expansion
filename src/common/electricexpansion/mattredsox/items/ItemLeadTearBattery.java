package electricexpansion.mattredsox.items;

import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;

public class ItemLeadTearBattery extends ItemElectric
{
    public ItemLeadTearBattery(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Lead Tear Battery");
        this.setCreativeTab(UETab.INSTANCE);
    }

    @Override
    public double getMaxJoules(Object... data)
    {
        return 750000;
    }

    @Override
    public boolean canProduceElectricity()
    {
        return true;
    }

 //   @Override
 //   public String getTextureFile()
 //   {
 //       return BasicComponents.ITEM_TEXTURE_FILE;
   // }

    @Override
    public double getVoltage()
    {
        return 50;
    }
}
