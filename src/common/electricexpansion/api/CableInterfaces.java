/**
 * 
 */
package electricexpansion.api;

import java.util.ArrayList;

import net.minecraftforge.common.ForgeDirection;

public class CableInterfaces
{
	/**
	 *	@author Alex_hawks
	 *	Implement this if you want your electric machine in UE to be able to render cables connected to the side properly.
	 *	Every @return is 0 indexed, not 1 indexed.
	 *	
	 */
	public interface IPanelElectricMachine 
	{
		/** 
		 * @param meta
		 * @return the side on which your block exists
		 * f.e. Atrain99's Solar Panels would be bottom.
		 */
		public ForgeDirection sideMountedTo(int meta, ForgeDirection side);
		
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
