package electricexpansion.api.hive;

public enum HiveSignalTemplate
{
    /** @see electricexpansion.api.hive.HiveSignal.DEVICE_TYPE */
    SENDER_TYPE(1),
    /** @see electricexpansion.api.hive.HiveSignal.DEVICE_TYPE */
    DESTINATION_TYPE(1),
    /** The combination of SENDER_TYPE and SENDER_ID must be unique to the network, easiest done by making it globally, or at least dimension, unique */
    SENDER_ID(1),
    /** The combination of DESTINATION_TYPE and DESTINATION_ID must be unique to the network, easiest done by making it globally, or at least dimension, unique */
    DESTINATION_ID(1),
    /** @see electricexpansion.api.hive.HiveSignal.PACKET_TYPE */
    PACKET_TYPE(1),
    
    /** can have a length of 0, EOS exceptions should be caught */
    DATA(251);
    
    private HiveSignalTemplate(int maxSizeInBytes) { }
}
