package electricexpansion.common.misc;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class EETab extends CreativeTabs
{
	public static final EETab INSTANCE = new EETab("ElectricExpansion");
	private static ItemStack itemStack;

	public EETab(String par2Str)
	{
		super(CreativeTabs.getNextID(), par2Str);
		LanguageRegistry.instance().addStringLocalization("itemGroup.ElectricExpansion", "en_US", "Electric Expansion");
	}

	public static void setItemStack(ItemStack newItemStack)
	{
		if (itemStack == null)
		{
			itemStack = newItemStack;
		}
	}

	@Override
	public ItemStack getIconItemStack()
	{
		if (itemStack == null) { return new ItemStack(Block.blocksList[this.getTabIconItemIndex()]); }

		return itemStack;
	}
}
