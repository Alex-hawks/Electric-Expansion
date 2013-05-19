package electricexpansion.common.cables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityLogisticsWire extends TileEntityConductorBase implements IRedstoneProvider
{
    public boolean buttonStatus0 = false;
    public boolean buttonStatus1 = false;
    public boolean buttonStatus2 = false;
    
    private double networkProduced = 0;
    
    private byte tick = 0;
    
    @Override
    public void initiate()
    {
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockLogisticsWire.blockID);
        PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                byte id = dataStream.readByte();
                
                if (id == 5)
                {
                    this.visuallyConnected[0] = dataStream.readBoolean();
                    this.visuallyConnected[1] = dataStream.readBoolean();
                    this.visuallyConnected[2] = dataStream.readBoolean();
                    this.visuallyConnected[3] = dataStream.readBoolean();
                    this.visuallyConnected[4] = dataStream.readBoolean();
                    this.visuallyConnected[5] = dataStream.readBoolean();
                    
                    this.buttonStatus0 = dataStream.readBoolean();
                    this.buttonStatus1 = dataStream.readBoolean();
                    this.buttonStatus2 = dataStream.readBoolean();
                }
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
        else
        {
            try
            {
                byte id = dataStream.readByte();
                if (id == -1)
                {
                    this.buttonStatus0 = dataStream.readBoolean();
                }
                if (id == 0)
                {
                    this.buttonStatus1 = dataStream.readBoolean();
                }
                if (id == 1)
                {
                    this.buttonStatus2 = dataStream.readBoolean();
                }
                if (id == 7)
                {
                    if (dataStream.readBoolean() == true)
                    {
                    }
                    else
                    {
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.buttonStatus0 = nbt.getBoolean("buttonStatus0");
        this.buttonStatus1 = nbt.getBoolean("buttonStatus1");
        this.buttonStatus2 = nbt.getBoolean("buttonStatus2");
        
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("buttonStatus0", this.buttonStatus0);
        nbt.setBoolean("buttonStatus1", this.buttonStatus1);
        nbt.setBoolean("buttonStatus2", this.buttonStatus2);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(this.channel, this, (byte) 5, this.visuallyConnected[0], this.visuallyConnected[1], this.visuallyConnected[2], this.visuallyConnected[3],
                this.visuallyConnected[4], this.visuallyConnected[5], this.buttonStatus0, this.buttonStatus1, this.buttonStatus2);
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote)
        {
            
            this.tick++;
            
            if (this.tick == 20)
            {
                this.tick = 0;
                
                if (this.networkProduced == 0 && this.getNetwork().getProduced().getWatts() != 0)
                {
                    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID);
                }
                
                if (this.networkProduced != 0 && this.getNetwork().getProduced().getWatts() == 0)
                {
                    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID);
                }
                
                this.networkProduced = this.getNetwork().getProduced().getWatts();
            }
        }
        
    }
    
    @Override
    public boolean isPoweringTo(ForgeDirection side)
    {
        if (this.buttonStatus0 && this.getNetwork().getProduced().getWatts() > 0)
            return true;
        
        return false;
    }
    
    @Override
    public boolean isIndirectlyPoweringTo(ForgeDirection side)
    {
        return this.isPoweringTo(side);
    }
}