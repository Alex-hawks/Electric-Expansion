package electricexpansion.common.tile;

import ic2.api.energy.tile.IEnergySource;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityHelper;
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

@SuppressWarnings("unused")
public class TileEntityTransformer extends TileEntityElectrical 
implements IRotatable, IPacketReceiver, IHiveMachine
{
    public static final float MAX_OUTPUT = 1_000;
    
    public boolean stepUp = false;
    public transient int type;
    
    private ForgeDirection input = ForgeDirection.NORTH; // TODO update to "new" rotation mechanics
    private ForgeDirection output = ForgeDirection.SOUTH; // TODO update to "new" rotation mechanics
    
    public transient IElectricityNetwork inputNetwork;
    public transient IElectricityNetwork outputNetwork;
    private transient IHiveNetwork hiveNetwork;
    
    @Override
    public void initiate()
    {
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        this.type = meta - (meta & 3);
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote)
        {
            ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - this.type + 2).getOpposite();
            TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
            
            // Check if requesting power on output
            ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() - this.type + 2);
            TileEntity outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
            
            this.inputNetwork = ElectricityHelper.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
            this.outputNetwork = ElectricityHelper.getNetworkFromTileEntity(outputTile, outputDirection);
            
            if (!this.worldObj.isRemote)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
            }
            
        }
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.stepUp, this.type);
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.stepUp = dataStream.readBoolean();
            this.type = dataStream.readInt();
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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.stepUp = par1NBTTagCompound.getBoolean("stepUp");
        this.type = par1NBTTagCompound.getInteger("type");
    }
    
    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("stepUp", this.stepUp);
        par1NBTTagCompound.setInteger("type", this.type);
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        return direction.ordinal() - 2 + this.type == meta || direction.getOpposite().ordinal() - 2 + this.type == meta;
    }
    
    @Override
    public void setDirection(ForgeDirection facingDirection)
    {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, facingDirection.ordinal() - 2 + this.type, 0);
    }
    
    @Override
    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - this.type);
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
        if (direction != this.input)
            return 0;
        else if (this.inputNetwork == null || this.outputNetwork == null)
            return 0;
        else if (this.inputNetwork.equals(this.outputNetwork))
            return 0;
        else
            return outputNetwork.getRequest().getWatts();
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        if (direction != this.output)
            return 0;
        else if (this.inputNetwork == null || this.outputNetwork == null)
            return 0;
        else if (this.inputNetwork.equals(this.outputNetwork))
            return 0;
        else
            return MAX_OUTPUT;
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        return 0;
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
}