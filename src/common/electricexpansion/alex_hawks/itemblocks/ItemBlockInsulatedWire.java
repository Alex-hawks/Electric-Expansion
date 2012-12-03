package electricexpansion.alex_hawks.itemblocks;

import java.util.logging.Logger;

import electricexpansion.alex_hawks.helpers.ItemBlockCableHelper;
import net.minecraft.src.Block;

public class ItemBlockInsulatedWire extends ItemBlockCableHelper 
{
	public ItemBlockInsulatedWire(int id)
	{super(id); }
	
	public int getIconFromDamage(int i)
	{return i + 16;}
}