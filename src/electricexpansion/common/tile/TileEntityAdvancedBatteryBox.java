package electricexpansion.common.tile;

import static electricexpansion.common.misc.EnumPowerConversion.ENERGY_UNIT;
import static electricexpansion.common.misc.EnumPowerConversion.JOULES_UE;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IEnergyStorage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

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
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityConductor;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import electricexpansion.api.IModifier;
import electricexpansion.api.hive.IHiveMachine;
import electricexpansion.api.hive.IHiveNetwork;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.items.ItemLinkCard;
import electricexpansion.common.misc.EnumAdvBattBoxMode;
import electricexpansion.common.misc.EnumPowerConversion;
import electricexpansion.common.misc.PowerConversionUtils;
import electricexpansion.common.misc.PowerConversionUtils.GenericPack;
import electricexpansion.common.misc.PowerUtils;

public class TileEntityAdvancedBatteryBox extends TileEntityElectrical
implements IPacketReceiver, ISidedInventory, IPeripheral, IEnergySink, IEnergySource, IEnergyStorage, IHiveMachine
{
    public static final float               BASE_OUTPUT     = 20.000F;
    public static final float               BASE_VOLTAGE    = 0.120F;
    public static final float               BASE_STORAGE    = 4_000F;
    public static final int                 UPGRADE_SIZE    = 3;
    public static final int                 OTHER_SIZE      = 3;
    
    private ItemStack[]                     upgrades        = new ItemStack[UPGRADE_SIZE];
    private ItemStack[]                     inventory       = new ItemStack[OTHER_SIZE];
    
    public final Set<EntityPlayer>          playersUsing    = new HashSet<EntityPlayer>();
    
    private EnumAdvBattBoxMode              inputMode       = EnumAdvBattBoxMode.OFF;
    private EnumAdvBattBoxMode              outputMode      = EnumAdvBattBoxMode.OFF;
    
    private ForgeDirection                  inputDir        = ForgeDirection.UP;
    private ForgeDirection                  outputDir       = ForgeDirection.DOWN;
    private TileEntity                      inputTile;
    private TileEntity                      outputTile;
    
    private transient IElectricityNetwork   inputNetwork;
    private transient IElectricityNetwork   outputNetwork;
    
    private IHiveNetwork                    hiveNetwork;
    
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
        
        switch (this.outputMode)
        {
            case BASIC:
                this.sendElectricalEnergy();
                break;
            case PNEUMATIC:
                this.sendPneumaticEnergy();
                break;
            case QUANTUM:
                this.sendQuantumEnergy();
                break;
            case MEKANISM:
                this.sendMekanismEnergy();
                break;
            case FACTORIZATION:
                this.sendFzEnergy();
                break;
            case UNIVERSAL:
                this.sendUniversalEnergy();
                break;
            default:
                break;
        }
        
        switch (this.inputMode)
        {
            case BASIC:
                this.drainElectricalEnergy();
                break;
            case PNEUMATIC:
                this.drainPneumaticEnergy();
                break;
            case MEKANISM:
                this.drainMekanismEnergy();
                break;
            case FACTORIZATION:
                this.drainFzEnergy();
                break;
            case UNIVERSAL:
                this.drainUniversalEnergy();
                break;
            default:
                break;
        }
        
        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 3L == 0L && this.playersUsing.size() > 0)
            {
                PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
            }
        }
        
        if (this.inventory[2] != null && this.inventory[2].getItem() instanceof ItemLinkCard)
        {
            ItemLinkCard card = (ItemLinkCard) this.inventory[2].getItem();
            card.getOrCreateLinkData(this.inventory[2], this);
        }
    }
    
    public void refreshConnections()
    {
        this.outputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDir);
        this.outputNetwork = ElectricityHelper.getNetworkFromTileEntity(outputTile, outputDir);
        
        this.inputTile = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDir);
        this.inputNetwork = ElectricityHelper.getNetworkFromTileEntity(inputTile, inputDir);
    }
    
    private boolean sendElectricalItemEnergy()
    {
        // Batteries (UE, then IC2. Will not call both charge methods)
        if (this.inventory[0] != null)
        {
            if (this.inventory[0].getItem() instanceof IItemElectric)
            {
                this.setEnergyStored(this.getEnergyStored() - (PowerUtils.UE.charge(this.inventory[0], PowerConversionUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap(), this.getEnergyStored()) / this.getVoltage(), this.getVoltage()), (int) Math.min((this.getMaxEnergyStored() % BASE_STORAGE) - 1, 4))).toUEWatts());
                return true;
            }
            else if (this.inventory[0].getItem() instanceof IElectricItem)
            {
                this.setEnergyStored(this.getEnergyStored() - (PowerUtils.IC2.charge(this.inventory[0], PowerConversionUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap(), this.getEnergyStored()) / this.getVoltage(), this.getVoltage()), (int) Math.min((this.getMaxEnergyStored() % BASE_STORAGE) - 1, 4))).toUEWatts());
                return true;
            }
        }
        return false;
    }
    
    private boolean sendElectricalEnergy()
    {
        if (sendElectricalItemEnergy())
            return true;
        
        // Cables (UE)
        {
            refreshConnections();
            
            if (this.produceUE(this.getOutputDir()))
                return true;
        }
        return false;
    }
    
    private boolean sendPneumaticEnergy()
    {
        return false;
    }
    
    private boolean sendQuantumEnergy()
    {
        ItemStack is = this.inventory[5];
        if (is != null && is.getItem() instanceof ItemLinkCard)
        {
            ItemLinkCard item = (ItemLinkCard) is.getItem();
            if (item.getHasLinkData(is) && !ItemLinkCard.isDataEqual(item.getOrCreateLinkData(is, this), this))
            {
                NBTTagCompound linkData = item.getOrCreateLinkData(is, this);
                int dimensionId = linkData.getInteger("dimension");
                int x = linkData.getInteger("x");
                int y = linkData.getInteger("y");
                int z = linkData.getInteger("z");
                
                if (this.worldObj.provider.dimensionId == dimensionId || this.hasUpgrade("CrossDimension"))
                {
                    if (DimensionManager.getWorld(dimensionId).getBlockTileEntity(x, y, z) instanceof TileEntityAdvancedBatteryBox)
                    {
                        TileEntityAdvancedBatteryBox target = (TileEntityAdvancedBatteryBox) DimensionManager.getWorld(dimensionId).getBlockTileEntity(x, y, z);
                        if (target.getInputMode() == EnumAdvBattBoxMode.QUANTUM)
                        {
                            GenericPack thisRequest = PowerConversionUtils.INSTANCE.new UEElectricPack(Math.min(this.getOutputCap() / 2, this.getEnergyStored()) / this.getVoltage(), this.getVoltage());
                            GenericPack targetRequest = PowerConversionUtils.INSTANCE.new UEElectricPack(target.getRequest(ForgeDirection.UNKNOWN));
                            
                            GenericPack actualSent = PowerUtils.CommonUtil.getSmallest(thisRequest, targetRequest);
                            
                            target.receiveElectricity(actualSent.toUEPack(Math.min(this.getVoltage(), target.getVoltage()), ElectricUnit.VOLTAGE), true);
                            this.setEnergyStored(this.getEnergyStored() - actualSent.toUEWatts() * 2);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean sendMekanismEnergy()
    {
        return false;
    }
    
    private boolean sendFzEnergy()
    {
        return false;
    }
    
    private boolean sendUniversalEnergy()
    {
        if (sendElectricalEnergy())
            return true;
        if (sendPneumaticEnergy())
            return true;
        if (sendQuantumEnergy())
            return true;
        if (sendMekanismEnergy())
            return true;
        if (sendFzEnergy())
            return true;
        return false;
    }
    
    private boolean drainElectricalItemEnergy()
    {
        if (this.inventory[1] != null)
        {
            if (this.inventory[1].getItem() instanceof IItemElectric)
            {
                this.setEnergyStored(this.getEnergyStored() + (PowerUtils.UE.discharge(this.inventory[1], PowerConversionUtils.INSTANCE.new UEElectricPack(this.getOutputCap() / this.getVoltage(), this.getVoltage()), (int) Math.min((this.getMaxEnergyStored() % BASE_STORAGE) - 1, 4))).toUEWatts());
                return true;
            }
            else if (this.inventory[1].getItem() instanceof IElectricItem)
                
            {
                this.setEnergyStored(this.getEnergyStored() + (PowerUtils.IC2.discharge(this.inventory[1], PowerConversionUtils.INSTANCE.new UEElectricPack(this.getOutputCap() / this.getVoltage(), this.getVoltage()), (int) Math.min((this.getMaxEnergyStored() % BASE_STORAGE) - 1, 4))).toUEWatts());
                return true;
            }
        }
        return false;
    }
    
    private boolean drainElectricalEnergy()
    {
        if (drainElectricalItemEnergy())
            return true;
        return false;
    }
    
    private boolean drainPneumaticEnergy()
    {
        return false;
    }
    
    private boolean drainMekanismEnergy()
    {
        return false;
    }
    
    private boolean drainFzEnergy()
    {
        return false;
    }
    
    private boolean drainUniversalEnergy()
    {
        if (drainElectricalEnergy())
            return true;
        if (drainPneumaticEnergy())
            return true;
        if (drainMekanismEnergy())
            return true;
        if (drainFzEnergy())
            return true;
        return false;
    }
    
    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        if (this.inputMode != EnumAdvBattBoxMode.OFF && this.inputMode != EnumAdvBattBoxMode.QUANTUM)
            return EnumSet.of(inputDir);
        return EnumSet.noneOf(ForgeDirection.class);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.getPacket(ElectricExpansion.CHANNEL, this, new Object[]
            {
             Float.valueOf(this.getEnergyStored()),
             Byte.valueOf((byte) this.inputMode.ordinal()),
             Byte.valueOf((byte) this.outputMode.ordinal()),
             Byte.valueOf((byte) this.inputDir.ordinal()),
             Byte.valueOf((byte) this.outputDir.ordinal())
            });
    }
    
    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                this.setEnergyStored(dataStream.readFloat());
                this.inputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
                this.outputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
                this.inputDir = ForgeDirection.getOrientation(dataStream.readByte());
                this.outputDir = ForgeDirection.getOrientation(dataStream.readByte());
                
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.blockType.blockID);
                this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 0x03);
            } catch (Exception e)
            {
            }
        }
        else
        {
            try
            {
                ForgeDirection newInputDir = ForgeDirection.getOrientation(dataStream.readByte());
                ForgeDirection newOutputDir = ForgeDirection.getOrientation(dataStream.readByte());
                EnumAdvBattBoxMode newInputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
                EnumAdvBattBoxMode newOutputMode = EnumAdvBattBoxMode.fromValue(dataStream.readByte());
                
                if (this.inputDir != newInputDir)
                    this.setInputDir(newInputDir);
                if (this.outputDir != newOutputDir)
                    this.setOutputDir(newOutputDir);
                
                if (this.inputMode != newInputMode)
                    this.setInputMode(newInputMode);
                if (this.outputMode != newOutputMode)
                    this.setOutputMode(newOutputMode);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[OTHER_SIZE];
        this.upgrades = new ItemStack[UPGRADE_SIZE];
        
        for (int i = 0; i < var2.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) var2.tagAt(i);
            byte var5 = tag.getByte("Slot");
            
            if (var5 >= 0 && var5 < this.inventory.length)
            {
                this.inventory[var5] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
        
        this.outputMode = EnumAdvBattBoxMode.fromValue(par1NBTTagCompound.getByte("outputMode"));
        this.inputMode = EnumAdvBattBoxMode.fromValue(par1NBTTagCompound.getByte("inputMode"));
        
        this.outputDir = ForgeDirection.getOrientation(par1NBTTagCompound.getByte("output"));
        this.inputDir = ForgeDirection.getOrientation(par1NBTTagCompound.getByte("input"));
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList var2 = new NBTTagList();
        
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
        
        par1NBTTagCompound.setByte("outputMode", (byte) this.outputMode.ordinal());
        par1NBTTagCompound.setByte("inputMode", (byte) this.inputMode.ordinal());
        
        par1NBTTagCompound.setByte("output", (byte) this.outputDir.ordinal());
        par1NBTTagCompound.setByte("input", (byte) this.inputDir.ordinal());
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
        return StatCollector.translateToLocal("tile.advbatbox.name");
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }
    
    @Override
    public float getMaxEnergyStored()
    {
        float energy = BASE_STORAGE;
        
        for (int i = 0; i < this.upgrades.length; i++)
        {
            if (this.inventory[i] != null && this.inventory[i].getItem() instanceof IModifier && ((IModifier) this.inventory[i].getItem()).getType(this.inventory[i]) == "Capacity")
            {
                energy += ((IModifier) this.inventory[i].getItem()).getEffectiveness(this.inventory[i]);
            }
        }
        return energy;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }
    
    @Override
    public float getVoltage()
    {
        return (float) (BASE_VOLTAGE * this.getVoltageModifier("VoltageModifier"));
    }
    
    @Override
    public float receiveElectricity(ForgeDirection from, ElectricityPack pack, boolean isReal)
    {
        float toReturn = 0;
        
        if (UniversalElectricity.isVoltageSensitive)
        {
            if (pack.voltage > this.getInputVoltage())
            {
                this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5F, true);
                toReturn = pack.getWatts();
            }
            else 
            {
                toReturn = Math.min(pack.getWatts(), this.getOutputCap());
            }
        }
        
        if (isReal)
            this.setEnergyStored(this.getEnergyStored() + pack.getWatts());
        
        return toReturn;
    }
    
    public float getInputVoltage()
    {
        return PowerUtils.expandVoltageRange((float) Math.max(this.getVoltage(), Math.max(BASE_VOLTAGE, this.getVoltageModifier("InputVoltageModifier") * this.getVoltageModifier("VoltageModifier") * BASE_VOLTAGE)));
    }
    
    private float getVoltageModifier(String type)
    {
        float multiplier = 1;
        
        for (int i = 0; i < this.upgrades.length; i++)
        {
            if (this.inventory[i] != null && this.inventory[i].getItem() instanceof IModifier && ((IModifier) this.inventory[i].getItem()).getType(this.inventory[i]).equalsIgnoreCase(type))
            {
                multiplier *= ((IModifier) this.inventory[i].getItem()).getEffectiveness(this.inventory[i]);
            }
        }
        return multiplier;
    }
    
    private float getOutputCap()
    {
        float cap = BASE_OUTPUT;
        
        for (int i = 0; i < this.upgrades.length; i++)
        {
            if (this.inventory[i] != null && this.inventory[i].getItem() instanceof IModifier && "Unlimiter".equalsIgnoreCase(((IModifier) this.inventory[i].getItem()).getType(this.inventory[i])))
            {
                cap *= 100 + ((IModifier) this.inventory[i].getItem()).getEffectiveness(this.inventory[i]);
                cap /= 100;
            }
        }
        return cap;
  }
    
    @Override
    public String getType()
    {
        return this.getInvName().replaceAll(" ", "");
    }
    
    @Override
    public String[] getMethodNames()
    {
        return new String[]
            { "getVoltage", "getEnergy", "isFull" };
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        final int getVoltage = 0;
        final int getWattage = 1;
        final int isFull = 2;
        
        switch (method)
        {
            case getVoltage:
                return new Object[]
                    { Double.valueOf(this.getVoltage()) };
            case getWattage:
                return new Object[]
                    { Double.valueOf(this.getEnergyStored()) };
            case isFull:
                return new Object[]
                    { Boolean.valueOf(this.getEnergyStored() >= this.getMaxEnergyStored()) };
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
    public void detach(IComputerAccess computer) { }
    
    @Override
    public double demandedEnergyUnits()
    {
        return EnumPowerConversion.JOULES_UE.convertToOtherUnit(ENERGY_UNIT, this.getMaxEnergyStored() - this.getEnergyStored());
    }
    
    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        double givenElectricity = EnumPowerConversion.ENERGY_UNIT.convertToOtherUnit(JOULES_UE, amount);
        double rejects = 0.0D;
        
        if (givenElectricity > this.getRequest(ForgeDirection.UNKNOWN))
        {
            rejects = givenElectricity - this.getRequest(ForgeDirection.UNKNOWN);
        }
        
        this.receiveElectricity(directionFrom, ElectricityPack.getFromWatts((float) givenElectricity, this.getVoltage()), true);
        
        return EnumPowerConversion.JOULES_UE.convertToOtherUnit(ENERGY_UNIT, rejects);
    }
    
    @Override
    public int getMaxSafeInput()
    {
        return 2048;
    }
    
    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        if (direction == this.inputDir)
        {
            return this.inputMode != EnumAdvBattBoxMode.OFF && this.inputMode != EnumAdvBattBoxMode.QUANTUM;
        } else if (direction == this.outputDir)
        {
            return this.outputMode != EnumAdvBattBoxMode.OFF && this.outputMode != EnumAdvBattBoxMode.QUANTUM;
        }
        return false;
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
    
    public boolean isLinkCardValid(ItemStack is)
    {
        if (is != null && is.getItem() instanceof ItemLinkCard)
        {
            NBTTagCompound link = ((ItemLinkCard) is.getItem()).getOrCreateLinkData(is, this);
            if (ItemLinkCard.isDataEqual(link, this))
            {
                return false;
            }
            return true;
        }
        else
            return false;
    }
    
    public void setLinkCard(ItemStack is)
    {
        this.inventory[5] = is;
    }
    
    public EnumAdvBattBoxMode getInputMode()
    {
        return this.inputMode;
    }
    
    public EnumAdvBattBoxMode getOutputMode()
    {
        return this.outputMode;
    }
    
    public ArrayList<EnumAdvBattBoxMode> getAvailableModes()
    {
        ArrayList<EnumAdvBattBoxMode> toReturn = new ArrayList<EnumAdvBattBoxMode>();
        toReturn.add(EnumAdvBattBoxMode.OFF);
        toReturn.add(EnumAdvBattBoxMode.BASIC);
        if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
        {
            // toReturn.add(EnumAdvBattBoxModeMode.PNEUMATIC);
        }
        if (this.hasUpgrade("Quantum"))
        {
            toReturn.add(EnumAdvBattBoxMode.QUANTUM);
        }
        if (Loader.isModLoaded("Mekanism") && this.hasUpgrade("Mekansim"))
        {
            // toReturn.add(EnumAdvBattBoxModeMod.MEKANISM);
        }
        if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
        {
            // toReturn.add(EnumAdvBattBoxModeMod.FACTORIZATION);
        }
        if ((!Loader.isModLoaded("Mekanism") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
        {
            // toReturn.add(EnumAdvBattBoxModeMod.UNIVERSAL);
        }
        return toReturn;
    }
    
    public void setInputMode(EnumAdvBattBoxMode newMode)
    {
        if (this.worldObj.isRemote)
        {
            this.sendUpdateToServer(inputDir, outputDir, newMode, outputMode);
        }
        else
        {
            switch (newMode)
            {
                default:
                    this.inputMode = EnumAdvBattBoxMode.OFF;
                    break;
                case BASIC:
                    this.inputMode = newMode;
                    break;
                case PNEUMATIC:
                    if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
                        this.inputMode = newMode;
                    break;
                case QUANTUM:
                    if (this.hasUpgrade("Quantum"))
                        this.inputMode = newMode;
                    break;
                case MEKANISM:
                    if (Loader.isModLoaded("Mekanism|Core") && this.hasUpgrade("Mekansim"))
                        this.inputMode = newMode;
                    break;
                case FACTORIZATION:
                    if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
                        this.inputMode = newMode;
                    break;
                case UNIVERSAL:
                    if ((!Loader.isModLoaded("Mekanism|Core") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
                        this.inputMode = newMode;
            }
            refreshConnections();
            if (inputTile instanceof TileEntityConductor)
            {
                ((TileEntityConductor) inputTile).refresh();
            }
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
        }
    }
    
    public void setOutputMode(EnumAdvBattBoxMode newMode)
    {
        if (this.worldObj.isRemote)
        {
            this.sendUpdateToServer(inputDir, outputDir, inputMode, newMode);
        }
        else
        {
            switch (newMode)
            {
                default:
                    this.outputMode = EnumAdvBattBoxMode.OFF;
                    break;
                case BASIC:
                    this.outputMode = newMode;
                    break;
                case PNEUMATIC:
                    if ((Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic"))
                        this.outputMode = newMode;
                    break;
                case QUANTUM:
                    if (this.hasUpgrade("Quantum"))
                        this.outputMode = newMode;
                    break;
                case MEKANISM:
                    if (Loader.isModLoaded("Mekanism") && this.hasUpgrade("Mekansim"))
                        this.outputMode = newMode;
                    break;
                case FACTORIZATION:
                    if (Loader.isModLoaded("factorization") && this.hasUpgrade("Factorization"))
                        this.outputMode = newMode;
                    break;
                case UNIVERSAL:
                    if ((!Loader.isModLoaded("Mekanism") || this.hasUpgrade("Mekansim")) && (!Loader.isModLoaded("factorization") || this.hasUpgrade("Factorization")) && (!(Loader.isModLoaded("BuildCraft|Energy") || Loader.isModLoaded("ThermalExpansion")) && this.hasUpgrade("Pnematic")))
                        this.outputMode = newMode;
            }
            refreshConnections();
            if (outputTile instanceof TileEntityConductor)
            {
                ((TileEntityConductor) outputTile).refresh();
            }
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
        }
    }
    
    public boolean hasUpgrade(String upgrade)
    {
        if (this.inventory[2] != null && this.inventory[2].getItem() instanceof IModifier)
        {
            String type = ((IModifier) this.inventory[2].getItem()).getType(this.inventory[2]);
            if (type != null && type.equals(upgrade))
                return true;
        }
        
        if (this.inventory[3] != null && this.inventory[3].getItem() instanceof IModifier)
        {
            String type = ((IModifier) this.inventory[3].getItem()).getType(this.inventory[3]);
            if (type != null && type.equals(upgrade))
                return true;
        }
        
        if (this.inventory[4] != null && this.inventory[4].getItem() instanceof IModifier)
        {
            String type = ((IModifier) this.inventory[4].getItem()).getType(this.inventory[4]);
            if (type != null && type.equals(upgrade))
                return true;
        }
        
        return false;
    }
    
    public void setInputDir(ForgeDirection newDirection)
    {
        if (this.worldObj.isRemote)
        {
            this.sendUpdateToServer(newDirection, outputDir, inputMode, outputMode);
        }
        else
        {
            if (newDirection != this.inputDir)
            {
                refreshConnections();
                if (inputTile instanceof TileEntityConductor)
                {
                    ((TileEntityConductor) inputTile).refresh();
                }
                this.inputDir = newDirection;
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
            }
            
        }
    }
    
    public void setOutputDir(ForgeDirection newDirection)
    {
        if (this.worldObj.isRemote)
        {
            this.sendUpdateToServer(inputDir, newDirection, inputMode, outputMode);
        }
        else
        {
            if (newDirection != this.outputDir)
            {
                refreshConnections();
                if (outputTile instanceof TileEntityConductor)
                {
                    ((TileEntityConductor) outputTile).refresh();
                }
                this.outputDir = newDirection;
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
            }
        }
    }
    
    private void sendUpdateToServer(ForgeDirection inputDir, ForgeDirection outputDir, EnumAdvBattBoxMode inputMode, EnumAdvBattBoxMode outputMode)
    {
        PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this, new Object[]
            {
             Byte.valueOf((byte) inputDir.ordinal()),
             Byte.valueOf((byte) outputDir.ordinal()),
             Byte.valueOf((byte) inputMode.ordinal()),
             Byte.valueOf((byte) outputMode.ordinal())
            }));
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return (var1 == 0 || var1 == 1) ? new int[]
            { var1 } : new int[] {};
    }
    
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return i == j && (i == 0 || i == 1);
    }
    
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return i == j && (i == 0 || i == 1);
    }
    
    public ForgeDirection getInputDir()
    {
        return this.inputDir;
    }
    
    public ForgeDirection getOutputDir()
    {
        return this.outputDir;
    }
    
    @Override
    public IElectricityNetwork[] getNetworks()
    {
        return new IElectricityNetwork[]
            { this.inputNetwork, this.outputNetwork };
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
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return this.getInputDirections().contains(direction);
    }
    
    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
    {
        return this.getOutputDirections().contains(direction);
    }
    
    @Override
    public float getRequest(ForgeDirection direction)
    {
        return Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), this.getOutputCap());
    }
    
    @Override
    public float getProvide(ForgeDirection direction)
    {
        return Math.min(this.getOutputCap(), this.getEnergyStored());
    }
    
    @Override
    public double getOfferedEnergy()
    {
        return JOULES_UE.convertToOtherUnit(ENERGY_UNIT, Math.min(this.getOutputCap(), this.getEnergyStored()));
    }
    
    @Override
    public void drawEnergy(double amount)
    {
        this.energyStored -= ENERGY_UNIT.convertToOtherUnit(JOULES_UE, amount);
    }
    
    @Override
    public int getSerialQuantity()
    { return 2; }
    
    @Override
    public int getInputQuantity()
    { return 1; }
    
    @Override
    public int getOutputQuantity()
    { return 1; }
    
    @Override
    public EnumSet<ForgeDirection> getSerialDirections()
    {
        return null;
    }
    
    @Override
    public int getStored()
    {
        return (int) JOULES_UE.convertToOtherUnit(ENERGY_UNIT, this.getEnergyStored());
    }
    
    @Override
    public void setStored(int energy)
    {
        this.setEnergyStored(ENERGY_UNIT.convertToOtherUnit(JOULES_UE, energy));
        
    }
    
    @Override
    public int addEnergy(int amount)
    {
        if (ENERGY_UNIT.convertToOtherUnit(JOULES_UE, amount) + this.energyStored <= this.getMaxEnergyStored())
        {
            this.energyStored += ENERGY_UNIT.convertToOtherUnit(JOULES_UE, amount);
            return 0;
        }
        else 
        {
            int toTake = this.getCapacity() - this.getStored();
            this.energyStored += ENERGY_UNIT.convertToOtherUnit(JOULES_UE, toTake);
            return amount - toTake;
        }
    }
    
    @Override
    public int getCapacity()
    {
        return (int) JOULES_UE.convertToOtherUnit(ENERGY_UNIT, this.getMaxEnergyStored());
    }
    
    @Override
    public int getOutput()
    {
        return (int) Math.max(0, Math.min(JOULES_UE.convertToOtherUnit(ENERGY_UNIT, this.getOutputCap()), 2048));
    }
    
    @Override
    public double getOutputEnergyUnitsPerTick()
    {
        return this.getOutput();
    }
    
    @Override
    public boolean isTeleporterCompatible(ForgeDirection side)
    {
        return this.getSerialDirections().contains(side);
    }
}
