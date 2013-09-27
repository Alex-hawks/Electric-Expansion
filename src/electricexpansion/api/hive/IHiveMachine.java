package electricexpansion.api.hive;

import java.util.EnumSet;

import net.minecraftforge.common.ForgeDirection;

import universalelectricity.core.block.IElectrical;

public interface IHiveMachine extends IHiveNetworkMember, IElectrical
{
    public int getSerialQuantity();
    
    public int getInputQuantity();
    
    public int getOutputQuantity();
    
    /**
     * This is a copy of {@link universalelectricity.prefab.tile.TileEntityElectrical#getInputDirections() 
     * TileEntityElectrical.getInputDirections()} to make things easier...
     * <br />   Refer to that method for proper documentation.
     */
    public EnumSet<ForgeDirection> getInputDirections();
    
    /**
     * This is a copy of {@link universalelectricity.prefab.tile.TileEntityElectrical#getOutputDirections() 
     * TileEntityElectrical.getOutputDirections()} to make things easier...
     * <br />   Refer to that method for proper documentation.
     */
    public EnumSet<ForgeDirection> getOutputDirections();
    
    /**
     * The Hive Network Serial Connection directions.
     * 
     * @return  The direction that connections can be made on from the tile. Return null, or an empty EnumSet for no valid sides.
     *          The directions specified here can overlap with input and output directions.
     */
    public EnumSet<ForgeDirection> getSerialDirections();
}
