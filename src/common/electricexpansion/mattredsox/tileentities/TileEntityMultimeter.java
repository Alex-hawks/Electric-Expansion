package electricexpansion.mattredsox.tileentities;

import java.util.EnumSet;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.ElectricExpansion;
import electricexpansion.mattredsox.blocks.BlockMultimeter;

public class TileEntityMultimeter extends TileEntityElectricityReceiver implements IPacketReceiver
{
	public ElectricityPack electricityReading = new ElectricityPack();

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
						this.electricityReading = ((IConductor) inputTile).getNetwork().getProduced();

						if (this.ticks % 20 == 0)
						{
							PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, Vector3.get(this), 12);
						}
					}
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
}
