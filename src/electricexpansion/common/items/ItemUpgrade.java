package electricexpansion.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.IModifier;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemUpgrade extends Item implements IModifier
{
    private String[] names = new String[] { "Storage1", "Storage2", "Storage3", "Storage4", "HalfVoltage", "HVUpgrade", "HVInputUpgrade", "DoubleVoltage", "Unlimiter1", "Unlimiter2", "Unlimiter3",
            "Unlimiter4", "Pnematic", "CrossDimension", "Mekanism", "Factorization", "Quantum" };
    
    private Icon[] icons = new Icon[this.names.length];
    
    @SideOnly(Side.CLIENT)
    private Icon defaultIcon;
    
    public ItemUpgrade(int id, int texture)
    {
        super(id);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("Upgrade");
    }
    
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return this.getUnlocalizedName() + "." + this.names[itemstack.getItemDamage()];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int i)
    {
        if (i <= this.icons.length)
            return this.icons[i];
        return this.defaultIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < this.names.length; i++)
        {
            par3List.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    public String getType(ItemStack itemstack)
    {
        switch (itemstack.getItemDamage())
        {
            case 0:
            case 1:
            case 2:
            case 3:
                return "Capacity";
            case 4:
            case 5:
                return "VoltageModifier";
            case 6:
                return "InputVoltageModifier";
            case 7:
                return "VoltageModifier"; // Seperate because it was added at a
                                          // later point in time
            case 8:
            case 9:
            case 10:
            case 11:
                return "Unlimiter";
            case 12:
                return "Pnematic";
            case 13:
                return "CrossDimension";
            case 14:
                return "Mekanism";
            case 15:
                return "Factorization";
            case 16:
                return "Quantum";
            default:
                return "Unknown";
        }
    }
    
    @Override
    public double getEffectiveness(ItemStack itemstack)
    {
        switch (itemstack.getItemDamage())
        {
            case 0:
                return 1000000;
            case 1:
                return 2000000;
            case 2:
                return 3000000;
            case 3:
                return 5000000;
            case 4:
                return 0.5;
            case 5:
                return 20;
            case 6:
                return 20;
            case 7:
                return 2;
            case 8:
                return 5;
            case 9:
                return 10;
            case 10:
                return 20;
            case 11:
                return 40;
            default:
                return 0;
        }
    }
    
    /**
     * Allows items to add custom lines of information to the mouseover
     * description. If you want to add more information to your item, you can
     * super.addInformation() to keep the electiricty info in the item info bar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemstack, EntityPlayer player, List par3List, boolean par4)
    {
        String strength = "";
        double effectiveness = this.getEffectiveness(itemstack);
        if (this.getType(itemstack).equals("Capacity"))
        {
            strength = ElectricityDisplay.getDisplay(this.getEffectiveness(itemstack), ElectricUnit.JOULES);
        }
        else
        {
            if (effectiveness < 0)
            {
                strength = "1/" + String.valueOf(effectiveness * -1);
            }
            else
            {
                strength = effectiveness + "";
            }
        }
        par3List.add("\u00a72" + StatCollector.translateToLocal("upgrades.description." + this.getType(itemstack)).replaceAll("<>", strength));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < this.names.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + this.names[i]);
        }
    }
    
    @Override
    public int getTier(ItemStack i)
    {
        switch (i.getItemDamage())
        {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 0;
            case 5:
                return 1;
            case 6:
                return 1;
            case 7:
                return 0;
            case 8:
                return 0;
            case 9:
                return 1;
            case 10:
                return 2;
            case 11:
                return 3;
        }
        return -1;
    }
}
