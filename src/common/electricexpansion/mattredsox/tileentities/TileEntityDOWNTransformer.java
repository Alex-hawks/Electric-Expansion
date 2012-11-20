package electricexpansion.mattredsox.tileentities;

import ic2.api.Direction;
import ic2.api.EnergyNet;
import ic2.api.IEnergySink;
import ic2.api.IEnergySource;
import ic2.api.IEnergyStorage;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import basiccomponents.block.BlockBasicMachine;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;
import cpw.mods.fml.common.Loader;

public class TileEntityDOWNTransformer extends TileEntityElectricityReceiver implements IEnergySink, IEnergySource, IEnergyStorage, IPowerReceptor, IJouleStorage, IRedstoneProvider {
	private double Joulestored = 0;


    private boolean isFull = false;
    
	private int playersUsing = 0;
	
	public IPowerProvider powerProvider;
	
	public boolean initialized = false;

	private boolean sendUpdate = true;
	
	public double voltin;
	
    public TileEntityDOWNTransformer()
    {
    	super();
    	this.setPowerProvider(null);
    }
    
    @Override
    public double wattRequest()
    {
        if (!this.isDisabled())
        {
            return ElectricInfo.getWatts(this.getMaxJoules()) - ElectricInfo.getWatts(this.Joulestored);
        }

        return 0;
    }

    @Override
    public boolean canReceiveFromSide(ForgeDirection side)
    {
        return side == ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    }

    @Override
    public boolean canConnect(ForgeDirection side)
    {
        return canReceiveFromSide(side) || side.ordinal() == this.getBlockMetadata();    }

    @Override
    public void onReceive(TileEntity sender, double amps, double voltage, ForgeDirection side)
    {        
    	voltin = voltage;
        
        if(!this.isDisabled())
        {
        	this.setJoules(this.Joulestored+ElectricInfo.getJoules(amps, voltage));
        }
    }
    
    @Override
    public void updateEntity() 
    {
    	super.updateEntity();
    	
    	if(!this.initialized)
    	{
    		if(Loader.isModLoaded("IC2"))
    		{
    			EnergyNet.getForWorld(worldObj).addTileEntity(this);
    		}
    		
    		this.initialized = true;
    	}
    	
        if(!this.isDisabled())
        {
        	if(this.powerProvider != null)
        	{
        		double receivedElectricity = this.powerProvider.useEnergy(25, 25, true)*universalelectricity.core.UniversalElectricity.BC3_RATIO;
        		this.setJoules(this.Joulestored + receivedElectricity);
        	
        		if(this.ticks % 2 == 0 && this.playersUsing > 0 && receivedElectricity > 0)
        		{
        			this.worldObj.markBlockNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
        		}
        	}
        	
                }

            //Power redstone if the battery box is full
            boolean isFullThisCheck = false;

            if (this.Joulestored >= this.getMaxJoules())
            {
                isFullThisCheck = true;
            }

            if (this.isFull != isFullThisCheck)
            {
                this.isFull = isFullThisCheck;
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
            }
            
            //Output electricity
            if (this.Joulestored > 0)
            {
                TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite());
            	
                //Output IC2 energy
            	if(Loader.isModLoaded("IC2"))
            	{
	 	            if(this.Joulestored*UniversalElectricity.TO_IC2_RATIO >= 32)
	 	            {
	 	            	this.setJoules(this.Joulestored - (32 - EnergyNet.getForWorld(worldObj).emitEnergyFrom(this, 32))*UniversalElectricity.IC2_RATIO);
	 	            }
            	}
            	
            	//Output BC energy
            	if(Loader.isModLoaded("BuildCraft|Transport"))
            	{
            		if (this.isPoweredTile(tileEntity))
					{
						IPowerReceptor receptor = (IPowerReceptor) tileEntity;
						double joulesNeeded = Math.min(receptor.getPowerProvider().getMinEnergyReceived(), receptor.getPowerProvider().getMaxEnergyReceived()) * UniversalElectricity.BC3_RATIO;
						float transferJoules = (float) Math.max(Math.min(Math.min(joulesNeeded, this.Joulestored), 80000), 0);
						receptor.getPowerProvider().receiveEnergy((float) (transferJoules * UniversalElectricity.TO_BC_RATIO), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockBasicMachine.BATTERY_BOX_METADATA + 2).getOpposite());
						this.setJoules(this.Joulestored - transferJoules);
					}
            	}
            	
                TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata()));
                
				// Output UE electricity
				if (connector instanceof IConductor)
				{
					double joulesNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor) connector).getNetwork());
					double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.Joulestored, this.getVoltage())), 80), 0);
					if (!this.worldObj.isRemote)
					{
						ElectricityManager.instance.produceElectricity(this, (IConductor) connector, transferAmps, this.getVoltage());
					}
					this.setJoules(this.Joulestored - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
				}
			}
		}

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.Joulestored = par1NBTTagCompound.getDouble("electricityStored");
        
    }
    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setDouble("electricityStored", this.Joulestored);
        }
    

  
    @Override
    public boolean isPoweringTo(ForgeDirection side)
    {
        return this.isFull;
    }

    @Override
    public boolean isIndirectlyPoweringTo(ForgeDirection side)
    {
        return isPoweringTo(side);
    }
    
    @Override
    public double getJoules(Object... data)
    {
    	return this.Joulestored;
    }

	@Override
	public void setJoules(double Joules, Object... data)
	{
		this.Joulestored = Math.max(Math.min(Joules, this.getMaxJoules()), 0);
	}
	
	@Override
	public double getMaxJoules(Object... data)
	{
		return 10;
	}
	
	/**
	 * BUILDCRAFT FUNCTIONS
	 */
	
	/**
	 * Is this tile entity a BC tile?
	 */
	public boolean isPoweredTile(TileEntity tileEntity)
	{
		if(tileEntity instanceof IPowerReceptor) 
		{
			IPowerReceptor receptor = (IPowerReceptor) tileEntity;
			IPowerProvider provider = receptor.getPowerProvider();
			return provider != null && provider.getClass().getSuperclass().equals(PowerProvider.class);
		}

		return false;
	}

	@Override
	public void setPowerProvider(IPowerProvider provider)
    {
		if(provider == null)
		{
			if(PowerFramework.currentFramework != null)
	    	{
		    	powerProvider = PowerFramework.currentFramework.createPowerProvider();
				powerProvider.configure(20, 25, 25, 25, (int) (this.getMaxJoules()*UniversalElectricity.BC3_RATIO));
	    	}
		}
		else
		{
			this.powerProvider = provider;
		}
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return this.powerProvider;
	}

	@Override
	public void doWork() { }

	@Override
	public int powerRequest()
	{
		return (int) Math.ceil((this.getMaxJoules() - this.Joulestored)*UniversalElectricity.BC3_RATIO);
	}

	/**
	 * INDUSTRIALCRAFT FUNCTIONS
	 */
	public int getStored() 
	{
		return (int) (this.Joulestored*UniversalElectricity.IC2_RATIO);
	}
	
	@Override
	public boolean isAddedToEnergyNet()
	{
		return initialized;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return canReceiveFromSide(direction.toForgeDirection());		
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return direction.toForgeDirection() ==ForgeDirection.getOrientation(this.getBlockMetadata());
	}

	@Override
	public int getCapacity() 
	{
		return (int)(this.getMaxJoules()/UniversalElectricity.IC2_RATIO);
	}

	@Override
	public int getOutput() 
	{
		return 32;
	}

	@Override
	public int getMaxEnergyOutput()
	{
		return 32;
	}

	@Override
	public boolean demandsEnergy()
	{
		if(!this.isDisabled() && UniversalElectricity.IC2_RATIO > 0)
		{
			return this.Joulestored < getMaxJoules();
		}
		
		return false;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int euAmount) 
	{
		if(!this.isDisabled())
		{
			double inputElectricity = euAmount*UniversalElectricity.IC2_RATIO;

			double rejectedElectricity = Math.max(inputElectricity - (this.getMaxJoules() - this.Joulestored), 0);
		
			this.setJoules(Joulestored + inputElectricity);
			
			if(this.ticks % 2 == 0 && this.playersUsing > 0)
			{
				this.worldObj.markBlockNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
		
			return (int) (rejectedElectricity*UniversalElectricity.TO_IC2_RATIO);
		}
		
		return euAmount;
	}
	
    @Override
    public double getVoltage()
    {
		return voltin / 2;
    }
}
