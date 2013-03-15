package electricexpansion.common.items;

import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.IItemFuse;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemFuse extends Item implements IItemFuse
{
	private Icon[] icons = new Icon[20];
	public ItemFuse(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("fuse");
//		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxVolts(ItemStack itemStack)
	{
		switch (itemStack.getItemDamage())
		{
			//	Fuses
			case 0:		
			case 1:		return 30;
			case 2:		
			case 3:		return 60;
			case 4:		
			case 5:		return 120;
			case 6:		
			case 7:		return 240;
			case 8:		
			case 9:		return 480;
			//	Circuit-Breakers
			case 10:		
			case 11:	return 30;
			case 12:	
			case 13:	return 60;
			case 14:	
			case 15:	return 120;
			case 16:	
			case 17:	return 240;
			case 18:	
			case 19:	return 480;

		}
		return 0;
	}
	
	@Override
	public ItemStack onFuseTrip(ItemStack itemStack)
	{
		ItemStack toReturn = itemStack.copy();
		toReturn.setItemDamage(itemStack.getItemDamage() + 1);
		return toReturn;
	}
	
	@Override
	public boolean isValidFuse(ItemStack itemStack)
	{
		return itemStack.getItemDamage() % 2 == 0;
	}
	
	@Override
	public boolean canReset(ItemStack itemStack)
	{
		return itemStack.getItemDamage() / 10 == 1;
	}
	
	@Override
	public ItemStack onReset(ItemStack itemStack)
	{
		if (this.canReset(itemStack))
		{
			ItemStack toReturn = itemStack.copy();
			toReturn.setItemDamage(itemStack.getItemDamage() - 1);
			return toReturn;
		}
		return null;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		double volts = this.getMaxVolts(itemStack);
		String type = this.canReset(itemStack) ? (this.isValidFuse(itemStack)? "+cb" : "-cb") : (this.isValidFuse(itemStack)? "+f" : "-f");
		return this.getUnlocalizedName() + "." + type + "." + ((int)volts);
	}
	
	@Override
	public Icon getIconFromDamage(int meta)
	{
		return this.icons[meta];
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 20; var4++)
			par3List.add(new ItemStack(this, 1, var4));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister)
	{
		for (int i = 0; i < this.icons.length; i++)
			this.icons[i] = par1IconRegister.func_94245_a(this.getUnlocalizedName(new ItemStack(this.itemID, 0, i)).replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
	}
}
