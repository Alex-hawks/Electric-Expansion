package electricexpansion.common.itemblocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockSwitchWire extends ItemBlockCableHelper
{
	public ItemBlockSwitchWire(int id)
	{
		super(id);
	}

	public int getIconFromDamage(int i)
	{
		return i + 112;
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

		par3List.add("Toggles connectivity when redstone powered");
	}
}