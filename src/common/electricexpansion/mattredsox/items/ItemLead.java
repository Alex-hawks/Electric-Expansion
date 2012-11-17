package electricexpansion.mattredsox.items;

import electricexpansion.EECommonProxy;
import net.minecraft.src.Item;

public class ItemLead extends Item

{

	public ItemLead(int par1, int par2) {
		super(par1);
        this.iconIndex = par2;
	}

	    @Override
	    public String getTextureFile()
	    {
	       return EECommonProxy.MattItem_TEXTURE_FILE;
	   }
}
