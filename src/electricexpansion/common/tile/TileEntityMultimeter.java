package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.IRotatable;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.ElectricExpansionEventHandler;

public class TileEntityMultimeter extends TileEntityElectrical 
implements IPacketReceiver, IRotatable, IHiveMachine
{
    public static final int[] ROTATION_MATRIX = { 0, 1, 2, 5, 3, 4 };
    public static final int[] META_MATRIX = { 0, 1, 2, 4, 5, 3 };
    
    public transient ElectricityPack electricityReading = new ElectricityPack();
    private transient ElectricityPack lastReading = new ElectricityPack();
    
    private transient IElectricityNetwork network;
    private transient IHiveNetwork hiveNetwork;
    
    private static final ElectricityPack EMPTY_PACK = new ElectricityPack();
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (this.ticks % 10 == 0L)
        {
            this.lastReading = this.electricityReading;
            
            if (!this.worldObj.isRemote)
            {
                ForgeDirection inputDirection = ForgeDirection.getOrientation(ROTATION_MATRIX[this.getBlockMetadata()]);
                TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
                
                if (inputTile != null && inputTile instanceof INetworkProvider)
                {
                    if (inputTile instanceof IConductor)
                    {
                        this.network = ((IConductor) inputTile).getNetwork();
                        
                        ElectricityPack temp = ElectricExpansionEventHandler.INSTANCE.getNetworkStat(this.network);
                        ElectricExpansionEventHandler.INSTANCE.cleanNetworkStat(this.network);
                        
                        if (temp != null)
                        {
                            this.electricityReading = temp.clone();
                            this.electricityReading.amperes *= 20.0F;
                        }
                        else
                        {
                            this.electricityReading = EMPTY_PACK.clone();
                        }
                    }
                    else
                    {
                        this.network = ((INetworkProvider) inputTile).getNetwork();
                        
                        this.electricityReading = EMPTY_PACK.clone();
                    }
                }
                else
                {
                    this.electricityReading = EMPTY_PACK.clone();
                }
                
                if (this.electricityReading.amperes != this.lastReading.amperes)
                {
                    PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 20.0D);
                }
            }
        }
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, new Object[] { Double.valueOf(this.electricityReading.amperes), Double.valueOf(this.electricityReading.voltage) });
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                this.electricityReading.amperes = dataStream.readFloat();
                this.electricityReading.voltage = dataStream.readFloat();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public String getInvName()
    {
        return StatCollector.translateToLocal("tile.multimeter.name");
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == ROTATION_MATRIX[this.getBlockMetadata()];
    }
    
    @Override
    public void setDirection(ForgeDirection facingDirection)
    {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, META_MATRIX[facingDirection.ordinal()], 0x02);
    }
    
    @Override
    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(ROTATION_MATRIX[this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)]);
    }
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[] { this.network };
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
    public float getRequest(ForgeDirection direction)
    {
        return 0;
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        return 0;
    }
    
    @Override
    public int getSerialQuantity()
    {
        return 1;
    }
    
    @Override
    public int getInputQuantity()
    {
        return 1;
    }
    
    @Override
    public int getOutputQuantity()
    {
        return 0;
    }
    
    @Override
    public EnumSet<ForgeDirection> getSerialDirections()
    {
        return EnumSet.of(this.getDirection());
    }
}
