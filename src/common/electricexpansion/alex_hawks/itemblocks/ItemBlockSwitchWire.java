package electricexpansion.alex_hawks.itemblocks;

import electricexpansion.alex_hawks.helpers.ItemBlockCableHelper;
import net.minecraft.src.Block;

public class ItemBlockSwitchWire extends ItemBlockCableHelper 
{
	public ItemBlockSwitchWire(int id)
	{super(id);}
	
	public int getIconFromDamage(int i)
	{return i + 32;}
}