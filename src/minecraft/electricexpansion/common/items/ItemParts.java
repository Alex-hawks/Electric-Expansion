package electricexpansion.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemParts extends Item
{
	public ItemParts(int par1, int meta)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setItemName("Parts");
		this.setCreativeTab(EETab.INSTANCE);
		this.setMaxStackSize(64);
		this.setIconIndex(48);
		this.setTextureFile(ElectricExpansion.MATT_ITEM_TEXTURE_FILE);
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

	@Override
	public String getItemNameIS(ItemStack i)
	{
		String name = "Unknown";
		int j = i.getItemDamage();
		switch (j)
		{
			case 0:
				name = "DrawPlates";
				break;
			case 1:
				name = "RawSuperConductorAlloy";
				break;
			case 2:
				name = "SuperConductorAlloyIngot";
				break;
			case 3:
				name = "RawHVAlloy";
				break;
			case 4:
				name = "HVAlloyIngot";
				break;
		}
		return i.getItem().getItemName() + "." + name;
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; var4++)
			par3List.add(new ItemStack(this, 1, var4));
	}
}
