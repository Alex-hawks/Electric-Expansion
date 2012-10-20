
package electricexpansion.api;

import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.api.CableConnectionInterfaces.IPanelElectricMachine;
import universalelectricity.prefab.BlockConductor;

/**
 * 
 * @author Alex_hawks
 * Extend this to make it easier to make neat and tidy cable connections to your slab-sized, panel-sized, etc UE Machine.
 *
 */
public class BlockPanelConductor extends BlockConductor implements IPanelElectricMachine
{
	
	private static final boolean[][][] solidPixels = null;

	public BlockPanelConductor(int id, Material material) {
		super(id, material);
	}

	@Override
	public ForgeDirection sideMountedTo(int meta, ForgeDirection side) 
	{return ForgeDirection.DOWN;}

	/**
	 * Override this, but either call it, or initialize the array yourself.
	 */
	public void setSolidPixels(int BlockID, int meta, ForgeDirection side) 
	{
	for(int face=0; face < solidPixels.length; face++)
		for(int x=0; x < solidPixels[face].length; x++ )
			for(int y=0; y < solidPixels[face][x].length; y++)
				solidPixels[face][x][y] = true;
	}

	public boolean[][][] getSolidPixels(int BlockID, int meta, ForgeDirection side, byte x, byte y) 
	{
	return this.solidPixels;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canConnectToBase(int meta, ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
