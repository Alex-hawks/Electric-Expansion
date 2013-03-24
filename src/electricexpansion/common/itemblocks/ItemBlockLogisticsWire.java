package electricexpansion.common.itemblocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockLogisticsWire extends ItemBlockCableHelper
{
	public ItemBlockLogisticsWire(int id)
	{
		super(id);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add("Can do awesome things");
	}
}