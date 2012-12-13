package electricexpansion.items;

import electricexpansion.EECommonProxy;
import net.minecraft.src.Item;

public class ItemTransformerCoil extends Item
{

	public ItemTransformerCoil(int par1) {
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
