package electricexpansion.alex_hawks.helpers;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.implement.IConnector;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityConductor;
import cpw.mods.fml.common.Loader;
import electricexpansion.ElectricExpansion;
import electricexpansion.api.CableInterfaces.ISelectiveConnector;

/**
 * 
 * 	@author Alex_hawks
 *	Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityCableHelper extends TileEntityConductor implements ISelectiveConnector
{
	private ElectricityNetwork network;
	
	public TileEntityCableHelper()
	{
		this.reset();
	}
	
	@Override
	public double getResistance() 
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		switch(meta)
		{
			case 0: return 0.05; 
			case 1: return 0.04;
			case 2: return 0.02;
			case 3: return 0.2;
			case 4: return 0.005;
			default: return 0.05;
		}
	}
	
	@Override
	public double getMaxAmps()
	{
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		switch(meta)
		{
			case 0: return 500;
			case 1: return 60; //Bit less than a basic Coal-Generator. #Cruel
			case 2: return 200;
			case 3: return 2500; //HV
			case 4: return 1000;
			default: return 500;
		}
	}
	
	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.worldObj.isRemote)
		{
			this.refreshConnectedBlocks();
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ElectricExpansion.CHANNEL, this);
	}
	
	public boolean canConnect(ForgeDirection side)
	{
		return true;
	}
	
	public String cableType(int ID, int meta)
	{
		String type = "Unknown";
		switch(meta)
		{
			case 0: type = "Copper";
			break;
			case 1: type = "Tin";
			break;
			case 2: type = "Silver";
			break;
			case 3: type = "Aluminium";
			break;
			case 4: type = "Endium";
			break;
			case 5: type = "Connector";
			break;
		}
		return type;
	}

	@Override
	public void onOverCharge() 
	{
		if(!this.worldObj.isRemote)
		{
			int ID = this.getBlockType().blockID;
			int setToID = 0;
			if(ID == ElectricExpansion.rawWire)
				setToID = 0;
			if(ID == ElectricExpansion.insulatedWire)
				setToID = 0;
			if(ID == ElectricExpansion.wireBlock)
				setToID = Block.stone.blockID;
			if(ID == ElectricExpansion.SwitchWire)
				setToID = 0;
			if(ID == ElectricExpansion.SwitchWireBlock)
				setToID = Block.stone.blockID;

			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, setToID);
		}
	}
}
