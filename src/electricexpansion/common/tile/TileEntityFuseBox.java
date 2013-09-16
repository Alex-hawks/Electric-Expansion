package electricexpansion.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import electricexpansion.api.IItemFuse;

public class TileEntityFuseBox extends TileEntityElectrical implements IPacketReceiver, IInventory
{
    public ItemStack[] inventory = new ItemStack[1];
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote)
        {
            if (this.hasFuse())
            {
                ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
                TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
                
                ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
                TileEntity outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
                
                IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, outputDirection.getOpposite());
                IElectricityNetwork outputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);
                
                if (outputNetwork != null && inputNetwork != null && outputNetwork != inputNetwork)
                {
                    ElectricityPack request = outputNetwork.getRequest(new TileEntity[0]);
                    inputNetwork.startRequesting(this, request);
                    
                    ElectricityPack recieved = inputNetwork.consumeElectricity(this);
                    
                    outputNetwork.startProducing(this, recieved);
                    
                    if (recieved.amperes > ((IItemFuse) this.inventory[0].getItem()).getMaxVolts(this.inventory[0]))
                    {
                        ((IItemFuse) this.inventory[0].getItem()).onFuseTrip(this.inventory[0]);
                    }
                }
                else if (outputNetwork != null && inputNetwork == null)
                {
                    outputNetwork.stopProducing(this);
                }
                else if (outputNetwork == null && inputNetwork != null)
                {
                    inputNetwork.stopRequesting(this);
                }
            }
            
            if (!this.worldObj.isRemote)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");
            
            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);
    }
    
    public boolean hasFuse()
    {
        if (this.inventory[0] != null)
        {
            if (this.inventory[0].getItem() instanceof IItemFuse)
                return ((IItemFuse) this.inventory[0].getItem()).isValidFuse(this.inventory[0]);
        }
        return false;
    }
    
    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal("tile.FuseBox.name");
    }
    
    @Override
    public int getSizeInventory()
    {
        return 1;
    }
    
    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.inventory[var1];
    }
    
    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (var1 < this.inventory.length && this.inventory[var1].stackSize >= var2)
        {
            ItemStack toReturn = this.inventory[var1].copy();
            toReturn.stackSize -= var2;
            if (this.inventory[var1].stackSize == 0)
            {
                this.inventory = null;
            }
            return toReturn;
        }
        return null;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return this.inventory[var1];
    }
    
    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        if (var1 < this.inventory.length)
        {
            this.inventory[var1] = var2;
        }
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
    public void openChest()
    {
    }
    
    @Override
    public void closeChest()
    {
        
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        return direction.ordinal() == meta + 2 || direction.getOpposite().ordinal() == meta + 2;
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        
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

    @Override
    public float getRequest(ForgeDirection direction)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getMaxEnergyStored()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
