package electricexpansion.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.modifier.IModifier;
import electricexpansion.common.ElectricExpansion;

public class ItemUpgrade extends Item implements IModifier
{
	private String[] names = new String[] { "Storage1", "Storage2", "Storage3" };

	public ItemUpgrade(int id, int texture)
	{
		super(id);
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		return getItemName() + "." + names[itemstack.getItemDamage()];
	}

	@Override
	public int getIconFromDamage(int i)
	{
		if (i == 0)

		{ return 5; }
		if (i == 1) { return 8; }
		if (i == 2) { return 4; }
		if (i == 3) { return 7; }
		return 6;

	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.MATT_ITEM_TEXTURE_FILE;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < names.length; i++)
		{
			par3List.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getName(ItemStack itemstack)
	{
		if (itemstack.getItemDamage() == 0) { return "Capacity"; }
		if (itemstack.getItemDamage() == 1) { return "Capacity"; }
		if (itemstack.getItemDamage() == 2) { return "Capacity"; }

		return null;
	}

	@Override
	public int getEffectiveness(ItemStack itemstack)
	{
		if (itemstack.getItemDamage() == 0) { return 1000000; }
		if (itemstack.getItemDamage() == 1) { return 2000000; }
		if (itemstack.getItemDamage() == 2) { return 3000000; }

		return 0;
	}
}
