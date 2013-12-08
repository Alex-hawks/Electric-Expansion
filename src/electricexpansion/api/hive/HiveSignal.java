package electricexpansion.api.hive;

public class HiveSignal
{
    public static final class PACKET_TYPE 
    {
        public static final byte BROADCAST                  = (byte) 0b1111_1111;
        public static final byte UNKNOWN                    = 0b0000_0000;
        
        public static final byte SIMPLE_REDSTONE_STATE      = 0b0000_0001;
        public static final byte PART_REDSTONE_STATE        = 0b0000_0010;
        public static final byte FULL_REDSTONE_STATE        = 0b0000_0011;
        public static final byte DISCOVER_REPLY             = 0b0000_0100;

        public static final byte DISCOVER_CC_COMPUTERS      = 0b0001_0000;
        public static final byte TO_CC_COMPUTER             = 0b0001_0001;
        public static final byte FROM_CC_COMPUTER           = 0b0001_0010;
    }
    
    public static final class DEVICE_TYPE
    {
        public static final byte CORE                       = (byte) 0b1111_1111;
        public static final byte UNKNOWN                    = 0b0000_0000;
        
        public static final byte LOGISTICS_WIRE             = 0b0000_0001;
        public static final byte REDSTONE_WIRE              = 0b0000_0010;
        public static final byte ENERGY_STORAGE             = 0b0000_0011;
        
        public static final byte CC_PERIPHERAL              = 0b0001_0000;
    }
}
