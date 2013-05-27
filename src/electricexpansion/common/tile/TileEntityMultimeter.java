package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.common.ElectricExpansion;

public class TileEntityMultimeter extends TileEntityElectrical 
implements IPacketReceiver, IRotatable, IHiveMachine
{
    public static final int[] ROTATION_MATRIX = { 0, 1, 2, 5, 3, 4 };
    public static final int[] META_MATRIX = { 0, 1, 2, 4, 5, 3 };
    
    public transient ElectricityPack electricityReading = new ElectricityPack();
    private transient ElectricityPack lastReading = new ElectricityPack();
    
    private transient IElectricityNetwork network;
    private transient IHiveNetwork hiveNetwork;
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (this.ticks % 10 == 0L)
        {
            this.lastReading = this.electricityReading;
            
            if (!this.worldObj.isRemote)
            {
                if (!this.isDisabled())
                {
                    ForgeDirection inputDirection = ForgeDirection.getOrientation(ROTATION_MATRIX[this.getBlockMetadata()]);
                    TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
                    
                    if (inputTile != null && inputTile instanceof INetworkProvider)
                    {
                        if (inputTile instanceof IConductor)
                        {
                            this.network = ((IConductor) inputTile).getNetwork();
                            
                            this.electricityReading = network.getProduced(new TileEntity[0]);
                            this.electricityReading.amperes *= 20.0D;
                        }
                        else
                        {
                            this.network = ((INetworkProvider) inputTile).getNetwork();
                            
                            this.electricityReading = new ElectricityPack();
                        }
                    }
                    else
                    {
                        this.electricityReading = new ElectricityPack();
                    }
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
                this.electricityReading.amperes = dataStream.readDouble();
                this.electricityReading.voltage = dataStream.readDouble();
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
    public void setDirection(World world, int x, int y, int z, ForgeDirection facingDirection)
    {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, META_MATRIX[facingDirection.ordinal()], 0x02);
    }
    
    @Override
    public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
    {
        return ForgeDirection.getOrientation(ROTATION_MATRIX[world.getBlockMetadata(x, y, z)]);
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
}
