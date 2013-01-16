package electricexpansion.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.tile.TileEntityConductor;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.api.EnumWireType;
import electricexpansion.api.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;

/**
 * 
 * @author Alex_hawks Helper Class used by me to make adding methods to all cables easily...
 */

public abstract class TileEntityConductorBase extends TileEntityConductor implements IAdvancedConductor
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
		return this.getWireMaterial(worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)).resistance;
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
				return 4800; // HV
			case 4:
				return 1500;
			default:
				return 500;
		}
	}

	@Override
	public EnumWireType getWireType(int metadata)
	{
		return EnumWireType.values()[metadata];
	}

	@Override
	public EnumWireMaterial getWireMaterial(int metadata)
	{
		return EnumWireMaterial.values()[metadata];
	}

	@Override
	public void onOverCharge()
	{
		if (!this.worldObj.isRemote)
		{
			int ID = this.getBlockType().blockID;
			int setToID = 0;
			if (ID == ElectricExpansion.blockRawWire.blockID)
				setToID = 0;
			if (ID == ElectricExpansion.blockInsulatedWire.blockID)
				setToID = 0;
			if (ID == ElectricExpansion.blockWireBlock.blockID)
				setToID = Block.stone.blockID;
			if (ID == ElectricExpansion.blockSwitchWire.blockID)
				setToID = 0;
			if (ID == ElectricExpansion.blockLogisticsWire.blockID)
				setToID = 0;
			if (ID == ElectricExpansion.blockSwitchWireBlock.blockID)
				setToID = Block.stone.blockID;

			this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, setToID);
		}
	}

	@Override
	public void updateConnection(TileEntity tileEntity, ForgeDirection side)
	{
		if (!this.worldObj.isRemote)
		{
			if (tileEntity instanceof IAdvancedConductor)
			{
				IAdvancedConductor tileEntityWire = (IAdvancedConductor) tileEntity;

				{

					if (tileEntity != null && tileEntityWire.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(getBlockMetadata()))
					{
						if (tileEntity instanceof TileEntityInsulatedWire)
						{
							TileEntityInsulatedWire tileEntityIns = (TileEntityInsulatedWire) tileEntity;

							 if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
								{
									this.connectedBlocks[side.ordinal()] = tileEntity;
									this.visuallyConnected[side.ordinal()] = true;

									if (tileEntity instanceof IConductor)
									{
										Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
									}

									return;
								}
						}

						else if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
						{
							this.connectedBlocks[side.ordinal()] = tileEntity;
							this.visuallyConnected[side.ordinal()] = true;

							if (tileEntity instanceof IConductor)
							{
								Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
							}

							return;
						}
					}
				}

			}
			else
			{
				if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
				{
					this.connectedBlocks[side.ordinal()] = tileEntity;
					this.visuallyConnected[side.ordinal()] = true;

					if (tileEntity instanceof IConductor)
					{
						Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
					}

					return;
				}
			}

		}

		if (this.connectedBlocks[side.ordinal()] != null)
		{
			if (this.connectedBlocks[side.ordinal()] instanceof IConductor)
			{
				Electricity.instance.splitConnection(this, (IConductor) this.getConnectedBlocks()[side.ordinal()]);
			}

			this.getNetwork().stopProducing(this.connectedBlocks[side.ordinal()]);
			this.getNetwork().stopRequesting(this.connectedBlocks[side.ordinal()]);
		}

		this.connectedBlocks[side.ordinal()] = null;
		this.visuallyConnected[side.ordinal()] = false;
	}

	@Override
	public void updateConnectionWithoutSplit(TileEntity tileEntity, ForgeDirection side)
	{
		if (!this.worldObj.isRemote)
		{
			if (tileEntity instanceof TileEntityInsulatedWire)
			{
				TileEntityInsulatedWire tileEntityIns = (TileEntityInsulatedWire) tileEntity;

				if (tileEntity != null && tileEntityIns.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(getBlockMetadata()))

				{
					if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
					{
						this.connectedBlocks[side.ordinal()] = tileEntity;
						this.visuallyConnected[side.ordinal()] = true;

						if (tileEntity instanceof IConductor)
						{
							Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
						}

						return;
					}
				}

				this.connectedBlocks[side.ordinal()] = null;
				this.visuallyConnected[side.ordinal()] = false;
			}

		}
		else
		{
			if (ElectricityConnections.canConnect(tileEntity, side.getOpposite()))
			{
				this.connectedBlocks[side.ordinal()] = tileEntity;
				this.visuallyConnected[side.ordinal()] = true;

				if (tileEntity instanceof IConductor)
				{
					Electricity.instance.mergeConnection(this.getNetwork(), ((IConductor) tileEntity).getNetwork());
				}

				return;
			}
		}

		this.connectedBlocks[side.ordinal()] = null;
	}

}
