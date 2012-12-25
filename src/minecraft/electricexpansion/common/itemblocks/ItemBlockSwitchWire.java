package electricexpansion.common.itemblocks;

import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockSwitchWire extends ItemBlockCableHelper
{
	public ItemBlockSwitchWire(int id)
	{
		super(id);
	}

	public int getIconFromDamage(int i)
	{
		return i + 32;
	}
}