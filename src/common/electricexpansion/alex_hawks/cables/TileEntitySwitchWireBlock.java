package electricexpansion.alex_hawks.cables;

import net.minecraft.src.Block;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.helpers.TileEntityCableHelper;

public class TileEntitySwitchWireBlock extends TileEntityCableHelper 
{
    @Override
	public double getResistance() 
    //Values will NOT be actual values or precise relative values. But if x is meant to be greater than y, it will be. 
    //Maybe by 10^10 or 10^-10. But the one meant to be greater, will be.
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	switch(meta)
    	{
    	case 0: return 0.05; 
    	case 1: return 0.04;
    	case 2: return 0.02;
    	case 3: return 0.2;
    	default: return 0.05;
    	}
	}
	@Override
	public double getMaxAmps()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
		case 0: return 500;
		case 1: return 60; //Bit less than a basic Coal-Generator. #Cruel
		case 2: return 200;
		case 3: return 2500; //HV
		default: return 500;
		}
	}
	@Override
	public boolean canConnect(int side)
	{
		if(this.getWorld().isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord))
			return super.canConnect(side);
		else return false;
	}
	@Override
	public void onOverCharge() {
		// TODO Auto-generated method stub
		
	}
}