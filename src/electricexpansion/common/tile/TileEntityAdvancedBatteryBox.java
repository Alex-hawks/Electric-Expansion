package electricexpansion.common.tile;

import ic2.api.Direction;
import ic2.api.IElectricItem;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;
import universalelectricity.prefab.tile.TileEntityConductor;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.IModifier;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.items.ItemLinkCard;
import electricexpansion.common.misc.ChargeUtils;
import electricexpansion.common.misc.UniversalPowerUtils;
import electricexpansion.common.misc.UniversalPowerUtils.GenericPack;
import electricexpansion.common.misc.EnumAdvBattBoxMode;

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityStorage 
implements IPacketReceiver, ISidedInventory, IPeripheral, IEnergySink, IEnergySource, IHiveMachine
{
	public static final double BASE_OUTPUT = 20000;
	public static final double BASE_VOLTAGE = 120;
	public static final int INVENTORY_SIZE = 6;

	private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
	public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();

	/**
	 * 0: OFF: none 
	 * 1: BASIC: Electricity (UE, IC2, and RP2 if/when permission is obtained and RP2 is up to date) 
	 * 2: PNEUMATIC: Pneumatic (Intelligence depends on upgrade. BuildCraft, ThermalExpansion)
	 * 3: QUANTUM: Quantum (Depends on Upgrade, Replaces Quantum Battery Box soon)
	 * 4: MEKANISM: Universal Cables (Depends on upgrade. Mekanism) (Unavailable for now) 
	 * 5: FACTORIZATION: Factorization Cables (Depends on upgrade. Factorization) (Unavailable for now) 
	 * 6: UNIVERSAL (Depends on Upgrade(s), Requires availability of Modes: 1, 2, 4 if Mekanism is installed, 5 if
	 * Factorization is installed) (Unavailable for now)
	 */
	private EnumAdvBattBoxMode inputMode = EnumAdvBattBoxMode.OFF;
	private EnumAdvBattBoxMode outputMode = EnumAdvBattBoxMode.OFF;

	private ForgeDirection inputDir = ForgeDirection.UP;
	private ForgeDirection outputDir = ForgeDirection.DOWN;
	private TileEntity inputTile;
	private TileEntity outputTile;
	
	private transient IElectricityNetwork inputNetwork;
    private transient IElectricityNetwork outputNetwork;
    
    private IHiveNetwork hiveNetwork;

	@Override
	public void initiate()
	{
		if (Loader.isModLoaded("IC2"))
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if (this.ticks > 0L)
			if (Loader.isModLoaded("IC2"))
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			switch (this.outputMode)
			{
				case BASIC:
					this.sendElectricalEnergy();
					break;
				case PNEUMATIC:
					this.sendPneumaticEnergy();
					break;
				case QUANTUM:
					this.sendQuantumEnergy();
					break;
				case MEKANISM:
					this.sendMekanismEnergy();
					break;
				case FACTORIZATION:
					this.sendFzEnergy();
					break;
				case UNIVERSAL:
					this.sendUniversalEnergy();
					break;
				default:
					break;
			}

			switch (this.inputMode)
			{
				case BASIC:
					this.drainElectricalEnergy();
					break;
				case PNEUMATIC:
					this.drainPneumaticEnergy();
					break;
				case MEKANISM:
					this.drainMekanismEnergy();
					break;
				case FACTORIZATION:
					this.drainFzEnergy();
					break;
				case UNIVERSAL:
					this.drainUniversalEnergy();
					break;
				default:
					break;
			}
		}

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3L == 0L && this.playersUsing.size() > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
			}
		}

		if (this.inventory[5] != null && this.inventory[5].getItem() instanceof ItemLinkCard)
		{
			ItemLinkCard card = (ItemLinkCard) this.inventory[5].getItem();
			card.getOrCreateLinkData(this.inventory[5], this);
		}
	}

	public void refreshConnections() 
	{
		this.outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDir);
		this.outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDir);

		this.inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDir);
		this.inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, inputDir);
	}

	private boolean sendBatteryEnergy()
	{
		// Batteries (UE, then IC2. Will not call both charge methods)
		if (this.inventory[0] != null)
		{
			if (this.inventory[0].getItem() instanceof IItemElectric)
			{
				this.setJoules(this.getJoules() - (ChargeUtils.UE.charge(this.inventory[0], UniversalPowerUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap(), this.getJoules()) / this.getVoltage(), this.getVoltage()))).toUEWatts());
				return true;
			}
			else if (this.inventory[0].getItem() instanceof IElectricItem)
			{
				this.setJoules(this.getJoules() - (ChargeUtils.IC2.charge(this.inventory[0], UniversalPowerUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap(), this.getJoules()) / this.getVoltage(), this.getVoltage()))).toUEWatts());
				return true;
			}
		}
		return false;
	}

	private boolean sendElectricalEnergy()
	{
		if (sendBatteryEnergy())
			return true;

		// Cables (UE)
		{
			refreshConnections();

			if (outputNetwork != null && inputNetwork != outputNetwork)
			{
				ElectricityPack actualOutput = new ElectricityPack(Math.min(outputNetwork.getLowestCurrentCapacity(), Math.min(this.getOutputCap(), outputNetwork.getRequest().getWatts()) / this.getVoltage()), this.getVoltage());

				if (this.getJoules() > 0 && actualOutput.getWatts() > 0)
				{
					outputNetwork.startProducing(this, actualOutput);
					this.setJoules(this.getJoules() - actualOutput.getWatts());
					return true;
				}
				else
				{
					outputNetwork.stopProducing(this);
				}
			}
		}

		// Cables (IC2, Will not work if already output to UE, will actively
		// avoid Mekanism Cables)
		{
			if (this.getJoules() > 0.0D)
			{
				if (Loader.isModLoaded("IC2"))
				{
					UniversalPowerUtils.GenericPack toOutput = UniversalPowerUtils.INSTANCE.new UEElectricPack(Math.max(this.getJoules(), this.getOutputCap()) / this.getVoltage(), this.getVoltage());

					if (toOutput.toEU() >= 128)
					{
						EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, toOutput.toEU());
						MinecraftForge.EVENT_BUS.post(event);

						this.setJoules(this.getJoules() - toOutput.toUEWatts());
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean sendPneumaticEnergy()
	{
		return false;
	}

	private boolean sendQuantumEnergy()
	{
		ItemStack is = this.inventory[5];
		if (is != null && is.getItem() instanceof ItemLinkCard)
		{
			ItemLinkCard item = (ItemLinkCard) is.getItem();
			if (item.getHasLinkData(is) && !ItemLinkCard.isDataEqual(item.getOrCreateLinkData(is, this), this))
			{
				NBTTagCompound linkData = item.getOrCreateLinkData(is, this);
				int dimensionId = linkData.getInteger("dimension");
				int x = linkData.getInteger("x");
				int y = linkData.getInteger("y");
				int z = linkData.getInteger("z");

				if (this.worldObj.provider.dimensionId == dimensionId || this.hasUpgrade("CrossDimension"))
				{
					if (DimensionManager.getWorld(dimensionId).getBlockTileEntity(x, y, z) instanceof TileEntityAdvancedBatteryBox)
					{
						TileEntityAdvancedBatteryBox target = (TileEntityAdvancedBatteryBox) DimensionManager.getWorld(dimensionId).getBlockTileEntity(x, y, z);
						if (target.getInputMode() == EnumAdvBattBoxMode.QUANTUM)
						{
							GenericPack thisRequest = UniversalPowerUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap() / 2, this.getJoules()) / this.getVoltage(), this.getVoltage());
							GenericPack targetRequest = UniversalPowerUtils.INSTANCE.new UEElectricPack(target.getRequest());

							GenericPack actualSent = ChargeUtils.CommonUtil.getSmallest(thisRequest, targetRequest);

							target.onReceive(actualSent.toUEPack(Math.min(this.getVoltage(), target.getVoltage()), ElectricUnit.VOLTAGE));
							this.setJoules(this.getJoules() - actualSent.toUEWatts() * 2);
						}
					}
				}
			}
		}
		return false;
	}

	private boolean sendMekanismEnergy()
	{
		return false;
	}

	private boolean sendFzEnergy()
	{
		return false;
	}

	private boolean sendUniversalEnergy()
	{
		if (sendElectricalEnergy())
			return true;
		if (sendPneumaticEnergy())
			return true;
		if (sendQuantumEnergy())
			return true;
		if (sendMekanismEnergy())
			return true;
		if (sendFzEnergy())
			return true;
		return false;
	}

	private boolean drainBatteryEnergy()
	{
		if (this.inventory[1] != null)
		{
			if (this.inventory[1].getItem() instanceof IItemElectric)
			{
				this.setJoules(this.getJoules() + (ChargeUtils.UE.discharge(this.inventory[1], UniversalPowerUtils.INSTANCE.new UEElectricPack(this.getOutputCap() / this.getVoltage(), this.getVoltage()))).toUEWatts());
				return true;
			}
			else if (this.inventory[1].getItem() instanceof IElectricItem)

			{
				this.setJoules(this.getJoules() + (ChargeUtils.IC2.discharge(this.inventory[1], UniversalPowerUtils.INSTANCE.new UEElectricPack(this.getOutputCap() / this.getVoltage(), this.getVoltage()))).toUEWatts());
				return true;
			}
		}
		return false;
	}

	private boolean drainElectricalEnergy()
	{
		if (drainBatteryEnergy())
			return true;
		return false;
	}

	private boolean drainPneumaticEnergy()
	{
		return false;
	}

	private boolean drainMekanismEnergy()
	{
		return false;
	}

	private boolean drainFzEnergy()
	{
		return false;
	}

	private boolean drainUniversalEnergy()
	{
		if (drainElectricalEnergy())
			return true;
		if (drainPneumaticEnergy())
			return true;
		if (drainMekanismEnergy())
			return true;
		if (drainFzEnergy())
			return true;
		return false;
	}

	@Override
	public ElectricityPack getRequest()
	{
		return ElectricityPack.getFromWatts(Math.min((this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getOutputCap() / 2.0), this.getVoltage());
	}

	@Override
	protected EnumSet<ForgeDirection> getConsumingSides()
	{
		if (this.inputMode != EnumAdvBattBoxMode.OFF && this.inputMode != EnumAdvBattBoxMode.QUANTUM)
			return EnumSet.of(inputDir);
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, new Object[] { 
			Double.valueOf(this.getJoules()),
					 Integer.valueOf(this.disabledTicks),
					 Byte.valueOf((byte) this.inputMode.ordinal()),
					 Byte.valueOf((byte) this.outputMode.ordinal()),
					 Byte.valueOf((byte) this.inputDir.ordinal()),
					 Byte.valueOf((byte) this.outputDir.ordinal()) 
		});
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				this.setJoules(dataStream.readDouble());
				this.disabledTicks = dataStream.readInt();
				this.inputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
				this.outputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
				this.inputDir = ForgeDirection.getOrientation(dataStream.readByte());
				this.outputDir = ForgeDirection.getOrientation(dataStream.readByte());

				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID);
				this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 0x03);
			}
			catch (Exception e)
			{
			}
		}
		else
		{
			try
			{
        ForgeDirection newInputDir = ForgeDirection.getOrientation(dataStream.readByte());
				ForgeDirection newOutputDir = ForgeDirection.getOrientation(dataStream.readByte());
				EnumAdvBattBoxMode newInputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
				EnumAdvBattBoxMode newOutputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());

				if (this.inputDir != newInputDir) 
					this.setInputDir(newInputDir);
				if (this.outputDir != newOutputDir) 
					this.setOutputDir(newOutputDir);

				if (this.inputMode != newInputMode) 
					this.setInputMode(newInputMode);
				if (this.outputMode != newOutputMode) 
					this.setOutputMode(newOutputMode);
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

	}

	@Override
	public void closeChest()
	{

	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inventory.length)
			{
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.outputMode = EnumAdvBattBoxMode.fromValue(par1NBTTagCompound.getByte("outputMode"));
		this.inputMode = EnumAdvBattBoxMode.fromValue(par1NBTTagCompound.getByte("inputMode"));

		this.outputDir = ForgeDirection.getOrientation(par1NBTTagCompound.getByte("output"));
		this.inputDir = ForgeDirection.getOrientation(par1NBTTagCompound.getByte("input"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.inventory.length; var3++)
		{
			if (this.inventory[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.inventory[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);

		par1NBTTagCompound.setByte("outputMode", (byte) this.outputMode.ordinal());
		par1NBTTagCompound.setByte("inputMode", (byte) this.inputMode.ordinal());

		par1NBTTagCompound.setByte("output", (byte) this.outputDir.ordinal());
		par1NBTTagCompound.setByte("input", (byte) this.inputDir.ordinal());
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.inventory[par1];
	}

	@Override
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

			if (this.inventory[par1].stackSize == 0)
			{
				this.inventory[par1] = null;
			}

			return var3;
		}

		return null;
	}

	@Override
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

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return StatCollector.translateToLocal("tile.advbatbox.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public double getMaxJoules()
	{
		double slot1 = 0;
		double slot2 = 0;
		double slot3 = 0;

		if (this.inventory[2] != null && this.inventory[2].getItem() instanceof IModifier && ((IModifier) this.inventory[2].getItem()).getType(this.inventory[2]) == "Capacity")
		{
			slot1 = ((IModifier) this.inventory[2].getItem()).getEffectiveness(this.inventory[2]);
		}
		if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IModifier && ((IModifier) this.inventory[3].getItem()).getType(this.inventory[3]) == "Capacity")
		{
			slot2 = ((IModifier) this.inventory[3].getItem()).getEffectiveness(this.inventory[3]);
		}
		if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IModifier && ((IModifier) this.inventory[4].getItem()).getType(this.inventory[4]) == "Capacity")
		{
			slot3 = ((IModifier) this.inventory[4].getItem()).getEffectiveness(this.inventory[4]);
		}
		return 5000000 + slot1 + slot2 + slot3;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	@Override
	public double getVoltage()
	{
		return BASE_VOLTAGE * this.getVoltageModifier("VoltageModifier");
	}

	@Override
	public void onReceive(ElectricityPack electricityPack)
	{
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > this.getInputVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5F, true);
				return;
			}
		}

		this.setJoules(this.getJoules() + electricityPack.getWatts());
	}

	public double getInputVoltage()
	{
		return Math.max(this.getVoltage(), Math.max(BASE_VOLTAGE, this.getVoltageModifier("InputVoltageModifier") * this.getVoltageModifier("VoltageModifier") * BASE_VOLTAGE));
	}

	private double getVoltageModifier(String type)
	{
		double slot1 = 1.0D;
		double slot2 = 1.0D;
		double slot3 = 1.0D;

		if (this.inventory[2] != null && this.inventory[2].getItem() instanceof IModifier && ((IModifier) this.inventory[2].getItem()).getType(this.inventory[2]) == type)
		{
			slot1 = ((IModifier) this.inventory[2].getItem()).getEffectiveness(this.inventory[2]);
		}
		if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IModifier && ((IModifier) this.inventory[3].getItem()).getType(this.inventory[3]) == type)
		{
			slot2 = ((IModifier) this.inventory[3].getItem()).getEffectiveness(this.inventory[3]);
		}
		if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IModifier && ((IModifier) this.inventory[4].getItem()).getType(this.inventory[4]) == type)
		{
			slot3 = ((IModifier) this.inventory[4].getItem()).getEffectiveness(this.inventory[4]);
		}
		return slot1 * slot2 * slot3;
	}

	private double getOutputCap()
	{
		double slot1 = 0;
		double slot2 = 0;
		double slot3 = 0;

		if (this.inventory[2] != null && this.inventory[2].getItem() instanceof IModifier && ((IModifier) this.inventory[2].getItem()).getType(this.inventory[2]) == "Unlimiter")
		{
			slot1 = ((IModifier) this.inventory[2].getItem()).getEffectiveness(this.inventory[2]);
		}
		if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IModifier && ((IModifier) this.inventory[3].getItem()).getType(this.inventory[3]) == "Unlimiter")
		{
			slot2 = ((IModifier) this.inventory[3].getItem()).getEffectiveness(this.inventory[3]);
		}
		if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IModifier && ((IModifier) this.inventory[4].getItem()).getType(this.inventory[4]) == "Unlimiter")
		{
			slot3 = ((IModifier) this.inventory[4].getItem()).getEffectiveness(this.inventory[4]);
		}

		return (100 + slot1) * (100 + slot2) * (100 + slot3) / 1000000 * BASE_OUTPUT;
	}

	@Override
	public String getType()
	{
		return this.getInvName().replaceAll(" ", "");
	}

	@Override
	public String[] getMethodNames()
	{
		return new String[] { "getVoltage", "getEnergy", "isFull" };
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
				return new Object[] { Double.valueOf(this.getVoltage()) };
			case getWattage:
				return new Object[] { Double.valueOf(this.getJoules()) };
			case isFull:
				return new Object[] { Boolean.valueOf(this.getJoules() >= this.getMaxJoules()) };
		}
		throw new Exception("Function unimplemented");
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
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return this.getConsumingSides().contains(direction.toForgeDirection());
	}

	@Override
	public boolean isAddedToEnergyNet()
	{
		return this.ticks > 0L;
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
		double rejects = 0.0D;

		if (givenElectricity > this.getRequest().getWatts())
		{
			rejects = givenElectricity - this.getRequest().getWatts();
		}

		this.onReceive(ElectricityPack.getFromWatts(givenElectricity, this.getVoltage()));

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

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if (direction == this.inputDir)
		{
			return this.inputMode != EnumAdvBattBoxMode.OFF && this.inputMode != EnumAdvBattBoxMode.QUANTUM;
		} else if (direction == this.outputDir)
		{
			return this.outputMode != EnumAdvBattBoxMode.OFF && this.outputMode != EnumAdvBattBoxMode.QUANTUM;
		}
		return false;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	public boolean isLinkCardValid(ItemStack is)
	{
		if (is != null && is.getItem() instanceof ItemLinkCard)
		{
			NBTTagCompound link = ((ItemLinkCard) is.getItem()).getOrCreateLinkData(is, this);
			if (ItemLinkCard.isDataEqual(link, this))
			{
				return false;
			}
			return true;
		}
		else
			return false;
	}

	public void setLinkCard(ItemStack is)
	{
		this.inventory[5] = is;
	}

	public EnumAdvBattBoxMode getInputMode()
	{
		return this.inputMode;
	}

	public EnumAdvBattBoxMode getOutputMode()
	{
		return this.outputMode;
	}

	public ArrayList<EnumAdvBattBoxMode> getAvailableModes()
	{
		ArrayList<EnumAdvBattBoxMode> toReturn = new ArrayList<EnumAdvBattBoxMode>();
		toReturn.add(EnumAdvBattBoxMode.OFF);
		toReturn.add(EnumAdvBattBoxMode.BASIC);
		if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
		{
			// toReturn.add(EnumAdvBattBoxModeMode.PNEUMATIC);
		}
		if (this.hasUpgrade("Quantum"))
		{
			toReturn.add(EnumAdvBattBoxMode.QUANTUM);
		}
		if (Loader.isModLoaded("Mekanism") && this.hasUpgrade("Mekansim"))
		{
			// toReturn.add(EnumAdvBattBoxModeMod.MEKANISM);
		}
		if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
		{
			// toReturn.add(EnumAdvBattBoxModeMod.FACTORIZATION);
		}
		if ((!Loader.isModLoaded("Mekanism") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
		{
			// toReturn.add(EnumAdvBattBoxModeMod.UNIVERSAL);
		}
		return toReturn;
	}

	public void setInputMode(EnumAdvBattBoxMode newMode)
	{
		if (this.worldObj.isRemote)
		{
			this.sendUpdateToServer(inputDir, outputDir, newMode, outputMode);
		}
		else
		{
			switch (newMode)
			{
				default:
					this.inputMode = EnumAdvBattBoxMode.OFF;
					break;
				case BASIC:
					this.inputMode = newMode;
					break;
				case PNEUMATIC:
					if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
						this.inputMode = newMode;
					break;
				case QUANTUM:
					if (this.hasUpgrade("Quantum"))
						this.inputMode = newMode;
					break;
				case MEKANISM:
					if (Loader.isModLoaded("Mekanism|Core") && this.hasUpgrade("Mekansim"))
						this.inputMode = newMode;
					break;
				case FACTORIZATION:
					if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
						this.inputMode = newMode;
					break;
				case UNIVERSAL:
					if ((!Loader.isModLoaded("Mekanism|Core") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
						this.inputMode = newMode;
			}
			refreshConnections();
			if (inputNetwork != null)
			{
				inputNetwork.stopRequesting(this);
			}
			if (inputTile instanceof TileEntityConductor)
			{
				((TileEntityConductor) inputTile).updateAdjacentConnections();
			}
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
		}
	}

	public void setOutputMode(EnumAdvBattBoxMode newMode)
	{
		if (this.worldObj.isRemote) {
			this.sendUpdateToServer(inputDir, outputDir, inputMode, newMode);
		}
		else
		{
			switch (newMode)
			{
				default:
					this.outputMode = EnumAdvBattBoxMode.OFF;
					break;
				case BASIC:
					this.outputMode = newMode;
					break;
				case PNEUMATIC:
					if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
						this.outputMode = newMode;
					break;
				case QUANTUM:
					if (this.hasUpgrade("Quantum"))
						this.outputMode = newMode;
					break;
				case MEKANISM:
					if (Loader.isModLoaded("Mekanism") && this.hasUpgrade("Mekansim"))
						this.outputMode = newMode;
					break;
				case FACTORIZATION:
					if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
						this.outputMode = newMode;
					break;
				case UNIVERSAL:
					if ((!Loader.isModLoaded("Mekanism") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
						this.outputMode = newMode;
			}
			refreshConnections();
			if (outputNetwork != null)
			{
				outputNetwork.stopProducing(this);
			}
			if (outputTile instanceof TileEntityConductor)
			{
				((TileEntityConductor) outputTile).updateAdjacentConnections();
			}
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
		}
	}

	public boolean hasUpgrade(String upgrade)
	{
		if (this.inventory[2] != null && this.inventory[2].getItem() instanceof IModifier)
		{
			String type = ((IModifier) this.inventory[2].getItem()).getType(this.inventory[2]);
			if (type != null && type.equals(upgrade))
				return true;
		}

		if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IModifier)
		{
			String type = ((IModifier) this.inventory[3].getItem()).getType(this.inventory[3]);
			if (type != null && type.equals(upgrade))
				return true;
		}

		if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IModifier)
		{
			String type = ((IModifier) this.inventory[4].getItem()).getType(this.inventory[4]);
			if (type != null && type.equals(upgrade))
				return true;
		}

		return false;
	}

	public void setInputDir(ForgeDirection newDirection)
	{
		if (this.worldObj.isRemote)
		{
			this.sendUpdateToServer(newDirection, outputDir, inputMode, outputMode);
		}
		else
		{
			if (newDirection != this.inputDir)
			{
				refreshConnections();
				if (inputNetwork != null)
				{
					inputNetwork.stopRequesting(this);
				}
				if (inputTile instanceof TileEntityConductor)
				{
					((TileEntityConductor) inputTile).updateAdjacentConnections();
				}
				this.inputDir = newDirection;
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
			}

		}
	}

	public void setOutputDir(ForgeDirection newDirection)
	{
		if (this.worldObj.isRemote)
		{
			this.sendUpdateToServer(inputDir, newDirection, inputMode, outputMode);
		}
		else
		{
			if (newDirection != this.outputDir)
			{
				refreshConnections();
				if (outputNetwork != null)
				{
					outputNetwork.stopProducing(this);
				}
				if (outputTile instanceof TileEntityConductor)
				{
					((TileEntityConductor) outputTile).updateAdjacentConnections();
				}
				this.outputDir = newDirection;
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
			}
		}
	}

	private void sendUpdateToServer(ForgeDirection inputDir, ForgeDirection outputDir, EnumAdvBattBoxMode inputMode, EnumAdvBattBoxMode outputMode)
	{
		PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, new Object[] { 
			Byte.valueOf((byte) inputDir.ordinal()), 
			Byte.valueOf((byte) outputDir.ordinal()),
			Byte.valueOf((byte) inputMode.ordinal()), 
			Byte.valueOf((byte) outputMode.ordinal()) 
		}));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return (var1 == 0 || var1 == 1) ? new int[] { var1 } : new int[] {};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return i == j && (i == 0 || i == 1);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return i == j && (i == 0 || i == 1);
	}

	public ForgeDirection getInputDir()
	{
		return this.inputDir;
	}

	public ForgeDirection getOutputDir()
	{
		return this.outputDir;
	}

	@Override
	public IElectricityNetwork[] getNetworks()
	{
			return new IElectricityNetwork[] { this.inputNetwork, this.outputNetwork };
	}

	@Override
	public IHiveNetwork getHiveNetwork()
	{
			return this.hiveNetwork;
	}

	@Override
	public boolean setHiveNetwork(IHiveNetwork hiveNetwork, boolean mustOverride)
	{
			if (this.hiveNetwork == null || mustOverride)
			{
					this.hiveNetwork = hiveNetwork;
					
					for (IElectricityNetwork net : getNetworks())
							this.hiveNetwork.addNetwork(net);
					
					return true;
			}
			return false;
	}
}
