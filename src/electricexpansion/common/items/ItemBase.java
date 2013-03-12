package electricexpansion.common.items;

import net.minecraft.item.Item;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemBase extends Item
{
	public ItemBase(int id, String name)
	{
		super(id);
		this.setCreativeTab(EETab.INSTANCE);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName(name);
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.ITEM_FILE;
	}
}