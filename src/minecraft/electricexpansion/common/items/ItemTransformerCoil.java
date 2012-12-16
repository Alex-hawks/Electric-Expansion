package electricexpansion.common.items;

import net.minecraft.item.Item;
import electricexpansion.common.CommonProxy;

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
		return CommonProxy.MattItem_TEXTURE_FILE;
	}

}
