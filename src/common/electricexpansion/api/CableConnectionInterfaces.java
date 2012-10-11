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
		 * solidPixels[side of block][x coordinate][y coordinate]
		 * coordinates are 0 indexed, (0,0) is at the bottom left of the 16*16 texture, if it were a standard 1*1*1 block
		 */
		boolean[][][] solidPixels = new boolean[6][16][16];
		/** 
		 * @param BlockID
		 * @param meta
		 * @return the side on which your block exists
		 * f.e. Atrain99's Solar Panels would be bottom.
		 */
		public ForgeDirection sideMountedTo(int BlockID, int meta, ForgeDirection side);
		
		/**
		 * set the solidPixels[][][] array for this instance, by BlockID, Block metadata, and side.
		 * @param BlockID
		 * @param meta
		 * @param side
		 */
		public void setSolidPixels(int BlockID, int meta, ForgeDirection side);
		
		/**
		 * Only returns, does NOT set
		 * @param BlockID
		 * @param meta
		 * @param side
		 * @param x The x coordinate of the pixel in question. See above creation of solidPixels[][][]
		 * @param y The y coordinate of the pixel in question. See above creation of solidPixels[][][]
		 * @return an array in the form of this.solidPixels[][][]
		 * @return see above for explanation.
		 */
		public boolean[][][] getSolidPixels(int BlockID, int meta, ForgeDirection side, byte x, byte y);
	}
}
