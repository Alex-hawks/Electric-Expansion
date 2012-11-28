package net.minecraft.src.Transformer;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.src.*;
import net.minecraft.src.basiccomponents.BasicComponents;
import net.minecraft.src.forge.*;
import net.minecraft.src.universalelectricity.*;
import net.minecraft.src.universalelectricity.electricity.*;
import net.minecraft.src.universalelectricity.extend.*;
import net.minecraft.src.universalelectricity.network.*;

public class TileEntityTransformer extends TileEntityElectricUnit implements ITextureProvider, IRotatable{
	
	public float electricityStored = 0;

	public byte facingDirection = 0;

	public float outgoingvoltage;
	public float incommingvoltage;

	private boolean firsttick = true;

	public int primaryCoil = 1;
	public int secondaryCoil = 1;

	public TileEntityTransformer() {
  		ElectricityManager.registerElectricUnit(this);
	}

	@Override
    public float electricityRequest()
    {
    	if(!this.isDisabled())
    	{
    		return this.getElectricityCapacity()-this.electricityStored;
    	}
    	
    	return 0;
    }
    
    private float getElectricityCapacity() {
		return 100;
	}

	@Override
	public boolean canReceiveFromSide(byte side)
    {
		if (this.blockMetadata != 0) {
			return false;
		}
		return side == UniversalElectricity.getOrientationFromSide(this.facingDirection, (byte) 2);	
	}
    
    @Override
	public boolean canConnect(byte side)
    {
		if (this.blockMetadata != 0) {
			return false;
		}
		return (side == this.facingDirection && !this.isDisabled()) || side == UniversalElectricity.getOrientationFromSide(this.facingDirection, (byte) 2);
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
	public String getTextureFile() {
		return BasicComponents.blockTextureFile;
	}

	@Override
	public float getVoltage() {
		return outgoingvoltage;
	}

	@Override
	public void onUpdate(float watts, float voltage, byte side)
	{
    		super.onUpdate(watts, voltage, side);

	    	if(!this.isDisabled())
	    	{	    		
				if(electricityRequest() > 0 && canConnect(side))
				{
					incommingvoltage = voltage;
					float rejectedElectricity = Math.max((this.electricityStored + watts) - this.getElectricityCapacity(), 0);
					this.electricityStored = Math.max(this.electricityStored + watts - rejectedElectricity, 0);
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
					this.incommingvoltage = base.incommingvoltage;
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
					this.incommingvoltage = base.incommingvoltage;
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
						outgoingvoltage = (secondaryCoil * incommingvoltage) / primaryCoil;
					}
				}
				
		    	if(this.electricityStored > 0)
		    	{
			    	TileEntity tileEntity = UniversalElectricity.getUEUnitFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), this.facingDirection);
	
			    	if(tileEntity != null)
			    	{
				    	if(tileEntity instanceof TileEntityConductor)
				    	{
				    		float electricityNeeded = ElectricityManager.electricityRequired(((TileEntityConductor)tileEntity).connectionID);
				    		float transferElectricity = Math.min(100, Math.min(this.electricityStored, electricityNeeded));
				    		ElectricityManager.produceElectricity((TileEntityConductor)tileEntity, transferElectricity, this.getVoltage());
				    		this.electricityStored -= transferElectricity;
				    	}
			    	}
			    }
			}
    }
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.electricityStored = par1NBTTagCompound.getFloat("electricityStored");
		this.facingDirection = par1NBTTagCompound.getByte("facingDirection");
		this.primaryCoil = par1NBTTagCompound.getInteger("primaryCoil");
		this.secondaryCoil = par1NBTTagCompound.getInteger("secondaryCoil");
		this.blockMetadata = par1NBTTagCompound.getInteger("blockMetadata");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setFloat("electricityStored", this.electricityStored);
		par1NBTTagCompound.setByte("facingDirection", this.facingDirection);
		par1NBTTagCompound.setInteger("primaryCoil", this.primaryCoil);
		par1NBTTagCompound.setInteger("secondaryCoil", this.secondaryCoil);
		par1NBTTagCompound.setInteger("blockMetadata", this.blockMetadata);

	}

	@Override
	public byte getDirection() {
		return this.facingDirection;
	}

	@Override
	public void setDirection(byte facingDirection) {	
		this.facingDirection = facingDirection;
	}
}
