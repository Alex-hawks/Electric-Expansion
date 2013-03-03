/*     */ package electricexpansion.common.tile;
/*     */ 
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import cpw.mods.fml.common.Loader;
/*     */ import electricexpansion.common.ElectricExpansion;
/*     */ import electricexpansion.common.misc.InsulationRecipes;
/*     */ import ic2.api.Direction;
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
/*     */ public class TileEntityInsulatingMachine extends TileEntityElectricityReceiver
/*     */   implements IInventory, ISidedInventory, IPacketReceiver, IJouleStorage, IEnergyTile, IEnergySink
/*     */ {
/*  42 */   public final double WATTS_PER_TICK = 500.0D;
/*  43 */   public final double TRANSFER_LIMIT = 1250.0D;
/*  44 */   private int processTicks = 0;
/*  45 */   private double joulesStored = 0.0D;
/*  46 */   public static double maxJoules = 150000.0D;
/*     */ 
/*  51 */   private ItemStack[] inventory = new ItemStack[3];
/*     */ 
/*  53 */   private int playersUsing = 0;
/*     */   public int orientation;
/*  55 */   private int baseID = 0;
/*  56 */   private int baseMeta = 0;
/*     */   private boolean initialized;
/*     */ 
/*     */   public void initiate()
/*     */   {
/*  63 */     ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2)));
/*  64 */     this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockInsulationMachine.blockID);
/*     */ 
/*  66 */     this.initialized = true;
/*     */ 
/*  68 */     if (Loader.isModLoaded("IC2"))
/*     */     {
/*  70 */       MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*  77 */     super.invalidate();
/*     */ 
/*  79 */     if (this.initialized)
/*     */     {
/*  81 */       if (Loader.isModLoaded("IC2"))
/*     */       {
/*  83 */         MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateEntity()
/*     */   {
/*  91 */     super.updateEntity();
/*     */ 
/*  93 */     if (!this.worldObj.isRemote)
/*     */     {
/*  95 */       ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
/*  96 */       TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
/*     */ 
/*  98 */       ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);
/*     */ 
/* 100 */       if (inputNetwork != null)
/*     */       {
/* 102 */         if (this.joulesStored < maxJoules)
/*     */         {
/* 104 */           inputNetwork.startRequesting(this, Math.min(getMaxJoules(new Object[0]) - getJoules(new Object[0]), 1250.0D) / getVoltage(new Object[0]), getVoltage(new Object[0]));
/* 105 */           ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
/* 106 */           setJoules(this.joulesStored + electricityPack.getWatts(), new Object[0]);
/*     */ 
/* 108 */           if (UniversalElectricity.isVoltageSensitive)
/*     */           {
/* 110 */             if (electricityPack.voltage > getVoltage(new Object[0]))
/*     */             {
/* 112 */               this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2.0F, true);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 118 */           inputNetwork.stopRequesting(this);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     if ((this.inventory[0] != null) && (this.joulesStored < getMaxJoules(new Object[0])))
/*     */     {
/* 127 */       if ((this.inventory[0].getItem() instanceof IItemElectric))
/*     */       {
/* 129 */         IItemElectric electricItem = (IItemElectric)this.inventory[0].getItem();
/*     */ 
/* 131 */         if (electricItem.canProduceElectricity())
/*     */         {
/* 133 */           double joulesReceived = electricItem.onUse(Math.max(electricItem.getMaxJoules(new Object[] { this.inventory[0] }) * 0.005D, 1250.0D), this.inventory[0]);
/* 134 */           setJoules(this.joulesStored + joulesReceived, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 139 */     getClass(); if ((this.joulesStored >= 500.0D - 50.0D) && (!isDisabled()))
/*     */     {
/* 143 */       if ((this.inventory[1] != null) && (canDraw()) && ((this.processTicks == 0) || (this.baseID != this.inventory[1].itemID) || (this.baseMeta != this.inventory[1].getItemDamage())))
/*     */       {
/* 145 */         this.baseID = this.inventory[1].itemID;
/* 146 */         this.baseMeta = this.inventory[1].getItemDamage();
/* 147 */         this.processTicks = getDrawingTime();
/*     */       }
/*     */ 
/* 154 */       if ((canDraw()) && (this.processTicks > 0))
/*     */       {
/* 156 */         this.processTicks -= 1;
/*     */ 
/* 160 */         if (this.processTicks < 1)
/*     */         {
/* 162 */           drawItem();
/* 163 */           this.processTicks = 0;
/*     */         }
/* 165 */         getClass(); this.joulesStored -= 500.0D;
/*     */       }
/*     */       else
/*     */       {
/* 169 */         this.processTicks = 0;
/*     */       }
/*     */     }
/*     */ 
/* 173 */     if (!this.worldObj.isRemote)
/*     */     {
/* 175 */       if ((this.ticks % 3L == 0L) && (this.playersUsing > 0))
/*     */       {
/* 177 */         PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
/*     */       }
/*     */     }
/*     */ 
/* 181 */     this.joulesStored = Math.min(this.joulesStored, getMaxJoules(new Object[0]));
/* 182 */     this.joulesStored = Math.max(this.joulesStored, 0.0D);
/*     */   }
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/* 188 */     return PacketManager.getPacket("ElecEx", this, new Object[] { Integer.valueOf(this.processTicks), Integer.valueOf(this.disabledTicks), Double.valueOf(this.joulesStored) });
/*     */   }
/*     */ 
/*     */   public void handlePacketData(INetworkManager inputNetwork, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
/*     */   {
/*     */     try
/*     */     {
/* 196 */       this.processTicks = dataStream.readInt();
/* 197 */       this.disabledTicks = dataStream.readInt();
/* 198 */       this.joulesStored = dataStream.readDouble();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 202 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void openChest()
/*     */   {
/* 209 */     if (!this.worldObj.isRemote)
/* 210 */       PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 15.0D);
/* 211 */     this.playersUsing += 1;
/*     */   }
/*     */ 
/*     */   public void closeChest()
/*     */   {
/* 217 */     this.playersUsing -= 1;
/*     */   }
/*     */ 
/*     */   public boolean canDraw()
/*     */   {
/* 223 */     boolean canWork = false;
/* 224 */     ItemStack inputSlot = this.inventory[1];
/* 225 */     ItemStack outputSlot = this.inventory[2];
/* 226 */     if (inputSlot != null)
/*     */     {
/* 228 */       if ((InsulationRecipes.getProcessing().getProcessResult(inputSlot) > 0) && (InsulationRecipes.getProcessing().getProcessResult(inputSlot) + this.inventory[2].stackSize <= 64))
/*     */       {
/* 230 */         canWork = true;
/*     */       }
/*     */     }
/*     */ 
/* 234 */     return canWork;
/*     */   }
/*     */ 
/*     */   public void drawItem()
/*     */   {
/* 242 */     if (canDraw())
/*     */     {
/* 244 */       int result = InsulationRecipes.getProcessing().getProcessResult(this.inventory[1]);
/*     */ 
/* 246 */       if (this.inventory[2] == null)
/* 247 */         this.inventory[2] = new ItemStack(ElectricExpansion.itemParts, result, 6);
/* 248 */       else if (this.inventory[2].stackSize + result <= 64) {
/* 249 */         this.inventory[2].stackSize += result;
/*     */       }
/* 251 */       InsulationRecipes.getProcessing(); this.inventory[1].stackSize -= InsulationRecipes.getInputQTY(this.inventory[1]);
/*     */ 
/* 253 */       if (this.inventory[1].stackSize <= 0)
/* 254 */         this.inventory[1] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 264 */     super.readFromNBT(par1NBTTagCompound);
/* 265 */     this.processTicks = par1NBTTagCompound.getInteger("processTicks");
/* 266 */     NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
/* 267 */     this.inventory = new ItemStack[getSizeInventory()];
/*     */     try
/*     */     {
/* 270 */       this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */ 
/* 276 */     for (int var3 = 0; var3 < var2.tagCount(); var3++)
/*     */     {
/* 278 */       NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
/* 279 */       byte var5 = var4.getByte("Slot");
/*     */ 
/* 281 */       if ((var5 >= 0) && (var5 < this.inventory.length))
/* 282 */         this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 292 */     super.writeToNBT(par1NBTTagCompound);
/* 293 */     par1NBTTagCompound.setInteger("processTicks", this.processTicks);
/* 294 */     NBTTagList var2 = new NBTTagList();
/* 295 */     par1NBTTagCompound.setDouble("joulesStored", getJoules(new Object[0]));
/*     */ 
/* 297 */     for (int var3 = 0; var3 < this.inventory.length; var3++)
/*     */     {
/* 299 */       if (this.inventory[var3] != null)
/*     */       {
/* 301 */         NBTTagCompound var4 = new NBTTagCompound();
/* 302 */         var4.setByte("Slot", (byte)var3);
/* 303 */         this.inventory[var3].writeToNBT(var4);
/* 304 */         var2.appendTag(var4);
/*     */       }
/*     */     }
/* 307 */     par1NBTTagCompound.setTag("Items", var2);
/*     */   }
/*     */ 
/*     */   public int getStartInventorySide(ForgeDirection side)
/*     */   {
/* 313 */     if ((side == ForgeDirection.DOWN) || (side == ForgeDirection.UP)) {
/* 314 */       return side.ordinal();
/*     */     }
/* 316 */     return 2;
/*     */   }
/*     */ 
/*     */   public int getSizeInventorySide(ForgeDirection side)
/*     */   {
/* 322 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getSizeInventory()
/*     */   {
/* 328 */     return this.inventory.length;
/*     */   }
/*     */ 
/*     */   public ItemStack getStackInSlot(int par1)
/*     */   {
/* 334 */     return this.inventory[par1];
/*     */   }
/*     */ 
/*     */   public ItemStack decrStackSize(int par1, int par2)
/*     */   {
/* 340 */     if (this.inventory[par1] != null)
/*     */     {
/* 344 */       if (this.inventory[par1].stackSize <= par2)
/*     */       {
/* 346 */         ItemStack var3 = this.inventory[par1];
/* 347 */         this.inventory[par1] = null;
/* 348 */         return var3;
/*     */       }
/*     */ 
/* 352 */       ItemStack var3 = this.inventory[par1].splitStack(par2);
/*     */ 
/* 354 */       if (this.inventory[par1].stackSize == 0) {
/* 355 */         this.inventory[par1] = null;
/*     */       }
/* 357 */       return var3;
/*     */     }
/*     */ 
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */   public ItemStack getStackInSlotOnClosing(int par1)
/*     */   {
/* 367 */     if (this.inventory[par1] != null)
/*     */     {
/* 369 */       ItemStack var2 = this.inventory[par1];
/* 370 */       this.inventory[par1] = null;
/* 371 */       return var2;
/*     */     }
/*     */ 
/* 374 */     return null;
/*     */   }
/*     */ 
/*     */   public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
/*     */   {
/* 380 */     this.inventory[par1] = par2ItemStack;
/*     */ 
/* 382 */     if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
/* 383 */       par2ItemStack.stackSize = getInventoryStackLimit();
/*     */   }
/*     */ 
/*     */   public String getInvName()
/*     */   {
/* 389 */     return "Insultion Refiner";
/*     */   }
/*     */ 
/*     */   public int getInventoryStackLimit()
/*     */   {
/* 395 */     return 64;
/*     */   }
/*     */ 
/*     */   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
/*     */   {
/* 401 */     return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
/*     */   }
/*     */ 
/*     */   public double getVoltage(Object[] data)
/*     */   {
/* 407 */     return 120.0D;
/*     */   }
/*     */ 
/*     */   public int getDrawingTime()
/*     */   {
/* 415 */     if (this.inventory[1] != null)
/*     */     {
/* 417 */       if (InsulationRecipes.getProcessing().getProcessResult(this.inventory[1]) != 0)
/*     */       {
/* 419 */         InsulationRecipes.getProcessing(); return InsulationRecipes.getProcessTicks(this.inventory[1]).intValue();
/*     */       }
/*     */     }
/* 422 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getProcessTimeLeft()
/*     */   {
/* 427 */     return this.processTicks;
/*     */   }
/*     */ 
/*     */   public double getJoules(Object[] data)
/*     */   {
/* 433 */     return this.joulesStored;
/*     */   }
/*     */ 
/*     */   public void setJoules(double joules, Object[] data)
/*     */   {
/* 439 */     this.joulesStored = joules;
/*     */   }
/*     */ 
/*     */   public double getMaxJoules(Object[] data)
/*     */   {
/* 445 */     return maxJoules;
/*     */   }
/*     */ 
/*     */   public boolean isAddedToEnergyNet()
/*     */   {
/* 451 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
/*     */   {
/* 458 */     if (direction.toForgeDirection() == ForgeDirection.getOrientation(getBlockMetadata() + 2))
/*     */     {
/* 460 */       return true;
/*     */     }
/*     */ 
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */   public int demandsEnergy()
/*     */   {
/* 472 */     return (int)((getMaxJoules(new Object[0]) - this.joulesStored) * UniversalElectricity.TO_IC2_RATIO);
/*     */   }
/*     */ 
/*     */   public int injectEnergy(Direction direction, int i)
/*     */   {
/* 478 */     double givenEnergy = i * UniversalElectricity.IC2_RATIO;
/* 479 */     double rejects = 0.0D;
/* 480 */     double neededEnergy = getMaxJoules(new Object[0]) - this.joulesStored;
/*     */ 
/* 482 */     if (givenEnergy < neededEnergy)
/*     */     {
/* 484 */       this.joulesStored += givenEnergy;
/*     */     }
/* 486 */     else if (givenEnergy > neededEnergy)
/*     */     {
/* 488 */       this.joulesStored += neededEnergy;
/* 489 */       rejects = givenEnergy - neededEnergy;
/*     */     }
/*     */ 
/* 492 */     return (int)(rejects * UniversalElectricity.TO_IC2_RATIO);
/*     */   }
/*     */ 
/*     */   public int getMaxSafeInput()
/*     */   {
/* 498 */     return 2048;
/*     */   }
/*     */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.common.tile.TileEntityInsulatingMachine
 * JD-Core Version:    0.6.2
 */