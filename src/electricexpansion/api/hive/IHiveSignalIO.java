package electricexpansion.api.hive;


/**
 *  A basic signal transfer. Packets, once sent are dropped, much like UDP. 
 *  Data is sent as a broadcast packet, and must be transferred as a byte array.
 *  If you don't understand that, you won't know how to use this.
 * 
 *  @author Alex_hawks
 *
 */
public interface IHiveSignalIO extends IHiveNetworkMember
{
    /**
     * This is designed to be powerful, and adaptable, not easy...
     */
    public void processData(byte[] data);
    
    public byte getDeviceTypeID();
}
