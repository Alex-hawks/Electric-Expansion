package electricexpansion.common.items;

import net.minecraft.item.Item;
import electricexpansion.common.EECommonProxy;

public class ItemTransformerCoil extends Item
{

	public ItemTransformerCoil(int par1)
	{
		super(par1);
		this.iconIndex = 1;
		this.setItemName("transformerCoil");
	}

	@Override
	public String getTextureFile()
	{
		return EECommonProxy.MattItem_TEXTURE_FILE;
	}

}
