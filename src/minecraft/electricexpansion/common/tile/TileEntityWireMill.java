/*     */ package electricexpansion.common.tile;
/*     */ 
/*     */ import buildcraft.api.power.IPowerProvider;
/*     */ import buildcraft.api.power.IPowerReceptor;
/*     */ import buildcraft.api.power.PowerFramework;
/*     */ import buildcraft.api.power.PowerProvider;
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import cpw.mods.fml.common.Loader;
/*     */ import electricexpansion.common.ElectricExpansion;
/*     */ import electricexpansion.common.misc.WireMillRecipes;
/*     */ import ic2.api.Direction;
/*     */ import ic2.api.ElectricItem;
/*     */ import ic2.api.IElectricItem;
/*     */ import ic2.api.energy.event.EnergyTileLoadEvent;
/*     */ import ic2.api.energy.event.EnergyTileUnloadEvent;
/*     */ import ic2.api.energy.tile.IEnergySink;
/*     */ import ic2.api.energy.tile.IEnergyTile;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.INetworkManager;
/*     */ import net.minecraft.network.packet.Packet;
/*     */ import net.minecraft.network.packet.Packet250CustomPayload;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.ForgeDirection;
/*     */ import net.minecraftforge.common.ISidedInventory;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.event.EventBus;
/*     */ import universalelectricity.core.UniversalElectricity;
/*     */ import universalelectricity.core.electricity.ElectricityConnections;
/*     */ import universalelectricity.core.electricity.ElectricityNetwork;
/*     */ import universalelectricity.core.electricity.ElectricityPack;
/*     */ import universalelectricity.core.implement.IItemElectric;
/*     */ import universalelectricity.core.implement.IJouleStorage;
/*     */ import universalelectricity.core.vector.Vector3;
/*     */ import universalelectricity.prefab.network.IPacketReceiver;
/*     */ import universalelectricity.prefab.network.PacketManager;
/*     */ import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
/*     */ 
/*     */ public class TileEntityWireMill extends TileEntityElectricityReceiver
/*     */   implements IInventory, IPowerReceptor, ISidedInventory, IPacketReceiver, IJouleStorage, IEnergyTile, IEnergySink
/*     */ {
/*  48 */   public final double WATTS_PER_TICK = 500.0D;
/*  49 */   public final double TRANSFER_LIMIT = 1250.0D;
/*  50 */   private int drawingTicks = 0;
/*  51 */   private double joulesStored = 0.0D;
/*  52 */   public static double maxJoules = 150000.0D;
/*     */   public IPowerProvider powerProvider;
/*  61 */   private ItemStack[] inventory = new ItemStack[3];
/*     */ 
/*  63 */   private int playersUsing = 0;
/*     */   public int orientation;
/*  65 */   private int targetID = 0;
/*  66 */   private int targetMeta = 0;
/*     */   private boolean initialized;
/*     */ 
/*     */   public TileEntityWireMill()
/*     */   {
/*  72 */     if (PowerFramework.currentFramework != null)
/*     */     {
/*  74 */       this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
/*  75 */       this.powerProvider.configure(0, 0, 100, 0, (int)(getMaxJoules(new Object[0]) * UniversalElectricity.TO_BC_RATIO));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initiate()
/*     */   {
/*  82 */     ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2)));
/*  83 */     this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockWireMill.blockID);
/*     */ 
/*  85 */     this.initialized = true;
/*     */ 
/*  87 */     if (Loader.isModLoaded("IC2"))
/*     */     {
/*  89 */       MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*  96 */     super.invalidate();
/*     */ 
/*  98 */     if (this.initialized)
/*     */     {
/* 100 */       if (Loader.isModLoaded("IC2"))
/*     */       {
/* 102 */         MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateEntity()
/*     */   {
/* 110 */     super.updateEntity();
/*     */ 
/* 112 */     if (this.powerProvider != null)
/*     */     {
/* 114 */       int received = (int)(this.powerProvider.useEnergy(0.0F, (float)((getMaxJoules(new Object[0]) - getJoules(new Object[0])) * UniversalElectricity.TO_BC_RATIO), true) * UniversalElectricity.BC3_RATIO);
/* 115 */       setJoules(getJoules(new Object[0]) + received, new Object[0]);
/*     */     }
/*     */ 
/* 118 */     if (!this.worldObj.isRemote)
/*     */     {
/* 120 */       ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
/* 121 */       TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
/*     */ 
/* 123 */       ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);
/*     */ 
/* 125 */       if (inputNetwork != null)
/*     */       {
/* 127 */         if (this.joulesStored < maxJoules)
/*     */         {
/* 129 */           inputNetwork.startRequesting(this, Math.min(getMaxJoules(new Object[0]) - getJoules(new Object[0]), 1250.0D) / getVoltage(new Object[0]), getVoltage(new Object[0]));
/* 130 */           ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
/* 131 */           setJoules(this.joulesStored + electricityPack.getWatts(), new Object[0]);
/*     */ 
/* 133 */           if (UniversalElectricity.isVoltageSensitive)
/*     */           {
/* 135 */             if (electricityPack.voltage > getVoltage(new Object[0]))
/*     */             {
/* 137 */               this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2.0F, true);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 143 */           inputNetwork.stopRequesting(this);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 150 */     if ((this.inventory[0] != null) && (this.joulesStored < getMaxJoules(new Object[0])))
/*     */     {
/* 152 */       if ((this.inventory[0].getItem() instanceof IItemElectric))
/*     */       {
/* 154 */         IItemElectric electricItem = (IItemElectric)this.inventory[0].getItem();
/*     */ 
/* 156 */         if (electricItem.canProduceElectricity())
/*     */         {
/* 158 */           double joulesReceived = electricItem.onUse(Math.max(electricItem.getMaxJoules(new Object[] { this.inventory[0] }) * 0.005D, 1250.0D), this.inventory[0]);
/* 159 */           setJoules(this.joulesStored + joulesReceived, new Object[0]);
/*     */         }
/*     */ 
/*     */       }
/* 163 */       else if ((this.inventory[0].getItem() instanceof IElectricItem))
/*     */       {
/* 165 */         IElectricItem item = (IElectricItem)this.inventory[0].getItem();
/* 166 */         if (item.canProvideEnergy())
/*     */         {
/* 168 */           double gain = ElectricItem.discharge(this.inventory[0], (int)((int)(getMaxJoules(new Object[0]) - getJoules(new Object[0])) * UniversalElectricity.TO_IC2_RATIO), 3, false, false) * UniversalElectricity.IC2_RATIO;
/* 169 */           setJoules(getJoules(new Object[0]) + gain, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 174 */     getClass(); if ((this.joulesStored >= 500.0D - 50.0D) && (!isDisabled()))
/*     */     {
/* 178 */       if ((this.inventory[1] != null) && (canDraw()) && ((this.drawingTicks == 0) || (this.targetID != this.inventory[1].itemID) || (this.targetMeta != this.inventory[1].getItemDamage())))
/*     */       {
/* 180 */         this.targetID = this.inventory[1].itemID;
/* 181 */         this.targetMeta = this.inventory[1].getItemDamage();
/* 182 */         this.drawingTicks = getDrawingTime();
/*     */       }
/*     */ 
/* 189 */       if ((canDraw()) && (this.drawingTicks > 0))
/*     */       {
/* 191 */         this.drawingTicks -= 1;
/*     */ 
/* 195 */         if (this.drawingTicks < 1)
/*     */         {
/* 197 */           drawItem();
/* 198 */           this.drawingTicks = 0;
/*     */         }
/* 200 */         getClass(); this.joulesStored -= 500.0D;
/*     */       }
/*     */       else
/*     */       {
/* 204 */         this.drawingTicks = 0;
/*     */       }
/*     */     }
/*     */ 
/* 208 */     if (!this.worldObj.isRemote)
/*     */     {
/* 210 */       if ((this.ticks % 3L == 0L) && (this.playersUsing > 0))
/*     */       {
/* 212 */         PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
/*     */       }
/*     */     }
/*     */ 
/* 216 */     this.joulesStored = Math.min(this.joulesStored, getMaxJoules(new Object[0]));
/* 217 */     this.joulesStored = Math.max(this.joulesStored, 0.0D);
/*     */   }
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/* 223 */     return PacketManager.getPacket("ElecEx", this, new Object[] { Integer.valueOf(this.drawingTicks), Integer.valueOf(this.disabledTicks), Double.valueOf(this.joulesStored) });
/*     */   }
/*     */ 
/*     */   public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
/*     */   {
/*     */     try
/*     */     {
/* 231 */       this.drawingTicks = dataStream.readInt();
/* 232 */       this.disabledTicks = dataStream.readInt();
/* 233 */       this.joulesStored = dataStream.readDouble();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 237 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void openChest()
/*     */   {
/* 244 */     if (!this.worldObj.isRemote)
/* 245 */       PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 15.0D);
/* 246 */     this.playersUsing += 1;
/*     */   }
/*     */ 
/*     */   public void closeChest()
/*     */   {
/* 252 */     this.playersUsing -= 1;
/*     */   }
/*     */ 
/*     */   public boolean canDraw()
/*     */   {
/* 258 */     boolean canWork = false;
/* 259 */     ItemStack inputSlot = this.inventory[1];
/* 260 */     ItemStack outputSlot = this.inventory[2];
/* 261 */     if (inputSlot != null)
/*     */     {
/* 263 */       if (WireMillRecipes.drawing().getDrawingResult(inputSlot) == null)
/*     */       {
/* 265 */         canWork = false;
/*     */       }
/* 267 */       else if ((WireMillRecipes.drawing().getDrawingResult(inputSlot) != null) && (outputSlot == null))
/*     */       {
/* 269 */         canWork = true;
/*     */       }
/* 271 */       else if (outputSlot != null)
/*     */       {
/* 273 */         String result = WireMillRecipes.stackSizeToOne(WireMillRecipes.drawing().getDrawingResult(inputSlot)) + "";
/* 274 */         String output2 = WireMillRecipes.stackSizeToOne(outputSlot) + "";
/* 275 */         int maxSpaceForSuccess = Math.min(outputSlot.getMaxStackSize(), inputSlot.getMaxStackSize()) - WireMillRecipes.drawing().getDrawingResult(inputSlot).stackSize;
/*     */ 
/* 277 */         if ((result.equals(output2)) && (outputSlot.stackSize > maxSpaceForSuccess))
/*     */         {
/* 279 */           canWork = false;
/*     */         }
/* 281 */         else if ((result.equals(output2)) && (outputSlot.stackSize <= maxSpaceForSuccess))
/*     */         {
/* 283 */           canWork = true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 288 */     return canWork;
/*     */   }
/*     */ 
/*     */   public void drawItem()
/*     */   {
/* 297 */     if (canDraw())
/*     */     {
/* 299 */       ItemStack resultItemStack = WireMillRecipes.drawing().getDrawingResult(this.inventory[1]);
/*     */ 
/* 301 */       if (this.inventory[2] == null)
/* 302 */         this.inventory[2] = resultItemStack.copy();
/* 303 */       else if (this.inventory[2].isItemEqual(resultItemStack)) {
/* 304 */         this.inventory[2].stackSize += resultItemStack.stackSize;
/*     */       }
/* 306 */       WireMillRecipes.drawing(); this.inventory[1].stackSize -= WireMillRecipes.getInputQTY(this.inventory[1]);
/*     */ 
/* 308 */       if (this.inventory[1].stackSize <= 0)
/* 309 */         this.inventory[1] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 319 */     super.readFromNBT(par1NBTTagCompound);
/* 320 */     this.drawingTicks = par1NBTTagCompound.getInteger("drawingTicks");
/* 321 */     NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
/* 322 */     this.inventory = new ItemStack[getSizeInventory()];
/*     */     try
/*     */     {
/* 325 */       this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */ 
/* 331 */     for (int var3 = 0; var3 < var2.tagCount(); var3++)
/*     */     {
/* 333 */       NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
/* 334 */       byte var5 = var4.getByte("Slot");
/*     */ 
/* 336 */       if ((var5 >= 0) && (var5 < this.inventory.length)) {
/* 337 */         this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
/*     */       }
/*     */     }
/* 340 */     if (PowerFramework.currentFramework != null)
/*     */     {
/* 342 */       PowerFramework.currentFramework.loadPowerProvider(this, par1NBTTagCompound);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 352 */     super.writeToNBT(par1NBTTagCompound);
/* 353 */     par1NBTTagCompound.setInteger("drawingTicks", this.drawingTicks);
/* 354 */     NBTTagList var2 = new NBTTagList();
/* 355 */     par1NBTTagCompound.setDouble("joulesStored", getJoules(new Object[0]));
/*     */ 
/* 357 */     for (int var3 = 0; var3 < this.inventory.length; var3++)
/*     */     {
/* 359 */       if (this.inventory[var3] != null)
/*     */       {
/* 361 */         NBTTagCompound var4 = new NBTTagCompound();
/* 362 */         var4.setByte("Slot", (byte)var3);
/* 363 */         this.inventory[var3].writeToNBT(var4);
/* 364 */         var2.appendTag(var4);
/*     */       }
/*     */     }
/* 367 */     par1NBTTagCompound.setTag("Items", var2);
/*     */ 
/* 369 */     if (PowerFramework.currentFramework != null)
/*     */     {
/* 371 */       PowerFramework.currentFramework.savePowerProvider(this, par1NBTTagCompound);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getStartInventorySide(ForgeDirection side)
/*     */   {
/* 378 */     if ((side == ForgeDirection.DOWN) || (side == ForgeDirection.UP)) {
/* 379 */       return side.ordinal();
/*     */     }
/* 381 */     return 2;
/*     */   }
/*     */ 
/*     */   public int getSizeInventorySide(ForgeDirection side)
/*     */   {
/* 387 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/* 393 */     return this.inventory.length;
/*     */   }
/*     */ 
/*     */   public ItemStack getStackInSlot(int par1)
/*     */   {
/* 399 */     return this.inventory[par1];
/*     */   }
/*     */ 
/*     */   public ItemStack decrStackSize(int par1, int par2)
/*     */   {
/* 405 */     if (this.inventory[par1] != null)
/*     */     {
/* 409 */       if (this.inventory[par1].stackSize <= par2)
/*     */       {
/* 411 */         ItemStack var3 = this.inventory[par1];
/* 412 */         this.inventory[par1] = null;
/* 413 */         return var3;
/*     */       }
/*     */ 
/* 417 */       ItemStack var3 = this.inventory[par1].splitStack(par2);
/*     */ 
/* 419 */       if (this.inventory[par1].stackSize == 0) {
/* 420 */         this.inventory[par1] = null;
/*     */       }
/* 422 */       return var3;
/*     */     }
/*     */ 
/* 426 */     return null;
/*     */   }
/*     */ 
/*     */   public ItemStack getStackInSlotOnClosing(int par1)
/*     */   {
/* 432 */     if (this.inventory[par1] != null)
/*     */     {
/* 434 */       ItemStack var2 = this.inventory[par1];
/* 435 */       this.inventory[par1] = null;
/* 436 */       return var2;
/*     */     }
/*     */ 
/* 439 */     return null;
/*     */   }
/*     */ 
/*     */   public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
/*     */   {
/* 445 */     this.inventory[par1] = par2ItemStack;
/*     */ 
/* 447 */     if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
/* 448 */       par2ItemStack.stackSize = getInventoryStackLimit();
/*     */   }
/*     */ 
/*     */   public String getInvName()
/*     */   {
/* 454 */     return "Wire Mill";
/*     */   }
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 460 */     return 64;
/*     */   }
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
/*     */   {
/* 466 */     return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
/*     */   }
/*     */ 
/*     */   public double getVoltage(Object[] data)
/*     */   {
/* 472 */     return 120.0D;
/*     */   }
/*     */ 
/*     */   public int getDrawingTime()
/*     */   {
/* 480 */     if (this.inventory[1] != null)
/*     */     {
/* 482 */       if (WireMillRecipes.drawing().getDrawingResult(this.inventory[1]) != null) { WireMillRecipes.drawing(); return WireMillRecipes.getDrawingTicks(this.inventory[1]).intValue(); }
/*     */     }
/* 484 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getDrawingTimeLeft()
/*     */   {
/* 489 */     return this.drawingTicks;
/*     */   }
/*     */ 
/*     */   public double getJoules(Object[] data)
/*     */   {
/* 495 */     return this.joulesStored;
/*     */   }
/*     */ 
/*     */   public void setJoules(double joules, Object[] data)
/*     */   {
/* 501 */     this.joulesStored = joules;
/*     */   }
/*     */ 
/*     */   public double getMaxJoules(Object[] data)
/*     */   {
/* 507 */     return maxJoules;
/*     */   }
/*     */ 
/*     */   public boolean isAddedToEnergyNet()
/*     */   {
/* 513 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
/*     */   {
/* 520 */     if (direction.toForgeDirection() == ForgeDirection.getOrientation(getBlockMetadata() + 2))
/*     */     {
/* 522 */       return true;
/*     */     }
/*     */ 
/* 527 */     return false;
/*     */   }
/*     */ 
/*     */   public int demandsEnergy()
/*     */   {
/* 534 */     return (int)((getMaxJoules(new Object[0]) - this.joulesStored) * UniversalElectricity.TO_IC2_RATIO);
/*     */   }
/*     */ 
/*     */   public int injectEnergy(Direction direction, int i)
/*     */   {
/* 540 */     double givenEnergy = i * UniversalElectricity.IC2_RATIO;
/* 541 */     double rejects = 0.0D;
/* 542 */     double neededEnergy = getMaxJoules(new Object[0]) - this.joulesStored;
/*     */ 
/* 544 */     if (givenEnergy < neededEnergy)
/*     */     {
/* 546 */       this.joulesStored += givenEnergy;
/*     */     }
/* 548 */     else if (givenEnergy > neededEnergy)
/*     */     {
/* 550 */       this.joulesStored += neededEnergy;
/* 551 */       rejects = givenEnergy - neededEnergy;
/*     */     }
/*     */ 
/* 554 */     return (int)(rejects * UniversalElectricity.TO_IC2_RATIO);
/*     */   }
/*     */ 
/*     */   public int getMaxSafeInput()
/*     */   {
/* 560 */     return 2048;
/*     */   }
/*     */ 
/*     */   public void setPowerProvider(IPowerProvider provider)
/*     */   {
/* 568 */     this.powerProvider = provider;
/*     */   }
/*     */ 
/*     */   public IPowerProvider getPowerProvider()
/*     */   {
/* 574 */     return this.powerProvider;
/*     */   }
/*     */ 
/*     */   public int powerRequest()
/*     */   {
/* 580 */     return (int)(getMaxJoules(new Object[0]) - getJoules(new Object[0]));
/*     */   }
/*     */ 
/*     */   public void doWork()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isPowerReceptor(TileEntity tileEntity)
/*     */   {
/* 596 */     if ((tileEntity instanceof IPowerReceptor))
/*     */     {
/* 598 */       IPowerReceptor receptor = (IPowerReceptor)tileEntity;
/* 599 */       IPowerProvider provider = receptor.getPowerProvider();
/* 600 */       return (provider != null) && (provider.getClass().getSuperclass().equals(PowerProvider.class));
/*     */     }
/* 602 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.common.tile.TileEntityWireMill
 * JD-Core Version:    0.6.2
 */