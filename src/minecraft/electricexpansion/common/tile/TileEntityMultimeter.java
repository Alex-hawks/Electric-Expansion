/*     */ package electricexpansion.common.tile;
/*     */ 
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import electricexpansion.common.ElectricExpansion;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.INetworkManager;
/*     */ import net.minecraft.network.packet.Packet;
/*     */ import net.minecraft.network.packet.Packet250CustomPayload;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.ForgeDirection;
/*     */ import universalelectricity.core.electricity.ElectricityConnections;
/*     */ import universalelectricity.core.electricity.ElectricityNetwork;
/*     */ import universalelectricity.core.electricity.ElectricityPack;
/*     */ import universalelectricity.core.implement.IConductor;
/*     */ import universalelectricity.core.vector.Vector3;
/*     */ import universalelectricity.prefab.implement.IRotatable;
/*     */ import universalelectricity.prefab.network.IPacketReceiver;
/*     */ import universalelectricity.prefab.network.PacketManager;
/*     */ import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
/*     */ 
/*     */ public class TileEntityMultimeter extends TileEntityElectricityReceiver
/*     */   implements IPacketReceiver, IRotatable
/*     */ {
/*  26 */   public ElectricityPack electricityReading = new ElectricityPack();
/*  27 */   private ElectricityPack lastReading = new ElectricityPack();
/*     */ 
/*     */   public void initiate()
/*     */   {
/*  32 */     ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2)));
/*  33 */     this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockMultimeter.blockID);
/*     */   }
/*     */ 
/*     */   public void updateEntity()
/*     */   {
/*  39 */     super.updateEntity();
/*     */ 
/*  41 */     if (this.ticks % 20L == 0L)
/*     */     {
/*  43 */       this.lastReading = this.electricityReading;
/*     */ 
/*  45 */       if (!this.worldObj.isRemote)
/*     */       {
/*  47 */         if (!isDisabled())
/*     */         {
/*  50 */           ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
/*  51 */           TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
/*     */ 
/*  53 */           if (inputTile != null)
/*     */           {
/*  55 */             if ((inputTile instanceof IConductor))
/*     */             {
/*  57 */               this.electricityReading = ((IConductor)inputTile).getNetwork().getProduced(new TileEntity[0]);
/*  58 */               this.electricityReading.amperes *= 20.0D;
/*     */             }
/*     */             else
/*     */             {
/*  62 */               this.electricityReading = new ElectricityPack();
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  67 */             this.electricityReading = new ElectricityPack();
/*     */           }
/*     */         }
/*     */ 
/*  71 */         if (this.electricityReading.amperes != this.lastReading.amperes)
/*     */         {
/*  73 */           PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 20.0D);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/*  82 */     return PacketManager.getPacket("ElecEx", this, new Object[] { Double.valueOf(this.electricityReading.amperes), Double.valueOf(this.electricityReading.voltage) });
/*     */   }
/*     */ 
/*     */   public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
/*     */   {
/*  89 */     if (this.worldObj.isRemote)
/*     */     {
/*     */       try
/*     */       {
/*  93 */         this.electricityReading.amperes = dataStream.readDouble();
/*  94 */         this.electricityReading.voltage = dataStream.readDouble();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*  99 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getInvName()
/*     */   {
/* 106 */     return "Multimeter";
/*     */   }
/*     */ 
/*     */   public ForgeDirection getDirection()
/*     */   {
/* 112 */     return ForgeDirection.getOrientation(getBlockMetadata());
/*     */   }
/*     */ 
/*     */   public void setDirection(ForgeDirection facingDirection)
/*     */   {
/* 118 */     this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
/*     */   }
/*     */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.common.tile.TileEntityMultimeter
 * JD-Core Version:    0.6.2
 */