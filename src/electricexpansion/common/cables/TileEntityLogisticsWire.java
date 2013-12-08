package electricexpansion.common.cables;

import static electricexpansion.api.hive.HiveSignal.DEVICE_TYPE.*;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.hive.IHiveSignalIO;
import electricexpansion.common.compatibility.LuaDataInputStream;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EnumWireFrequency;

public class TileEntityLogisticsWire extends TileEntityConductorBase implements IHiveSignalIO, IPeripheral
{
    @SuppressWarnings("unused")
    private byte            tick = 0;
    private IComputerAccess computer;
    private Byte            uniqueID;
    
    @Override
    public void initiate()
    {
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockLogisticsWire.blockID);
        this.uniqueID = this.getHiveNetwork().registerIO(this);
    }
    
    @Override
    @SuppressWarnings("unused")
    public void processData(byte[] data)
    {
        try (
            ByteArrayInputStream bs = new ByteArrayInputStream(data.clone());
            DataInputStream ds = new DataInputStream(bs);)
            {
            byte senderType = ds.readByte();
            byte endType = ds.readByte();
            byte sender = ds.readByte();
            byte end = ds.readByte();
            byte packetType = ds.readByte();
            
            if (packetType == DISCOVER_CC_COMPUTERS && this.computer != null)
            {
                sendDiscoverReply(senderType, sender);
            }
            else if (this.computer != null)
            {
                this.computer.queueEvent("recievedDataStream", new Object[]
                    { new LuaDataInputStream(data) });
            }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    
    @Override
    public boolean canUpdate() 
    { 
        return true;
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (ticks % 10 != 0)
            return;
        if (this.computer != null)
            return;
        
        try (
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(bs);)
            {
            if (this.frequency == EnumWireFrequency.NONE)
            {
                ds.writeByte(LOGISTICS_WIRE);
                ds.writeByte(REDSTONE_WIRE);
                ds.writeByte(this.uniqueID);
                ds.writeByte(-1);
                ds.writeByte(SIMPLE_REDSTONE_STATE);
                ds.writeByte(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord));
                ds.writeByte(this.worldObj.getStrongestIndirectPower(this.xCoord, this.yCoord, this.zCoord));
            }
            else
            {
                ds.writeByte(LOGISTICS_WIRE);
                ds.writeByte(REDSTONE_WIRE);
                ds.writeByte(this.uniqueID);
                ds.writeByte(-1);
                ds.writeByte(PART_REDSTONE_STATE);
                ds.writeByte(this.frequency.getIndex());
                ds.writeByte(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord));
                ds.writeByte(this.frequency.getIndex() + 0b0001_0000);
                ds.writeByte(this.worldObj.getStrongestIndirectPower(this.xCoord, this.yCoord, this.zCoord));
            }
            
            ds.close();
            
            this.getHiveNetwork().sendData(bs.toByteArray());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    
    private void sendDiscoverReply(byte destinationType, byte destination)
    {
        try (        
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(bs);
            )
            {
            ds.writeByte(destinationType);
            ds.writeByte(LOGISTICS_WIRE);
            ds.writeByte(destination);
            ds.writeByte(this.uniqueID);
            ds.writeByte(DISCOVER_REPLY);
            ds.writeInt(this.computer.getID());
            
            byte[] data = bs.toByteArray();
            
            this.hiveNetwork.sendData(data);
            } catch (IOException e) { }
    }
    
    @Override
    public String getType()
    {
        return "Logistics Wire";
    }
    
    @Override
    public String[] getMethodNames()
    {
        return new String[] { /* "sendData" */};
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        // TODO Make the computer able to send data.
        return null;
    }
    
    @Override
    public boolean canAttachToSide(int side)
    {
        return this.computer == null;
    }
    
    @Override
    public void attach(IComputerAccess computer)
    {
        this.computer = computer;
        this.hiveNetwork.unregisterIO(this);
        this.uniqueID = this.hiveNetwork.registerIO(this);
    }
    
    @Override
    public void detach(IComputerAccess computer)
    {
        synchronized (this.computer)
        {
            this.computer = null;
        }
    }
    
    @Override
    public byte getDeviceTypeID()
    {
        return this.computer == null ? LOGISTICS_WIRE : CC_PERIPHERAL;
    }
    
    @Override
    public Byte getNetworkUniqueID()
    {
        return uniqueID;
    }
    
    @Override
    protected int getID()
    {
        return ElectricExpansionItems.blockLogisticsWire.blockID;
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