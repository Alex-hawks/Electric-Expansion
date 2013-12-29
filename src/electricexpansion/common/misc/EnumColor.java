package electricexpansion.common.misc;

public enum EnumColor
{
    NONE(-1),
    
    BLACK(0),
    RED(1),
    DARK_GREEN(2),
    BROWN(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    LIGHT_GRAY(7),
    GRAY(8),
    PINK(9),
    LIGHT_GREEN(10),
    YELLOW(11),
    LIGHT_BLUE(12),
    MAGENTA(13),
    ORANGE(14),
    WHITE(15);
    
    private final byte                 index;
    private static EnumColor[] indexToName =
                                                   {
                                                   BLACK, RED, DARK_GREEN, BROWN,
                                                   BLUE, PURPLE, CYAN, LIGHT_GRAY,
                                                   GRAY, PINK, LIGHT_GREEN, YELLOW,
                                                   LIGHT_BLUE, MAGENTA, ORANGE, WHITE
                                                   };
    
    private EnumColor(byte i)
    {
        this.index = i;
    }
    
    private EnumColor(int i)
    {
        this((byte) i);
    }
    
    public byte getIndex()
    {
        return this.index;
    }
    
    public static final EnumColor getFromIndex(byte i)
    {
        if (i >= 0 && i < indexToName.length)
        {
            return indexToName[i];
        }
        else
            return NONE;
    }
}
