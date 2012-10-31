package electricexpansion.alex_hawks.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemParts extends Item 
{
	public ItemParts(int par1, int meta) 
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setItemName("Parts");
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setMaxStackSize(32);
	}
	@Override
    public int getIconFromDamage(int meta)
    {
        return this.iconIndex + meta;
    }
    @Override
	public int getMetadata(int meta)
    {
        return meta;
    }
    public String getItemNameIs(ItemStack i)
    {
		String name = "Unknown";
		int j = i.getItemDamage();
		switch(j)
		{
			case 0:	name = "DrawPlates";
					break;
		}
		return i.getItem().getItemName() + name;
    }

}
