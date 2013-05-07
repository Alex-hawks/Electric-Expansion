package electricexpansion.common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;

public class ItemLinkCard extends Item
{
    public ItemLinkCard(int par1)
    {
        super(par1);
        this.setUnlocalizedName("EELinkCard");
        this.setMaxStackSize(1);
    }
    
    public boolean getHasLinkData(ItemStack is)
    {
        try
        {
            if (is.getTagCompound().hasKey("ElectricExpansion"))
                return is.getTagCompound().getCompoundTag("ElectricExpansion").hasKey("link");
            return false;
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }
    
    public NBTTagCompound getOrCreateLinkData(ItemStack is, TileEntity te)
    {
        if (is.getTagCompound() == null || !getHasLinkData(is))
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setCompoundTag("ElectricExpansion", new NBTTagCompound());
            tag.getCompoundTag("ElectricExpansion").setCompoundTag("link", new NBTTagCompound());
            
            NBTTagCompound link = tag.getCompoundTag("ElectricExpansion").getCompoundTag("link");
            link.setInteger("x", te.xCoord);
            link.setInteger("y", te.yCoord);
            link.setInteger("z", te.zCoord);
            link.setInteger("dimension", te.worldObj.provider.dimensionId);
            
            is.setTagCompound(tag);
            return link;
        }
        
        return is.getTagCompound().getCompoundTag("ElectricExpansion").getCompoundTag("link");
    }
    
    public static boolean isDataEqual(NBTTagCompound link, TileEntity te)
    {
        return 
                link.getInteger("x") == te.xCoord &&
                link.getInteger("y") == te.yCoord &&
                link.getInteger("z") == te.zCoord &&
                link.getInteger("dimension") == te.worldObj.provider.dimensionId;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item.",
                ElectricExpansion.TEXTURE_NAME_PREFIX));
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List currentTips, boolean par4)
    {
        if (!getHasLinkData(itemStack))
            currentTips.add("No Data");
        else if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode))
        {
            NBTTagCompound link = itemStack.getTagCompound().getCompoundTag("ElectricExpansion").getCompoundTag("link");
            currentTips.add("X-Coordinate: " + link.getInteger("x"));
            currentTips.add("Y-Coordinate: " + link.getInteger("y"));
            currentTips.add("Z-Coordinate: " + link.getInteger("z"));
            currentTips.add("World: " + link.getInteger("dimension"));
        }
        else
        {
            currentTips.add("Hold Sneak for more information");
        }
        
    }
}
