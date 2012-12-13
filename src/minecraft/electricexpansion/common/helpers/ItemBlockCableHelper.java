package electricexpansion.common.helpers;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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

	public String getItemNameIS(ItemStack i)
	{
		String name = null;
		int j = i.getItemDamage();
		switch (j)
		{
			case 0:
				name = "Copper";
				break;
			case 1:
				name = "Tin";
				break;
			case 2:
				name = "Silver";
				break;
			case 3:
				name = "HV";
				break;
			case 4:
				name = "Endium";
				break;
			case 5:
				name = "Connector";
				break;
			default:
				name = "Unknown";
				break;
		}
		return i.getItem().getItemName() + "." + name;
	}
}
