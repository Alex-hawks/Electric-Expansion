package electricexpansion.common.items;

import universalelectricity.core.item.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemUltimateBattery extends ItemElectric
{
	public ItemUltimateBattery(int par1)
	{
		super(par1);
		this.iconIndex = 4;
		this.setItemName("UltimateBattery");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return 5000000;
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
		return 75;
	}

}
