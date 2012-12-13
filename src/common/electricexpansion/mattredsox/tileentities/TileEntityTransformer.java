package electricexpansion.mattredsox.tileentities;

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
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import buildcraft.api.power.IPowerProvider;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;
import electricexpansion.ElectricExpansion;
import electricexpansion.mattredsox.blocks.BlockTransformer;
import electricexpansion.mattredsox.items.ItemTransformerCoil;

public class TileEntityTransformer extends TileEntityElectricityReceiver implements IJouleStorage, IPacketReceiver, IInventory
{
	private double joules = 0;
	
	public ItemStack[] containingItems = new ItemStack[2];

	private boolean isFull = false;

	private int playersUsing = 0;

	public IPowerProvider powerProvider;

	public ElectricityPack elecPack = new ElectricityPack(0, 0);
	
	public double voltageAdd = elecPack.voltage;

	public TileEntityTransformer()
	{
		super();
	}

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.meta + 4), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.meta + 4).getOpposite()));
	}


	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.meta + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), inputDirection);

				if (inputTile != null)
				{
					if (inputTile instanceof IConductor)
					{
						 elecPack = ((IConductor)inputTile).getNetwork().getProduced();
						 
						 if(this.containingItems[1] != null && this.containingItems[1].getItem() instanceof ItemTransformerCoil)
						 {
							if(this.containingItems[1].stackSize == 1)
								voltageAdd = voltageAdd + 120;
						 }
						 
						if (this.joules >= this.getMaxJoules())
						{
							((IConductor) inputTile).getNetwork().stopRequesting(this);
						}
						else
						{
							((IConductor) inputTile).getNetwork().startRequesting(this, this.getMaxJoules() - this.getJoules(), voltageAdd);
							this.setJoules(this.joules + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts());
						}
					}
				}
			}
			/**
			 * Output Electricity
			 */

			if (this.joules > 0)
			{
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.meta + 2);
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), outputDirection);

				if (tileEntity != null)
				{
					TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), outputDirection);

					// Output UE electricity
					if (connector instanceof IConductor)
					{
						double joulesNeeded = ((IConductor) connector).getNetwork().getRequest().getWatts();
						double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, voltageAdd), ElectricInfo.getAmps(this.joules, voltageAdd)), 80), 0);

						if (!this.worldObj.isRemote && transferAmps > 0)
						{
							((IConductor) connector).getNetwork().startProducing(this, transferAmps, voltageAdd);
							this.setJoules(this.joules - ElectricInfo.getWatts(transferAmps, voltageAdd));
						}
						else
						{
							((IConductor) connector).getNetwork().stopProducing(this);
						}

					}
					
				}
			}
		}

		// Energy Loss
		this.setJoules(this.joules - 50);

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
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.elecPack.voltage);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.elecPack.voltage = dataStream.readDouble();
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
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		return "          Transformer";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 4;
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
		return 1000;
	}

	@Override
	public double getVoltage() 
	{	
/*		int slot1 = 0, slot2 = 0, slot3 = 0;
		
		if(this.containingItems[0] != null && this.containingItems[0].getItem() instanceof ItemTransformerCoil)
				{
			if(this.containingItems[0].stackSize == 1)
				slot1 = 120;
				}
		//if(this.containingItems[3] != null && this.containingItems[3].getItem() instanceof IModifier && ((IModifier)this.containingItems[3].getItem()).getName(this.containingItems[3]) == "Capacity")
		//	slot2 = ((IModifier)this.containingItems[3].getItem()).getEffectiveness(this.containingItems[3]);
		//if(this.containingItems[4] != null && this.containingItems[4].getItem() instanceof IModifier && ((IModifier)this.containingItems[4].getItem()).getName(this.containingItems[4]) == "Capacity")
		///	slot3 = ((IModifier)this.containingItems[4].getItem()).getEffectiveness(this.containingItems[4]);
*/	
		//return elecPack.voltage + slot1 + slot2 + slot3;
		return 120;
	}
	

	
}
