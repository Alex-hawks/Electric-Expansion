package electricexpansion.alex_hawks.machines;

import hawksmachinery.api.HMRepairInterfaces.IHMRepairable;
import hawksmachinery.api.HMRepairInterfaces.IHMSapper;

import java.util.Random;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.wpt.InductionNetworks;
import electricexpansion.alex_hawks.wpt.distributionNetworks;
import electricexpansion.api.WirelessPowerMachine;
import electricexpansion.mattredsox.blocks.BlockAdvBatteryBox;

public class TileEntityDistribution extends TileEntityElectricityReceiver implements IHMRepairable, IJouleStorage, IPacketReceiver, IRedstoneProvider, IPeripheral, IInventory, WirelessPowerMachine
{
	private short frequency;
	private ItemStack sapper;
	private int machineHP;
	private boolean isOpen;
	private int playersUsing;
	private static final int maxHP = 20;

	@Override
	public boolean canUpdate()
	{return true;}

	@Override
	public void updateEntity()
	{
		TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.blockMetadata));

		if (connector != null)
		{
			//Output UE electricity
			if (connector instanceof IConductor)
			{
				double joulesNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor) connector).getNetwork());
				double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.getJoules(), this.getVoltage())), 80), 0);
				if (!this.worldObj.isRemote)
					ElectricityManager.instance.produceElectricity(this, (IConductor) connector, transferAmps, this.getVoltage());
				this.addJoules(0 - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
			} 
		}
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;}

	@Override
	public void openChest()
	{
		if(!this.worldObj.isRemote)
			PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, Vector3.get(this), 15);
		this.playersUsing++;
	}
	
	@Override
	public void closeChest()
	{this.playersUsing--;}
	
	@Override
	public int getSizeInventory() 
	{return 0;}

	@Override
	public ItemStack getStackInSlot(int var1) 
	{return null;}

	@Override
	public ItemStack decrStackSize(int var1, int var2) 
	{return null;}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{return null;}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2){}

	@Override
	public String getInvName()
	{return "Induction Power Sender";}

	@Override
	public int getInventoryStackLimit() 
	{return 0;}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.frequency = par1NBTTagCompound.getShort("frequency");
		this.machineHP = par1NBTTagCompound.getInteger("machineHP");
		try{this.sapper = ItemStack.loadItemStackFromNBT((NBTTagCompound) par1NBTTagCompound.getTag("Sapper"));}
		catch(Exception e){this.sapper = null;}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("frequency", this.frequency);
		par1NBTTagCompound.setInteger("machineHP", this.machineHP);
		if (this.sapper != null)
			par1NBTTagCompound.setCompoundTag("Sapper", this.sapper.writeToNBT(new NBTTagCompound()));

	}

	@Override
	public void onReceive(Object sender, double amps, double voltage, ForgeDirection side) 
	{
		if (!this.isDisabled())
		{
			if (voltage > this.getVoltage())
			{
				this.worldObj.createExplosion((Entity) null, this.xCoord, this.yCoord, this.zCoord, 1F, true);
				return;
			}

			this.addJoules(ElectricInfo.getJoules(amps, voltage, 1));
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		if (this.isOpen)
			return PacketManager.getPacket("ElecEx", this, this.frequency, this.machineHP, this.getJoules());
		else return PacketManager.getPacket("ElecEx", this, this.frequency, this.machineHP);
	}

	private void addJoules(double joules) 
	{distributionNetworks.addJoules(this.frequency, joules);}

	@Override
	public double wattRequest() 
	{
		if (!this.isDisabled()) { return ElectricInfo.getWatts(this.getMaxJoules()) - ElectricInfo.getWatts(this.getJoules()); }
		return 0;
	}

	@Override
	public boolean canReceiveFromSide(ForgeDirection side) 
	{return side.ordinal() == this.blockMetadata;}

	@Override
	public String getType() 
	{return "Balence Wireless Power Transfer";}

	@Override
	public String[] getMethodNames() 
	{return new String[] { "", "getWattage", "isFull", "getJoules", "getFrequency", "setFrequency", "getHP" };}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws IllegalArgumentException 
	{
		final int getWattage = 1;
		final int isFull = 2;
		final int getJoules = 3;
		final int getFrequency = 4;
		final int setFrequency = 5;
		final int getHP = 6;
		int arg0 = 0;
		try
		{
			if((Integer)arguments[0] != null)
				arg0 = ((Integer)arguments[0]).intValue();
		}
		catch(Exception e)
		{ElectricExpansion.EELogger.fine("Failed to get new frequency, from ComputerCraft functions.");}

		if(!this.isBeingSapped())
		{
			switch (method)
			{
			case getWattage:		return new Object[]{ ElectricInfo.getWatts(getJoules((Object)null)) };
			case isFull:			return new Object[]{ isFull() };
			case getJoules:			return new Object[]{ getJoules() };
			case getFrequency:		return new Object[]{ getFrequency() };
			case setFrequency:		return new Object[]{ setFrequency((short)arg0, true) };
			case getHP:				return new Object[]{ getHP() };
			default:				throw new IllegalArgumentException("Function unimplemented");
			}
		}
		else return new Object[] { "Remove the sapper first" };
	}

	@Override
	public short getFrequency() 
	{return frequency;}
	
	@Override
	public void setFrequency(short newFrequency) 
	{this.frequency = newFrequency;}

	public void setFrequency(int frequency) 
	{this.setFrequency((short)frequency);}

	private int setFrequency(int frequency, boolean b) 
	{return this.setFrequency((short)frequency, b);}
	
	private int setFrequency(short frequency, boolean b) 
	{
		this.setFrequency(frequency);
		return this.frequency;
	}

	private boolean isFull() 
	{return this.getJoules((Object)null) == this.getMaxJoules();}

	@Override
	public boolean canAttachToSide(int side) 
	{return side == this.blockMetadata;}

	@Override
	public void attach(IComputerAccess computer, String computerSide) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean isPoweringTo(ForgeDirection side) 
	{return distributionNetworks.getJoules(this.frequency) == distributionNetworks.getMaxJoules();}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side) 
	{return distributionNetworks.getJoules(this.frequency) == distributionNetworks.getMaxJoules();}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			this.frequency = dataStream.readShort();
			this.disabledTicks = dataStream.readInt();
		}
		catch(Exception e)
		{e.printStackTrace(); }
	}

	@Override
	public double getJoules(Object... data) 
	{return distributionNetworks.getJoules(this.frequency);}

	@Override
	public void setJoules(double wattHours, Object... data) 
	{distributionNetworks.setJoules(this.frequency, ElectricInfo.getJoules(ElectricInfo.getWatts(wattHours), 1));}

	@Override
	public double getMaxJoules(Object... data) 
	{return distributionNetworks.getMaxJoules();}

	@Override
	public boolean isBeingSapped() 
	{return this.sapper != null;}

	@Override
	public boolean attemptToRepair(int repairAmount) 
	{
		if (this.machineHP != this.getMaxHP() && !this.isBeingSapped())
		{
			this.machineHP += repairAmount;
			return true;
		}
		else return false;
	}

	@Override
	public boolean setSapper(ItemStack newSapper) 
	{
		if(this.sapper == (ItemStack)null)
		{
			this.sapper = newSapper;
			return true;
		}
		else return false;
	}

	@Override
	public boolean attemptToUnSap(EntityPlayer player) 
	{
		boolean returnValue = false;
		if (this.isBeingSapped())
		{
			int randomDigit = new Random().nextInt(((IHMSapper)this.sapper.getItem()).getRemovalValue(this.sapper, player));
			if (randomDigit == ((IHMSapper)this.sapper.getItem()).getRemovalValue(this.sapper, player) / 2)
			{
				((IHMSapper)this.sapper.getItem()).onRemoved(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				this.sapper = null;
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override
	public int getMaxHP() 
	{return this.maxHP;}

	@Override
	public int getHP() 
	{return this.machineHP;}

}
