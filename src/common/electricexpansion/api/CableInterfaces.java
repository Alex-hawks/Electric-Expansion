package electricexpansion.api;

import java.util.ArrayList;

import universalelectricity.core.implement.IConductor;

import net.minecraftforge.common.ForgeDirection;

public class CableInterfaces
{
	/**
	 *	@author Alex_hawks
	 *	
	 */
	public interface IPanelElectricMachine 
	{	
		/**
		 * @param meta
		 * @param side
		 * @return true if there is a 4*4 gap, at the bottom of your machine,
		 * @return on the side passed, for a cable to connect to
		 */
		public boolean canConnectToBase(int meta, ForgeDirection side);

	}
	/**
	 * 
	 * @author Alex_hawks
	 * Implement this if you want cables to obey selective connective rules, works on machines too.
	 */
	public interface ISelectiveConnector
	{
		public String cableType(int ID, int meta);

	}
}
