package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.blocks.BlockTransformer;

public class TileEntityTransformerT1 extends TileEntityElectricityReceiver implements IRotatable
{
	// USING A WRENCH ONE CAN CHANGE THE TRANSFORMER TO EITHER STEP UP OR STEP DOWN.
	public boolean stepUp = false;

	@Override
	public void initiate()
	{
		ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.TIER_1_META + 2), ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.TIER_1_META  + 2).getOpposite()));
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
				ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.TIER_1_META  + 2).getOpposite();
				TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

				// Check if requesting power on output
				ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.TIER_1_META  + 2);
				TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);

				ElectricityNetwork network = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
				ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);

				if (network != null && inputNetwork != null && network != inputNetwork)
				{

					if (network.getRequest().getWatts() > 0)
					{

						inputNetwork.startRequesting(this, network.getRequest());

						if (inputNetwork.getProduced().getWatts() > 0)
						{

							ElectricityPack actualEnergy = inputNetwork.consumeElectricity(this);
							double newVoltage = actualEnergy.voltage + 60;

							if (!stepUp)
								newVoltage = actualEnergy.voltage - 60;

							network.startProducing(this, inputNetwork.getProduced().getWatts() / newVoltage, newVoltage);
						}
						else
						{
							network.stopProducing(this);
						}

					}

					else
					{
						network.stopRequesting(this);
					}

				}
			}
		}
	}


	@Override
	public ForgeDirection getDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - BlockTransformer.TIER_1_META );
	}

	@Override
	public void setDirection(ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
	}


}