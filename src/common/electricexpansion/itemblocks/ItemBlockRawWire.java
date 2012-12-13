package electricexpansion.alex_hawks.itemblocks;

import electricexpansion.alex_hawks.helpers.ItemBlockCableHelper;
import net.minecraft.src.Block;

public class ItemBlockRawWire extends ItemBlockCableHelper 
{
	public ItemBlockRawWire(int id)
	{
			super(id);
			setItemName("RawWire");
	}

	public int getIconFromDamage(int i)
	{return i;}
}
