package electricexpansion.common.itemblocks;

import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockLogisticsWire extends ItemBlockCableHelper
{
	public ItemBlockLogisticsWire(int id)
	{
		super(id);
	}

	public int getIconFromDamage(int i)
	{
		return i + 16;
	}
}