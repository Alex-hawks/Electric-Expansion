package electricexpansion.alex_hawks.itemblocks;

import electricexpansion.alex_hawks.helpers.ItemBlockCableHelper;
import net.minecraft.src.Block;

public class ItemBlockRawWire extends ItemBlockCableHelper 
{
	public ItemBlockRawWire(int par1, Block mainBlock)
	{super(par1, mainBlock);
	this.setItemName("RawWire");}

	public int getIconFromDamage(int i)
	{return i;}
}
