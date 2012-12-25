package electricexpansion.common.itemblocks;

import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockSwitchWire extends ItemBlockCableHelper
{
	public ItemBlockSwitchWire(int id)
	{
		super(id);
		this.setIconIndex(32);
	}

	public int getIconFromDamage(int i)
	{
		return i + this.iconIndex;
	}
}