package mattredsox.electricexpansion;

import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.prefab.TileEntityConductor;
import universalelectricity.prefab.TileEntityElectricityReceiver;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityVoltDetector extends TileEntityElectricityReceiver
{

	double energyStored = 0;
	double energyNeeded = 0;
	public double voltsin = 0;
	double scale = 2;
	
	public TileEntityVoltDetector()	
    {
        super();
    }

    public boolean canProduceElectricity(ForgeDirection side)
    {
        return canConnect(side) && !this.isDisabled();
    }

    @Override
    public boolean canReceiveFromSide(ForgeDirection side)
    {
    	return side == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    }
    
    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return canReceiveFromSide(side) || side.ordinal() == this.getBlockMetadata();
    }

	@Override
	public double wattRequest() {
		
		if (!this.isDisabled())
        {
			return energyNeeded;
        }
		return 0;
	}

	@Override
	public void onReceive(TileEntity sender, double amps, double voltage, ForgeDirection side)
	{ 
		super.onReceive(sender, amps, voltage, side);
  
        TileEntity tileEntity = Vector3.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), ForgeDirection.getOrientation(this.getBlockMetadata()));
        
        voltsin = voltage;
        
        if (tileEntity != null)
        {
        	if (tileEntity instanceof TileEntityConductor)
        	{
        		energyStored = amps * voltage / 3600;
        		energyNeeded = ElectricityManager.instance.getElectricityRequired(((TileEntityConductor)tileEntity).connectionID);
        		double transferAmps = Math.max(Math.min(ElectricInfo.getAmps(energyNeeded, this.getVoltage()), ElectricInfo.getAmpsFromWattHours(this.energyStored, this.getVoltage())), 0);                        
        		ElectricityManager.instance.produceElectricity((TileEntityConductor)tileEntity, , transferAmps, this.getVoltage());   
        		
        	}
       	}      
    }
	
	@Override
	public double getVoltage()
	{
		return voltsin;
	}
	/**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        this.voltsin = par1NBTTagCompound.getDouble("voltsin");
              
    }
    
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setDouble("voltsin", this.voltsin);
        NBTTagList var2 = new NBTTagList(); 
    }

}