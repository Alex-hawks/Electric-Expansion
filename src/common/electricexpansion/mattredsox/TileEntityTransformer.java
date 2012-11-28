package electricexpansion.mattredsox;

import electricexpansion.mattredsox.blocks.BlockDOWNTransformer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityConductor;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import basiccomponents.BasicComponents;

public class TileEntityTransformer extends TileEntityElectricityReceiver implements IJouleStorage{
	
	public double electricityStored = 0;


	public double outgoingvoltage;
	public double incomingvoltage;

	private boolean firsttick = true;

	public int primaryCoil = 1;
	public int secondaryCoil = 1;

	public TileEntityTransformer() {
	}

	@Override
    public double wattRequest()
    {
    	if(!this.isDisabled())
    	{
    		return this.getMaxJoules()-this.electricityStored;
    	}
    	
    	return 0;
    }

	@Override
	public boolean canReceiveFromSide(ForgeDirection side)
    {
		if (this.blockMetadata != 0) {
			return false;
		}
		return side == ForgeDirection.getOrientation(this.getBlockMetadata() - 2).getOpposite();
	}
    
    @Override
	public boolean canConnect(ForgeDirection side)
    {
		if (this.blockMetadata != 0) {
			return false;
		}
		return canReceiveFromSide(side) || side == ForgeDirection.getOrientation(this.getBlockMetadata() - 2);
	}

	public int getMaxCoils() {
		switch (this.blockMetadata) {
		case 0:
			return 6;
		case 1:
			return 12;
		case 2:
			return 18;
		}
		return 0;
	}

	@Override
	public double getVoltage() {
		return outgoingvoltage;
	}

	@Override
	public void onReceive(Object sender, double amps, double voltage, ForgeDirection side)	{
	    	if(!this.isDisabled())
	    	{	    		
				if(wattRequest() > 0 && canConnect(side))
				{
					incomingvoltage = voltage;
					double rejectedElectricity = Math.max((this.electricityStored + amps) - this.getMaxJoules(), 0);
					this.electricityStored = Math.max(this.electricityStored + amps - rejectedElectricity, 0);
				}
		    			   
				if ((this.blockMetadata == 1 || this.blockMetadata == 2) && firsttick) {
					firsttick = false;
					this.primaryCoil = ((TileEntityTransformer) ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord)).primaryCoil;
					this.secondaryCoil = ((TileEntityTransformer) ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord)).secondaryCoil;
				}
				
				if (this.blockMetadata == 2) {TileEntityTransformer base = (TileEntityTransformer) ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
					TileEntityTransformer middle = (TileEntityTransformer) ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
					base.primaryCoil = this.primaryCoil;
					base.secondaryCoil = this.secondaryCoil;
					middle.primaryCoil = this.primaryCoil;
					middle.secondaryCoil = this.secondaryCoil;
					this.incomingvoltage = base.incomingvoltage;
					this.outgoingvoltage = base.outgoingvoltage;
				}
				
				if (this.blockMetadata == 1 && ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) == null) {
					if (ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) == null) {
						if (secondaryCoil > getMaxCoils()) { 
							secondaryCoil = getMaxCoils();
						}
						if (primaryCoil > getMaxCoils()) {
							primaryCoil = getMaxCoils();
						}
					}
					TileEntityTransformer base = (TileEntityTransformer) ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
					base.primaryCoil = this.primaryCoil;
					base.secondaryCoil = this.secondaryCoil;
					this.incomingvoltage = base.incomingvoltage;
					this.outgoingvoltage = base.outgoingvoltage;
				}
				
				if (this.blockMetadata == 0) {
					if (ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord) == null) {
						if (secondaryCoil > getMaxCoils()) {
							secondaryCoil = getMaxCoils();
						}
						if (primaryCoil > getMaxCoils()) {
							primaryCoil = getMaxCoils();
						}
					}
					
					if (this.isDisabled()) {
						outgoingvoltage = 0;
					} else {
						outgoingvoltage = (secondaryCoil * incomingvoltage) / primaryCoil;
					}
				}
				
				//Output electricity
				if (this.electricitySored > 0)
				{ TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDOWNTransformer.meta + 2));

				TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockDOWNTransformer.meta + 2));

	                {
	                	//Output UE electricity
	                	if (connector instanceof IConductor)
						{
							double joulesNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor) connector).getNetwork());
							double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.joules, this.getVoltage())), 80), 0);
							if (!this.worldObj.isRemote)
							{
								ElectricityManager.instance.produceElectricity(this, (IConductor) connector, transferAmps, this.getVoltage());
							}
							this.setJoules(this.joules - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
	                    } 
	                }
				}
		}
			    }
			}
    }
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.electricityStored = par1NBTTagCompound.getFloat("electricityStored");
		this.primaryCoil = par1NBTTagCompound.getInteger("primaryCoil");
		this.secondaryCoil = par1NBTTagCompound.getInteger("secondaryCoil");
		this.blockMetadata = par1NBTTagCompound.getInteger("blockMetadata");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("electricityStored", this.electricityStored);
		par1NBTTagCompound.setInteger("primaryCoil", this.primaryCoil);
		par1NBTTagCompound.setInteger("secondaryCoil", this.secondaryCoil);
		par1NBTTagCompound.setInteger("blockMetadata", this.blockMetadata);

	}

	@Override
	public double getJoules(Object... data)
	{return this.electricityStored;}

	@Override
	public void setJoules(double joules, Object... data)
	{this.electricityStored = Math.max(Math.min(joules, this.getMaxJoules()), 0);}

	@Override
	public double getMaxJoules(Object... data) {
		return 100;
	}
}
