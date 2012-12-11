package electricexpansion.mattredsox.tileentities;

import java.util.EnumSet;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NBTTagCompound;
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
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.ElectricExpansion;
import electricexpansion.mattredsox.blocks.BlockMultimeter;

public class TileEntityMultimeter extends TileEntityElectricityReceiver implements IPacketReceiver
{
	private int playersUsing = 0;

	/**
	 * The reading this multimeter did on how much electricity was produced on the last tick.
	*/
	public final ElectricityPack electricityReading = new ElectricityPack();

	@Override
	public void initiate()
	{
		//The connection should be only one side, since that's all you need to test and get a reading.
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2)));
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!this.worldObj.isRemote)
		{
			if (!this.isDisabled())
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), inputDirection);

				if (inputTile != null)
				{
					if (inputTile instanceof IConductor)
					{
					 	this.electricityReading = ((IConductor)inputTile).getNetwork().getProduced();
					}
					else
					{
						this.electricityReading.amperes = 0;
						this.electricityReading.voltage = 0;
					}
				}
				else
				{
					this.electricityReading.amperes = 0;
					this.electricityReading.voltage = 0;
				}
			
			}
		
			if (this.ticks % 10 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, Vector3.get(this), 12);
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 1, this.electricityReading.amperes, this.electricityReading.voltage);

	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if(this.worldObj.isRemote)
		{
			try
			{
				final int id = dataStream.readInt();
				
				if(id == -1) 
				{
					if(dataStream.readBoolean())
					{
						this.playersUsing++;
					}
					else
					{
						playersUsing--;
					}
				}
				else
				{
					this.electricityReading.amperes = dataStream.readDouble();			
					this.electricityReading.voltage = dataStream.readDouble();
				}
				
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
}
