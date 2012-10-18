package electricexpansion.api;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraftforge.common.ForgeDirection;
import electricexpansion.api.CableInterfaces.IPanelElectricMachine;
import universalelectricity.implement.IDisableable;
import universalelectricity.implement.IElectricityProducer;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.prefab.AdvancedTile;
import universalelectricity.prefab.BlockConductor;
import universalelectricity.prefab.BlockMachine;

/**
 * 
 * @author Alex_hawks
 * Extend this to make it easier to make neat and tidy cable connections to your slab-sized, panel-sized, etc UE Machine.
 *
 */
public abstract class TilePanelMachine extends AdvancedTile implements IPanelElectricMachine, IPacketReceiver, IDisableable
{
	public int disableTicks = 0;
	private int ticksIdling = 0;

	@Override
	public ForgeDirection sideMountedTo(int meta, ForgeDirection side) 
	{return ForgeDirection.DOWN;}

	@Override
	public boolean canConnectToBase(int meta, ForgeDirection side) 
	{return true;}

	@Override
	public boolean isDisabled() 
	{return this.disableTicks > 0;}
	
	public void updateEntity()
	{
		this.disableTicks -= 1;
		if(this.disableTicks < 0) this.disableTicks = 0;
	}
	
	@Override
	public void onDisable(int duration) 
	{this.disableTicks += duration;	}
}
