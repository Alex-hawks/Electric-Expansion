package electricexpansion.alex_hawks.machines;

import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.cables.TileEntitySuperConductor;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricityNetwork;
import universalelectricity.implement.IConductor;
import universalelectricity.prefab.TileEntityElectricityReceiver;

public class TileEntitySuperconductorMantainer extends TileEntityElectricityReceiver
{	
	public void onReceive(TileEntity sender, double amps, double voltage, ForgeDirection side) 
	{
		if(ElectricInfo.getWatts(amps, voltage) > this.wattRequest())
			System.err.print("Too Much Electricity!!!");
		else if(ElectricInfo.getWatts(amps, voltage) == this.wattRequest())
			for(IConductor conductor : ElectricityNetwork.conductors)
				if(conductor instanceof TileEntitySuperConductor)
					((TileEntitySuperConductor)conductor).setPowered(true);
		else if(ElectricInfo.getWatts(amps, voltage) == this.wattRequest())
			for(IConductor conductor1 : ElectricityNetwork.conductors)
				if(conductor1 instanceof TileEntitySuperConductor)
					((TileEntitySuperConductor)conductor1).setPowered(false);	
	}

	@Override
	public double wattRequest() 
	{
		if(this.isDisabled()){return 0;}
		int SuperconductorQTY = 0;
		for(IConductor conductor : ElectricityNetwork.conductors)
			if(conductor instanceof TileEntitySuperConductor) {SuperconductorQTY += 1;}
		return (double)SuperconductorQTY*ElectricExpansion.superConductorUpkeep;
	}

	@Override
	public boolean canReceiveFromSide(ForgeDirection side) 
	{
		return true; //temp...
	}

}