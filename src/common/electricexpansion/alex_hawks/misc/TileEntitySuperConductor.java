package electricexpansion.alex_hawks.misc;

import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySuperConductor extends TileEntityCableHelper
{

	@Override
	public double getResistance() 
	{
		return 0;
	}

	@Override
	public double getMaxAmps() 
	{
		return 0;
	}

	@Override
	public void onOverCharge() 
	{
		// TODO Auto-generated method stub
		
	}
}
