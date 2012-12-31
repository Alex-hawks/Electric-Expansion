package electricexpansion.common.cables;

import java.util.EnumSet;

import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.PacketManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityLogisticsWire extends TileEntityConductorBase implements IRedstoneProvider
{
	// everything is in the helper class.
	// this class MUST remain existent...
	
	public boolean buttonStatus0 = false;
	public boolean buttonStatus1 = false;
	public boolean buttonStatus2 = false;
		
	private ElectricityPack oldPack;
	
	@Override
	public void initiate()
	{
	//	this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockLogisticsWire.blockID);
	//	oldPack = new ElectricityPack();
		
		PacketManager.sendPacketToClients(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 4, this.buttonStatus0, this.buttonStatus1, this.buttonStatus2), this.worldObj, new Vector3(this), 12);
	}
	
	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
			try
			{
				int id = dataStream.readInt();
				if(id == -1) 
				{
					this.buttonStatus0 = dataStream.readBoolean();			
				}
				if(id == 0)
				{
					this.buttonStatus1 = dataStream.readBoolean();			
				}
				if(id == 1)
				{
					this.buttonStatus2 = dataStream.readBoolean();			
				}
				if(id == 3) 
				{
						PacketManager.sendPacketToClients(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 4, this.buttonStatus0, this.buttonStatus1, this.buttonStatus2), this.worldObj, new Vector3(this), 12);
				}
				if(id == 4)
				{
					this.buttonStatus0 = dataStream.readBoolean();			
					this.buttonStatus1 = dataStream.readBoolean();			
					this.buttonStatus2 = dataStream.readBoolean();			
				}
				
				if (this.worldObj.isRemote)
				{
					this.visuallyConnected[0] = dataStream.readBoolean();
					this.visuallyConnected[1] = dataStream.readBoolean();
					this.visuallyConnected[2] = dataStream.readBoolean();
					this.visuallyConnected[3] = dataStream.readBoolean();
					this.visuallyConnected[4] = dataStream.readBoolean();
					this.visuallyConnected[5] = dataStream.readBoolean();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	
	@Override
	public boolean isPoweringTo(ForgeDirection side)
	{
		if(this.buttonStatus0 && this.getNetwork().getProduced().getWatts() > 0)
			return true;
		
		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{
		return isPoweringTo(side);
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.buttonStatus0 = par1NBTTagCompound.getBoolean("buttonStatus0");
		this.buttonStatus1 = par1NBTTagCompound.getBoolean("buttonStatus1");
		this.buttonStatus2 = par1NBTTagCompound.getBoolean("buttonStatus2");

	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("buttonStatus0", this.buttonStatus0);
		par1NBTTagCompound.setBoolean("buttonStatus1", this.buttonStatus1);
		par1NBTTagCompound.setBoolean("buttonStatus2", this.buttonStatus2);

	}
	
/*	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticks % 20 == 0)
		{
			if (!this.worldObj.isRemote)
			{

		//System.out.println("TTTTT");
		ElectricityPack newPack = this.getNetwork().getProduced();
		
		if(this.oldPack.getWatts() == 0 && newPack.getWatts() > 0 && this.buttonStatus0)
		{
			this.worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, ElectricExpansion.blockLogisticsWire.blockID);
			System.out.println("Toggling from off to on!");
		}
		
		newPack = this.oldPack;

			}
		}
	}
*/
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(this.channel, this, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5]);
	}
	
}