/**
 * 
 */
package electricexpansion.api;

import net.minecraftforge.common.ForgeDirection;

/**
 *	@author Alex_hawks
 *	Implement this if you want your electric machine in UE to be able to render cables connected to the side properly.
 */
public interface IPanelElectric 
{
	/** 
	 * @param BlockID
	 * @param meta
	 * @return the side on which your block exists
	 * f.e. Atrain99's Solar Panels would be bottom.
	 */
	public ForgeDirection sideMountedTo(int BlockID, int meta);
	
	/**
	 * @param BlockID
	 * @param meta
	 * @return the height of the block in pixels GIVEN a 16*16 texture pack.
	 */
	public byte height(int BlockID, int meta);
	
	/**
	 * @param BlockID
	 * @param meta
	 * @return 0 if the block is mounted directly on the next block,
	 * @return the Height of the area between the connectable area and the block it is 'mounted' on.
	 * @return In pixels GIVEN a 16*16 texture pack.
	 */
	public byte offset(int BlockID, int meta);
}
