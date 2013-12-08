package electricexpansion.common.tile;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.common.ElectricExpansion;

public class TileEntityTransformer extends TileEntityElectrical 
implements IPacketReceiver, IHiveMachine
{
    public static final float MAX_OUTPUT = 1_000;
    
    public boolean stepUp = false;
    public transient int tier;
    
    private ForgeDirection input = ForgeDirection.NORTH;
    private ForgeDirection output = ForgeDirection.SOUTH;
    
    public transient IElectricityNetwork inputNetwork;
    public transient IElectricityNetwork outputNetwork;
    private transient IHiveNetwork hiveNetwork;
    private transient float voltsNextSend;
    
    public TileEntityTransformer(int tier)
    {
        this.tier = tier;
    }
    
    public TileEntityTransformer() { }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote && this.ticks % 3 == 0)
        {
            TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), this.input);
            
            // Check if requesting power on output
            TileEntity outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), this.output);
            
            this.inputNetwork = ElectricityHelper.getNetworkFromTileEntity(inputTile, this.input);
            this.outputNetwork = ElectricityHelper.getNetworkFromTileEntity(outputTile, this.output);
            
            PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
        }
        this.produceUE(this.output);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.stepUp, this.tier, this.input.ordinal(), this.output.ordinal());
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.stepUp = dataStream.readBoolean();
            this.tier = dataStream.readInt();
            this.setInput(ForgeDirection.getOrientation(dataStream.readInt()));
            this.setOutput(ForgeDirection.getOrientation(dataStream.readInt()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.stepUp = tag.getBoolean("stepUp");
        this.tier = tag.getInteger("tier");
        this.input = ForgeDirection.getOrientation(tag.getByte("input"));
        this.output = ForgeDirection.getOrientation(tag.getByte("output"));
    }
    
    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("stepUp", this.stepUp);
        tag.setInteger("tier", this.tier);
        tag.setByte("input", (byte) this.input.ordinal());
        tag.setByte("output", (byte) this.output.ordinal());
    }
    
    @Override
    public boolean canConnect(ForgeDirection dir)
    {
        return this.input == dir || this.output == dir;
    }
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[] { this.inputNetwork, this.outputNetwork };
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
            
            for (IElectricityNetwork net : getNetworks())
                this.hiveNetwork.addNetwork(net);
            
            return true;
        }
        return false;
    }
    
    @Override
    public float getRequest(ForgeDirection direction)
    {
        return Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), MAX_OUTPUT);
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        return this.energyStored;
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        return (float) Math.pow(2, this.tier + 2) * 10;
    }
    
    @Override
    public int getSerialQuantity()
    {
        return 0;
    }
    
    @Override
    public int getInputQuantity()
    {
        return 1;
    }
    
    @Override
    public int getOutputQuantity()
    {
        return 1;
    }
    
    @Override
    public EnumSet<ForgeDirection> getSerialDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }
    
    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.of(this.input, this.output);
    }
    
    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.of(this.output);
    }
    
    public void setInput(ForgeDirection dir)
    {
        if (this.output != dir)
        {
            this.input = dir;
            if (!this.worldObj.isRemote)
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockTransformer.blockID);
        }
    }
    
    public void setOutput(ForgeDirection dir)
    {
        if (this.input != dir)
        {
            this.output = dir;
            if (!this.worldObj.isRemote)
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansionItems.blockTransformer.blockID);
        }
    }
    
    public ForgeDirection getInput()
    {
        return this.input;
    }
    
    public ForgeDirection getOutput()
    {
        return this.output;
    }
    
    @Override
    public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
    {
        if (this.getInputDirections().contains(from))
        {
            this.voltsNextSend = (float) (this.stepUp ? receive.voltage * Math.pow(2, this.tier + 1) : receive.voltage / Math.pow(2, this.tier + 1));
            return this.receiveElectricity(receive, doReceive);
        }

        return 0;
    }

    @Override
    public float getVoltage()
    {
        return this.voltsNextSend;
    }
}