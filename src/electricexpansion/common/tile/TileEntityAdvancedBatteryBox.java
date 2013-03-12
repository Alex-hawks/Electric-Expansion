package electricexpansion.common.tile;

import ic2.api.Direction;
import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.common.ElectricExpansion;

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityStorage implements IJouleStorage, IRedstoneProvider, IPacketReceiver, ISidedInventory, IPeripheral, IEnergySink, IEnergySource, IPowerReceptor
{
	private ItemStack[] containingItems = new ItemStack[5];

	private boolean isFull = false;

	private int playersUsing = 0;
	public IPowerProvider powerProvider;

	public TileEntityAdvancedBatteryBox()
	{
		if (PowerFramework.currentFramework != null)
		{
			this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
			this.powerProvider.configure(0, 0, 100, 0, (int) (getMaxJoules(new Object[0]) * UniversalElectricity.TO_BC_RATIO));
		}
	}

	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2), ForgeDirection.getOrientation(getBlockMetadata() + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockAdvBatteryBox.blockID);

		if (Loader.isModLoaded("IC2"))
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		}
	}

	public void invalidate()
	{
		super.invalidate();

		if (this.ticks > 0L)
		{
			if (Loader.isModLoaded("IC2"))
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
		}
	}

	public void updateEntity()
	{
		super.updateEntity();

		if (!isDisabled())
		{
			if (this.powerProvider != null)
			{
				int received = (int) (this.powerProvider.useEnergy(0.0F, (float) ((getMaxJoules(new Object[0]) - getJoules(new Object[0])) * UniversalElectricity.TO_BC_RATIO), true) * UniversalElectricity.BC3_RATIO);
				setJoules(getJoules(new Object[0]) + received, new Object[0]);
			}

			if ((this.containingItems[0] != null) && (getJoules(new Object[0]) > 0.0D))
			{
				if ((this.containingItems[0].getItem() instanceof IItemElectric))
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[0].getItem();
					double ampsToGive = Math.min(ElectricInfo.getAmps(Math.min(electricItem.getMaxJoules(new Object[] { this.containingItems[0] }) * 0.005D, getJoules(new Object[0])), getVoltage(new Object[0])), getJoules(new Object[0]));
					double rejects = electricItem.onReceive(ampsToGive, getVoltage(new Object[0]), this.containingItems[0]);
					setJoules(getJoules(new Object[0]) - (ElectricInfo.getJoules(ampsToGive, getVoltage(new Object[0]), 1.0D) - rejects), new Object[0]);
				}
				else if ((this.containingItems[0].getItem() instanceof IElectricItem))
				{
					double sent = ElectricItem.charge(this.containingItems[0], (int) (getJoules(new Object[0]) * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
					setJoules(getJoules(new Object[0]) - sent, new Object[0]);
				}

			}

			if ((this.containingItems[1] != null) && (getJoules(new Object[0]) < getMaxJoules(new Object[0])))
			{
				if ((this.containingItems[1].getItem() instanceof IItemElectric))
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();

					if (electricItem.canProduceElectricity())
					{
						double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(new Object[] { this.containingItems[1] }) * 0.005D, this.containingItems[1]);
						setJoules(getJoules(new Object[0]) + joulesReceived, new Object[0]);
					}

				}
				else if ((this.containingItems[1].getItem() instanceof IElectricItem))
				{
					IElectricItem item = (IElectricItem) this.containingItems[1].getItem();
					if (item.canProvideEnergy())
					{
						double gain = ElectricItem.discharge(this.containingItems[1], (int) ((int) (getMaxJoules(new Object[0]) - getJoules(new Object[0])) * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
						setJoules(getJoules(new Object[0]) + gain, new Object[0]);
					}
				}
			}

			ForgeDirection outputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
			TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

			if (!this.worldObj.isRemote)
			{
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection.getOpposite());

				ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
				ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);

				if ((outputNetwork != null) && (inputNetwork != outputNetwork))
				{
					double outputWatts = Math.min(outputNetwork.getRequest(new TileEntity[] { this }).getWatts(), Math.min(getJoules(new Object[0]), 10000.0D));

					if ((getJoules(new Object[0]) > 0.0D) && (outputWatts > 0.0D))
					{
						outputNetwork.startProducing(this, outputWatts / getVoltage(new Object[0]), getVoltage(new Object[0]));
						setJoules(getJoules(new Object[0]) - outputWatts, new Object[0]);
					}
					else
					{
						outputNetwork.stopProducing(this);
					}
				}

			}

			if (getJoules(new Object[0]) > 0.0D)
			{
				if (Loader.isModLoaded("IC2"))
				{
					if (getJoules(new Object[0]) >= 128.0D * UniversalElectricity.IC2_RATIO)
					{
						EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, 128);
						MinecraftForge.EVENT_BUS.post(event);
						setJoules(getJoules(new Object[0]) - (128.0D * UniversalElectricity.IC2_RATIO - event.amount * UniversalElectricity.IC2_RATIO), new Object[0]);
					}
				}

				if (isPowerReceptor(outputTile))
				{
					IPowerReceptor receptor = (IPowerReceptor) outputTile;
					double electricityNeeded = Math.min(receptor.powerRequest(), receptor.getPowerProvider().getMaxEnergyStored() - receptor.getPowerProvider().getEnergyStored()) * UniversalElectricity.BC3_RATIO;
					float transferEnergy = (float) Math.min(getJoules(new Object[0]), Math.min(electricityNeeded, 100.0D));
					receptor.getPowerProvider().receiveEnergy((float) (transferEnergy * UniversalElectricity.TO_BC_RATIO), outputDirection.getOpposite());
					setJoules(getJoules(new Object[0]) - transferEnergy, new Object[0]);
				}

			}

		}

		setJoules(getJoules(new Object[0]) - 0.0001D, new Object[0]);

		if (!this.worldObj.isRemote)
		{
			if ((this.ticks % 3L == 0L) && (this.playersUsing > 0))
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
			}
		}
		
		if (this.getJoules() >= this.getMaxJoules())
			this.isFull = true;
		else
			this.isFull = false;
	}

	protected EnumSet getConsumingSides()
	{
		return EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2).getOpposite());
	}

	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("ElecEx", this, new Object[] { Double.valueOf(getJoules(new Object[0])), Integer.valueOf(this.disabledTicks) });
	}

	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			setJoules(dataStream.readDouble(), new Object[0]);
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void openChest()
	{
		this.playersUsing += 1;
	}

	public void closeChest()
	{
		this.playersUsing -= 1;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if ((var5 >= 0) && (var5 < this.containingItems.length))
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		if (PowerFramework.currentFramework != null)
		{
			PowerFramework.currentFramework.loadPowerProvider(this, par1NBTTagCompound);
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; var3++)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);

		if (PowerFramework.currentFramework != null)
		{
			PowerFramework.currentFramework.savePowerProvider(this, par1NBTTagCompound);
		}
	}

	public int getStartInventorySide(ForgeDirection side)
	{
		if (side == ForgeDirection.DOWN)
			return 1;

		if (side == ForgeDirection.UP)
			return 1;

		return 0;
	}

	public int getSizeInventorySide(ForgeDirection side)
	{
		return 1;
	}

	public int getSizeInventory()
	{
		return this.containingItems.length;
	}

	public ItemStack getStackInSlot(int par1)
	{
		return this.containingItems[par1];
	}

	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.containingItems[par1] != null)
		{
			if (this.containingItems[par1].stackSize <= par2)
			{
				ItemStack var3 = this.containingItems[par1];
				this.containingItems[par1] = null;
				return var3;
			}

			ItemStack var3 = this.containingItems[par1].splitStack(par2);

			if (this.containingItems[par1].stackSize == 0)
			{
				this.containingItems[par1] = null;
			}

			return var3;
		}

		return null;
	}

	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.containingItems[par1] != null)
		{
			ItemStack var2 = this.containingItems[par1];
			this.containingItems[par1] = null;
			return var2;
		}

		return null;
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.containingItems[par1] = par2ItemStack;

		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
		{
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}

	public String getInvName()
	{
		return "Advanced Battery Box";
	}

	public int getInventoryStackLimit()
	{
		return 1;
	}

	public double getMaxJoules(Object... data)
	{
		int slot1 = 0;
		int slot2 = 0;
		int slot3 = 0;

		if ((this.containingItems[2] != null) && ((this.containingItems[2].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == "Capacity"))
			slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		if ((this.containingItems[3] != null) && ((this.containingItems[3].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == "Capacity"))
			slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		if ((this.containingItems[4] != null) && ((this.containingItems[4].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == "Capacity"))
			slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
		return 5000000 + slot1 + slot2 + slot3;
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	public boolean isPoweringTo(ForgeDirection side)
	{
		return this.isFull;
	}

	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		return isPoweringTo(side);
	}

	public double getVoltage(Object... data)
	{
		return 120.0D * getVoltageModifier("VoltageModifier");
	}

	public void onReceive(ElectricityPack electricityPack)
	{
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > getInputVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5F, true);
				return;
			}
		}

		setJoules(getJoules(new Object[0]) + electricityPack.getWatts(), new Object[0]);
	}

	public double getInputVoltage()
	{
		return Math.max(getVoltage(new Object[0]), Math.max(120.0D, getVoltageModifier("InputVoltageModifier") * 240.0D));
	}

	private double getVoltageModifier(String type)
	{
		double slot1 = 1.0D;
		double slot2 = 1.0D;
		double slot3 = 1.0D;

		if ((this.containingItems[2] != null) && ((this.containingItems[2].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == type))
			slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		if ((this.containingItems[3] != null) && ((this.containingItems[3].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == type))
			slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		if ((this.containingItems[4] != null) && ((this.containingItems[4].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == type))
			slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
		if (slot1 < 0.0D)
			slot1 = 1.0D / (slot1 * -1.0D);
		if (slot2 < 0.0D)
			slot2 = 1.0D / (slot2 * -1.0D);
		if (slot3 < 0.0D)
			slot3 = 1.0D / (slot3 * -1.0D);
		return slot1 * slot2 * slot3;
	}

	public String getType()
	{
		return "BatteryBox";
	}

	public String[] getMethodNames()
	{
		return new String[] { "getVoltage", "getWattage", "isFull" };
	}

	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
	{
		int getVoltage = 0;
		int getWattage = 1;
		int isFull = 2;

		switch (method)
		{
			case 0:
				return new Object[] { Double.valueOf(getVoltage(new Object[0])) };
			case 1:
				return new Object[] { Double.valueOf(ElectricInfo.getWatts(getJoules(new Object[0]))) };
			case 2:
				return new Object[] { Boolean.valueOf(this.isFull) };
		}
		throw new Exception("Function unimplemented");
	}

	public boolean canAttachToSide(int side)
	{
		return true;
	}

	public void attach(IComputerAccess computer)
	{
	}

	public void detach(IComputerAccess computer)
	{
	}

	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return getConsumingSides().contains(direction.toForgeDirection());
	}

	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0L;
	}

	public int demandsEnergy()
	{
		return (int) (getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
	}

	public int injectEnergy(Direction direction, int i)
	{
		double givenElectricity = i * UniversalElectricity.IC2_RATIO;
		double rejects = 0.0D;

		if (givenElectricity > getRequest().getWatts())
		{
			rejects = givenElectricity - getRequest().getWatts();
		}

		onReceive(new ElectricityPack(givenElectricity / getVoltage(new Object[0]), getVoltage(new Object[0])));

		return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
	}

	public int getMaxSafeInput()
	{
		return 2048;
	}

	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return getConsumingSides().contains(direction.toForgeDirection().getOpposite());
	}

	public int getMaxEnergyOutput()
	{
		return 128;
	}

	public int sendEnergy(int send)
	{
		EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, send);
		MinecraftForge.EVENT_BUS.post(event);
		return event.amount;
	}

	public void setPowerProvider(IPowerProvider provider)
	{
		this.powerProvider = provider;
	}

	public IPowerProvider getPowerProvider()
	{
		return this.powerProvider;
	}

	public int powerRequest()
	{
		return (int) (getMaxJoules() - getJoules());
	}

	public void doWork()
	{
	}

	public boolean isPowerReceptor(TileEntity tileEntity)
	{
		if ((tileEntity instanceof IPowerReceptor))
		{
			IPowerReceptor receptor = (IPowerReceptor) tileEntity;
			IPowerProvider provider = receptor.getPowerProvider();
			return (provider != null) && (provider.getClass().getSuperclass().equals(PowerProvider.class));
		}
		return false;
	}
}