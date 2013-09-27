package electricexpansion.common.tile;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.IElectricItem;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.block.INetworkProvider;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.api.tile.ITileRunnable;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.PowerUtils;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.misc.PowerConversionUtils;
import electricexpansion.common.misc.PowerConversionUtils.GenericPack;

public class TileEntityInsulatingMachine extends TileEntityElectrical
implements ISidedInventory, IPacketReceiver, IElectricalStorage, IEnergyTile, IEnergySink, IHiveMachine, ITileRunnable
{
    //  constants
    public static final float WATTS_PER_TICK = 0.500F;
    public static final float TRANSFER_LIMIT = 1.250F;
    public static final float MAX_JOULES = 150.0F;
    
    //  Not saved
    public transient int orientation;
    private transient int recipeTicks = 0;
    private transient int playersUsing = 0;
    private transient int baseID = 0;
    private transient int baseMeta = 0;
    private transient boolean initialized;
    private transient IHiveNetwork hiveNetwork;
    private transient IElectricityNetwork network;
    
    //  Saved
    private int processTicks = 0;
    private ItemStack[] inventory = new ItemStack[3];
    
    @Override
    public void initiate()
    {
        this.initialized = true;
        
        if (Loader.isModLoaded("IC2"))
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
        
        if (this.initialized && Loader.isModLoaded("IC2"))
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.worldObj.isRemote)
        {
            ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
            TileEntity inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
            
            if (inputTile instanceof INetworkProvider)
                this.network = ((INetworkProvider) inputTile).getNetwork();
            else
                this.network = null;
        }
        
        if (this.inventory[0] != null && this.energyStored < this.getMaxEnergyStored())
        {
            if (this.inventory[1].getItem() instanceof IItemElectric)
            {
                this.setEnergyStored(this.getEnergyStored() + (PowerUtils.UE.discharge(this.inventory[1], PowerConversionUtils.INSTANCE.new UEElectricPack((this.getMaxEnergyStored() - this.getEnergyStored()) / this.getVoltage(), this.getVoltage()), 2).toUEWatts()));
            }
            else if (this.inventory[1].getItem() instanceof IElectricItem)
            {
                this.setEnergyStored(this.getEnergyStored() + (PowerUtils.IC2.discharge(this.inventory[1], PowerConversionUtils.INSTANCE.new UEElectricPack((this.getMaxEnergyStored() - this.getEnergyStored()) / this.getVoltage(), this.getVoltage()), 2).toUEWatts()));
            }
            
        }
        
        if (this.energyStored >= WATTS_PER_TICK - .0500F)
        {
            if (this.inventory[1] != null && this.canProcess() && (this.processTicks == 0 || this.baseID != this.inventory[1].itemID || this.baseMeta != this.inventory[1].getItemDamage()))
            {
                this.baseID = this.inventory[1].itemID;
                this.baseMeta = this.inventory[1].getItemDamage();
                this.processTicks = this.getProcessingTime();
                this.recipeTicks = this.getProcessingTime();
            }
            
            if (this.canProcess() && this.processTicks > 0)
            {
                this.processTicks--;
                
                if (this.processTicks < 1)
                {
                    this.processItem();
                    this.processTicks = 0;
                }
                this.getClass();
                this.energyStored -= WATTS_PER_TICK;
            }
            else
            {
                this.processTicks = 0;
            }
        }
        
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 3L == 0L && this.playersUsing > 0)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
            }
        }
        
        this.energyStored = Math.min(this.energyStored, this.getMaxEnergyStored());
        this.energyStored = Math.max(this.energyStored, 0.0F);
    }
    
    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2));
    }
    
    @Override
    public float receiveElectricity(ElectricityPack receive, boolean doReceive)
    {
        if (receive.getWatts() + this.energyStored > this.getMaxEnergyStored())
            this.setEnergyStored(this.getMaxEnergyStored());
        else
            this.setEnergyStored(this.energyStored + receive.getWatts());
        
        if (UniversalElectricity.isVoltageSensitive)
        {
            if (receive.voltage > this.getVoltage())
            {
                this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2.0F, true);
            }
        }

        return receive.getWatts() - (this.getMaxEnergyStored() - this.getEnergyStored());
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this,
            new Object[] { Integer.valueOf(this.processTicks), Float.valueOf(this.energyStored), Integer.valueOf(this.recipeTicks) });
    }
    
    @Override
    public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.processTicks = dataStream.readInt();
            this.energyStored = dataStream.readFloat();
            this.recipeTicks = dataStream.readInt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void openChest()
    {
        if (!this.worldObj.isRemote)
        {
            PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15.0D);
        }
        this.playersUsing++;
    }
    
    @Override
    public void closeChest()
    {
        this.playersUsing--;
    }
    
    public boolean canProcess()
    {
        boolean canWork = false;
        ItemStack inputSlot = this.inventory[1];
        int outputSlot = this.inventory[2] != null ? this.inventory[2].stackSize : 0;
        if (inputSlot != null)
        {
            if (InsulationRecipes.INSTANCE.getProcessResult(inputSlot) > 0 && InsulationRecipes.INSTANCE.getProcessResult(inputSlot) + outputSlot <= 64)
            {
                canWork = true;
            }
        }
        
        return canWork;
    }
    
    public void processItem()
    {
        if (this.canProcess())
        {
            int result = InsulationRecipes.INSTANCE.getProcessResult(this.inventory[1]);
            
            if (this.inventory[2] == null)
            {
                this.inventory[2] = new ItemStack(ElectricExpansionItems.itemParts, result, 6);
            }
            else if (this.inventory[2].stackSize + result <= 64)
            {
                this.inventory[2].stackSize += result;
            }
            this.inventory[1].stackSize -= InsulationRecipes.INSTANCE.getInputQTY(this.inventory[1]);
            
            if (this.inventory[1].stackSize <= 0)
            {
                this.inventory[1] = null;
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInteger("processTicks");
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];
        try
        {
            this.energyStored = par1NBTTagCompound.getFloat("energyStored");
        }
        catch (Exception e)
        {
        }
        
        for (int var3 = 0; var3 < var2.tagCount(); var3++)
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
        par1NBTTagCompound.setInteger("processTicks", this.processTicks);
        NBTTagList var2 = new NBTTagList();
        par1NBTTagCompound.setFloat("energyStored", this.getEnergyStored());
        
        for (int var3 = 0; var3 < this.inventory.length; var3++)
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
    
    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.inventory[par1];
    }
    
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inventory[par1] != null)
        {
            if (this.inventory[par1].stackSize <= par2)
            {
                ItemStack var3 = this.inventory[par1];
                this.inventory[par1] = null;
                return var3;
            }
            
            ItemStack var3 = this.inventory[par1].splitStack(par2);
            
            if (this.inventory[par1].stackSize == 0)
            {
                this.inventory[par1] = null;
            }
            return var3;
        }
        
        return null;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inventory[par1] != null)
        {
            ItemStack var2 = this.inventory[par1];
            this.inventory[par1] = null;
            return var2;
        }
        
        return null;
    }
    
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inventory[par1] = par2ItemStack;
        
        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal("tile.insulator.name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }
    
    @Override
    public float getVoltage()
    {
        return 0.120F;
    }
    
    public int getProcessingTime()
    {
        if (this.inventory[1] != null)
        {
            if (InsulationRecipes.INSTANCE.getProcessResult(this.inventory[1]) != 0)
                return InsulationRecipes.INSTANCE.getProcessTicks(this.inventory[1]).intValue();
        }
        return -1;
    }
    
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        if (this.getInputDirections().contains(direction))
            return true;
        
        return false;
    }
    
    @Override
    public double demandedEnergyUnits()
    {
        return PowerConversionUtils.INSTANCE.new UEElectricPack(this.getMaxEnergyStored() - this.energyStored).toEU();
    }
    
    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        GenericPack givenEnergy = PowerConversionUtils.INSTANCE.new IC2Pack(amount, 1);
        double rejects = 0;
        GenericPack neededEnergy = PowerConversionUtils.INSTANCE.new UEElectricPack(this.getMaxEnergyStored() - this.energyStored);
        
        if (givenEnergy.toUEWatts() < neededEnergy.toUEWatts())
        {
            this.energyStored += givenEnergy.toUEWatts();
        }
        else if (givenEnergy.toUEWatts() > neededEnergy.toUEWatts())
        {
            this.energyStored += neededEnergy.toUEWatts();
            rejects = givenEnergy.toEU() - neededEnergy.toEU();
        }
        
        return rejects;
    }
    
    @Override
    public int getMaxSafeInput()
    {
        return 2048;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata() + 2;
    }
    
    @Override
    public float getEnergyStored()
    {
        return this.energyStored;
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        return TileEntityInsulatingMachine.MAX_JOULES;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        if (i == 1)
            return InsulationRecipes.INSTANCE.getProcessResult(itemstack) >= 1;
            
        return false;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0, 1, 2 };
    }
    
    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        switch (slot)
        {
            case 0:
                return PowerUtils.UE.isFull(itemstack);
            case 1:
                return InsulationRecipes.INSTANCE.getProcessResult(itemstack) >= 0;
                
            default:
                return false;
        }
    }
    
    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side)
    {
        switch (slot)
        {
            case 0:
                return PowerUtils.UE.isEmpty(itemstack);
            case 2:
                return true;
                
            default:
                return false;
        }
    }
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[] { this.network };
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
            return true;
        }
        return false;
    }
    
    @Override
    public float getRequest(ForgeDirection direction)
    {
        return MAX_JOULES - this.energyStored;
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }
    
    @Override
    public int getProcessTime()
    {
        return this.recipeTicks;
    }
    
    @Override
    public int getTimeRemaining()
    {
        return this.recipeTicks - this.processTicks;
    }
    
    @Override
    public int getCurrentProgress()
    {
        return this.processTicks;
    }

    @Override
    public int getSerialQuantity() 
    { return 0; }

    @Override
    public int getInputQuantity()
    { return 1; }

    @Override
    public int getOutputQuantity()
    { return 0; }

    @Override
    public EnumSet<ForgeDirection> getSerialDirections()
    { return null; }
}
