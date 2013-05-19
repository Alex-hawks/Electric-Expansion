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
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.ChargeUtils;
import electricexpansion.common.misc.WireMillRecipes;

public class TileEntityWireMill extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage, IEnergyTile, IEnergySink
{
    public static final double WATTS_PER_TICK = 500;
    public static final double TRANSFER_LIMIT = 1250;
    private int drawingTicks = 0;
    private double joulesStored = 0;
    public static double maxJoules = 150000;
    /**
     * The ItemStacks that hold the items currently being used in the wire mill;
     * 0 = battery; 1 = input; 2 = output;
     */
    private ItemStack[] inventory = new ItemStack[3];
    
    private int playersUsing = 0;
    public int orientation;
    private int targetID = 0;
    private int targetMeta = 0;
    
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
            
            IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, inputDirection.getOpposite());
            
            if (inputNetwork != null)
            {
                if (this.joulesStored < TileEntityWireMill.maxJoules)
                {
                    inputNetwork.startRequesting(this, Math.min(this.getMaxJoules() - this.getJoules(), TRANSFER_LIMIT) / this.getVoltage(), this.getVoltage());
                    ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
                    this.setJoules(this.joulesStored + electricityPack.getWatts());
                    
                    if (UniversalElectricity.isVoltageSensitive)
                    {
                        if (electricityPack.voltage > this.getVoltage())
                        {
                            this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
                        }
                    }
                }
                else
                {
                    inputNetwork.stopRequesting(this);
                }
            }
        }
        
        // The bottom slot is for portable
        // batteries
        if (this.inventory[0] != null && this.joulesStored < this.getMaxJoules())
        {
            if (this.inventory[0].getItem() instanceof IItemElectric)
            {
                IItemElectric electricItem = (IItemElectric) this.inventory[0].getItem();
                
                if (electricItem.getProvideRequest(this.inventory[0]).getWatts() > 0)
                {
                    double joulesReceived = electricItem
                            .onProvide(ElectricityPack.getFromWatts(Math.max(electricItem.getMaxJoules(this.inventory[0]) * 0.005, TRANSFER_LIMIT), electricItem.getVoltage(this.inventory[0])),
                                    this.inventory[0]).getWatts();
                    this.setJoules(this.joulesStored + joulesReceived);
                }
            }
            
            else if (this.inventory[0].getItem() instanceof IElectricItem)
            {
                IElectricItem item = (IElectricItem) this.inventory[0].getItem();
                if (item.canProvideEnergy(this.inventory[0]))
                {
                    double gain = ElectricItem.discharge(this.inventory[0], (int) ((int) (this.getMaxJoules() - this.getJoules()) * UniversalElectricity.TO_IC2_RATIO), 3, false, false)
                            * UniversalElectricity.IC2_RATIO;
                    this.setJoules(this.getJoules() + gain);
                }
            }
        }
        
        if (this.joulesStored >= WATTS_PER_TICK - 50 && !this.isDisabled())
        {
            // The left slot contains the item to
            // be processed
            if (this.inventory[1] != null && this.canDraw() && (this.drawingTicks == 0 || this.targetID != this.inventory[1].itemID || this.targetMeta != this.inventory[1].getItemDamage()))
            {
                this.targetID = this.inventory[1].itemID;
                this.targetMeta = this.inventory[1].getItemDamage();
                this.drawingTicks = this.getDrawingTime();
            }
            
            // Checks if the item can be processed
            // and if the drawing time left is
            // greater than 0, if so, then draw
            // the item.
            if (this.canDraw() && this.drawingTicks > 0)
            {
                this.drawingTicks--;
                
                // When the item is finished
                // drawing
                if (this.drawingTicks < 1)
                {
                    this.drawItem();
                    this.drawingTicks = 0;
                }
                this.joulesStored -= WATTS_PER_TICK;
            }
            else
            {
                this.drawingTicks = 0;
            }
        }
        
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 3 == 0 && this.playersUsing > 0)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
            }
        }
        
        this.joulesStored = Math.min(this.joulesStored, this.getMaxJoules());
        this.joulesStored = Math.max(this.joulesStored, 0d);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, this.drawingTicks, this.disabledTicks, this.joulesStored);
    }
    
    @Override
    public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.drawingTicks = dataStream.readInt();
            this.disabledTicks = dataStream.readInt();
            this.joulesStored = dataStream.readDouble();
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
            PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
        }
        this.playersUsing++;
    }
    
    @Override
    public void closeChest()
    {
        this.playersUsing--;
    }
    
    // Check all conditions and see if we can start drawing
    public boolean canDraw()
    {
        boolean canWork = false;
        ItemStack inputSlot = this.inventory[1];
        ItemStack outputSlot = this.inventory[2];
        if (inputSlot != null)
        {
            if (WireMillRecipes.INSTANCE.getDrawingResult(inputSlot) == null)
            {
                canWork = false;
            }
            else if (WireMillRecipes.INSTANCE.getDrawingResult(inputSlot) != null && outputSlot == null)
            {
                canWork = true;
            }
            else if (outputSlot != null)
            {
                String result = WireMillRecipes.stackSizeToOne(WireMillRecipes.INSTANCE.getDrawingResult(inputSlot)) + "";
                String output2 = WireMillRecipes.stackSizeToOne(outputSlot) + "";
                int maxSpaceForSuccess = Math.min(outputSlot.getMaxStackSize(), inputSlot.getMaxStackSize()) - WireMillRecipes.INSTANCE.getDrawingResult(inputSlot).stackSize;
                
                if (result.equals(output2) && !(outputSlot.stackSize <= maxSpaceForSuccess))
                {
                    canWork = false;
                }
                else if (result.equals(output2) && outputSlot.stackSize <= maxSpaceForSuccess)
                {
                    canWork = true;
                }
            }
        }
        
        return canWork;
    }
    
    /**
     * Turn item(s) from the wire mill source stack into the appropriate drawn
     * item(s) in the wire mill result stack
     */
    public void drawItem()
    {
        if (this.canDraw())
        {
            ItemStack resultItemStack = WireMillRecipes.INSTANCE.getDrawingResult(this.inventory[1]);
            
            if (this.inventory[2] == null)
            {
                this.inventory[2] = resultItemStack.copy();
            }
            else if (this.inventory[2].isItemEqual(resultItemStack))
            {
                this.inventory[2].stackSize = this.inventory[2].stackSize + resultItemStack.stackSize;
            }
            
            this.inventory[1].stackSize = this.inventory[1].stackSize - WireMillRecipes.INSTANCE.getInputQTY(this.inventory[1]);
            
            if (this.inventory[1].stackSize <= 0)
            {
                this.inventory[1] = null;
            }
        }
    }
    
    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.drawingTicks = par1NBTTagCompound.getInteger("drawingTicks");
        this.inventory = new ItemStack[this.getSizeInventory()];
        try
        {
            this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
        }
        catch (Exception e)
        {
        }
        
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
    
    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("drawingTicks", this.drawingTicks);
        par1NBTTagCompound.setDouble("joulesStored", this.getJoules());
        
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
            ItemStack var3;
            
            if (this.inventory[par1].stackSize <= par2)
            {
                var3 = this.inventory[par1];
                this.inventory[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.inventory[par1].splitStack(par2);
                
                if (this.inventory[par1].stackSize == 0)
                {
                    this.inventory[par1] = null;
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
        if (this.inventory[par1] != null)
        {
            ItemStack var2 = this.inventory[par1];
            this.inventory[par1] = null;
            return var2;
        }
        else
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
        return StatCollector.translateToLocal("tile.wiremill.name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
                : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }
    
    @Override
    public double getVoltage()
    {
        return 120;
    }
    
    /**
     * @return The amount of ticks required to draw this item
     */
    public int getDrawingTime()
    {
        if (this.inventory[1] != null)
        {
            if (WireMillRecipes.INSTANCE.getDrawingResult(this.inventory[1]) != null)
                return WireMillRecipes.INSTANCE.getDrawingTicks(this.inventory[1]);
        }
        return -1;
    }
    
    public int getDrawingTimeLeft()
    {
        return this.drawingTicks;
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
        return TileEntityWireMill.maxJoules;
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
        else
            return false;
    }
    
    @Override
    public int demandsEnergy()
    {
        return (int) ((this.getMaxJoules() - this.joulesStored) * UniversalElectricity.TO_IC2_RATIO);
    }
    
    @Override
    public int injectEnergy(Direction direction, int i)
    {
        double givenEnergy = i * UniversalElectricity.IC2_RATIO;
        double rejects = 0;
        double neededEnergy = this.getMaxJoules() - this.joulesStored;
        
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
        return Integer.MAX_VALUE;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction.ordinal() == this.getBlockMetadata() + 2;
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
                return ChargeUtils.UE.isFull(itemstack);
            case 1:
                return WireMillRecipes.INSTANCE.getDrawingResult(itemstack) != null;
                
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
                return ChargeUtils.UE.isEmpty(itemstack);
            case 2:
                return true;
                
            default:
                return false;
        }
    }
}
