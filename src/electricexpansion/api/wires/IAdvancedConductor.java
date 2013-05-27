package electricexpansion.api.wires;

import universalelectricity.core.block.IConductor;

/**
 * Applied to TileEntities. Gets the wire information based on the block's
 * metadata.
 * 
 * @author Calclavia
 * 
 */
public interface IAdvancedConductor extends IConductor
{
    public EnumWireMaterial getWireMaterial(int metadata);
    
    public EnumWireType getWireType(int metadata);
}