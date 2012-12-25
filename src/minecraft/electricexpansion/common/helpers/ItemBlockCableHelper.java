package electricexpansion.common.helpers;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import electricexpansion.api.EnumWireMaterial;

public class ItemBlockCableHelper extends ItemBlock
{
	public ItemBlockCableHelper(int id)
	{
		super(id);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	public String getItemNameIS(ItemStack itemStack)
	{
		return this.getItemName() + "." + EnumWireMaterial.values()[itemStack.getItemDamage()].name;
	}
}
