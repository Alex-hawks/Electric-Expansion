package electricexpansion.api;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.implement.IDisableable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import electricexpansion.api.CableInterfaces.IPanelElectricMachine;

/**
 * 
 * @author Alex_hawks
 * Extend this to make it easier to make neat and tidy cable connections to your slab-sized, panel-sized, etc UE Machine.
 *
 */
public abstract class TilePanelMachine extends TileEntityAdvanced implements IPanelElectricMachine, IPacketReceiver, IDisableable
{
	public int disableTicks = 0;
	private int ticksIdling = 0;

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
