package electricexpansion.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemBase extends Item
{
	public ItemBase(int id, String name)
	{
		super(id);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName(name);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister par1IconRegister)
    {
		this.iconIndex = par1IconRegister.func_94245_a(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
    }
}