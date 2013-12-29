package electricexpansion.common.misc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils
{
    public static EnumColor isDye(ItemStack is)
    {
        String[] dyes =
        {
            "dyeBlack",
            "dyeRed",
            "dyeGreen",
            "dyeBrown",
            "dyeBlue",
            "dyePurple",
            "dyeCyan",
            "dyeLightGray",
            "dyeGray",
            "dyePink",
            "dyeLime",
            "dyeYellow",
            "dyeLightBlue",
            "dyeMagenta",
            "dyeOrange",
            "dyeWhite"
        };
        
        for (int i = 0; i < dyes.length; i++)
        {
            if (OreDictionary.getOreID(is) != -1 && OreDictionary.getOreName(OreDictionary.getOreID(is)).equals(dyes[i]))
                return EnumColor.getFromIndex((byte) i);
        }
        
        return EnumColor.NONE;
    }
}
