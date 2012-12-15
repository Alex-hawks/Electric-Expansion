package electricexpansion.common.helpers;

import java.util.Collections;
import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.prefab.tile.TileEntityConductor;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import electricexpansion.api.CableInterfaces.ISelectiveConnector;
import electricexpansion.common.ElectricExpansion;

/**
 * 
 * @author Alex_hawks Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityCableHelper extends TileEntityConductor implements ISelectiveConnector
{
	@Override
	public boolean canUpdate()
	{
		return true;
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		ticks++;
		if(ticks == 20)
		{
			this.registerConnections();
			ticks = 0;
			System.out.println("The code was called!!! 1");
		}
		System.out.println("The code was called!!! 2");
	}
	
	public TileEntityCableHelper()
	{
	//	this.reset();
		super();
		this.channel = ElectricExpansion.CHANNEL;
	}

	@Override
	public double getResistance()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		switch (meta)
		{
			case 0:
				return 0.05;
			case 1:
				return 0.04;
			case 2:
				return 0.02;
			case 3:
				return 0.2;
			case 4:
				return 0.005;
			default:
				return 0.05;
		}
	}

	@Override
	public double getMaxAmps()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch (meta)
		{
			case 0:
				return 500;
			case 1:
				return 60; // Bit less than a basic Coal-Generator. #Cruel
			case 2:
				return 200;
			case 3:
				return 2500; // HV
			case 4:
				return 1000;
			default:
				return 500;
		}
	}
	
	public void registerConnections()
	{
		int xOffset = 0, yOffset = 0, zOffset = 0;
		
		if(!this.worldObj.isRemote)
		{
			ElectricityConnections.unregisterConnector(this);
			EnumSet<ForgeDirection> validDirections = EnumSet.noneOf(ForgeDirection.class);
		
			for(int i = 0; i < 6; i++)
			{
				xOffset = ForgeDirection.getOrientation(i).offsetX;
				yOffset = ForgeDirection.getOrientation(i).offsetY;
				zOffset = ForgeDirection.getOrientation(i).offsetZ;
				
				int neighbourX = this.xCoord + xOffset;
				int neighbourY = this.yCoord + yOffset;
				int neighbourZ = this.zCoord + zOffset;
				
				TileEntity neighbourTE = this.worldObj.getBlockTileEntity(neighbourX, neighbourY, neighbourZ);
				
				if(ElectricityConnections.isConnector(neighbourTE))
				{
					if(ElectricityConnections.canConnect(neighbourTE, ForgeDirection.getOrientation(i).getOpposite()))
					{
						if(neighbourTE instanceof ISelectiveConnector)
						{
							if(canConnectToThisType(neighbourTE))
							{
								validDirections.add(ForgeDirection.getOrientation(i));
							}
						}
					}
				}
			}
			if(validDirections.contains(null))
				validDirections.remove(null);
			ElectricityConnections.registerConnector(this, validDirections);
			this.refreshConnectedBlocks();
			this.initiate();
			System.out.println("The code was called!!! 3");
		}
	}

	@Override
	public String cableType(int ID, int meta)
	{
		String type = "Unknown";
		switch (meta)
		{
			case 0:
				type = "Copper";
				break;
			case 1:
				type = "Tin";
				break;
			case 2:
				type = "Silver";
				break;
			case 3:
				type = "Aluminium";
				break;
			case 4:
				type = "Endium";
				break;
			case 5:
				type = "Connector";
				break;
		}
		return type;
	}

	@Override
	public void onOverCharge()
	{
		if (!this.worldObj.isRemote)
		{
			int ID = this.getBlockType().blockID;
			int setToID = 0;
			if (ID == ElectricExpansion.rawWire)
				setToID = 0;
			if (ID == ElectricExpansion.insulatedWire)
				setToID = 0;
			if (ID == ElectricExpansion.wireBlock)
				setToID = Block.stone.blockID;
			if (ID == ElectricExpansion.SwitchWire)
				setToID = 0;
			if (ID == ElectricExpansion.SwitchWireBlock)
				setToID = Block.stone.blockID;

			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, setToID);
		}
	}
	
	protected boolean canConnectToThisType(TileEntity neighbour)
	{
		if(this.cableType(this.worldObj.getBlockId(neighbour.xCoord, neighbour.yCoord, neighbour.zCoord), neighbour.blockMetadata) == "Connector")
			return true;
		else if(this.cableType(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord), this.blockMetadata) == this.cableType(this.worldObj.getBlockId(neighbour.xCoord, neighbour.yCoord, neighbour.zCoord), neighbour.blockMetadata))
			return true;
		else if(this.cableType(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord), this.blockMetadata) == "Connector")
			return true;
		else return false;
	}
}
