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

public class TileEntityMultimeter extends TileEntityElectricityReceiver implements IJouleStorage, IPacketReceiver
{
	private double joules = 0;
	
	private boolean isFull = false;

	private int playersUsing = 0;

	public ElectricityPack elecPack = new ElectricityPack(0, 0);


	public TileEntityMultimeter()
	{
		super();
	}

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2).getOpposite()));
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isDisabled())
		{
			if (!this.worldObj.isRemote)
			{
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), inputDirection);

				if (inputTile != null)
				{
					if (inputTile instanceof IConductor)
					{
					 elecPack = ((IConductor)inputTile).getNetwork().getProduced();

						if (this.joules >= this.getMaxJoules())
						{
							((IConductor) inputTile).getNetwork().stopRequesting(this);
						}
						else
						{
							((IConductor) inputTile).getNetwork().startRequesting(this, this.getMaxJoules() - this.getJoules(), this.getVoltage());
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
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMultimeter.MULTIMETER_METADATA + 2);
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), outputDirection);

				if (tileEntity != null)
				{
					TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), outputDirection);

					// Output UE electricity
					if (connector instanceof IConductor)
					{
						double joulesNeeded = ((IConductor) connector).getNetwork().getRequest().getWatts();
						double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.elecPack.voltage), ElectricInfo.getAmps(this.joules, this.elecPack.voltage)), 80), 0);

						if (!this.worldObj.isRemote && transferAmps > 0)
						{
							((IConductor) connector).getNetwork().startProducing(this, transferAmps, elecPack.voltage);
							this.setJoules(this.joules - ElectricInfo.getWatts(transferAmps, elecPack.voltage));
						}
						else
						{
							((IConductor) connector).getNetwork().stopProducing(this);
						}
					}
				}
			}
			this.setJoules(this.joules - 1);

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
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, (int) 1, this.elecPack.amperes, this.elecPack.voltage);

	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			int id = dataStream.readInt();
			if(id == -1) 
			{
				if(dataStream.readBoolean())
				{
					this.playersUsing++;
				}
			else playersUsing--;
			}
			if(id != -1)
			{
			this.elecPack.amperes = dataStream.readDouble();			
			this.elecPack.voltage = dataStream.readDouble();
			}
			
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
		this.joules = par1NBTTagCompound.getDouble("electricityStored");
	}
	

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("electricityStored", this.joules);
	}

	
	public String getInvName()
	{
		return "Multimeter";
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
		return 100;
	}
	

}
