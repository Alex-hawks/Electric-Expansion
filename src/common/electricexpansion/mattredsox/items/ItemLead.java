package electricexpansion.mattredsox.items;

import electricexpansion.EECommonProxy;
import net.minecraft.src.Item;

public class ItemLead extends ItemBase

{

	public ItemLead(int par1, int par2) {
		super(par1, par2);
		this.setIconIndex(par2);
	}

	    @Override
	    public String getTextureFile()
	    {
	       return EECommonProxy.MattItem_TEXTURE_FILE;
	   }
}
