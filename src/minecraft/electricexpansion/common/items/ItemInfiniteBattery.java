package electricexpansion.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemInfiniteBattery extends Item implements IItemElectric
{
	public ItemInfiniteBattery(int id)
	{
		super(id);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(EETab.INSTANCE);
		this.iconIndex = 3;
		this.setTextureFile(ElectricExpansion.MATT_ITEM_TEXTURE_FILE);
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean canProduceElectricity()
	{
		return true;
	}

	/**
	 * Allows items to add custom lines of information to the mouseover description. If you want to
	 * add more information to your item, you can super.addInformation() to keep the electiricty
	 * info in the item info bar.
	 */
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{

		par3List.add("\u00a72 Infinite");
	}
	
	@Override
	public double getVoltage()
	{
		return 50;
	}
	
	
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack)
	{
		return 0;
	}
	
	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack)
	{
		double electricityToUse = Math.min(this.getJoules(itemStack), joulesNeeded);
		return electricityToUse;
	}
	
	/**
	 * This function sets the electriicty. Do not directly call this function. Try to use
	 * onReceiveElectricity or onUseElectricity instead.
	 * 
	 * @param wattHours - The amount of electricity in joules
	 */
	@Override
	public void setJoules(double wattHours, Object... data)
	{

	}
	
	/**
	 * This function is called to get the electricity stored in this item
	 * 
	 * @return - The amount of electricity stored in watts
	 */
	@Override
	public double getJoules(Object... data)
	{
		return 10000;
	}

	@Override
	public boolean canReceiveElectricity()
	{
		return true;
	}
}
