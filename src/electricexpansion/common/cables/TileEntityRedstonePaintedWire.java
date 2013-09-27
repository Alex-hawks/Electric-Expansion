package electricexpansion.common.cables;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import electricexpansion.api.hive.IHiveSignalIO;
import electricexpansion.common.helpers.TileEntityConductorBase;

import static electricexpansion.api.hive.HiveSignal.DEVICE_TYPE.*;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.*;

public class TileEntityRedstonePaintedWire extends TileEntityConductorBase implements IHiveSignalIO
{
    private byte rsLevel = 0;
    private byte[] rsArray = new byte[16];
    
    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
    @SuppressWarnings("unused")
    public void processData(byte[] data)
    {
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
                this.rsLevel = ds.readByte();
            }
            else if (packetType == FULL_REDSTONE_STATE)
            {
                for (int i = 0; i < this.rsArray.length; i++)
                    this.rsArray[i] = ds.readByte();
            }
            else if (packetType == PART_REDSTONE_STATE)
            {
                while (true)
                {
                    try 
                    {
                        byte freq = ds.readByte();
                        rsArray[freq] = ds.readByte();
                    }
                    catch (EOFException e)
                    {
                        break;
                    }
                }
            }
            
            } catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    
    @Override
    public byte getDeviceTypeID()
    {
        return REDSTONE_WIRE;
    }

    public int getRsLevel()
    {
        return this.rsLevel;
    }
}
