package electricexpansion.mattredsox.tileentities;

import ic2.api.Direction;
import ic2.api.ElectricItem;
import ic2.api.EnergyNet;
import ic2.api.IElectricItem;
import ic2.api.IEnergySink;
import ic2.api.IEnergySource;
import ic2.api.IEnergyStorage;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityPiston;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.Ticker;
import universalelectricity.UniversalElectricity;
import universalelectricity.basiccomponents.UELoader;
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.implement.IConductor;
import universalelectricity.implement.IItemElectric;
import universalelectricity.implement.IJouleStorage;
import universalelectricity.implement.IRedstoneProvider;
import universalelectricity.network.IPacketReceiver;
import universalelectricity.network.PacketManager;
import universalelectricity.prefab.TileEntityConductor;
import universalelectricity.prefab.TileEntityElectricityReceiver;
import universalelectricity.prefab.Vector3;
import buildcraft.api.core.Orientations;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;

public class TileEntityAdvBatteryBox extends TileEntityElectricityReceiver implements IEnergySink, IEnergySource, IEnergyStorage, IPowerReceptor, IJouleStorage, IPacketReceiver, IRedstoneProvider, IInventory, ISidedInventory
{	
	private double Joulestored = 0;

    private ItemStack[] containingItems = new ItemStack[2];

    private boolean isFull = false;
    
	private int playersUsing = 0;
	
	public IPowerProvider powerProvider;
	
	public boolean initialized = false;

	private boolean sendUpdate = true;
	
	public boolean isUpgraded = false;
	
	public int upgradeType = 0;
		
    public TileEntityAdvBatteryBox()
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
        if (voltage > this.getVoltage())
        {
            this.worldObj.createExplosion((Entity)null, this.xCoord, this.yCoord, this.zCoord, 1F);
            return;
        }
        
        if(!this.isDisabled())
        {
        	this.setJoules(this.Joulestored+ElectricInfo.getJoules(amps, voltage));
        }
    }
    
    @Override
    public void updateEntity() 
    {
    	super.updateEntity();
    	
        if(!this.isDisabled())
        {
        	if(this.powerProvider != null)
        	{
        		double receivedElectricity = this.powerProvider.useEnergy(25, 25, true)*UniversalElectricity.BC3_RATIO;
        		this.setJoules(this.Joulestored + receivedElectricity);
        	}
        	
            //The top slot is for recharging items. Check if the item is a electric item. If so, recharge it.
            if (this.containingItems[0] != null && this.Joulestored > 0)
            {
                if (this.containingItems[0].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric)this.containingItems[0].getItem();
                    double ampsToGive = Math.min(ElectricInfo.getAmps(electricItem.getMaxJoules()*0.005, this.getVoltage()), this.Joulestored);
                    double joules = electricItem.onReceive(ampsToGive, this.getVoltage(), this.containingItems[0]);
                    this.setJoules(this.Joulestored - (ElectricInfo.getJoules(ampsToGive, this.getVoltage(), 1) - joules));
                }
                else if(this.containingItems[0].getItem() instanceof IElectricItem)
                {
                	double sent = ElectricItem.charge(containingItems[0], (int) (Joulestored*UniversalElectricity.TO_IC2_RATIO), 3, false, false)*UniversalElectricity.IC2_RATIO;
                	this.setJoules(Joulestored - sent);
                }
            }

            //The bottom slot is for decharging. Check if the item is a electric item. If so, decharge it.
            if (this.containingItems[1] != null && this.Joulestored < this.getMaxJoules())
            {
                if (this.containingItems[1].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric)this.containingItems[1].getItem();

                    if (electricItem.canProduceElectricity())
                    {
                        double joulesReceived = electricItem.onUse(electricItem.getMaxJoules()*0.005, this.containingItems[1]);
                        this.setJoules(this.Joulestored + joulesReceived);
                    }
                }
                else if(containingItems[1].getItem() instanceof IElectricItem)
                {
                	IElectricItem item = (IElectricItem)containingItems[1].getItem();
                	if(item.canProvideEnergy())
                	{
                		double gain = ElectricItem.discharge(containingItems[1], (int) ((int)(getMaxJoules()-Joulestored)*UniversalElectricity.TO_IC2_RATIO), 3, false, false)*UniversalElectricity.IC2_RATIO;
                		this.setJoules(Joulestored + gain);
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
	 	            if(this.isPoweredTile(tileEntity))
	 	            {
	 	            	IPowerReceptor receptor = (IPowerReceptor) tileEntity;
	 	            	double JoulesNeeded = Math.min(receptor.getPowerProvider().getMinEnergyReceived(), receptor.getPowerProvider().getMaxEnergyReceived())*UniversalElectricity.BC3_RATIO;
	 	            	float transferJoules = (float) Math.max(Math.min(Math.min(JoulesNeeded, this.Joulestored), 54000), 0);
	 	            	receptor.getPowerProvider().receiveEnergy((float)(transferJoules*UniversalElectricity.TO_BC_RATIO), Orientations.dirs()[ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite().ordinal()]);
	 	            	this.setJoules(this.Joulestored - transferJoules);
	 	            }
            	}
            	
                TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata()));
                
                if (connector != null)
                {
                	//Output UE electricity
                    if (connector instanceof TileEntityConductor)
                    {
                        double wattsNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor)connector).getConnectionID());
                        double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(wattsNeeded, this.getVoltage()), ElectricInfo.getAmpsFromWattHours(this.Joulestored, this.getVoltage()) ), 15), 0);                        
                        ElectricityManager.instance.produceElectricity(this, (IConductor)connector, transferAmps, this.getVoltage());
                        this.setJoules(this.Joulestored - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
                    } 
                }
            }
        }
        
        if(!this.worldObj.isRemote)
        {
	        if(this.sendUpdate || (Ticker.inGameTicks % 40 == 0 && this.playersUsing > 0))
	        {
	        	PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, Vector3.get(this), 15);
	        	this.sendUpdate = false;
	        }
        }
    }
  
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket("ElecEx", this, this.Joulestored, this.disabledTicks, this.upgradeType);
    }
    
    @Override
	public void handlePacketData(NetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
			this.Joulestored = dataStream.readDouble();
	        this.disabledTicks = dataStream.readInt();
	        this.upgradeType = dataStream.readInt();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
    
    @Override
    public void openChest()
    {
    	if(!this.worldObj.isRemote)
        {
    		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, Vector3.get(this), 15);
        }
    	
    	this.playersUsing  ++;
    }
    
    @Override
    public void closeChest()
    {
    	this.playersUsing --;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.Joulestored = par1NBTTagCompound.getDouble("electricityStored");
        
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }
    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setDouble("electricityStored", this.Joulestored);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
    }

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if(side == side.DOWN)
        {
            return 1;
        }

        if(side == side.UP)
        {
            return 1;
        }

        return 0;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }
    
    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    @Override
    public String getInvName()
    {
        if(this.upgradeType == 1)
        {
    	return "Superconducting Magnet Box";
        }
        else
        {
        return "     Advanced Battery Box";
        }
    }
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }
    
    @Override
    public boolean isPoweringTo(byte side)
    {
        return this.isFull;
    }

    @Override
    public boolean isIndirectlyPoweringTo(byte side)
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
	public double getMaxJoules()
	{
		if (this.upgradeType == 1) 
		{
		return 7000;
		}
		else
		{
			return 2000;
		}
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
			
			if(Ticker.inGameTicks % 2 == 0 && this.playersUsing > 0)
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
    	if (this.upgradeType == 1) 
    	{
    		return 960;
	   	}

    	else {
    		return 120;
    	}
    }
}
