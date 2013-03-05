package electricexpansion.common.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.InsulationRecipes;

public class TileEntityInsulatingMachine extends TileEntityElectricityReceiver
implements IInventory, ISidedInventory, IPacketReceiver, IJouleStorage, IEnergyTile, IEnergySink
{
	public final double WATTS_PER_TICK = 500.0D;
	public final double TRANSFER_LIMIT = 1250.0D;
	private int processTicks = 0;
	private double joulesStored = 0.0D;
	private int recipeTicks = 0;
	public static double maxJoules = 150000.0D;

	private ItemStack[] inventory = new ItemStack[3];

	private int playersUsing = 0;
	public int orientation;
	private int baseID = 0;
	private int baseMeta = 0;
	private boolean initialized;

	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2)));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockInsulationMachine.blockID);

		this.initialized = true;

		if (Loader.isModLoaded("IC2"))
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		}
	}

	public void invalidate()
	{
		super.invalidate();

		if (this.initialized)
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

		if (!this.worldObj.isRemote)
		{
			ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
			TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

			ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);

			if (inputNetwork != null)
			{
				if (this.joulesStored < maxJoules)
				{
					inputNetwork.startRequesting(this, Math.min(getMaxJoules(new Object[0]) - getJoules(new Object[0]), 1250.0D) / getVoltage(new Object[0]), getVoltage(new Object[0]));
					ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
					setJoules(this.joulesStored + electricityPack.getWatts(), new Object[0]);

					if (UniversalElectricity.isVoltageSensitive)
					{
						if (electricityPack.voltage > getVoltage(new Object[0]))
						{
							this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2.0F, true);
						}
					}
				}
				else
				{
					inputNetwork.stopRequesting(this);
				}

			}

		}

		if ((this.inventory[0] != null) && (this.joulesStored < getMaxJoules()))
		{
			if ((this.inventory[0].getItem() instanceof IItemElectric))
			{
				IItemElectric electricItem = (IItemElectric)this.inventory[0].getItem();

				if (electricItem.canProduceElectricity())
				{
					double joulesReceived = electricItem.onUse(Math.max(electricItem.getMaxJoules(new Object[] { this.inventory[0] }) * 0.005D, 1250.0D), this.inventory[0]);
					setJoules(this.joulesStored + joulesReceived);
				}
			}
		}

		if ((this.joulesStored >= this.WATTS_PER_TICK - 50.0D) && (!this.isDisabled()))
		{
			if ((this.inventory[1] != null) && (canProcess()) && ((this.processTicks == 0) || (this.baseID != this.inventory[1].itemID) || (this.baseMeta != this.inventory[1].getItemDamage())))
			{
				this.baseID = this.inventory[1].itemID;
				this.baseMeta = this.inventory[1].getItemDamage();
				this.processTicks = getProcessingTime();
				this.recipeTicks = getProcessingTime();
			}

			if ((canProcess()) && (this.processTicks > 0))
			{
				this.processTicks -= 1;

				if (this.processTicks < 1)
				{
					processItem();
					this.processTicks = 0;
				}
				getClass(); this.joulesStored -= 500.0D;
			}
			else
			{
				this.processTicks = 0;
			}
		}

		if (!this.worldObj.isRemote)
		{
			if ((this.ticks % 3L == 0L) && (this.playersUsing > 0))
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
			}
		}

		this.joulesStored = Math.min(this.joulesStored, getMaxJoules(new Object[0]));
		this.joulesStored = Math.max(this.joulesStored, 0.0D);
	}

	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("ElecEx", this, new Object[] { Integer.valueOf(this.processTicks), Integer.valueOf(this.disabledTicks), Double.valueOf(this.joulesStored), Integer.valueOf(this.recipeTicks) });
	}

	public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
			this.disabledTicks = dataStream.readInt();
			this.joulesStored = dataStream.readDouble();
			this.recipeTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void openChest()
	{
		if (!this.worldObj.isRemote)
			PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 15.0D);
		this.playersUsing += 1;
	}

	public void closeChest()
	{
		this.playersUsing -= 1;
	}

	public boolean canProcess()
	{
		boolean canWork = false;
		ItemStack inputSlot = this.inventory[1];
		int outputSlot = (this.inventory[2] != null) ? this.inventory[2].stackSize : 0;
		if (inputSlot != null)
		{
			if ((InsulationRecipes.getProcessing().getProcessResult(inputSlot) > 0) && (InsulationRecipes.getProcessing().getProcessResult(inputSlot) + outputSlot <= 64))
			{
				canWork = true;
			}
		}

		return canWork;
	}

	public void processItem()
	{
		if (canProcess())
		{
			int result = InsulationRecipes.getProcessing().getProcessResult(this.inventory[1]);

			if (this.inventory[2] == null)
				this.inventory[2] = new ItemStack(ElectricExpansion.itemParts, result, 6);
			else if (this.inventory[2].stackSize + result <= 64) {
				this.inventory[2].stackSize += result;
			}
			InsulationRecipes.getProcessing(); this.inventory[1].stackSize -= InsulationRecipes.getInputQTY(this.inventory[1]);

			if (this.inventory[1].stackSize <= 0)
				this.inventory[1] = null;
		}
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("processTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[getSizeInventory()];
		try
		{
			this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
		}
		catch (Exception e)
		{
		}

		for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if ((var5 >= 0) && (var5 < this.inventory.length))
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("processTicks", this.processTicks);
		NBTTagList var2 = new NBTTagList();
		par1NBTTagCompound.setDouble("joulesStored", getJoules(new Object[0]));

		for (int var3 = 0; var3 < this.inventory.length; var3++)
		{
			if (this.inventory[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.inventory[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}
		par1NBTTagCompound.setTag("Items", var2);
	}

	public int getStartInventorySide(ForgeDirection side)
	{
		if ((side == ForgeDirection.DOWN) || (side == ForgeDirection.UP)) {
			return side.ordinal();
		}
		return 2;
	}

	public int getSizeInventorySide(ForgeDirection side)
	{
		return 1;
	}

	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	public ItemStack getStackInSlot(int par1)
	{
		return this.inventory[par1];
	}

	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventory[par1] != null)
		{
			if (this.inventory[par1].stackSize <= par2)
			{
				ItemStack var3 = this.inventory[par1];
				this.inventory[par1] = null;
				return var3;
			}

			ItemStack var3 = this.inventory[par1].splitStack(par2);

			if (this.inventory[par1].stackSize == 0) {
				this.inventory[par1] = null;
			}
			return var3;
		}

		return null;
	}

	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var2 = this.inventory[par1];
			this.inventory[par1] = null;
			return var2;
		}

		return null;
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;

		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
			par2ItemStack.stackSize = getInventoryStackLimit();
	}

	public String getInvName()
	{
		return "Insultion Refiner";
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	public double getVoltage(Object... data)
	{
		return 120.0D;
	}

	public int getProcessingTime()
	{
		if (this.inventory[1] != null)
		{
			if (InsulationRecipes.getProcessing().getProcessResult(this.inventory[1]) != 0)
			{
				InsulationRecipes.getProcessing(); 
				return InsulationRecipes.getProcessTicks(this.inventory[1]).intValue();
			}
		}
		return -1;
	}

	public int getProcessTimeLeft()
	{
		return this.processTicks;
	}

	public double getJoules(Object... data)
	{
		return this.joulesStored;
	}

	public void setJoules(double joules, Object... data)
	{
		this.joulesStored = joules;
	}

	public double getMaxJoules(Object... data)
	{
		return maxJoules;
	}

	public boolean isAddedToEnergyNet()
	{
		return this.initialized;
	}

	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		if (direction.toForgeDirection() == ForgeDirection.getOrientation(getBlockMetadata() + 2))
		{
			return true;
		}

		return false;
	}

	public int demandsEnergy()
	{
		return (int)((getMaxJoules(new Object[0]) - this.joulesStored) * UniversalElectricity.TO_IC2_RATIO);
	}

	public int injectEnergy(Direction direction, int i)
	{
		double givenEnergy = i * UniversalElectricity.IC2_RATIO;
		double rejects = 0.0D;
		double neededEnergy = getMaxJoules(new Object[0]) - this.joulesStored;

		if (givenEnergy < neededEnergy)
		{
			this.joulesStored += givenEnergy;
		}
		else if (givenEnergy > neededEnergy)
		{
			this.joulesStored += neededEnergy;
			rejects = givenEnergy - neededEnergy;
		}

		return (int)(rejects * UniversalElectricity.TO_IC2_RATIO);
	}

	public int getMaxSafeInput()
	{
		return 2048;
	}
}
