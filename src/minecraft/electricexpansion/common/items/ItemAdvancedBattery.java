package electricexpansion.common.items;

import universalelectricity.prefab.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemAdvancedBattery extends ItemElectric
{
	public ItemAdvancedBattery(int par1)
	{
		super(par1);
		this.iconIndex = 9;
		this.setItemName("AdvancedBattery");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return 100000;
	}

	@Override
	public boolean canProduceElectricity()
	{
		return true;
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.MATT_ITEM_TEXTURE_FILE;
	}

	@Override
	public double getVoltage(Object... data)
	{
		return 35;
	}
}
