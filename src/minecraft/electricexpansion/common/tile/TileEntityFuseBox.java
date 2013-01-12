package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.IItemFuse;
import electricexpansion.common.ElectricExpansion;

public class TileEntityFuseBox extends TileEntityElectricityReceiver implements IRotatable, IPacketReceiver
{
	private ItemStack inventory = null;

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
			if (!this.worldObj.isRemote)
			{
				if(this.hasFuse())
				{
					ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
					TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

					ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
					TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

					ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);
					ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
					
					if (outputNetwork != null && inputNetwork != null && outputNetwork != inputNetwork)
					{
						ElectricityPack request = outputNetwork.getRequest();
						inputNetwork.startRequesting(this, request);
						
						ElectricityPack recieved = inputNetwork.consumeElectricity(this);
						
						outputNetwork.startProducing(this, recieved);
						
						if(recieved.voltage > ((IItemFuse) this.inventory.getItem()).getMaxVolts(this.inventory))
						{
							((IItemFuse) this.inventory.getItem()).onFuseTrip(this.inventory);
						}
					}
					else 
					{
						outputNetwork.stopProducing(this);
						inputNetwork.stopRequesting(this);
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
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
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
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
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
	
	public boolean hasFuse()
	{
		if (this.inventory != null)
		{
			if (this.inventory.getItem() instanceof IItemFuse)
			{
				return ((IItemFuse)this.inventory.getItem()).isValidFuse(this.inventory);
			}
		}
		return false;
	}

}