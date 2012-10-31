package electricexpansion.mattredsox.items;

import electricexpansion.EECommonProxy;
import net.minecraft.src.Item;

public class ItemUpgrade extends Item
{
	public ItemUpgrade(int par1, int par2) 
	{
		super(par1);
		this.setIconIndex(par2);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getTextureFile()
	{return EECommonProxy.MattItem_TEXTURE_FILE;}
}
