package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.ElectricExpansion;

public class TileEntityTransformer extends TileEntityElectricityReceiver implements IPacketReceiver, IRotatable, IInventory
{
	public ElectricityPack electricityReading = new ElectricityPack();
	private ElectricityPack lastReading = new ElectricityPack();
	private ItemStack[] containingItems = new ItemStack[5];
	private int playersUsing = 0;

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockTransformer.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 20 == 0)
		{
			this.lastReading = this.electricityReading;

			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

				if (!this.isDisabled())
				{
					if (inputTile != null)
					{
						if (inputTile instanceof IConductor)
						{
							this.electricityReading = ((IConductor) inputTile).getNetwork().getProduced();
						}
						else
						{
							this.electricityReading = new ElectricityPack();
						}
					}
					else
					{
						this.electricityReading = new ElectricityPack();
					}
				}

				// Check if requesting power on output
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork network = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
				ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);

				if (network != null)
				{
					if (inputNetwork != null)
					{

						if (network.getRequest().getWatts() != 0)
						{
							System.out.println(network.getRequest().voltage + " REQUESTED VOLTAGE OUT");
							System.out.println(network.getRequest().getWatts() + " REQUESTED WATT OUT");

							double requestedAmp = network.getRequest().amperes;
							double requestedVolts = network.getRequest().voltage;

							double actualProduce = network.getRequest().getWatts() / 25;

							inputNetwork.startRequesting(this, network.getRequest());

							if (inputNetwork.getProduced().getWatts() != 0)
							{
								System.out.println(inputNetwork.getProduced().voltage + " PRODUCED VOLTAGE INPUT");
								System.out.println(inputNetwork.getProduced().getWatts() + " PRODUCED WATT INPUT");
								System.out.println(actualProduce + " ACTUAL REQUESTED OUT");

								network.consumeElectricity(this);
								network.startProducing(this, actualProduce / requestedVolts, network.getRequest().voltage);
							}

						}

						else
						{
							network.stopProducing(this);
						}
					}

				}
				// Send packet to clients with Amps and Volts
				if (this.electricityReading.getWatts() != this.lastReading.getWatts())
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 20);
				}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.electricityReading.amperes, this.electricityReading.voltage);

	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			try
			{
				this.electricityReading.amperes = dataStream.readDouble();
				this.electricityReading.voltage = dataStream.readDouble();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public String getInvName()
	{
		return "Multimeter";
	}

	@Override
	public ForgeDirection getDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata());
	}

	@Override
	public void setDirection(ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
	}

	@Override
	public int getSizeInventory()
	{
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return this.containingItems[var1];
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
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
	public int getInventoryStackLimit()
	{
		return 4;
	}
}
