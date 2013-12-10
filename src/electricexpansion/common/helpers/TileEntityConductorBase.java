package electricexpansion.common.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import org.bouncycastle.util.Arrays;

import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityConductor;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.hive.IHiveConductor;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveNetworkMember;
import electricexpansion.api.wires.EnumWireMaterial;
import electricexpansion.api.wires.EnumWireType;
import electricexpansion.api.wires.IAdvancedConductor;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EnumWireFrequency;
import electricexpansion.common.misc.HiveNetwork;

/**
 * @author Alex_hawks Helper Class used by me to make adding methods to all
 *         cables easily...
 */
public abstract class TileEntityConductorBase extends TileEntityConductor 
implements IPacketReceiver, IAdvancedConductor, IHiveConductor
{
    protected static final String CHANNEL = ElectricExpansion.CHANNEL;
    public ItemStack textureItemStack;
    
    /** Locked Icon for hidden wires. */
    public boolean mode = false;
    
    protected IHiveNetwork hiveNetwork;
    
    private EnumWireMaterial cachedMaterial;
    protected boolean[] visuallyConnected = new boolean[6];
    /** Color for cables that use color, RS Frequency for cable that use RS Frequency   */
    protected EnumWireFrequency frequency = EnumWireFrequency.NONE;
    
    public TileEntityConductorBase()
    {
        super();
        this.adjacentConnections = new TileEntity[6];
    }
    
    @Override
    public void initiate()
    {
        super.initiate();
        this.refresh();
        this.getHiveNetwork();
        this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
        
        if (this.hiveNetwork != null)
            this.hiveNetwork.clear();
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
    
    public void updateConnection(TileEntity that, ForgeDirection side)
    {
        if (!this.worldObj.isRemote && that != null && this.canConnect(side))
        {
            if (that instanceof TileEntityConductorBase)
            {
                TileEntityConductorBase tileEntityIns = (TileEntityConductorBase) that;
                
                if ((tileEntityIns.frequency == ((TileEntityConductorBase) this).frequency || ((TileEntityConductorBase) this).frequency.getIndex() == -1 || tileEntityIns.frequency.getIndex() == -1)
                    && tileEntityIns.getWireMaterial(that.getBlockMetadata()) == this.getWireMaterial(this.getBlockMetadata()))
                {
                    if (((IConnector) that).canConnect(side.getOpposite()))
                    {
                        this.adjacentConnections[side.ordinal()] = that;
                        this.visuallyConnected[side.ordinal()] = true;
                        
                        this.networkLogic(that);
                        
                        return;
                    }
                }
            }
            
            else if (that instanceof IAdvancedConductor)
            {
                IAdvancedConductor tileEntityWire = (IAdvancedConductor) that;
                
                if (tileEntityWire.getWireMaterial(that.getBlockMetadata()) == this.getWireMaterial(this.getBlockMetadata()))
                {
                    
                    if (((IConnector) that).canConnect(side.getOpposite()))
                    {
                        this.adjacentConnections[side.ordinal()] = that;
                        this.visuallyConnected[side.ordinal()] = true;
                        
                        this.networkLogic(that);
                        
                        return;
                    }
                }
            }
            else if (that instanceof IConductor)
            {
                if (((IConnector) that).canConnect(side.getOpposite()))
                {
                    this.adjacentConnections[side.ordinal()] = that;
                    this.visuallyConnected[side.ordinal()] = true;
                    
                    this.networkLogic(that);
                    
                    return;
                }
            }
            
            else if (that instanceof IConnector)
            {
                if (((IConnector) that).canConnect(side.getOpposite()))
                {
                    this.adjacentConnections[side.ordinal()] = that;
                    this.visuallyConnected[side.ordinal()] = true;
                    
                    this.networkLogic(that);
                    
                    return;
                }
            }
        }
        
        this.adjacentConnections[side.ordinal()] = null;
        this.visuallyConnected[side.ordinal()] = false;
    }
    
    private void networkLogic(TileEntity that)
    {
        if (that instanceof INetworkProvider)
        {
            INetworkProvider thatN = (INetworkProvider) that;
            if (this.getNetwork() != null && thatN.getNetwork() != null)
            {
                this.getNetwork().merge(thatN.getNetwork());
            }
            else if (this.getNetwork() == null && thatN.getNetwork() != null)
            {
                this.setNetwork(thatN.getNetwork());
            }
            else if (this.getNetwork() != null && thatN.getNetwork() == null)
            {
                thatN.setNetwork(this.getNetwork());
            }
        }
        
        if (that instanceof IHiveNetworkMember)
        {
            IHiveNetworkMember thatN = (IHiveNetworkMember) that;
            if (this.getHiveNetwork() != null && thatN.getHiveNetwork() != null)
            {
                this.getHiveNetwork().merge(thatN.getHiveNetwork());
            }
            else if (this.getHiveNetwork() == null && thatN.getHiveNetwork() != null)
            {
                this.setHiveNetwork(thatN.getHiveNetwork(), false);
            }
            else if (this.getHiveNetwork() != null && thatN.getHiveNetwork() == null)
            {
                thatN.setHiveNetwork(this.getHiveNetwork(), false);
            }
        }
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
                    ForgeDirection dir = ForgeDirection.getOrientation(i);
                    this.updateConnection(this.worldObj.getBlockTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ), dir);
                }
                
                /**
                 * Only send packet updates if visuallyConnected changed.
                 */
                if (!Arrays.areEqual(previousConnections, this.visuallyConnected))
                {
                    // Clear the material cache to provide an easy way to fix issues (by changing adjacent wires)
                    cachedMaterial = null;
                    
                    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getID());
                }
            }
        }
    }
    
    protected abstract int getID();
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[] { this.getNetwork() };
    }
    
    @Override
    public IHiveNetwork getHiveNetwork()
    {
        if (this.hiveNetwork == null)
        {
            new HiveNetwork().addNetwork(this.getNetwork());
        }
        return this.hiveNetwork;
    }
    
    @Override
    public boolean setHiveNetwork(IHiveNetwork newHiveNetwork, boolean mustOverride)
    {
        if (this.hiveNetwork == null || mustOverride)
        {
            this.onHiveChanged(this.hiveNetwork, newHiveNetwork);
            this.hiveNetwork = newHiveNetwork;
            return true;
        }
        return false;
    }
    
    public void onHiveChanged(IHiveNetwork oldNetwork, IHiveNetwork newNetwork) { }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
    {
        byte b = dataStream.readByte();
        
        for (int i = 0; i < this.visuallyConnected.length; i++)
            this.visuallyConnected[i] = ((b & (1 << i)) == 1 << i);
        
        this.frequency = EnumWireFrequency.getFromIndex(dataStream.readByte());
        
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
    
    public void setFrequency(EnumWireFrequency dye)
    {
        this.frequency = dye;
        
        PacketManager.sendPacketToClients(this.getDescriptionPacket());
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        byte b = 0;
        
        for (int i = 0; i < this.visuallyConnected.length; i++)
        {
            b += (byte) ((this.visuallyConnected[i] ? ((byte) 0b0000_0001) : ((byte) 0b0000_0000)) << i);
        }
        
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, b, this.frequency.getIndex());
    }
    
    @Override
    public String toString()
    {
        return "[" + this.getBlockType().getUnlocalizedName() + "::" + this.xCoord + "," + this.yCoord + "," + this.zCoord + "]";
    }
}
