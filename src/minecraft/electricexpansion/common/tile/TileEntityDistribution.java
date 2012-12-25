package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
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
import electricexpansion.api.WirelessPowerMachine;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.wpt.DistributionNetworks;

public class TileEntityDistribution extends TileEntityElectricityReceiver implements IJouleStorage, IPacketReceiver, IRedstoneProvider, IPeripheral, IInventory, WirelessPowerMachine
{
	private short frequency;
	private boolean isOpen;
	private int playersUsing;

	public TileEntityDistribution()
	{
		super();
	}

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - blockMetadata + 2), ForgeDirection.getOrientation(this.getBlockMetadata() - blockMetadata + 2).getOpposite()));
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - blockMetadata + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

				if (inputTile != null)
				{
					if (inputTile instanceof IConductor)
					{
						if (this.getJoules() >= this.getMaxJoules())
						{
							((IConductor) inputTile).getNetwork().stopRequesting(this);
						}
						else
						{
							((IConductor) inputTile).getNetwork().startRequesting(this, this.getMaxJoules() - this.getJoules(), this.getVoltage());
							this.setJoules(this.getJoules() + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts());
						}
					}
				}
			}

			/**
			 * Output Electricity
			 */

			if (this.getJoules() > 0)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - blockMetadata + 2);
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				if (tileEntity != null)
				{

					TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(this.blockMetadata));
					// Output UE electricity
					if (connector instanceof IConductor)
					{
						double joulesNeeded = ((IConductor) connector).getNetwork().getRequest().getWatts();
						double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.getJoules(), this.getVoltage())), 80), 0);

						if (!this.worldObj.isRemote && transferAmps > 0)
						{
							((IConductor) connector).getNetwork().startProducing(this, transferAmps, this.getVoltage());
							this.addJoules(0 - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
							System.out.println("PROD");
						}
						else
						{
							((IConductor) connector).getNetwork().stopProducing(this);
						}

					}
				}
			}
		}
		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				this.sendPacket();
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	public void sendPacket()
	{
		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.getFrequency(), this.disabledTicks);
	}
	
	@Override
	public void openChest()
	{
		if (!this.worldObj.isRemote)
			this.sendPacket();
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}

	@Override
	public int getSizeInventory()
	{
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
	}

	@Override
	public String getInvName()
	{
		return "Quantum Battery Box";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.frequency = par1NBTTagCompound.getShort("frequency");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("frequency", this.frequency);
	}

	private void addJoules(double joules)
	{
		DistributionNetworks.addJoules(this.frequency, joules);
	}

	@Override
	public double getJoules(Object... data)
	{
		return DistributionNetworks.getJoules(this.frequency);
	}

	@Override
	public void setJoules(double wattHours, Object... data)
	{
		DistributionNetworks.setJoules(this.frequency, ElectricInfo.getJoules(ElectricInfo.getWatts(wattHours), 1));
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return DistributionNetworks.getMaxJoules();
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws IllegalArgumentException
	{
		final int getWattage = 1;
		final int isFull = 2;
		final int getJoules = 3;
		final int getFrequency = 4;
		final int setFrequency = 5;
		int arg0 = 0;
		try
		{
			if ((Integer) arguments[0] != null)
				arg0 = ((Integer) arguments[0]).intValue();
		}
		catch (Exception e)
		{
		}

		if (!this.isDisabled())
		{
			switch (method)
			{
				case getWattage:
					return new Object[] { ElectricInfo.getWatts(getJoules((Object) null)) };
				case isFull:
					return new Object[] { isFull() };
				case getJoules:
					return new Object[] { getJoules() };
				case getFrequency:
					return new Object[] { getFrequency() };
				case setFrequency:
					return new Object[] { setFrequency((short) arg0, true) };
				default:
					throw new IllegalArgumentException("Function unimplemented");
			}
		}
		else
			return new Object[] { "Please wait for the EMP to run out." };
	}

	@Override
	public short getFrequency()
	{
		return frequency;
	}

	@Override
	public void setFrequency(short newFrequency)
	{
		this.frequency = newFrequency;
	}

	public void setFrequency(int frequency)
	{
		this.setFrequency((short) frequency);
	}

	private int setFrequency(int frequency, boolean b)
	{
		return this.setFrequency((short) frequency, b);
	}

	private int setFrequency(short frequency, boolean b)
	{
		this.setFrequency(frequency);
		return this.frequency;
	}

	private boolean isFull()
	{
		return this.getJoules((Object) null) == this.getMaxJoules();
	}

	@Override
	public boolean canAttachToSide(int side)
	{
		return side == this.blockMetadata;
	}

	@Override
	public void attach(IComputerAccess computer) {
		
	}
	
	@Override
	public void detach(IComputerAccess computer)
	{
	}

	@Override
	public String getType()
	{
		return "Quantum Battery Box";
	}

	@Override
	public String[] getMethodNames()
	{
		return new String[] { "getWattage", "isFull", "getJoules", "getFrequency", "setFrequency" };
	}

	public boolean isPoweringTo(ForgeDirection side)
	{
		return DistributionNetworks.getJoules(this.frequency) == DistributionNetworks.getMaxJoules();
	}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		return DistributionNetworks.getJoules(this.frequency) == DistributionNetworks.getMaxJoules();
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.frequency = dataStream.readShort();
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
