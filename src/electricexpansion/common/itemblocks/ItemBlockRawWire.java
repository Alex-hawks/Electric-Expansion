package electricexpansion.common.itemblocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.common.helpers.ItemBlockCableHelper;

public class ItemBlockRawWire extends ItemBlockCableHelper
{
    public ItemBlockRawWire(int id)
    {
        super(id);
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack par1ItemStack,
            EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        
        par3List.add("Will shock you when near");
        par3List.add("Shock Damage: "
                + (double) EnumWireMaterial.values()[par1ItemStack
                        .getItemDamage()].electrocutionDamage / 2 + " Hearts");
        
    }
}
