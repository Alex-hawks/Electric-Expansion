package electricexpansion.mattredsox;

import electricexpansion.EECommonProxy;
import net.minecraft.src.Item;

public class ItemUpgrade extends Item
{
	public ItemUpgrade(int par1, int par2) 
	{
		super(par1);
		setIconIndex(par2);
	}
	
	@Override
	public String getTextureFile()
	{return EECommonProxy.MattItem_TEXTURE_FILE;}
}
