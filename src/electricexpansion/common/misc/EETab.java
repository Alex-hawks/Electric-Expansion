package electricexpansion.common.misc;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class EETab extends CreativeTabs
{
    public static final EETab INSTANCE = new EETab("ElectricExpansion");
    private ItemStack itemStack;
    
    public EETab(String par2Str)
    {
        super(CreativeTabs.getNextID(), par2Str);
        LanguageRegistry.instance().addStringLocalization(
                "itemGroup.ElectricExpansion", "en_US", "Electric Expansion");
    }
    
    public void setItemStack(ItemStack newItemStack)
    {
        if (this.itemStack == null)
        {
            this.itemStack = newItemStack;
        }
    }
    
    @Override
    public ItemStack getIconItemStack()
    {
        if (this.itemStack == null)
            return new ItemStack(Block.blocksList[this.getTabIconItemIndex()]);
        
        return this.itemStack;
    }
}
