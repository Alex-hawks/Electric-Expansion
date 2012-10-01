package electricexpansion.additionalcables.cables;

import universalelectricity.prefab.TileEntityConductor;
import electricexpansion.EECommonProxy;

public class TileEntityRawWire extends TileEntityConductor
{
    @Override
	public double getResistance() 
    //Values will NOT be actual values or precise relative values. But if x is meant to be greater than y, it will be. 
    //Maybe by 10^10 or 10^-10. But the one meant to be greater, will be.
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	switch(meta)
    	{
    	case 0: return 0.6f;
    	case 1: return 0.6f;
    	case 2: return 0.2f;
    	case 3: return 2.0f;
    	default: return 1.0f;
    	}
	}
	@Override
	public double getMaxAmps()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0: return 10000;
		case 1: return 9999; //Bit less than a basic Coal-Generator. #Cruel
		case 2: return 1000;
		case 3: return 50000; //HV
		default: return 10000;
		}
	}
	@Override
	public void onConductorMelt() 
	{
		//this.nothing();
	}
}