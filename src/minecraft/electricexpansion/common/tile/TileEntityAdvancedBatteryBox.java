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
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IJouleStorage;
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

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityStorage implements IJouleStorage, IRedstoneProvider, IPacketReceiver, ISidedInventory, IPeripheral, IEnergySink, IEnergySource
{
	private ItemStack[] containingItems = new ItemStack[5];

	private boolean isFull = false;

	private int playersUsing = 0;

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockAdvBatteryBox.blockID);

		if (Loader.isModLoaded("IC2"))
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if (this.ticks > 0)
		{
			if (Loader.isModLoaded("IC2"))
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			/**
			 * Recharges the battery box via batteries
			 */
			if (this.containingItems[0] != null && this.getJoules() > 0)
			{
				if (this.containingItems[0].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[0].getItem();

					if (electricItem.canProduceElectricity())
					{
						double ampsToGive = Math.min(ElectricInfo.getAmps(Math.min(electricItem.getMaxJoules(this.containingItems[0]) * 0.005, this.getJoules()), this.getVoltage()), this.getJoules());
						double joules = electricItem.onReceive(ampsToGive, this.getVoltage(), this.containingItems[0]);
						this.setJoules(this.getJoules() - (ElectricInfo.getJoules(ampsToGive, this.getVoltage(), 1) - joules));
					}
				}

				else if (this.containingItems[0].getItem() instanceof IElectricItem)
				{
					double sent = ElectricItem.charge(containingItems[0], (int) (joules * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
					this.setJoules(joules - sent);
				}
			}

			/**
			 * Output Electricity
			 */
			if (this.containingItems[1] != null && this.getJoules() < this.getMaxJoules())
			{
				if (this.containingItems[1].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();

					if (electricItem.canProduceElectricity())
					{
						double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(this.containingItems[1]) * 0.005, this.containingItems[1]);
						this.setJoules(this.getJoules() + joulesReceived);
					}
				}

				else if (containingItems[1].getItem() instanceof IElectricItem)
				{
					IElectricItem item = (IElectricItem) containingItems[1].getItem();
					if (item.canProvideEnergy())
					{
						double gain = ElectricItem.discharge(containingItems[1], (int) ((int) (getMaxJoules() - joules) * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
						this.setJoules(joules + gain);
					}
				}
			}

			if (!this.worldObj.isRemote)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection.getOpposite());
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
				ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);

				if (outputNetwork != null && inputNetwork != outputNetwork)
				{
					double outputWatts = Math.min(outputNetwork.getRequest().getWatts(), Math.min(this.getJoules(), 10000));

					if (this.getJoules() > 0 && outputWatts > 0)
					{
						outputNetwork.startProducing(this, outputWatts / this.getVoltage(), this.getVoltage());
						this.setJoules(this.getJoules() - outputWatts);
					}
					else
					{
						outputNetwork.stopProducing(this);
					}
				}

			}

			if (this.joules > 0)
			{
				if (Loader.isModLoaded("IC2"))
				{
					if (joules >= 128 * UniversalElectricity.IC2_RATIO)
					{
						EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, 128);
						MinecraftForge.EVENT_BUS.post(event);
						setJoules(this.joules - (128 * UniversalElectricity.IC2_RATIO - event.amount * UniversalElectricity.IC2_RATIO));
					}
				}

			}
		}

		/**
		 * Gradually lose energy.
		 */
		this.setJoules(this.getJoules() - 0.0001);

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);

			}
		}
	}

	@Override
	protected EnumSet<ForgeDirection> getConsumingSides()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.getJoules(), this.disabledTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.setJoules(dataStream.readDouble());
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
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
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
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
	}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		if (side == ForgeDirection.DOWN) { return 1; }

		if (side == ForgeDirection.UP) { return 1; }

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
		return "Advanced Battery Box";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		int slot1 = 0, slot2 = 0, slot3 = 0;

		if (this.containingItems[2] != null && this.containingItems[2].getItem() instanceof IModifier && ((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == "Capacity")
			slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		if (this.containingItems[3] != null && this.containingItems[3].getItem() instanceof IModifier && ((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == "Capacity")
			slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		if (this.containingItems[4] != null && this.containingItems[4].getItem() instanceof IModifier && ((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == "Capacity")
			slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);

		return 5000000 + slot1 + slot2 + slot3;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
	public double getVoltage(Object... data)
	{
		return 240 * this.getVoltageModifier("VoltageModifier");
	}

	public double getInputVoltage()
	{
		return Math.max(this.getVoltage(), Math.max(240, this.getVoltageModifier("InputVoltageModifier") * 240));
	}

	private double getVoltageModifier(String type)
	{
		double slot1 = 1, slot2 = 1, slot3 = 1;

		if (this.containingItems[2] != null && this.containingItems[2].getItem() instanceof IModifier && ((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == type)
			slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		if (this.containingItems[3] != null && this.containingItems[3].getItem() instanceof IModifier && ((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == type)
			slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		if (this.containingItems[4] != null && this.containingItems[4].getItem() instanceof IModifier && ((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == type)
			slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
		if (slot1 < 0)
			slot1 = 1 / (slot1 * -1);
		if (slot2 < 0)
			slot2 = 1 / (slot2 * -1);
		if (slot3 < 0)
			slot3 = 1 / (slot3 * -1);
		return slot1 * slot2 * slot3;
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
		return new String[] { "getVoltage", "getWattage", "isFull" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
	{

		final int getVoltage = 0;
		final int getWattage = 1;
		final int isFull = 2;

		switch (method)
		{
			case getVoltage:
				return new Object[] { getVoltage() };
			case getWattage:
				return new Object[] { ElectricInfo.getWatts(this.getJoules()) };
			case isFull:
				return new Object[] { isFull };
			default:
				throw new Exception("Function unimplemented");
		}
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

	// IC2 Functions
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return this.getConsumingSides().contains(direction.toForgeDirection());
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0;
	}

	@Override
	public int demandsEnergy()
	{
		return (int) (this.getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int injectEnergy(Direction direction, int i)
	{
		double givenElectricity = i * UniversalElectricity.IC2_RATIO;
		double rejects = 0;

		if (givenElectricity > this.getRequest().getWatts())
		{
			rejects = givenElectricity - this.getRequest().getWatts();
		}

		this.onReceive(new ElectricityPack(givenElectricity / this.getVoltage(), this.getVoltage()));

		return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
	}

	@Override
	public int getMaxSafeInput()
	{
		return 2048;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return this.getConsumingSides().contains(direction.toForgeDirection().getOpposite());
	}

	@Override
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

}