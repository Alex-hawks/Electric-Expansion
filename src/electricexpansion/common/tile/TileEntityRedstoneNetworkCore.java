package electricexpansion.common.tile;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.INetworkProvider;
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
                int netRs = this.network.rsLevel;
                for (IRedstoneNetAccessor rsCable : this.network.getRedstoneInterfacers())
                {
                    int worldRs = rsCable.getRsSignalFromBlock();
                    this.network.rsLevel = (byte) (Math.max(netRs, worldRs));
                }
            }
            else
            {
                ForgeDirection facing = ForgeDirection.getOrientation(blockMetadata);
                if (this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ) instanceof INetworkProvider)
                    if (((INetworkProvider) this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ)).getNetwork() != null)
                        this.setNetwork(((INetworkProvider) this.worldObj.getBlockTileEntity(this.xCoord + facing.offsetX, this.yCoord + facing.offsetY, this.zCoord + facing.offsetZ)).getNetwork());
            }
        }
        if (this.ticks % 300 == 0 && this.network != null)
            this.network.cleanUpConductors();
    }
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        return this.network;
    }
    
    @Override
    public void setNetwork(IElectricityNetwork network)
    {
        // It is an RsNetwork with no controller.
        if (network instanceof EENetwork && ((EENetwork) network).coreProcessor == null)
        {
            this.network = (EENetwork) network;
            ((EENetwork) network).coreProcessor = this;
            this.network.cleanUpConductors();
        }
        // It is another mod's network... (f.e. Mekanism's Universal cable) or,
        // another controller is in the network.
        // Either way, we're not touching it...
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
