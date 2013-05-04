package electricexpansion.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ItemLinkCard extends Item
{
    public ItemLinkCard(int par1)
    {
        super(par1);
        this.setUnlocalizedName("LinkCard");
        this.setMaxStackSize(1);
    }
    
    public boolean getHasLinkData(ItemStack is)
    {
        if (is.getTagCompound().hasKey("ElectricExpansion"))
            return is.getTagCompound().getCompoundTag("ElectricExpansion").hasKey("link");
        return false;
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
}
