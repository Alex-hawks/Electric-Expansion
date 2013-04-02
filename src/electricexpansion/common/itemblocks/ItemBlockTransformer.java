package electricexpansion.common.itemblocks;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTransformer extends ItemBlock
{
    public ItemBlockTransformer(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack i)
    {
        String name = null;
        int j = i.getItemDamage();
        int tier = j - (j & 3);
        if (tier == 0)
        {
            name = "2x";
        }
        if (tier == 4)
        {
            name = "4x";
        }
        if (tier == 8)
        {
            name = "8x";
        }
        return i.getItem().getUnlocalizedName() + "." + name;
    }
}
