package electricexpansion.common.tile;

import ic2.api.Direction;
import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.misc.InsulationRecipes;

public class TileEntityInsulatingMachine extends TileEntityElectricityRunnable 
implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage, IEnergyTile, IEnergySink
{
    public static final double WATTS_PER_TICK = 500.0D;
    public static final double TRANSFER_LIMIT = 1250.0D;
    private int processTicks = 0;
    private double joulesStored = 0.0D;
    private int recipeTicks = 0;
    public static double maxJoules = 150000.0D;
    
    private ItemStack[] inventory = new ItemStack[3];
    
    private int playersUsing = 0;
    public int orientation;
    private int baseID = 0;
    private int baseMeta = 0;
    private boolean initialized;
    
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
        
        if (this.initialized)
        {
            if (Loader.isModLoaded("IC2"))
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }
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
            
            IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile,
                    inputDirection.getOpposite());
            
            if (inputNetwork != null)
            {
                if (this.joulesStored < maxJoules)
                {
                    inputNetwork.startRequesting(
                            this,
                            Math.min(this.getMaxJoules(new Object[0]) - this.getJoules(new Object[0]), 1250.0D)
                            / this.getVoltage(new Object[0]), this.getVoltage(new Object[0]));
                    ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
                    this.setJoules(this.joulesStored + electricityPack.getWatts(), new Object[0]);
                    
                    if (UniversalElectricity.isVoltageSensitive)
                    {
                        if (electricityPack.voltage > this.getVoltage(new Object[0]))
                        {
                            this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2.0F, true);
                        }
                    }
                }
                else
                {
                    inputNetwork.stopRequesting(this);
                }
                
            }
            
        }
        
        if (this.inventory[0] != null && this.joulesStored < this.getMaxJoules())
        {
            if (this.inventory[0].getItem() instanceof IItemElectric)
            {
                IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();
                
                if (electricItem.getProvideRequest(this.inventory[0]).getWatts() > 0)
                {
                    double joulesReceived = electricItem.onProvide(
                            ElectricityPack.getFromWatts(
                                    Math.max(electricItem.getMaxJoules(this.inventory[0]) * 0.005D, 1250.0D),
                                    electricItem.getVoltage(this.inventory[0])), this.inventory[0]).getWatts();
                    this.setJoules(this.joulesStored + joulesReceived);
                }
            }
            else if (this.inventory[0].getItem() instanceof IElectricItem)
            {
                IElectricItem item = (IElectricItem) this.inventory[0].getItem();
                if (item.canProvideEnergy(this.inventory[0]))
                {
                    double gain = ElectricItem.discharge(this.inventory[0],
                            (int) ((int) (this.getMaxJoules() - this.getJoules()) * UniversalElectricity.TO_IC2_RATIO),
                            3, false, false) * UniversalElectricity.IC2_RATIO;
                    this.setJoules(this.getJoules() + gain);
                }
            }
        }
        
        
        if (this.joulesStored >= this.WATTS_PER_TICK - 50.0D && !this.isDisabled())
        {
            if (this.inventory[1] != null
                    && this.canProcess()
                    && (this.processTicks == 0 || this.baseID != this.inventory[1].itemID || this.baseMeta != this.inventory[1]
                            .getItemDamage()))
            {
                this.baseID = this.inventory[1].itemID;
                this.baseMeta = this.inventory[1].getItemDamage();
                this.processTicks = this.getProcessingTime();
                this.recipeTicks = this.getProcessingTime();
            }
            
            if (this.canProcess() && this.processTicks > 0)
            {
                this.processTicks -= 1;
                
                if (this.processTicks < 1)
                {
                    this.processItem();
                    this.processTicks = 0;
                }
                this.getClass();
                this.joulesStored -= 500.0D;
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
        
        this.joulesStored = Math.min(this.joulesStored, this.getMaxJoules(new Object[0]));
        this.joulesStored = Math.max(this.joulesStored, 0.0D);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(
                "ElecEx",
                this,
                new Object[] { Integer.valueOf(this.processTicks), Integer.valueOf(this.disabledTicks),
                        Double.valueOf(this.joulesStored), Integer.valueOf(this.recipeTicks) });
    }
    
    @Override
    public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet,
            EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.processTicks = dataStream.readInt();
            this.disabledTicks = dataStream.readInt();
            this.joulesStored = dataStream.readDouble();
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
        this.playersUsing += 1;
    }
    
    @Override
    public void closeChest()
    {
        this.playersUsing -= 1;
    }
    
    public boolean canProcess()
    {
        boolean canWork = false;
        ItemStack inputSlot = this.inventory[1];
        int outputSlot = this.inventory[2] != null ? this.inventory[2].stackSize : 0;
        if (inputSlot != null)
        {
            if (InsulationRecipes.INSTANCE.getProcessResult(inputSlot) > 0
                    && InsulationRecipes.INSTANCE.getProcessResult(inputSlot) + outputSlot <= 64)
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
            this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
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
        par1NBTTagCompound.setDouble("joulesStored", this.getJoules(new Object[0]));
        
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
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN || side == ForgeDirection.UP)
            return side.ordinal();
        return 2;
    }
    
    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
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
    
    public double getVoltage(Object... data)
    {
        return 120.0D;
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
    
    public int getProcessTimeLeft()
    {
        return this.processTicks;
    }
    
    public double getJoules(Object... data)
    {
        return this.joulesStored;
    }
    
    public void setJoules(double joules, Object... data)
    {
        this.joulesStored = joules;
    }
    
    public double getMaxJoules(Object... data)
    {
        return maxJoules;
    }
    
    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.initialized;
    }
    
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        if (direction.toForgeDirection() == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
            return true;
        
        return false;
    }
    
    @Override
    public int demandsEnergy()
    {
        return (int) ((this.getMaxJoules(new Object[0]) - this.joulesStored) * UniversalElectricity.TO_IC2_RATIO);
    }
    
    @Override
    public int injectEnergy(Direction direction, int i)
    {
        double givenEnergy = i * UniversalElectricity.IC2_RATIO;
        double rejects = 0.0D;
        double neededEnergy = this.getMaxJoules(new Object[0]) - this.joulesStored;
        
        if (givenEnergy < neededEnergy)
        {
            this.joulesStored += givenEnergy;
        }
        else if (givenEnergy > neededEnergy)
        {
            this.joulesStored += neededEnergy;
            rejects = givenEnergy - neededEnergy;
        }
        
        return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
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
    public double getJoules()
    {
        return this.joulesStored;
    }
    
    @Override
    public void setJoules(double joules)
    {
        this.joulesStored = joules;
    }
    
    @Override
    public double getMaxJoules()
    {
        return TileEntityInsulatingMachine.maxJoules;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
    
    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        if (i == 1)
            return InsulationRecipes.INSTANCE.getProcessResult(itemstack) >= 1;
            return false;
    }
}
