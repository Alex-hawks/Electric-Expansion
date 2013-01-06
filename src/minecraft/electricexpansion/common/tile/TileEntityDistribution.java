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
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.IWirelessPowerMachine;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.DistributionNetworks;

public class TileEntityDistribution extends TileEntityElectricityReceiver implements IWirelessPowerMachine, IJouleStorage, IPacketReceiver, IInventory, IPeripheral
{
	private ItemStack[] containingItems = new ItemStack[2];

	private int playersUsing = 0;

	private short frequency;

	private boolean isOpen;

	private double joulesForDisplay = 0;

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockDistribution.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
			TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
			ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);

			if (!this.worldObj.isRemote)
			{
				if (inputNetwork != null)
				{
					if (this.getJoules() >= this.getMaxJoules())
					{
						inputNetwork.stopRequesting(this);
					}
					else
					{
						inputNetwork.startRequesting(this, Math.min((this.getMaxJoules() - this.getJoules()), 10000) / this.getVoltage(), this.getVoltage());
						ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
						this.addJoules(electricityPack.getWatts());

						if (UniversalElectricity.isVoltageSensitive)
						{
							if (electricityPack.voltage > this.getVoltage())
							{
								this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
							}
						}
					}
				}
			}

			// Power redstone if the battery box is full
			boolean isFullThisCheck = false;

			if (this.getJoules() >= this.getMaxJoules())
			{
				isFullThisCheck = true;
			}

			/**
			 * Output Electricity
			 */

			if (!this.worldObj.isRemote)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);

				if (outputNetwork != null && inputNetwork != outputNetwork)
				{
					double outputWatts = Math.min(outputNetwork.getRequest().getWatts(), Math.min(this.getJoules(), 10000));

					if (this.getJoules() > 0 && outputWatts > 0)
					{
						outputNetwork.startProducing(this, outputWatts / this.getVoltage(), this.getVoltage());
						this.removeJoules(outputWatts);
					}
					else
					{
						outputNetwork.stopProducing(this);
					}
				}
			}
		}

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	public void sendPacket()
	{
		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.getFrequency(), this.disabledTicks, this.getJoules());
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				this.frequency = dataStream.readShort();
				this.disabledTicks = dataStream.readInt();
				this.joulesForDisplay = dataStream.readDouble();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				this.setFrequency(dataStream.readShort());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void openChest()
	{
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
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
	public double removeJoules(double outputWatts)
	{
		return DistributionNetworks.removeJoules(this.frequency, outputWatts);
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

	public double getJoulesForDisplay(Object... data)
	{
		return this.joulesForDisplay;
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
		return "Quantum Battery Box";
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

	/**
	 * COMPUTERCRAFT FUNCTIONS
	 */

	@Override
	public String getType()
	{
		return "BatteryBox";
	}

	@Override
	public String[] getMethodNames()
	{
		return new String[] { "getVoltage", "getJoules", "isFull" };
	}

	@Override
	public boolean canAttachToSide(int side)
	{
		return true;
	}

	@Override
	public void attach(IComputerAccess computer)
	{
	}

	@Override
	public void detach(IComputerAccess computer)
	{
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
		if (this.worldObj.isRemote)
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, newFrequency));
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

}