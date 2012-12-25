package electricexpansion.common.items;

import net.minecraft.item.Item;
import universalelectricity.prefab.UETab;
import electricexpansion.common.ElectricExpansion;

public class ItemBase extends Item
{
	public ItemBase(int id, int texture)
	{
		super(id);
		this.setIconIndex(texture);
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.MATT_ITEM_TEXTURE_FILE;
	}
}