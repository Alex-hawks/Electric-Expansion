package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.IWirelessPowerMachine;
import electricexpansion.api.tile.EnergyCoordinates;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.DistributionNetworks;

public class TileEntityQuantumBatteryBox extends TileEntityElectrical
implements IWirelessPowerMachine, IPacketReceiver, IInventory, IPeripheral
{
    private ItemStack[] containingItems = new ItemStack[2];
    private int playersUsing = 0;
    private float joulesForDisplay = 0;
    private String owningPlayer = null;
    private EnergyCoordinates coords = null;
    
    @Override
    public void setPlayer(EntityPlayer player)
    {
        this.owningPlayer = player.username;
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
        
        if (!this.produceUE(outputDirection))
        {
            // TODO Produce other electrical
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
    public float getRequest(ForgeDirection direction)
    {
        if (this.getOutputDirections().contains(ForgeDirection.OPPOSITES[direction.ordinal()]))
            return Math.min((this.getMaxEnergyStored() - this.getEnergyStored()), this.getOutputCap());
        else 
            return 0;
    }
    
    private float getOutputCap()
    {
        return 10;
    }
    
    public void sendPacket()
    {
        PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        EnergyCoordinates freq = this.getFrequency();
        float x = freq.x, y = freq.y, z = freq.z;
        
        String name = ElectricExpansion.useHashCodes ? Integer.valueOf(this.owningPlayer.hashCode()).toString() : this.owningPlayer;
        
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, x, y, z, this.getEnergyStored(), name);
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                float x = dataStream.readFloat(), y = dataStream.readFloat(), z = dataStream.readFloat();
                this.coords = new EnergyCoordinates(x, y, z);
                this.joulesForDisplay = dataStream.readFloat();
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
                float x = dataStream.readFloat(), y = dataStream.readFloat(), z = dataStream.readFloat();
                this.setFrequency(new EnergyCoordinates(x, y, z));
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
            float x = par1NBTTagCompound.getFloat("frequency_x");
            float y = par1NBTTagCompound.getFloat("frequency_y");
            float z = par1NBTTagCompound.getFloat("frequency_z");
            
            this.coords = new EnergyCoordinates(x, y, z);
        }
        catch (Exception e)
        {
            this.coords = null;
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
        par1NBTTagCompound.setFloat("frequency_x", this.coords.x);
        par1NBTTagCompound.setFloat("frequency_y", this.coords.y);
        par1NBTTagCompound.setFloat("frequency_z", this.coords.z);
        par1NBTTagCompound.setString("owner", this.owningPlayer);
    }
    
    @Override
    public float getEnergyStored()
    {
        return ElectricExpansion.DistributionNetworksInstance.getJoules(this.coords);
    }
    
    @Override
    public void removeJoules(float outputWatts)
    {
        ElectricExpansion.DistributionNetworksInstance.removeJoules(this.coords, outputWatts);
    }
    
    @Override
    public void setEnergyStored(float joules)
    {
        ElectricExpansion.DistributionNetworksInstance.setJoules(this.coords, joules);
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        return DistributionNetworks.getMaxJoules();
    }
    
    public float getJoulesForDisplay()
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
    public EnergyCoordinates getFrequency()
    {
        return this.coords;
    }
    
    @Override
    public void setFrequency(EnergyCoordinates coords)
    {
        this.coords = coords;
        
        if (this.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, coords));
        }
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
        return new String[] { "getVoltage", "isFull", "getEnergyStored", "getFrequency", "setFrequency", "getPlayer" };
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
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws IllegalArgumentException
    {
        final int getVoltage = 0;
        final int isFull = 1;
        final int getEnergyStored = 2;
        final int getFrequency = 3;
        final int setFrequency = 4;
        final int getPlayer = 5;
        
        switch (method)
        {
            case getVoltage:
                return new Object[] { this.getVoltage() };
            case isFull:
                return new Object[] { this.getEnergyStored() >= this.getMaxEnergyStored() };
            case getEnergyStored:
                return new Object[] { this.getEnergyStored() };
            case getFrequency:
                return new Object[] { this.getFrequency().x, this.getFrequency().y, this.getFrequency().z };
            case setFrequency:
                return arguments.length == 3 ? this.setFrequency(arguments) : new Object[] { "Expected args for this function is 3. You have provided %s.".replace("%s", arguments.length + "") };
            case getPlayer:
                return new Object[] { this.getOwningPlayer() };
            default:
                return new Object[] { "Function unimplemented" };
        }
    }
    
    private Object[] setFrequency(Object[] args)
    {
        if (args.length == 3 && args[0] instanceof Double && args[1] instanceof Double && args[2] instanceof Double)
            this.setFrequency(new EnergyCoordinates((float) args[0], (float) args[1], (float) args[2]));
        return new Object[] {this.getFrequency().x, this.getFrequency().y, this.getFrequency().z};
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
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        return Math.max(0, Math.min(this.getEnergyStored(), this.getOutputCap()));
    }
}