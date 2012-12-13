package electricexpansion.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemConnectorAlloy extends Item
{
	public ItemConnectorAlloy(int ID, int meta) 
	{
		super(ID);
		this.setIconIndex(0);
		this.setMaxStackSize(64);
		this.setHasSubtypes(true);
		this.setItemName("ConnectionAlloy");
		this.setCreativeTab(CreativeTabs.tabMaterials);
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
			case 0:	name = "Clump";
					break;
			case 1: name = "Ingot";
					break;
		}
		return i.getItem().getItemName() + name;
    }
}
