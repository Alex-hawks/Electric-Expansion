package electricexpansion.common.items;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemParts extends Item
{
	private static String[] names = { 
		"DrawPlates", "CondensedElectrumDust", "ElectrumIngot", 
		"RawHVAlloy", "HVAlloyIngot", "Unknown", "Insulation"};
	private Icon[] icons = new Icon[names.length];
	
	public ItemParts(int par1, int meta)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("Parts");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public Icon getIconFromDamage(int meta)
	{
		return this.icons[meta];
	}
	
	@Override
	public void func_94581_a(IconRegister par1IconRegister)
	{
		for (int i =0; i < names.length; i++)
		{
			this.icons[i] = par1IconRegister.func_94245_a(ElectricExpansion.TEXTURE_NAME_PREFIX + names[i]);
		}
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack i)
	{
		return i.getItem().getUnlocalizedName() + "." + (i.getItemDamage() < names.length ? names[i.getItemDamage()] : "Unknown");
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 7; var4++)
			par3List.add(new ItemStack(this, 1, var4));
		par3List.remove(new ItemStack(this, 1, 5));
	}
}
