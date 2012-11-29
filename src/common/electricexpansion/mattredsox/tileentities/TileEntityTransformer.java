package electricexpansion.mattredsox.tileentities;

import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import buildcraft.api.power.IPowerProvider;
import electricexpansion.mattredsox.blocks.BlockAdvBatteryBox;

public class TileEntityTransformer extends TileEntityElectricityReceiver implements IJouleStorage, IInventory
{
	private double joules = 0;

	private ItemStack[] containingItems = new ItemStack[2];

	private boolean isFull = false;

	private int playersUsing = 0;

	public double voltin;
	public double voltout;
	
	public TileEntityTransformer()
	{
		super();
	}

	@Override
	public double wattRequest()
	{
		if (!this.isDisabled()) { return ElectricInfo.getWatts(this.getMaxJoules()) - ElectricInfo.getWatts(this.joules); }

		return 0;
	}

	@Override
	public boolean canReceiveFromSide(ForgeDirection side)
	{
		return side == ForgeDirection.getOrientation(this.getBlockMetadata() - BlockAdvBatteryBox.BATTERY_BOX_METADATA + 2).getOpposite();
	}

	@Override
	public boolean canConnect(ForgeDirection side)
	{
		return canReceiveFromSide(side) || side == ForgeDirection.getOrientation(this.getBlockMetadata() - BlockAdvBatteryBox.BATTERY_BOX_METADATA + 2);
	}

	@Override
	public void onReceive(Object sender, double amps, double voltage, ForgeDirection side)
	{
		if (!this.isDisabled())
		{
			if (voltage > this.getVoltage())
			{
				this.worldObj.createExplosion((Entity) null, this.xCoord, this.yCoord, this.zCoord, 1F, true);
				return;
			}

			this.setJoules(this.joules + ElectricInfo.getJoules(amps, voltage, 1));
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			// The top slot is for recharging
			// items. Check if the item is a
			// electric item. If so, recharge it.
			if (this.containingItems[0] != null && this.joules > 0)
			{
				if (this.containingItems[0].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[0].getItem();
					double ampsToGive = Math.min(ElectricInfo.getAmps(electricItem.getMaxJoules() * 0.005, this.getVoltage()), this.joules);
					double joules = electricItem.onReceive(ampsToGive, this.getVoltage(), this.containingItems[0]);
					this.setJoules(this.joules - (ElectricInfo.getJoules(ampsToGive, this.getVoltage(), 1) - joules));
				}
				else if (this.containingItems[0].getItem() instanceof IElectricItem)
				{
					//	if(this.hasIC2Comp == true)
					//{
					double sent = ElectricItem.charge(containingItems[0], (int) (joules * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
					this.setJoules(joules - sent);
					//	}
				}
			}

			// The bottom slot is for decharging.
			// Check if the item is a electric
			// item. If so, decharge it.
			if (this.containingItems[1] != null && this.joules < this.getMaxJoules())
			{
				if (this.containingItems[1].getItem() instanceof IItemElectric)
				{
					IItemElectric electricItem = (IItemElectric) this.containingItems[1].getItem();

					if (electricItem.canProduceElectricity())
					{
						double joulesReceived = electricItem.onUse(electricItem.getMaxJoules() * 0.005, this.containingItems[1]);
						this.setJoules(this.joules + joulesReceived);
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

			// Output electricity
			if (this.joules > 0)
			{
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockAdvBatteryBox.BATTERY_BOX_METADATA + 2));
				TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockAdvBatteryBox.BATTERY_BOX_METADATA + 2));

				if (connector != null)
				{
					// Output UE electricity
					if (connector instanceof IConductor)
					{
						double joulesNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor) connector).getNetwork());
						double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.joules, this.getVoltage())), 80), 0);

						if (!this.worldObj.isRemote)
						{
							ElectricityManager.instance.produceElectricity(this, (IConductor) connector, transferAmps, this.getVoltage());
						}

						this.setJoules(this.joules - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
					}
				}
			}
		}

		this.setJoules(this.joules - 0.00005);
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
		return "   Advanced Battery Box";	}

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
		return 1;
	}

	public double getVoltage()
	{
/*		if(this.getMaxJoules() == 3000000) {
		return 240; }
		
		if(this.getMaxJoules() == 6000000) {
		return 240; }
		
		if(this.getMaxJoules() == 9000000) {
		return 480; }
		
		if(this.getMaxJoules() == 1200000) {
		return 480; }*/
		
		return 120;
		
	}
}
