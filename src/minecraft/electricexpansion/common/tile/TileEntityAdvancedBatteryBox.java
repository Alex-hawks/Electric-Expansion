package electricexpansion.common.tile;

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
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.common.ElectricExpansion;

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityReceiver implements IJouleStorage, IPacketReceiver, IRedstoneProvider, IInventory, ISidedInventory, IPeripheral
{
	private static final double MAX_OUTPUT = 10000;

	private double joules = 0;

	private ItemStack[] containingItems = new ItemStack[5];

	private boolean isFull = false;

	private int playersUsing = 0;

	public TileEntityAdvancedBatteryBox()
	{
		super();
	}

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockAdvBatteryBox.blockID);
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
						this.setJoules(this.joules + electricityPack.getWatts());
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

			/*
			 * The top slot is for recharging items. Check if the item is a electric item. If so,
			 * recharge it.
			 */
			if (this.containingItems[0] != null && this.joules > 0)
			{
				if (this.containingItems[0].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[0].getItem();
					double ampsToGive = Math.min(ElectricInfo.getAmps(electricItem.getMaxJoules(this.containingItems[0]) * 0.005, this.getVoltage()), this.joules);
					double joules = electricItem.onReceive(ampsToGive, this.getVoltage(), this.containingItems[0]);
					this.setJoules(this.joules - (ElectricInfo.getJoules(ampsToGive, this.getVoltage(), 1) - joules));
				}
			}

			// Power redstone if the battery box is full
			boolean isFullThisCheck = false;

			if (this.joules >= this.getMaxJoules())
			{
				isFullThisCheck = true;
			}

			if (this.isFull != isFullThisCheck)
			{
				this.isFull = isFullThisCheck;
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockAdvBatteryBox.blockID);
			}

			/**
			 * Output Electricity
			 */

			/**
			 * The bottom slot is for decharging. Check if the item is a electric item. If so,
			 * decharge it.
			 */
			if (this.containingItems[1] != null && this.joules < this.getMaxJoules())
			{
				if (this.containingItems[1].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();

					if (electricItem.canProduceElectricity())
					{
						double joulesReceived = electricItem.onUse(electricItem.getMaxJoules(this.containingItems[1]) * 0.005, this.containingItems[1]);
						this.setJoules(this.joules + joulesReceived);
					}
				}
			}

			if (!this.worldObj.isRemote)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);

				if (outputNetwork != null && inputNetwork != outputNetwork)
				{
					double outputWatts = Math.min(outputNetwork.getRequest().getWatts(), Math.min(this.getJoules(), this.MAX_OUTPUT));

					if (this.getJoules() > 0 && outputWatts > 0)
					{
						outputNetwork.startProducing(this, outputWatts / this.getVoltage(), this.getVoltage());
						this.setJoules(this.joules - outputWatts);
					}
					else
					{
						outputNetwork.stopProducing(this);
					}
				}
			}
		}

		// Energy Loss
		this.setJoules(this.joules - 0.0005);
		
		byte ticks = 0;
		
		if (!this.worldObj.isRemote)
		{
			this.ticks++;

			if (ticks == 20)
			{
				ticks = 0;

				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.joules, this.disabledTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.joules = dataStream.readDouble();
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
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		this.playersUsing--;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.joules = par1NBTTagCompound.getDouble("electricityStored");

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
		par1NBTTagCompound.setDouble("electricityStored", this.joules);
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
		if (side == side.DOWN) { return 1; }

		if (side == side.UP) { return 1; }

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
	public double getJoules(Object... data)
	{
		return this.joules;
	}

	@Override
	public void setJoules(double joules, Object... data)
	{
		this.joules = Math.max(Math.min(joules, this.getMaxJoules()), 0);
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
				return new Object[] { ElectricInfo.getWatts(joules) };
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
	public void detach(IComputerAccess computer)
	{
	}

	@Override
	public void attach(IComputerAccess computer)
	{

	}

}
