package electricexpansion.common.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.prefab.tile.TileEntityConductor;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.api.EnumWireType;
import electricexpansion.api.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.misc.EENetwork;

/**
 * @author Alex_hawks 
 * Helper Class used by me to make adding methods to all cables easily...
 */
public abstract class TileEntityConductorBase extends TileEntityConductor implements IAdvancedConductor
{
    /**
     * For hidden wires...
     */
    public ItemStack textureItemStack;
    public boolean isIconLocked = false;
    
    public EENetwork smartNetwork;
    public IElectricityNetwork network;
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.smartNetwork == null && this.network == null)
        {
            this.setNetwork(new EENetwork(this));
        }

        return this.smartNetwork;
    }
    
    @Override
    public void setNetwork(IElectricityNetwork network)
    {
        if (network instanceof EENetwork)
        {
            this.smartNetwork = (EENetwork) network;
            this.network = null;
        }
        else
        {
            this.network = network;
            this.smartNetwork = null;
        }
    }
    
    public TileEntityConductorBase()
    {
        super();
        this.channel = ElectricExpansion.CHANNEL;
    }
    
    @Override
    public void initiate()
    {
        super.initiate();
        this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    @Override
    public double getResistance()
    {
        return this.getWireMaterial(this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)).resistance;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("isIconLocked", this.isIconLocked);
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
            this.textureItemStack = ItemStack.loadItemStackFromNBT(tag);
        }
        catch (Exception e)
        {
            this.textureItemStack = null;
        }
        
        try
        {
            this.isIconLocked = tag.getBoolean("isIconLocked");
        }
        catch (Exception e)
        {
            this.isIconLocked = false;
        }
    }
    
    @Override
    public double getCurrentCapcity()
    {
        // Amps, not Volts or Watts
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        if (meta < EnumWireMaterial.values().length - 1)
            return EnumWireMaterial.values()[meta].maxAmps;
        else
            return EnumWireMaterial.UNKNOWN.maxAmps;
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
    @Override
    public void updateConnection(TileEntity tileEntity, ForgeDirection side)
    {
        if (!this.worldObj.isRemote && tileEntity != null)
        {
            if (tileEntity instanceof TileEntityInsulatedWire && this instanceof TileEntityInsulatedWire)
            {
                TileEntityInsulatedWire tileEntityIns = (TileEntityInsulatedWire) tileEntity;
                
                if ((tileEntityIns.colorByte == ((TileEntityInsulatedWire) this).colorByte
                        || ((TileEntityInsulatedWire) this).colorByte == -1 || tileEntityIns.colorByte == -1) 
                        && tileEntityIns.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(this
                                .getBlockMetadata()))
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
                
                {
                    
                    if (tileEntityWire.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(this
                            .getBlockMetadata()))
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
                
            }
            else
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
        
        this.connectedBlocks[side.ordinal()] = null;
        this.visuallyConnected[side.ordinal()] = false;
    }
}
