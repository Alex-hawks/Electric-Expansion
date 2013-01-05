package electricexpansion.common.tile;

import java.util.EnumSet;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import electricexpansion.common.ElectricExpansion;

public class TileEntityTransformer extends TileEntityElectricityReceiver implements IRotatable, IPacketReceiver
{
	// USING A WRENCH ONE CAN CHANGE THE TRANSFORMER TO EITHER STEP UP OR STEP DOWN.
	public boolean stepUp = false;

	public int type;

	@Override
	public void initiate()
	{
		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) >= 8)
		{
			this.type = 8;
		}

		else if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) >= 4)
		{
			this.type = 4;
		}

		else
		{
			this.type = 0;
		}

		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - type + 2), ForgeDirection.getOrientation(this.getBlockMetadata() - type + 2).getOpposite()));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockTransformer.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 20 == 0)
		{
			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - type + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

				// Check if requesting power on output
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - type + 2);
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork network = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
				ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);

				if (network != null && inputNetwork != null && network != inputNetwork)
				{

					if (network.getRequest().getWatts() > 0)
					{

						inputNetwork.startRequesting(this, network.getRequest());

						if (inputNetwork.getProduced().getWatts() > 0)
						{

							System.out.print("input more than 0");

							ElectricityPack actualEnergy = inputNetwork.consumeElectricity(this);
							double typeChange;

							if (this.type == 0)
								typeChange = 60;

							if (this.type == 4)
								typeChange = 120;

							else
							{
								typeChange = 240;
							}

							double newVoltage = actualEnergy.voltage + typeChange;

							if (!stepUp)
								newVoltage = actualEnergy.voltage - typeChange;

							network.startProducing(this, inputNetwork.getProduced().getWatts() / newVoltage, newVoltage);
						}
						else
						{
							network.stopProducing(this);
						}

					}

					else
					{
						network.stopRequesting(this);
					}

				}

				if (!this.worldObj.isRemote)
				{
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				}

			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.stepUp);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.stepUp = dataStream.readBoolean();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.stepUp = par1NBTTagCompound.getBoolean("stepUp");
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("stepUp", this.stepUp);
	}

	@Override
	public ForgeDirection getDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - type);
	}

	@Override
	public void setDirection(ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
	}

}