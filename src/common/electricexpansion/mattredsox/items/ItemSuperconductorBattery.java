package electricexpansion.mattredsox.items;

import universalelectricity.prefab.ItemElectric;

public class ItemSuperconductorBattery extends ItemElectric
{
    public ItemSuperconductorBattery(int par1, int par2)
    {
        super(par1);
        this.iconIndex = par2;
        this.setItemName("Superconductor Battery");
    }

    @Override
    public double getMaxJoules(Object... data)
    {
        return 3000000;
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
