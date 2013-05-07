package electricexpansion.common.helpers;

import org.bouncycastle.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.EnumWireMaterial;
import electricexpansion.api.EnumWireType;
import electricexpansion.api.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.misc.EENetwork;

/**
 * @author Alex_hawks 
 * Helper Class used by me to make adding methods to all cables easily...
 * Code was taken from {@link universalelectricity.prefab.tile.TileEntityConductor TileEntityConductor}.
 * I have removed the extension of that class to attempt to fix bugs...
 */
public abstract class TileEntityConductorBase extends TileEntityAdvanced implements IPacketReceiver, IAdvancedConductor
{
    /**
     * For hidden wires...
     */
    public ItemStack textureItemStack;
    public boolean isIconLocked = false;
    
    public EENetwork smartNetwork;
    
    
    protected final String channel;
    public boolean[] visuallyConnected = { false, false, false, false, false, false };
    public TileEntity[] connectedBlocks = { null, null, null, null, null, null };
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.smartNetwork == null)
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
        }
        else
        {
            this.smartNetwork = new EENetwork(network);
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
        this.updateAdjacentConnections();
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
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            this.visuallyConnected[0] = dataStream.readBoolean();
            this.visuallyConnected[1] = dataStream.readBoolean();
            this.visuallyConnected[2] = dataStream.readBoolean();
            this.visuallyConnected[3] = dataStream.readBoolean();
            this.visuallyConnected[4] = dataStream.readBoolean();
            this.visuallyConnected[5] = dataStream.readBoolean();
        }
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            this.getNetwork().splitNetwork(this);
        }

        super.invalidate();
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 300 == 0)
            {
                this.updateAdjacentConnections();
            }
        }
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(this.channel, this, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3], this.visuallyConnected[4], this.visuallyConnected[5]);
    }

    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        return this.connectedBlocks;
    }

    @Override
    public void updateAdjacentConnections()
    {
        if (this.worldObj != null)
        {
            if (!this.worldObj.isRemote)
            {
                boolean[] previousConnections = this.visuallyConnected.clone();
                
                if (this.smartNetwork != null)
                    this.smartNetwork.refreshConductors();
                
                for (byte i = 0; i < 6; i++)
                {
                    this.updateConnection(VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), ForgeDirection.getOrientation(i)), ForgeDirection.getOrientation(i));
                }

                /**
                 * Only send packet updates if visuallyConnected changed.
                 */
                if (!Arrays.areEqual(previousConnections, this.visuallyConnected))
                {
                    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                }
            }

        }
    }
}
