package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.common.ElectricExpansion;

public class TileEntityTransformer extends TileEntityElectrical implements IRotatable, IPacketReceiver
{
    // USING A WRENCH ONE CAN CHANGE THE TRANSFORMER TO EITHER STEP UP OR STEP
    // DOWN.
    public boolean stepUp = false;
    
    public int type;
    
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
            
            IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
            IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);
            
            if (outputNetwork != null && inputNetwork == null)
            {
                outputNetwork.stopProducing(this);
            }
            else if (outputNetwork == null && inputNetwork != null)
            {
                inputNetwork.stopRequesting(this);
            }
            
            if (outputNetwork != null && inputNetwork != null)
            {
                if (outputNetwork != inputNetwork)
                {
                    if (outputNetwork.getRequest().getWatts() > 0)
                    {
                        inputNetwork.startRequesting(this, outputNetwork.getRequest());
                        ElectricityPack actualEnergy = inputNetwork.consumeElectricity(this);
                        
                        if (actualEnergy.getWatts() > 0)
                        {
                            double typeChange = 0;
                            
                            if (this.type == 0)
                            {
                                typeChange = 2;
                            }
                            else if (this.type == 4)
                            {
                                typeChange = 4;
                            }
                            else if (this.type == 8)
                            {
                                typeChange = 8;
                            }
                            
                            double newVoltage = actualEnergy.voltage * typeChange;
                            
                            if (!this.stepUp)
                            {
                                newVoltage = actualEnergy.voltage / typeChange;
                            }
                            
                            outputNetwork.startProducing(this, actualEnergy.getWatts() / newVoltage, newVoltage);
                        }
                        else
                        {
                            outputNetwork.stopProducing(this);
                        }
                    }
                    else
                    {
                        inputNetwork.stopRequesting(this);
                        outputNetwork.stopProducing(this);
                    }
                }
                else
                {
                    inputNetwork.stopRequesting(this);
                    outputNetwork.stopProducing(this);
                }
            }
            
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
    public void setDirection(World world, int x, int y, int z, ForgeDirection facingDirection)
    {
        this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, facingDirection.ordinal() - 2 + this.type, 0);
    }
    
    @Override
    public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - this.type);
    }
    
}