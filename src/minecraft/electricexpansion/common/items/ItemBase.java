package electricexpansion.common.items;

import net.minecraft.item.Item;
import universalelectricity.prefab.UETab;
import electricexpansion.common.CommonProxy;

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
		return CommonProxy.MattItem_TEXTURE_FILE;
	}
}