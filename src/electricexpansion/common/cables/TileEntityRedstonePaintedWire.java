package electricexpansion.common.cables;

import static electricexpansion.api.hive.HiveSignal.DEVICE_TYPE.REDSTONE_WIRE;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.FULL_REDSTONE_STATE;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.PART_REDSTONE_STATE;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.SIMPLE_REDSTONE_STATE;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveSignalIO;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EnumWireFrequency;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase implements IHiveSignalIO
{
    private byte rsLevel = 0;
    private byte irsLevel = 0;
    private List<Byte> rstemp = new ArrayList<Byte>();
    private List<Byte> irstemp = new ArrayList<Byte>();
    
    private byte[] rsArray = new byte[16];
    private byte[] irsArray = new byte[16];
    @SuppressWarnings("unchecked")
    private List<Byte>[] rsArrayTemp = (ArrayList<Byte>[])new ArrayList[16];
    @SuppressWarnings("unchecked")
    private List<Byte>[] irsArrayTemp = (ArrayList<Byte>[])new ArrayList[16];
    
    public static final byte RS_FIRST   = 0b0000_0000;
    public static final byte RS_LAST    = 0b0000_1111;
    public static final byte IRS_FIRST  = 0b0001_0000;
    public static final byte IRS_LAST   = 0b0001_1111;
    
    private Byte uniqueID;
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    public void updateEntity()
    {
        super.updateEntity();
        
        if (ticks % 10 == 9)
        {
            byte temp = 0;
            int i;
            
            for(Byte b : rstemp)
            {
                temp = (byte) Math.max(temp, b);
            }
            
            this.rstemp.clear();
            this.rsLevel = temp;
            temp = 0;
            
            for(Byte b : irstemp)
            {
                temp = (byte) Math.max(temp, b);
            }
            
            this.irstemp.clear();
            this.irsLevel = temp;
            temp = 0;
            
            for (i = 0; i < rsArrayTemp.length; i++)
            {
                for(Byte b : rsArrayTemp[i])
                {
                    temp = (byte) Math.max(temp, b);
                }
                
                rsArrayTemp[i].clear();
                this.rsArray[i] = temp;
                temp = 0;
            }
            
            for (i = 0; i < irsArrayTemp.length; i++)
            {
                for(Byte b : irsArrayTemp[i])
                {
                    temp = (byte) Math.max(temp, b);
                }
                
                irsArrayTemp[i].clear();
                this.irsArray[i] = temp;
                temp = 0;
            }
            
            
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockRedstonePaintedWire.blockID);
        }
    }
    
    @Override
    public void initiate()
    {
        for (int i = 0; i < rsArrayTemp.length; i++)
        {
            rsArrayTemp[i] = new ArrayList<Byte>();
        }
        for (int i = 0; i < irsArrayTemp.length; i++)
        {
            irsArrayTemp[i] = new ArrayList<Byte>();
        }
        
        this.refresh();
        
        this.uniqueID = this.getHiveNetwork().registerIO(this);
    }
    
    @SuppressWarnings("unused")
    public void processData(byte[] data)
    {
        int i;
        
        try (
            ByteArrayInputStream bs = new ByteArrayInputStream(data.clone());
            DataInputStream ds = new DataInputStream(bs); )
            {
            byte senderType = ds.readByte();
            byte endType = ds.readByte();
            byte sender = ds.readByte();
            byte end = ds.readByte();
            byte packetType = ds.readByte();
            
            if (packetType == SIMPLE_REDSTONE_STATE)
            {
                this.rstemp.add(ds.readByte());
                this.irstemp.add(ds.readByte());
                
            }
            else if (packetType == FULL_REDSTONE_STATE)
            {
                for (i = 0; i < this.rsArray.length; i++)
                    this.rsArrayTemp[i].add(ds.readByte());
                
                for (i = 0; i < this.irsArray.length; i++)
                    this.irsArrayTemp[i].add(ds.readByte());
                
            }
            else if (packetType == PART_REDSTONE_STATE)
            {
                byte[] oldRsArray = this.rsArray;
                byte[] oldIrsArray = this.irsArray;
                
                while (ds.available() > 1)
                {
                    try 
                    {
                        byte freq = ds.readByte();
                        if (freq >= RS_FIRST && freq <= RS_LAST)
                            this.rsArrayTemp[freq].add(ds.readByte());
                        else if (freq >= IRS_FIRST && freq <= IRS_LAST)
                            this.irsArrayTemp[freq - IRS_FIRST].add(ds.readByte());
                    }
                    catch (EOFException e)
                    {
                        break;
                    }
                }
            }
            
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockRedstonePaintedWire.blockID);
            
            } catch (IOException e) { }
    }
    
    @Override
    public byte getDeviceTypeID()
    {
        return REDSTONE_WIRE;
    }
    
    public int getRsLevel()
    {
        if (this.frequency == EnumWireFrequency.NONE)
            return this.rsLevel;
        else 
            return this.rsArray[this.frequency.getIndex()];
    }
    
    public int getIrsLevel()
    {
        if (this.frequency == EnumWireFrequency.NONE)
            return this.irsLevel;
        else 
            return this.irsArray[this.frequency.getIndex()];
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
    {
        super.handlePacketData(network, type, packet, player, dataStream);
    }
    
    @Override
    protected int getID()
    {
        return ElectricExpansionItems.blockRedstonePaintedWire.blockID;
    }
    
    @Override
    public Byte getNetworkUniqueID()
    {
        return uniqueID;
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
        if (this.hiveNetwork != null)
            this.hiveNetwork.unregisterIO(this);
    }
    
    @Override
    public void onHiveChanged(IHiveNetwork oldNetwork, IHiveNetwork newNetwork)
    {
        if (oldNetwork == newNetwork)
            return;
        if (oldNetwork != null)
            this.uniqueID = oldNetwork.unregisterIO(this);
        if (newNetwork != null)
            this.uniqueID = newNetwork.registerIO(this);
    }
}
