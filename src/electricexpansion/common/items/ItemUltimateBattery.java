package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemUltimateBattery extends ItemElectric
{
	public ItemUltimateBattery(int par1)
	{
		super(par1);
		this.setUnlocalizedName("UltimateBattery");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(ItemStack i)
	{
		return 5000000;
	}

	@Override
	public double getVoltage(ItemStack i)
	{
		return 75;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister)
	{
		this.iconIndex = par1IconRegister.func_94245_a(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
	}
}
