package electricexpansion.common.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import org.bouncycastle.util.Arrays;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityConductor;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.wires.EnumWireMaterial;
import electricexpansion.api.wires.EnumWireType;
import electricexpansion.api.wires.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.misc.EnumWireFrequency;

/**
 * @author Alex_hawks Helper Class used by me to make adding methods to all
 *         cables easily...
 */
public abstract class TileEntityConductorBase extends TileEntityConductor 
implements IPacketReceiver, IAdvancedConductor, IHiveConductor
{
    protected static final String CHANNEL = ElectricExpansion.CHANNEL;
    public ItemStack textureItemStack;
    
    /** Locked Icon for hidden wires. RS input/output mode for RS wires (true is input) */
    public boolean mode = false;
    
    protected IHiveNetwork hiveNetwork;

    private EnumWireMaterial cachedMaterial;
    protected boolean[] visuallyConnected = new boolean[6];
    /** Color for cables that use color, RS Frequency for cable that use RS Frequency   */
    protected EnumWireFrequency frequency;
    
    public TileEntityConductorBase()
    {
        super();
    }
    
    @Override
    public void initiate()
    {
        super.initiate();
        this.refresh();
        this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    @Override
    public float getResistance()
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
        tag.setByte("frequency", this.frequency.getIndex());
        if (this.textureItemStack != null)
        {
            // Write the item stack to a separate tag to avoid namespace clashes in the tag
            NBTTagCompound textureTag = new NBTTagCompound();
            this.textureItemStack.writeToNBT(textureTag);
            tag.setCompoundTag("texture", textureTag);
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
            this.frequency = EnumWireFrequency.getFromIndex(tag.getByte("frequency"));
        }
        catch (Exception e)
        {
            this.frequency = EnumWireFrequency.NONE;
        }

        NBTBase textureTag = tag.getTag("texture");
        if (textureTag instanceof NBTTagCompound) {
            try {
                this.textureItemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) textureTag);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.textureItemStack = null;
            }
        }
    }
    
    @Override
    public float getCurrentCapacity()
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
                
                if ((tileEntityIns.frequency == ((TileEntityInsulatedWire) this).frequency || ((TileEntityInsulatedWire) this).frequency.getIndex() == -1 || tileEntityIns.frequency.getIndex() == -1)
                        && tileEntityIns.getWireMaterial(tileEntity.getBlockMetadata()) == this.getWireMaterial(this.getBlockMetadata()))
                {
                    if (((IConnector) tileEntity).canConnect(side.getOpposite()))
                    {
                        this.adjacentConnections[side.ordinal()] = tileEntity;
                        
                        if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider)
                        {
                            this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork());
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
                        this.adjacentConnections[side.ordinal()] = tileEntity;
                        
                        if (tileEntity instanceof IConductor && tileEntity instanceof INetworkProvider)
                        {
                            this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork());
                        }
                        
                        return;
                    }
                }
            }
            else
            {
                if (((IConnector) tileEntity).canConnect(side.getOpposite()))
                {
                    this.adjacentConnections[side.ordinal()] = tileEntity;
                    
                    if (tileEntity instanceof IConductor && tileEntity instanceof INetworkProvider)
                    {
                        this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork());
                    }
                    
                    return;
                }
            }
        }
        
        this.adjacentConnections[side.ordinal()] = null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }
    
    @Override
    public void refresh()
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
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
    {
        for (int i = 0; i < this.visuallyConnected.length; i++)
            this.visuallyConnected[i] = dataStream.readBoolean();
        this.frequency = EnumWireFrequency.getFromIndex(dataStream.readByte());
    }

    public boolean[] getVisualConnections()
    {
        return this.visuallyConnected;
    }

    public EnumWireFrequency getFrequency()
    {
        return this.frequency;
    }

    public void setFrequency(byte frequency)
    {
        this.frequency = EnumWireFrequency.getFromIndex(frequency);
        
        PacketManager.sendPacketToClients(this.getDescriptionPacket());
    }
}
