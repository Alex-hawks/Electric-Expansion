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
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

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

			/* 156 */ForgeDirection outputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
			TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

			/* 159 */if (!this.worldObj.isRemote)
			{
				/* 161 */TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection.getOpposite());

				/* 163 */ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
				/* 164 */ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);

				/* 166 */if ((outputNetwork != null) && (inputNetwork != outputNetwork))
				{
					/* 168 */double outputWatts = Math.min(outputNetwork.getRequest(new TileEntity[] { this }).getWatts(), Math.min(getJoules(new Object[0]), 10000.0D));

					/* 170 */if ((getJoules(new Object[0]) > 0.0D) && (outputWatts > 0.0D))
					{
						/* 172 */outputNetwork.startProducing(this, outputWatts / getVoltage(new Object[0]), getVoltage(new Object[0]));
						/* 173 */setJoules(getJoules(new Object[0]) - outputWatts, new Object[0]);
					}
					else
					{
						/* 177 */outputNetwork.stopProducing(this);
					}
				}

			}

			/* 183 */if (getJoules(new Object[0]) > 0.0D)
			{
				/* 185 */if (Loader.isModLoaded("IC2"))
				{
					/* 187 */if (getJoules(new Object[0]) >= 128.0D * UniversalElectricity.IC2_RATIO)
					{
						/* 189 */EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, 128);
						/* 190 */MinecraftForge.EVENT_BUS.post(event);
						/* 191 */setJoules(getJoules(new Object[0]) - (128.0D * UniversalElectricity.IC2_RATIO - event.amount * UniversalElectricity.IC2_RATIO), new Object[0]);
					}
				}

				/* 195 */if (isPowerReceptor(outputTile))
				{
					/* 197 */IPowerReceptor receptor = (IPowerReceptor) outputTile;
					/* 198 */double electricityNeeded = Math.min(receptor.powerRequest(), receptor.getPowerProvider().getMaxEnergyStored() - receptor.getPowerProvider().getEnergyStored()) * UniversalElectricity.BC3_RATIO;
					/* 199 */float transferEnergy = (float) Math.min(getJoules(new Object[0]), Math.min(electricityNeeded, 100.0D));
					/* 200 */receptor.getPowerProvider().receiveEnergy((float) (transferEnergy * UniversalElectricity.TO_BC_RATIO), outputDirection.getOpposite());
					/* 201 */setJoules(getJoules(new Object[0]) - transferEnergy, new Object[0]);
				}

			}

		}

		/* 210 */setJoules(getJoules(new Object[0]) - 0.0001D, new Object[0]);

		/* 212 */if (!this.worldObj.isRemote)
		{
			/* 214 */if ((this.ticks % 3L == 0L) && (this.playersUsing > 0))
			{
				/* 216 */PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
			}
		}
	}

	protected EnumSet getConsumingSides()
	{
		/* 225 */return EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2).getOpposite());
	}

	public Packet getDescriptionPacket()
	{
		/* 231 */return PacketManager.getPacket("ElecEx", this, new Object[] { Double.valueOf(getJoules(new Object[0])), Integer.valueOf(this.disabledTicks) });
	}

	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			/* 239 */setJoules(dataStream.readDouble(), new Object[0]);
			/* 240 */this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			/* 244 */e.printStackTrace();
		}
	}

	public void openChest()
	{
		/* 251 */this.playersUsing += 1;
	}

	public void closeChest()
	{
		/* 257 */this.playersUsing -= 1;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		/* 266 */super.readFromNBT(par1NBTTagCompound);

		/* 268 */NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		/* 269 */this.containingItems = new ItemStack[getSizeInventory()];

		/* 271 */for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			/* 273 */NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			/* 274 */byte var5 = var4.getByte("Slot");

			/* 276 */if ((var5 >= 0) && (var5 < this.containingItems.length))
			{
				/* 278 */this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		/* 282 */if (PowerFramework.currentFramework != null)
		{
			/* 284 */PowerFramework.currentFramework.loadPowerProvider(this, par1NBTTagCompound);
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		/* 294 */super.writeToNBT(par1NBTTagCompound);
		/* 295 */NBTTagList var2 = new NBTTagList();

		/* 297 */for (int var3 = 0; var3 < this.containingItems.length; var3++)
		{
			/* 299 */if (this.containingItems[var3] != null)
			{
				/* 301 */NBTTagCompound var4 = new NBTTagCompound();
				/* 302 */var4.setByte("Slot", (byte) var3);
				/* 303 */this.containingItems[var3].writeToNBT(var4);
				/* 304 */var2.appendTag(var4);
			}
		}

		/* 308 */par1NBTTagCompound.setTag("Items", var2);

		/* 310 */if (PowerFramework.currentFramework != null)
		{
			/* 312 */PowerFramework.currentFramework.savePowerProvider(this, par1NBTTagCompound);
		}
	}

	public int getStartInventorySide(ForgeDirection side)
	{
		/* 319 */if (side == ForgeDirection.DOWN)
			return 1;

		/* 321 */if (side == ForgeDirection.UP)
			return 1;

		/* 323 */return 0;
	}

	public int getSizeInventorySide(ForgeDirection side)
	{
		/* 329 */return 1;
	}

	public int getSizeInventory()
	{
		/* 335 */return this.containingItems.length;
	}

	public ItemStack getStackInSlot(int par1)
	{
		/* 341 */return this.containingItems[par1];
	}

	public ItemStack decrStackSize(int par1, int par2)
	{
		/* 347 */if (this.containingItems[par1] != null)
		{
			/* 351 */if (this.containingItems[par1].stackSize <= par2)
			{
				/* 353 */ItemStack var3 = this.containingItems[par1];
				/* 354 */this.containingItems[par1] = null;
				/* 355 */return var3;
			}

			/* 359 */ItemStack var3 = this.containingItems[par1].splitStack(par2);

			/* 361 */if (this.containingItems[par1].stackSize == 0)
			{
				/* 363 */this.containingItems[par1] = null;
			}

			/* 366 */return var3;
		}

		/* 371 */return null;
	}

	public ItemStack getStackInSlotOnClosing(int par1)
	{
		/* 378 */if (this.containingItems[par1] != null)
		{
			/* 380 */ItemStack var2 = this.containingItems[par1];
			/* 381 */this.containingItems[par1] = null;
			/* 382 */return var2;
		}

		/* 386 */return null;
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		/* 393 */this.containingItems[par1] = par2ItemStack;

		/* 395 */if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
		{
			/* 397 */par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}

	public String getInvName()
	{
		/* 404 */return "Advanced Battery Box";
	}

	public int getInventoryStackLimit()
	{
		/* 410 */return 1;
	}

	public double getMaxJoules(Object[] data)
	{
		/* 416 */int slot1 = 0;
		int slot2 = 0;
		int slot3 = 0;

		/* 418 */if ((this.containingItems[2] != null) && ((this.containingItems[2].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == "Capacity"))
			/* 419 */slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		/* 420 */if ((this.containingItems[3] != null) && ((this.containingItems[3].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == "Capacity"))
			/* 421 */slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		/* 422 */if ((this.containingItems[4] != null) && ((this.containingItems[4].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == "Capacity"))
		{
			/* 423 */slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
		}
		/* 425 */return 5000000 + slot1 + slot2 + slot3;
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		/* 431 */return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	public boolean isPoweringTo(ForgeDirection side)
	{
		/* 437 */return this.isFull;
	}

	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		/* 443 */return isPoweringTo(side);
	}

	public double getVoltage(Object[] data)
	{
		/* 450 */return 120.0D * getVoltageModifier("VoltageModifier");
	}

	public void onReceive(ElectricityPack electricityPack)
	{
		/* 465 */if (UniversalElectricity.isVoltageSensitive)
		{
			/* 467 */if (electricityPack.voltage > getInputVoltage())
			{
				/* 469 */this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5F, true);
				/* 470 */return;
			}
		}

		/* 474 */setJoules(getJoules(new Object[0]) + electricityPack.getWatts(), new Object[0]);
	}

	public double getInputVoltage()
	{
		/* 479 */return Math.max(getVoltage(new Object[0]), Math.max(120.0D, getVoltageModifier("InputVoltageModifier") * 240.0D));
	}

	private double getVoltageModifier(String type)
	{
		/* 484 */double slot1 = 1.0D;
		double slot2 = 1.0D;
		double slot3 = 1.0D;

		/* 486 */if ((this.containingItems[2] != null) && ((this.containingItems[2].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[2].getItem()).getName(this.containingItems[2]) == type))
			/* 487 */slot1 = ((IModifier) this.containingItems[2].getItem()).getEffectiveness(this.containingItems[2]);
		/* 488 */if ((this.containingItems[3] != null) && ((this.containingItems[3].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[3].getItem()).getName(this.containingItems[3]) == type))
			/* 489 */slot2 = ((IModifier) this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		/* 490 */if ((this.containingItems[4] != null) && ((this.containingItems[4].getItem() instanceof IModifier)) && (((IModifier) this.containingItems[4].getItem()).getName(this.containingItems[4]) == type))
			/* 491 */slot3 = ((IModifier) this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
		/* 492 */if (slot1 < 0.0D)
			/* 493 */slot1 = 1.0D / (slot1 * -1.0D);
		/* 494 */if (slot2 < 0.0D)
			/* 495 */slot2 = 1.0D / (slot2 * -1.0D);
		/* 496 */if (slot3 < 0.0D)
		{
			/* 497 */slot3 = 1.0D / (slot3 * -1.0D);
		}
		/* 499 */return slot1 * slot2 * slot3;
	}

	public String getType()
	{
		/* 509 */return "BatteryBox";
	}

	public String[] getMethodNames()
	{
		/* 515 */return new String[] { "getVoltage", "getWattage", "isFull" };
	}

	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
	{
		/* 522 */int getVoltage = 0;
		/* 523 */int getWattage = 1;
		/* 524 */int isFull = 2;

		/* 526 */switch (method)
		{
			case 0:
				/* 529 */return new Object[] { Double.valueOf(getVoltage(new Object[0])) };
			case 1:
				/* 531 */return new Object[] { Double.valueOf(ElectricInfo.getWatts(getJoules(new Object[0]))) };
			case 2:
				/* 533 */return new Object[] { Integer.valueOf(2) };
		}
		/* 535 */throw new Exception("Function unimplemented");
	}

	public boolean canAttachToSide(int side)
	{
		/* 542 */return true;
	}

	public void attach(IComputerAccess computer)
	{
	}

	public void detach(IComputerAccess computer)
	{
	}

	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		/* 559 */return getConsumingSides().contains(direction.toForgeDirection());
	}

	public boolean isAddedToEnergyNet()
	{
		/* 565 */return this.ticks > 0L;
	}

	public int demandsEnergy()
	{
		/* 571 */return (int) (getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
	}

	public int injectEnergy(Direction direction, int i)
	{
		/* 577 */double givenElectricity = i * UniversalElectricity.IC2_RATIO;
		/* 578 */double rejects = 0.0D;

		/* 580 */if (givenElectricity > getRequest().getWatts())
		{
			/* 582 */rejects = givenElectricity - getRequest().getWatts();
		}

		/* 585 */onReceive(new ElectricityPack(givenElectricity / getVoltage(new Object[0]), getVoltage(new Object[0])));

		/* 587 */return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
	}

	public int getMaxSafeInput()
	{
		/* 593 */return 2048;
	}

	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		/* 599 */return getConsumingSides().contains(direction.toForgeDirection().getOpposite());
	}

	public int getMaxEnergyOutput()
	{
		/* 605 */return 128;
	}

	public int sendEnergy(int send)
	{
		/* 610 */EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, send);
		/* 611 */MinecraftForge.EVENT_BUS.post(event);
		/* 612 */return event.amount;
	}

	public void setPowerProvider(IPowerProvider provider)
	{
		/* 619 */this.powerProvider = provider;
	}

	public IPowerProvider getPowerProvider()
	{
		/* 625 */return this.powerProvider;
	}

	public int powerRequest()
	{
		/* 631 */return (int) (getMaxJoules(new Object[0]) - getJoules(new Object[0]));
	}

	public void doWork()
	{
	}

	public boolean isPowerReceptor(TileEntity tileEntity)
	{
		/* 647 */if ((tileEntity instanceof IPowerReceptor))
		{
			/* 649 */IPowerReceptor receptor = (IPowerReceptor) tileEntity;
			/* 650 */IPowerProvider provider = receptor.getPowerProvider();
			/* 651 */return (provider != null) && (provider.getClass().getSuperclass().equals(PowerProvider.class));
		}
		/* 653 */return false;
	}
}

/*
 * Location: C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip Qualified Name:
 * electricexpansion.common.tile.TileEntityAdvancedBatteryBox JD-Core Version: 0.6.2
 */