package electricexpansion.itemblocks;

import electricexpansion.helpers.ItemBlockCableHelper;

public class ItemBlockRawWire extends ItemBlockCableHelper
{
	public ItemBlockRawWire(int id)
	{
		super(id);
		setItemName("RawWire");
	}

	public int getIconFromDamage(int i)
	{
		return i;
	}
}
