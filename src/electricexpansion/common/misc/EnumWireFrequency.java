package electricexpansion.common.misc;

public enum EnumWireFrequency
{
    NONE(-1),
    
    BLACK(0),
    RED(1),
    DARK_GREEN(2),
    BROWN(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    LIGHT_GRAY(8),
    PINK(9),
    LIGHT_GREEN(10),
    YELLOW(11),
    LIGHT_BLUE(12),
    MAGENTA(13),
    ORANGE(14), 
    WHITE(15);
    
    private final byte index;
    private static EnumWireFrequency[] indexToName = new EnumWireFrequency[16];
    
    private EnumWireFrequency(byte i)
    {
        this.index = i;
    }
    
    private EnumWireFrequency(int i)
    {
        this((byte) i);
    }
    
    public byte getIndex()
    {
        return this.index;
    }
    
    public static final EnumWireFrequency getFromIndex(byte i)
    {
        if (i >= 0 && i < indexToName.length)
        {
            return indexToName[i];
        }
        else return NONE;
    }
}
