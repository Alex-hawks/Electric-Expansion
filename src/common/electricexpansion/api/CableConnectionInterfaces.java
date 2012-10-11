/**
 * 
 */
package electricexpansion.api;

import java.util.ArrayList;

import net.minecraftforge.common.ForgeDirection;

public class CableConnectionInterfaces
{
	/**
	 *	@author Alex_hawks
	 *	Implement this if you want your electric machine in UE to be able to render cables connected to the side properly.
	 *	Every @return is 0 indexed, not 1 indexed.
	 *	
	 *	Row 0 is at the bottom of the 1 unit by 1 unit Block face.
	 *	Column 0 is at the far left of the 1 unit by 1 unit Block face.
	 *	As it appears in the texture file, if the texture is not modified before applying it to the block.
	 *	This will be for my render code to find a suitable location on your block to connect to.
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
}
