package electricexpansion.api;

/**
 * Applied to TileEntities. Gets the wire information based on the block's
 * metadata.
 * 
 * @author Calclavia
 * 
 */
public interface IAdvancedConductor
{
    public EnumWireMaterial getWireMaterial(int metadata);
    
    public EnumWireType getWireType(int metadata);
}