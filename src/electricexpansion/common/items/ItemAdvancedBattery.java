package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemAdvancedBattery extends ItemElectric
{
	public ItemAdvancedBattery(int par1)
	{
		super(par1);
		this.setUnlocalizedName("AdvancedBattery");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 2000000;
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 30;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister par1IconRegister)
    {
		this.iconIndex = par1IconRegister.func_94245_a(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
    }
}
