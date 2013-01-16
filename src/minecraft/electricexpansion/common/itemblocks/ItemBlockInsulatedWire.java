package electricexpansion.common.itemblocks;

import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockInsulatedWire extends ItemBlockCableHelper
{
	public ItemBlockInsulatedWire(int id)
	{
		super(id);
	}

	public int getIconFromDamage(int i)
	{
		return i + 96;
	}
}