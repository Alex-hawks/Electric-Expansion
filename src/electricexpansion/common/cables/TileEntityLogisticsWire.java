package electricexpansion.common.cables;

import static electricexpansion.api.hive.HiveSignal.DEVICE_TYPE.*;
import static electricexpansion.api.hive.HiveSignal.PACKET_TYPE.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.api.hive.IHiveSignalIO;
import electricexpansion.common.compatibility.LuaDataInputStream;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityLogisticsWire extends TileEntityConductorBase implements IHiveSignalIO, IPeripheral
{
    @SuppressWarnings("unused")
    private byte tick = 0;
    private IComputerAccess computer;
    private byte uniqueID;
    
    @Override
    public void initiate()
    {
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockLogisticsWire.blockID);
        PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
        this.uniqueID = this.hiveNetwork.registerIO(this);
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        super.handlePacketData(network, type, packet, player, dataStream);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
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
            
            if (packetType == BROADCAST)
            {
                if (senderType == CORE)
                {
                    sendDiscoverReply(senderType, sender);
                }
            }
            else if (this.computer != null)
            {
                this.computer.queueEvent("recievedDataStream", new Object[] { new LuaDataInputStream(data) });
            }
            } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendDiscoverReply(byte destinationType, byte destination)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);
        
        try
        {
            ds.writeByte(destinationType);
            ds.writeByte(LOGISTICS_WIRE);
            ds.writeByte(destination);
            ds.writeByte(this.uniqueID);
            ds.writeByte(DISCOVER_REPLY);
            ds.writeBoolean(this.computer != null);
            
            ds.close();
            bs.close();
            
            byte[] data = bs.toByteArray();

            this.hiveNetwork.sendData(data);
        } 
        catch (IOException e) { }
    }

    @Override
    public String getType()
    {
        return "Logistics Wire";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "sendData" };
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
        return LOGISTICS_WIRE;
    }
}