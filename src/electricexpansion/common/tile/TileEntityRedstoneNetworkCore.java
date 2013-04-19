package electricexpansion.common.tile;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricityNetwork;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.prefab.tile.TileEntityElectrical;
import electricexpansion.api.IRedstoneNetAccessor;
import electricexpansion.common.misc.EENetwork;

public class TileEntityRedstoneNetworkCore extends TileEntityElectrical implements INetworkProvider
{
    private EENetwork network;
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public void updateEntity()
    {
        if (this.ticks % 5 == 0)
        {
            if (this.network != null)
            {
                for (IRedstoneNetAccessor rsCable : this.network.getRedstoneInterfacers())
                {
                    if (rsCable.getIsAcceptingRsSignal())
                    {
                        int netRs = this.network.rsLevel;
                        int worldRs = rsCable.getWorld().getBlockPowerInput(rsCable.getX(), rsCable.getY(), rsCable.getZ());
                        this.network.rsLevel = (byte) (netRs > worldRs ? netRs : worldRs);
                    }
                }
            }
            else if (this.network == null)
            {
                ForgeDirection facing = ForgeDirection.getOrientation(blockMetadata);
                if (this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ) instanceof INetworkProvider)
                {
                    if (((IElectricityNetwork) this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ)) != null)
                    {
                        this.setNetwork((IElectricityNetwork) this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ));
                    }
                }
            }
        }
    }
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        return this.network;
    }
    
    @Override
    public void setNetwork(IElectricityNetwork network)
    {
        //  It is an RsNetwork with no controller.
        if (network instanceof EENetwork && ((EENetwork) network).coreProcessor == null)
        {
            this.network = (EENetwork) network;
            ((EENetwork) network).coreProcessor = this;
        }
        //  It is a Basic UE network. Time to convert...
        else if (network instanceof ElectricityNetwork)
        {
            this.network = new EENetwork(network);
            this.network.coreProcessor = this;
        }
        //  It is another mod's network... (f.e. Mekanism's Universal cable) or, another controller is in the network.
        //  Either way, we're not touching it...
        else
        {
            
        }
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata();
    }
}
