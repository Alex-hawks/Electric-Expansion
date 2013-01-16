package electricexpansion.common.items;

import net.minecraft.item.Item;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemBase extends Item
{
	public ItemBase(int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.ITEM_FILE;
	}
}