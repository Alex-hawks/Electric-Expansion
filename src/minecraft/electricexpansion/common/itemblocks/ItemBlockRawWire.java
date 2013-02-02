package electricexpansion.common.itemblocks;

import java.util.List;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockRawWire extends ItemBlockCableHelper
{
	public ItemBlockRawWire(int id)
	{
		super(id);
		setItemName("RawWire");
	}

	public int getIconFromDamage(int i)
	{
		return i + 80;
	}
	
	/**
	 * Allows items to add custom lines of information to the mouseover description. If you want to
	 * add more information to your item, you can super.addInformation() to keep the electiricty
	 * info in the item info bar.
	 */
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

		par3List.add("Will shock you when touched");
		par3List.add("Shock Damage: " + (double) EnumWireMaterial.values()[par1ItemStack.getItemDamage()].electrocutionDamage / 2 + " Hearts");

	}
}
