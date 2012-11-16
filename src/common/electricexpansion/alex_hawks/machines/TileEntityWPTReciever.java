package electricexpansion.alex_hawks.machines;

import hawksmachinery.api.HMRepairInterfaces.IHMRepairable;
import hawksmachinery.api.HMRepairInterfaces.IHMSapper;

import java.util.Random;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityDisableable;

import com.google.common.io.ByteArrayDataInput;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.ElectricExpansion;

public class TileEntityWPTReciever extends TileEntityDisableable implements IHMRepairable, IPacketReceiver, IJouleStorage, IPeripheral, IRedstoneProvider
{
	private double joules = 0;
	private int playersUsing = 0;
	private ItemStack sapper;
	private int machineHP;
	private short frequency;
	private static final double maxJoules = 500000; //To eventually go in config #Eventually
	private final int maxMachineHP = 20;
	private byte orientation;
	private boolean isOpen = false;
	
	public short getFrequency() 
	{return frequency;}
	
	public void setFrequency(short frequency) 
	{this.frequency = frequency;}

	public void setFrequency(int frequency) 
	{this.setFrequency((short)frequency);}

	private int setFrequency(int frequency, boolean b) 
	{return this.setFrequency((short)frequency, b);}
	
	private int setFrequency(short frequency, boolean b) 
	{
		this.frequency = frequency;
		return this.frequency;
	}
	
	@Override
	public boolean canUpdate()
	{return true;}
	
	@Override
	public void updateEntity()
	{
		if (this.joules < 0)
			this.joules = 0;
		if (this.joules > this.maxJoules)
			this.joules = this.maxJoules;
		if (this.machineHP < 0)
			this.machineHP = 0;
		if (this.machineHP > this.getMaxHP())
			this.machineHP = this.getMaxHP();
		if (!this.worldObj.isRemote)
			this.sendPacket();
		if (this.isBeingSapped())
			((IHMSapper)this.sapper.getItem()).sapperTick(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		if (this.orientation != this.blockMetadata)
			this.orientation = (byte)ForgeDirection.getOrientation(this.blockMetadata).ordinal();
	}
	
	private void sendPacket()
	{PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, Vector3.get(this), 8);}
	
	@Override
	public Packet getDescriptionPacket()
	{
		if (this.isOpen)
			return PacketManager.getPacket("ElecEx", this, this.joules, this.machineHP);
		else return PacketManager.getPacket("ElecEx", this, this.machineHP);
	}

	public boolean isFull()
	{return this.joules == this.maxJoules;}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.joules = par1NBTTagCompound.getInteger("joules");
		this.frequency = par1NBTTagCompound.getShort("frequency");
		this.machineHP = par1NBTTagCompound.getInteger("machineHP");
		this.sapper = ItemStack.loadItemStackFromNBT((NBTTagCompound) par1NBTTagCompound.getTag("Sapper"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("joules" ,this.joules);
		par1NBTTagCompound.setShort("frequency", this.frequency);
		par1NBTTagCompound.setInteger("machineHP", this.machineHP);
		if (this.sapper != null)
			par1NBTTagCompound.setCompoundTag("Sapper", this.sapper.writeToNBT(new NBTTagCompound()));

	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
        {
			this.joules = dataStream.readDouble();
	        this.disabledTicks = dataStream.readInt();
        }
        catch(Exception e)
        {e.printStackTrace(); }
	}

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
	public boolean setSapper(ItemStack sapper) 
	{
		if (this.sapper == null)
		{
			this.sapper = sapper;
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
	public boolean isBeingSapped()
	{return this.sapper != null;}

	@Override
	public int getMaxHP() 
	{return maxMachineHP;}

	@Override
	public int getHP() 
	{return machineHP;}

	@Override
	public boolean isPoweringTo(ForgeDirection side) 
	{
		boolean returnValue = false;
		if(this.joules == (double)this.maxJoules)
			returnValue = true;
		return returnValue;
	}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side) 
	{
		boolean returnValue = false;
		if(this.joules == (double)this.maxJoules)
			returnValue = true;
		return returnValue;
	}

	@Override
	public double getJoules(Object... data) 
	{return joules;}

	@Override
	public void setJoules(double wattHours, Object... data) 
	{this.joules = Math.max(Math.min(joules, this.getMaxJoules()), 0);}

	@Override
	public double getMaxJoules(Object... data) 
	{return maxJoules;}

	@Override
	public String getType() 
	{return "Wireless Power Transmitter";}

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
				case getWattage:		return new Object[]{ ElectricInfo.getWatts(joules) };
				case isFull:			return new Object[]{ isFull() };
				case getJoules:			return new Object[]{ getJoules() };
				case getFrequency:		return new Object[]{ getFrequency() };
				case setFrequency:		return new Object[]{ setFrequency(arg0, true) };
				case getHP:				return new Object[]{ getHP() };
				default:				throw new IllegalArgumentException("Function unimplemented");
			}
		}
		else return new Object[] { "Remove the sapper first" };
	}

	@Override
	public boolean canAttachToSide(int side) 
	{return side != this.blockMetadata;}

	@Override
	public void attach(IComputerAccess computer, String computerSide) {}

	@Override
	public void detach(IComputerAccess computer) {}

}
