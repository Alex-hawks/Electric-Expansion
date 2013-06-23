package electricexpansion.common.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import org.bouncycastle.util.Arrays;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.tile.TileEntityConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.wires.EnumWireMaterial;
import electricexpansion.api.wires.EnumWireType;
import electricexpansion.api.wires.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;

/**
 * @author Alex_hawks Helper Class used by me to make adding methods to all
 *         cables easily...
 */
public abstract class TileEntityConductorBase extends TileEntityConductor 
implements IPacketReceiver, IAdvancedConductor, IHiveConductor
{
    public ItemStack textureItemStack;
    
    /** Locked Icon for hidden wires. RS input/output mode for RS wires (true is input) */
    public boolean mode = false;
    
    protected IHiveNetwork hiveNetwork;

    private EnumWireMaterial cachedMaterial;
    
    public TileEntityConductorBase()
    {
        super();
        this.channel = ElectricExpansion.CHANNEL;
    }
    
    @Override
    public void initiate()
    {
        super.initiate();
        this.updateAdjacentConnections();
        this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    @Override
    public double getResistance()
    {
        return this.getWireMaterial().resistance;
    }

    // Tries to use the local cached wire material, otherwise it retrieves it from the chunk
    private EnumWireMaterial getWireMaterial()
    {
        if (cachedMaterial == null)
        {
            cachedMaterial = this.getWireMaterial(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
        }

        return cachedMaterial;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("mode", this.mode);
        if (this.textureItemStack != null)
        {
            this.textureItemStack.writeToNBT(tag);
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        try
        {
            this.mode = tag.getBoolean("mode");
        }
        catch (Exception e)
        {
            try
            {
                this.mode = tag.getBoolean("isIconLocked");
            }
            catch (Exception e2)
            {
                this.mode = false;
            }
        }

        try
        {
            this.textureItemStack = ItemStack.loadItemStackFromNBT(tag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.textureItemStack = null;
        }
    }
    
    @Override
    public double getCurrentCapcity()
    {
        // Amps, not Volts or Watts
        return getWireMaterial().maxAmps;
    }
    
    @Override
    public EnumWireType getWireType(int metadata)
    {
        return EnumWireType.values()[metadata];
    }
    
    @Override
    public EnumWireMaterial getWireMaterial(int metadata)
    {
        if (metadata < EnumWireMaterial.values().length - 1)
            return EnumWireMaterial.values()[metadata];
        else
            return EnumWireMaterial.UNKNOWN;
    }
    
    /*
     * @Override public void onOverCharge() { if (!this.worldObj.isRemote) { int
     * ID = this.getBlockType().blockID; int setToID = 0; if (ID ==
     * ElectricExpansion.blockRawWire.blockID) setToID = 0; if (ID ==
     * ElectricExpansion.blockInsulatedWire.blockID) setToID = 0; if (ID ==
     * ElectricExpansion.blockWireBlock.blockID) setToID = Block.stone.blockID;
     * if (ID == ElectricExpansion.blockSwitchWire.blockID) setToID = 0; if (ID
     * == ElectricExpansion.blockLogisticsWire.blockID) setToID = 0; if (ID ==
     * ElectricExpansion.blockSwitchWireBlock.blockID) setToID =
     * Block.stone.blockID;
     * 
     * this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord,
     * setToID); } }
     */
    
    public void updateConnection(TileEntity tileEntity, ForgeDirection side)
    {
        if (!this.worldObj.isRemote && tileEntity != null)
        {
            if (tileEntity instanceof TileEntityInsulatedWire && this instanceof TileEntityInsulatedWire)
            {
                TileEntityInsulatedWire tileEntityIns = (TileEntityInsulatedWire) tileEntity;
                
                if ((tileEntityIns.colorByte == ((TileEntityInsulatedWire) this).colorByte || ((TileEntityInsulatedWire) this).colorByte == -1 || tileEntityIns.colorByte == -1)
                        && tileEntityIns.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(this.getBlockMetadata()))
                {
                    if (((IConnector) tileEntity).canConnect(side.getOpposite()))
                    {
                        this.connectedBlocks[side.ordinal()] = tileEntity;
                        this.visuallyConnected[side.ordinal()] = true;
                        
                        if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider)
                        {
                            this.getNetwork().mergeConnection(((INetworkProvider) tileEntity).getNetwork());
                        }
                        
                        return;
                    }
                }
            }
            
            else if (tileEntity instanceof IAdvancedConductor)
            {
                IAdvancedConductor tileEntityWire = (IAdvancedConductor) tileEntity;
                
                if (tileEntityWire.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(this.getBlockMetadata()))
                {
                    
                    if (((IConnector) tileEntity).canConnect(side.getOpposite()))
                    {
                        this.connectedBlocks[side.ordinal()] = tileEntity;
                        this.visuallyConnected[side.ordinal()] = true;
                        
                        if (tileEntity instanceof IConductor && tileEntity instanceof INetworkProvider)
                        {
                            this.getNetwork().mergeConnection(((INetworkProvider) tileEntity).getNetwork());
                        }
                        
                        return;
                    }
                }
            }
            else
            {
                if (((IConnector) tileEntity).canConnect(side.getOpposite()))
                {
                    this.connectedBlocks[side.ordinal()] = tileEntity;
                    this.visuallyConnected[side.ordinal()] = true;
                    
                    if (tileEntity instanceof IConductor && tileEntity instanceof INetworkProvider)
                    {
                        this.getNetwork().mergeConnection(((INetworkProvider) tileEntity).getNetwork());
                    }
                    
                    return;
                }
            }
        }
        
        this.connectedBlocks[side.ordinal()] = null;
        this.visuallyConnected[side.ordinal()] = false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }
    
    @Override
    public void updateAdjacentConnections()
    {
        if (this.worldObj != null)
        {
            if (!this.worldObj.isRemote)
            {
                boolean[] previousConnections = this.visuallyConnected.clone();
                
                for (byte i = 0; i < 6; i++)
                {
                    this.updateConnection(VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
                }
                
                /**
                 * Only send packet updates if visuallyConnected changed.
                 */
                if (!Arrays.areEqual(previousConnections, this.visuallyConnected))
                {
                    // Clear the material cache to provide an easy way to fix issues (by changing adjacent wires)
                    cachedMaterial = null;

                    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                }
            }
        }
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
