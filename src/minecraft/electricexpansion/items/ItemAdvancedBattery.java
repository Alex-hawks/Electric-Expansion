package electricexpansion.items;

import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
import electricexpansion.EECommonProxy;

public class ItemAdvancedBattery extends ItemElectric
{
	public ItemAdvancedBattery(int par1)
	{
		super(par1);
		this.iconIndex = 9;
		this.setItemName("AdvancedBattery");
		this.setCreativeTab(UETab.INSTANCE);
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
		return EECommonProxy.MattItem_TEXTURE_FILE;
	}

	@Override
	public double getVoltage()
	{
		return 40;
	}
}
