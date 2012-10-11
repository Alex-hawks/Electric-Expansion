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

	@Override
	public boolean canConnectToBase(int BlockID, int meta, ForgeDirection side) 
	{return true;}
}
