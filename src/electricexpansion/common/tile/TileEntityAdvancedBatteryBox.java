package electricexpansion.common.tile;

import ic2.api.Direction;
import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
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
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.modifier.IModifier;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import electricexpansion.common.ElectricExpansion;

public class TileEntityAdvancedBatteryBox extends TileEntityElectricityStorage
        implements IRedstoneProvider, IPacketReceiver, ISidedInventory,
        IPeripheral, IEnergySink, IEnergySource
{
    private static final double BASE_OUTPUT = 50000;
    private ItemStack[] containingItems = new ItemStack[5];
    private int playersUsing = 0;
    
    @Override
    public void initiate()
    {
        if (Loader.isModLoaded("IC2"))
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
        
        if (this.ticks > 0L)
            if (Loader.isModLoaded("IC2"))
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!this.isDisabled())
        {
            if (this.containingItems[0] != null && this.getJoules() > 0.0D)
            {
                if (this.containingItems[0].getItem() instanceof IItemElectric)
                {
                    this.setJoules(this.getJoules()
                            - ElectricItemHelper.chargeItem(
                                    this.containingItems[0], this.getJoules(),
                                    this.getVoltage()));
                }
                else if (this.containingItems[0].getItem() instanceof IElectricItem)
                {
                    double sent = ElectricItem
                            .charge(this.containingItems[0],
                                    (int) (this.getJoules() * UniversalElectricity.TO_IC2_RATIO),
                                    3, false, false)
                            * UniversalElectricity.IC2_RATIO;
                    this.setJoules(this.getJoules() - sent);
                }
                
            }
            
            if (this.containingItems[1] != null
                    && this.getJoules() < this.getMaxJoules())
            {
                if (this.containingItems[1].getItem() instanceof IItemElectric)
                {
                    IItemElectric electricItem = (IItemElectric) this.containingItems[1]
                            .getItem();
                    
                    if (electricItem.getProvideRequest(this.containingItems[1])
                            .getWatts() > 0)
                    {
                        ElectricityPack packRecieved = electricItem.onProvide(
                                ElectricityPack.getFromWatts(
                                        this.getMaxJoules() - this.getJoules(),
                                        this.getVoltage()),
                                this.containingItems[1]);
                        this.setJoules(this.getJoules()
                                + packRecieved.getWatts());
                    }
                    
                }
                else if (this.containingItems[1].getItem() instanceof IElectricItem)
                {
                    IElectricItem item = (IElectricItem) this.containingItems[1]
                            .getItem();
                    if (item.canProvideEnergy())
                    {
                        double gain = ElectricItem
                                .discharge(
                                        this.containingItems[1],
                                        (int) ((int) (this.getMaxJoules() - this
                                                .getJoules()) * UniversalElectricity.TO_IC2_RATIO),
                                        3, false, false)
                                * UniversalElectricity.IC2_RATIO;
                        this.setJoules(this.getJoules() + gain);
                    }
                }
            }
            
            ForgeDirection outputDirection = ForgeDirection.getOrientation(this
                    .getBlockMetadata() + 2);
            TileEntity outputTile = VectorHelper.getTileEntityFromSide(
                    this.worldObj, new Vector3(this), outputDirection);
            
            if (!this.worldObj.isRemote)
            {
                TileEntity inputTile = VectorHelper.getTileEntityFromSide(
                        this.worldObj, new Vector3(this),
                        outputDirection.getOpposite());
                
                IElectricityNetwork inputNetwork = ElectricityNetworkHelper
                        .getNetworkFromTileEntity(inputTile,
                                outputDirection.getOpposite());
                IElectricityNetwork outputNetwork = ElectricityNetworkHelper
                        .getNetworkFromTileEntity(outputTile, outputDirection);
                
                if (outputNetwork != null && inputNetwork != outputNetwork)
                {
                    double outputWatts = Math.min(
                            outputNetwork.getRequest(new TileEntity[] { this })
                                    .getWatts(), Math.min(this.getJoules(),
                                    this.getOutputCap()));
                    
                    if (this.getJoules() > 0.0D && outputWatts > 0.0D)
                    {
                        outputNetwork.startProducing(this,
                                outputWatts / this.getVoltage(),
                                this.getVoltage());
                        this.setJoules(this.getJoules() - outputWatts);
                    }
                    else
                    {
                        outputNetwork.stopProducing(this);
                    }
                }
                
            }
            
            if (this.getJoules() > 0.0D)
                if (Loader.isModLoaded("IC2"))
                    if (this.getJoules() >= 128.0D * UniversalElectricity.IC2_RATIO)
                    {
                        this.setJoules(this.getJoules() - this.sendEnergy(128)
                                * UniversalElectricity.IC2_RATIO);
                    }
        }
        
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 3L == 0L && this.playersUsing > 0)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(),
                        this.worldObj, new Vector3(this), 12.0D);
            }
        }
    }
    
    @Override
    protected EnumSet<ForgeDirection> getConsumingSides()
    {
        return EnumSet.of(ForgeDirection.getOrientation(
                this.getBlockMetadata() + 2).getOpposite());
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(
                ElectricExpansion.CHANNEL,
                this,
                new Object[] { Double.valueOf(this.getJoules()),
                        Integer.valueOf(this.disabledTicks) });
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type,
            Packet250CustomPayload packet, EntityPlayer player,
            ByteArrayDataInput dataStream)
    {
        try
        {
            this.setJoules(dataStream.readDouble());
            this.disabledTicks = dataStream.readInt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];
        
        for (int var3 = 0; var3 < var2.tagCount(); var3++)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");
            
            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack
                        .loadItemStackFromNBT(var4);
            }
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();
        
        for (int var3 = 0; var3 < this.containingItems.length; var3++)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        
        par1NBTTagCompound.setTag("Items", var2);
    }
    
    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        if (side == ForgeDirection.DOWN)
            return 1;
        
        if (side == ForgeDirection.UP)
            return 1;
        
        return 0;
    }
    
    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
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
            if (this.containingItems[par1].stackSize <= par2)
            {
                ItemStack var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            
            ItemStack var3 = this.containingItems[par1].splitStack(par2);
            
            if (this.containingItems[par1].stackSize == 0)
            {
                this.containingItems[par1] = null;
            }
            
            return var3;
        }
        
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
        
        return null;
    }
    
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;
        
        if (par2ItemStack != null
                && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal("tile.advbatbox.name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
    @Override
    public double getMaxJoules()
    {
        int slot1 = 0;
        int slot2 = 0;
        int slot3 = 0;
        
        if (this.containingItems[2] != null
                && this.containingItems[2].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[2].getItem())
                        .getName(this.containingItems[2]) == "Capacity")
        {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null
                && this.containingItems[3].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[3].getItem())
                        .getName(this.containingItems[3]) == "Capacity")
        {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null
                && this.containingItems[4].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[4].getItem())
                        .getName(this.containingItems[4]) == "Capacity")
        {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        return 5000000 + slot1 + slot2 + slot3;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
                this.zCoord) == this;
    }
    
    @Override
    public boolean isPoweringTo(ForgeDirection side)
    {
        return this.getJoules() >= this.getMaxJoules();
    }
    
    @Override
    public boolean isIndirectlyPoweringTo(ForgeDirection side)
    {
        return this.isPoweringTo(side);
    }
    
    @Override
    public double getVoltage()
    {
        return 120.0D * this.getVoltageModifier("VoltageModifier");
    }
    
    @Override
    public void onReceive(ElectricityPack electricityPack)
    {
        if (UniversalElectricity.isVoltageSensitive)
        {
            if (electricityPack.voltage > this.getInputVoltage())
            {
                this.worldObj.createExplosion(null, this.xCoord, this.yCoord,
                        this.zCoord, 1.5F, true);
                return;
            }
        }
        
        this.setJoules(this.getJoules() + electricityPack.getWatts());
    }
    
    public double getInputVoltage()
    {
        return Math.max(
                this.getVoltage(),
                Math.max(
                        120.0D,
                        this.getVoltageModifier("InputVoltageModifier")
                                * this.getVoltageModifier("VoltageModifier")
                                * 120.0D));
    }
    
    private double getVoltageModifier(String type)
    {
        double slot1 = 1.0D;
        double slot2 = 1.0D;
        double slot3 = 1.0D;
        
        if (this.containingItems[2] != null
                && this.containingItems[2].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[2].getItem())
                        .getName(this.containingItems[2]) == type)
        {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null
                && this.containingItems[3].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[3].getItem())
                        .getName(this.containingItems[3]) == type)
        {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null
                && this.containingItems[4].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[4].getItem())
                        .getName(this.containingItems[4]) == type)
        {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        if (slot1 < 0.0D)
        {
            slot1 = 1.0D / (slot1 * -1.0D);
        }
        if (slot2 < 0.0D)
        {
            slot2 = 1.0D / (slot2 * -1.0D);
        }
        if (slot3 < 0.0D)
        {
            slot3 = 1.0D / (slot3 * -1.0D);
        }
        return slot1 * slot2 * slot3;
    }
    
    private double getOutputCap()
    {
        double slot1 = 0;
        double slot2 = 0;
        double slot3 = 0;
        
        if (this.containingItems[2] != null
                && this.containingItems[2].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[2].getItem())
                        .getName(this.containingItems[2]) == "Unlimiter")
        {
            slot1 = ((IModifier) this.containingItems[2].getItem())
                    .getEffectiveness(this.containingItems[2]);
        }
        if (this.containingItems[3] != null
                && this.containingItems[3].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[3].getItem())
                        .getName(this.containingItems[3]) == "Unlimiter")
        {
            slot2 = ((IModifier) this.containingItems[3].getItem())
                    .getEffectiveness(this.containingItems[3]);
        }
        if (this.containingItems[4] != null
                && this.containingItems[4].getItem() instanceof IModifier
                && ((IModifier) this.containingItems[4].getItem())
                        .getName(this.containingItems[4]) == "Unlimiter")
        {
            slot3 = ((IModifier) this.containingItems[4].getItem())
                    .getEffectiveness(this.containingItems[4]);
        }
        
        return (100 + slot1) * (100 + slot2) * (100 + slot3) / 1000000
                * TileEntityAdvancedBatteryBox.BASE_OUTPUT;
    }
    
    @Override
    public String getType()
    {
        return this.getInvName().replaceAll(" ", "");
    }
    
    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getVoltage", "getEnergy", "isFull" };
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, int method,
            Object[] arguments) throws Exception
    {
        final int getVoltage = 0;
        final int getWattage = 1;
        final int isFull = 2;
        
        switch (method)
        {
            case getVoltage:
                return new Object[] { Double.valueOf(this.getVoltage()) };
            case getWattage:
                return new Object[] { Double.valueOf(this.getJoules()) };
            case isFull:
                return new Object[] { Boolean.valueOf(this.getJoules() >= this
                        .getMaxJoules()) };
        }
        throw new Exception("Function unimplemented");
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
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return this.getConsumingSides().contains(direction.toForgeDirection());
    }
    
    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.ticks > 0L;
    }
    
    @Override
    public int demandsEnergy()
    {
        return (int) (this.getRequest().getWatts() * UniversalElectricity.TO_IC2_RATIO);
    }
    
    @Override
    public int injectEnergy(Direction direction, int i)
    {
        double givenElectricity = i * UniversalElectricity.IC2_RATIO;
        double rejects = 0.0D;
        
        if (givenElectricity > this.getRequest().getWatts())
        {
            rejects = givenElectricity - this.getRequest().getWatts();
        }
        
        this.onReceive(ElectricityPack.getFromWatts(givenElectricity,
                this.getVoltage()));
        
        return (int) (rejects * UniversalElectricity.TO_IC2_RATIO);
    }
    
    @Override
    public int getMaxSafeInput()
    {
        return 2048;
    }
    
    @Override
    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return this.getConsumingSides().contains(
                direction.toForgeDirection().getOpposite());
    }
    
    @Override
    public int getMaxEnergyOutput()
    {
        return 128;
    }
    
    public int sendEnergy(int send)
    {
        EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, send);
        MinecraftForge.EVENT_BUS.post(event);
        return event.amount;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return this.getBlockMetadata() + 2 == direction.ordinal()
                || this.getBlockMetadata() + 2 == direction.getOpposite()
                        .ordinal();
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