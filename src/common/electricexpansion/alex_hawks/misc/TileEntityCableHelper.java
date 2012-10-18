package electricexpansion.alex_hawks.misc;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.Block;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.BasicComponents;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import universalelectricity.basiccomponents.UELoader;
import universalelectricity.implement.IConductor;
import universalelectricity.implement.IConnector;
import universalelectricity.network.PacketManager;
import universalelectricity.prefab.TileEntityConductor;

/**
 * 
 * 	@author Alex_hawks
 *	Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityCableHelper extends TileEntityConductor
{
	@Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(UELoader.CHANNEL, this);
    }
	@Override
	public boolean canConnect(ForgeDirection side)
	{
		return this.canConnect(side.ordinal());
	}
	
	public boolean canConnect(int side)
	{
		int x=0,y=0,z=0;
				if	(side == 0){x=0;y=1;z=0;}
		else 	if	(side == 1){x=0;y=-1;z=0;}
		else 	if	(side == 2){x=0;y=0;z=-1;}
		else 	if	(side == 3){x=0;y=0;z=1;}
		else 	if	(side == 4){x=-1;y=0;z=0;}
		else 	if	(side == 5){x=1;y=0;z=0;}
		
		int thisID 			= this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
		int thismeta 		= this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		TileEntity thisTE 	= this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
		int ID 				= this.worldObj.getBlockId(this.xCoord + x, this.yCoord + y, this.zCoord + z);
		int meta 			= this.worldObj.getBlockMetadata(this.xCoord + x, this.yCoord + y, this.zCoord + z);
		TileEntity TE 		= this.worldObj.getBlockTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
		
		if((TE instanceof IConnector) && !(TE instanceof IConductor))
			return true;
		if(this.cableType(ID, meta) == "Connector")
			return true;
		if(this.cableType(thisID, thismeta) == this.cableType(ID, meta))
			return true;
		else return false;
	}
	public String cableType(int ID, int meta)
	{
		String type = "Unknown";
		if(ID == BasicComponents.blockCopperWire.blockID)
			type = "Copper";
		else switch(meta)
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
	public void onConductorMelt() 
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
