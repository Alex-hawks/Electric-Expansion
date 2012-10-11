package electricexpansion.api;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.api.CableConnectionInterfaces.IPanelElectricMachine;
import universalelectricity.prefab.BlockConductor;
import universalelectricity.prefab.BlockMachine;

/**
 * 
 * @author Alex_hawks
 * Extend this to make it easier to make neat and tidy cable connections to your slab-sized, panel-sized, etc UE Machine.
 *
 */
public class BlockPanelMachine extends BlockMachine implements IPanelElectricMachine
{
	public BlockPanelMachine(String name, int id, Material material) 
	{
		super(name, id, material);
	}

	@Override
	public ForgeDirection sideMountedTo(int BlockID, int meta, ForgeDirection side) 
	{return ForgeDirection.DOWN;}

	/**
	 * Override this, but either call it, or initialize the array yourself.
	 */
	@Override
	public void setSolidPixels(int BlockID, int meta, ForgeDirection side) 
	{
	for(int face=0; face < solidPixels.length; face++)
		for(int x=0; x < solidPixels[face].length; x++ )
			for(int y=0; y < solidPixels[face][x].length; y++)
				solidPixels[face][x][y] = true;
	}

	@Override
	public boolean[][][] getSolidPixels(int BlockID, int meta, ForgeDirection side, byte x, byte y) 
	{
	return this.solidPixels;
	}
	
	
}
