package electricexpansion.common.items;

import universalelectricity.prefab.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemEliteBattery extends ItemElectric
{
	public ItemEliteBattery(int par1)
	{
		super(par1);
		this.iconIndex = 1;
		this.setItemName("EliteBattery");
		this.setCreativeTab(EETab.INSTANCE);
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

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.ITEM_FILE;
	}

	@Override
	public double getVoltage(Object... data)
	{
		return 45;
	}
}
