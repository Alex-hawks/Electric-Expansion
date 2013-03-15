package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemEliteBattery extends ItemElectric
{
	public ItemEliteBattery(int par1)
	{
		super(par1);
		this.setUnlocalizedName("EliteBattery");
		this.setCreativeTab(EETab.INSTANCE);
	}

	@Override
	public double getMaxJoules(ItemStack i)
	{
		return 3000000;
	}

	@Override
	public double getVoltage(ItemStack i)
	{
		return 45;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister par1IconRegister)
    {
		this.iconIndex = par1IconRegister.func_94245_a(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
    }
}
