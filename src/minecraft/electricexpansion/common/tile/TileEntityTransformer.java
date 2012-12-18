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

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()));
		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
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
				if (!this.isDisabled())
				{

					ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
					TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getInventoryStackLimit()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChest()
	{
		// TODO Auto-generated method stub

	}
}
