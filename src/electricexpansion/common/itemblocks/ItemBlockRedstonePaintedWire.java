package electricexpansion.common.itemblocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockRedstonePaintedWire extends ItemBlockCableHelper
{
    
    public ItemBlockRedstonePaintedWire(int id)
    {
        super(id);
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        par3List.add("Normal wire, does not shock you");
        par3List.add("Conveys redstone signal, and does it very well.");
    }
}
