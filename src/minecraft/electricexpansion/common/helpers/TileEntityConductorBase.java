package electricexpansion.common.helpers;

import net.minecraft.block.Block;
import universalelectricity.prefab.tile.TileEntityConductor;
import electricexpansion.api.CableInterfaces.ISelectiveConnector;
import electricexpansion.common.ElectricExpansion;

/**
 * 
 * @author Alex_hawks Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityConductorBase extends TileEntityConductor implements ISelectiveConnector
{
	public TileEntityConductorBase()
	{
		super();
		this.channel = ElectricExpansion.CHANNEL;
	}

	@Override
	public void initiate()
	{
		super.initiate();
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
				return 0;
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
				return 5000; // HV
			case 4:
				return 1500;
			default:
				return 500;
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
}
