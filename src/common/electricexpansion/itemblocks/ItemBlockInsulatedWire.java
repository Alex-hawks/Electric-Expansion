package electricexpansion.itemblocks;

import electricexpansion.helpers.ItemBlockCableHelper;

public class ItemBlockInsulatedWire extends ItemBlockCableHelper 
{
	public ItemBlockInsulatedWire(int id)
	{super(id); }
	
	public int getIconFromDamage(int i)
	{return i + 16;}
}