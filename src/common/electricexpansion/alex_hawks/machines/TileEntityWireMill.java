package electricexpansion.alex_hawks.machines;

import java.util.EnumSet;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.alex_hawks.blocks.BlockWireMill;
import electricexpansion.alex_hawks.misc.WireMillRecipes;

public class TileEntityWireMill extends TileEntityElectricityReceiver implements IInventory, ISidedInventory, IPacketReceiver
{
	public final double WATTS_PER_TICK = 500;
	public int drawingTicks = 0;
	public double wattsReceived = 0;
	/**
	 * The ItemStacks that hold the items currently being used in the wire mill;
	 * 0 = battery;
	 * 1 = input;
	 * 2 = output;
	 */
	private ItemStack[] inventory = new ItemStack[3];

	private int playersUsing = 0;
	public int orientation;
	
	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockWireMill.metaWireMill + 2)));
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockWireMill.metaWireMill + 2);
			TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), inputDirection);

			if (inputTile != null)
			{
				if (inputTile instanceof IConductor)
				{
					if (this.canDraw())
					{
						((IConductor) inputTile).getNetwork().startRequesting(this, WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
						this.wattsReceived = Math.max(Math.min(this.wattsReceived + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts(), WATTS_PER_TICK), 0);
					}
					else
					{
						((IConductor) inputTile).getNetwork().stopRequesting(this);
					}
				}
			}
		}

		// The bottom slot is for portable
		// batteries
		if (this.inventory[0] != null)
		{
			if (this.inventory[0].getItem() instanceof IItemElectric)
			{
				IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();

				if (electricItem.canProduceElectricity())
				{
					double receivedWattHours = electricItem.onUse(Math.min(electricItem.getMaxJoules(this.inventory[0]) * 0.01, ElectricInfo.getWattHours(WATTS_PER_TICK)), this.inventory[0]);
					this.wattsReceived += ElectricInfo.getWatts(receivedWattHours);
				}
			}
		}

		if (this.wattsReceived >= this.WATTS_PER_TICK-50 && !this.isDisabled())
		{
			if (this.inventory[1] != null && this.canDraw() && this.drawingTicks == 0)
			{
				this.drawingTicks = this.getDrawingTime();
			}

			if (this.canDraw() && this.drawingTicks > 0)
			{
				this.drawingTicks--;

				if (this.drawingTicks < 1)
				{
					this.drawItem();
					this.drawingTicks = 0;
				}
			}
			else
			{
				this.drawingTicks = 0;
			}

			this.wattsReceived -= this.WATTS_PER_TICK;
		}

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, Vector3.get(this), 12);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{return PacketManager.getPacket("ElecEx", this, this.drawingTicks, this.disabledTicks);}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			this.drawingTicks = dataStream.readInt();
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{e.printStackTrace();}
	}

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

	//Check all conditions and see if we can start drawing
	public boolean canDraw()
	{
		boolean canWork = false;
		ItemStack inputSlot = this.inventory[1];
		ItemStack outputSlot = this.inventory[2];
		if(inputSlot != null)
		{
			if (WireMillRecipes.drawing().getDrawingResult(inputSlot) == null)
			{canWork = false;}
			else if (WireMillRecipes.drawing().getDrawingResult(inputSlot) != null && outputSlot == null)
			{canWork = true;}
			else if (outputSlot != null) 
			{
				String result = (String)(WireMillRecipes.stackSizeToOne(WireMillRecipes.drawing().getDrawingResult(inputSlot)) + "");
				String output2 = (String)(WireMillRecipes.stackSizeToOne(outputSlot) + "");
				int maxSpaceForSuccess = Math.min(outputSlot.getMaxStackSize(),inputSlot.getMaxStackSize()) - WireMillRecipes.drawing().getDrawingResult(inputSlot).stackSize;

				if ((result.equals(output2)) && !(outputSlot.stackSize < maxSpaceForSuccess))
				{canWork = false;}
				else if ((result.equals(output2)) && (outputSlot.stackSize< maxSpaceForSuccess))
				{canWork = true;}
			}
		}

		return canWork;
	}

	/**
	 * Turn item(s) from the wire mill source stack into the appropriate drawn item(s) in the wire mill result stack
	 */
	public void drawItem()
	{
		if (this.canDraw())
		{
			ItemStack resultItemStack = WireMillRecipes.drawing().getDrawingResult(this.inventory[1]);

			if (this.inventory[2] == null)
				this.inventory[2] = resultItemStack.copy();
			else if (this.inventory[2].isItemEqual(resultItemStack))
				this.inventory[2].stackSize = this.inventory[2].stackSize + resultItemStack.stackSize;

			this.inventory[1].stackSize = this.inventory[1].stackSize - WireMillRecipes.drawing().getInputQTY(this.inventory[1]);

			if (this.inventory[1].stackSize <= 0)
				this.inventory[1] = null;
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.drawingTicks = par1NBTTagCompound.getInteger("drawingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inventory.length)
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
		}
	}
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("drawingTicks", this.drawingTicks);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.inventory.length; ++var3)
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

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		if(side == side.DOWN || side == side.UP)
			return side.ordinal();
		else return 2;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{return 1;}

	@Override
	public int getSizeInventory()
	{return this.inventory.length;}

	@Override
	public ItemStack getStackInSlot(int par1)
	{return this.inventory[par1];}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var3;

			if (this.inventory[par1].stackSize <= par2)
			{
				var3 = this.inventory[par1];
				this.inventory[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.inventory[par1].splitStack(par2);

				if (this.inventory[par1].stackSize == 0)
					this.inventory[par1] = null;

				return var3;
			}
		}
		else return null;
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
		else return null;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
			par2ItemStack.stackSize = this.getInventoryStackLimit();
	}

	@Override
	public String getInvName()
	{return "Wire Mill";}

	@Override
	public int getInventoryStackLimit()
	{return 64;}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;}

	@Override
	public double getVoltage()
	{return 120;}

	/**
	 * @return The amount of ticks required to draw this item
	 */
	public int getDrawingTime()
	{
		if(this.inventory[1] != null)
		{
			if(WireMillRecipes.drawing().getDrawingResult(this.inventory[1]) != null)
			{
				return (int) WireMillRecipes.drawing().getDrawingTicks(this.inventory[1]);
			}
		}
		return -1;
	}
}
