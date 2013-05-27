package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.IWirelessPowerMachine;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.DistributionNetworks;

public class TileEntityQuantumBatteryBox extends TileEntityElectricityStorage 
implements IWirelessPowerMachine, IPacketReceiver, IInventory, IPeripheral
{
    private ItemStack[] containingItems = new ItemStack[2];
    private int playersUsing = 0;
    private byte frequency = 0;
    private double joulesForDisplay = 0;
    private String owningPlayer = null;
    
    @Override
    public void setPlayer(EntityPlayer player)
    {
        this.owningPlayer = player.username;
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.isDisabled())
        {
            ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
            TileEntity outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
            
            TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection.getOpposite());
            
            IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
            IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);
            
            if (outputNetwork != null && inputNetwork != outputNetwork)
            {
                ElectricityPack actualOutput = new ElectricityPack(Math.min(outputNetwork.getLowestCurrentCapacity(),
                        Math.min(this.getOutputCap(), outputNetwork.getRequest().getWatts()) / this.getVoltage()), this.getVoltage());
                
                if (this.getJoules() > 0 && actualOutput.getWatts() > 0)
                {
                    outputNetwork.startProducing(this, actualOutput);
                    this.setJoules(this.getJoules() - actualOutput.getWatts());
                }
                else
                {
                    outputNetwork.stopProducing(this);
                }
            }
        }
        
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 3L == 0L && this.playersUsing > 0)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
            }
        }
    }
    
    @Override
    public ElectricityPack getRequest()
    {
        return new ElectricityPack(Math.min((this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getOutputCap() / 2.0), this.getVoltage());
    }
    
    private double getOutputCap()
    {
        return 10000;
    }
    
    public void sendPacket()
    {
        PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        if (ElectricExpansion.useHashCodes)
            return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.getFrequency(), this.disabledTicks, this.getJoules(), Integer.valueOf(this.owningPlayer.hashCode()).toString());
        else
            return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.getFrequency(), this.disabledTicks, this.getJoules(), this.owningPlayer);
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                this.frequency = dataStream.readByte();
                this.disabledTicks = dataStream.readInt();
                this.joulesForDisplay = dataStream.readDouble();
                this.owningPlayer = dataStream.readUTF();
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
                this.setFrequency(dataStream.readByte());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void openChest()
    {
        this.playersUsing++;
    }
    
    @Override
    public void closeChest()
    {
        this.playersUsing--;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        try
        {
            this.frequency = par1NBTTagCompound.getByte("frequency");
        }
        catch (Exception e)
        {
            this.frequency = 0;
        }
        
        try
        {
            this.owningPlayer = par1NBTTagCompound.getString("owner");
        }
        catch (Exception e)
        {
            this.owningPlayer = null;
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("frequency", this.frequency);
        par1NBTTagCompound.setString("owner", this.owningPlayer);
    }
    
    @Override
    public double getJoules()
    {
        return ElectricExpansion.DistributionNetworksInstance.getJoules(this.owningPlayer, this.frequency);
    }
    
    @Override
    public void removeJoules(double outputWatts)
    {
        ElectricExpansion.DistributionNetworksInstance.removeJoules(this.owningPlayer, this.frequency, outputWatts);
    }
    
    @Override
    public void setJoules(double joules)
    {
        ElectricExpansion.DistributionNetworksInstance.setJoules(this.owningPlayer, this.frequency, joules);
    }
    
    @Override
    public double getMaxJoules()
    {
        return DistributionNetworks.getMaxJoules();
    }
    
    public double getJoulesForDisplay(Object... data)
    {
        return this.joulesForDisplay;
    }
    
    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }
    
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;
            
            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);
                
                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }
                
                return var3;
            }
        }
        else
            return null;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
            return null;
    }
    
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;
        
        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public String getInvName()
    {
        return TranslationHelper.getLocal("tile.Distribution.name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
                : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }
    
    @Override
    public byte getFrequency()
    {
        return this.frequency;
    }
    
    @Override
    public void setFrequency(byte newFrequency)
    {
        this.frequency = newFrequency;
        
        if (this.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, newFrequency));
        }
    }
    
    public void setFrequency(int frequency)
    {
        this.setFrequency((byte) frequency);
    }
    
    public void setFrequency(short frequency)
    {
        this.setFrequency((byte) frequency);
    }
    
    private int setFrequency(Object frequency)
    {
        if (frequency instanceof Double)
        {
            Double freq = (Double) frequency;
            this.setFrequency((int) Math.floor(freq));
        }
        return this.frequency;
    }
    
    public String getOwningPlayer()
    {
        return this.owningPlayer;
    }
    
    /**
     * COMPUTERCRAFT FUNCTIONS
     */
    
    @Override
    public String getType()
    {
        return this.getInvName().replaceAll(" ", "");
    }
    
    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getVoltage", "isFull", "getJoules", "getFrequency", "setFrequency", "getPlayer" };
    }
    
    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }
    
    @Override
    public void attach(IComputerAccess computer)
    {
    }
    
    @Override
    public void detach(IComputerAccess computer)
    {
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws IllegalArgumentException
    {
        final int getVoltage = 0;
        final int isFull = 1;
        final int getJoules = 2;
        final int getFrequency = 3;
        final int setFrequency = 4;
        final int getPlayer = 5;
        
        if (!this.isDisabled())
        {
            switch (method)
            {
                case getVoltage:
                    return new Object[] { this.getVoltage() };
                case isFull:
                    return new Object[] { this.getJoules() >= this.getMaxJoules() };
                case getJoules:
                    return new Object[] { this.getJoules() };
                case getFrequency:
                    return new Object[] { this.getFrequency() };
                case setFrequency:
                    return new Object[] { arguments.length == 1 ? this.setFrequency(arguments[0]) : "Expected args for this function is 1. You have provided %s.".replace("%s", arguments.length + "") };
                case getPlayer:
                    return new Object[] { this.getOwningPlayer() };
                default:
                    return new Object[] { "Function unimplemented" };
            }
        }
        else
            return new Object[] { "Please wait for the EMP to run out." };
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2) || direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
    
    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
    
}