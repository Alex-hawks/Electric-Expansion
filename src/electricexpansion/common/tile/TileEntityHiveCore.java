package electricexpansion.common.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import electricexpansion.api.hive.IHiveController;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveNetworkMember;
import electricexpansion.common.misc.HiveNetwork;

public class TileEntityHiveCore extends TileEntityAdvanced implements INetworkProvider, IHiveController
{
    private IHiveNetwork hiveNetwork;
    private IElectricityNetwork network;
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    public void initiate()
    {
        ForgeDirection facing = ForgeDirection.getOrientation(this.getBlockMetadata());
        TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), facing);
        
        if (inputTile instanceof INetworkProvider)
        {
            this.network = ((INetworkProvider) inputTile).getNetwork();
            if (inputTile instanceof IHiveNetworkMember && ((IHiveNetworkMember) inputTile).getHiveNetwork() == null)
            {
                IHiveNetworkMember member = (IHiveNetworkMember) inputTile;
                this.setHiveNetwork(new HiveNetwork(), true);
                
                for (IElectricityNetwork net : member.getNetworks())
                    this.hiveNetwork.addNetwork(net);
            }
        }
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata();
    }
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[] { this.getNetwork() };
    }
    
    @Override
    public IHiveNetwork getHiveNetwork()
    {
        return this.hiveNetwork;
    }
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        return this.network;
    }
    
    @Override
    public void setNetwork(IElectricityNetwork network)
    {
        this.network = network;
    }
    
    @Override
    public boolean setHiveNetwork(IHiveNetwork hiveNetwork, boolean mustOverride)
    {
        if (this.hiveNetwork == null || mustOverride)
        {
            this.hiveNetwork = hiveNetwork;
            return true;
        }
        return false;
    }
}
