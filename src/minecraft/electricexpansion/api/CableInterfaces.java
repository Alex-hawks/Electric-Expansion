package electricexpansion.api;

import net.minecraftforge.common.ForgeDirection;

public class CableInterfaces
{
	/**
	 * 
	 * @author Alex_hawks Implement this if you want cables to obey selective connective rules,
	 * works on machines too.
	 */
	public interface ISelectiveConnector
	{
		public String cableType(int ID, int meta);

	}
}
