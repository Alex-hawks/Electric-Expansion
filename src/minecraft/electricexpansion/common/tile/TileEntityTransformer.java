/*     */ package electricexpansion.common.tile;
/*     */ 
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import electricexpansion.common.ElectricExpansion;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.INetworkManager;
/*     */ import net.minecraft.network.packet.Packet;
/*     */ import net.minecraft.network.packet.Packet250CustomPayload;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.ForgeDirection;
/*     */ import universalelectricity.core.electricity.ElectricityConnections;
/*     */ import universalelectricity.core.electricity.ElectricityNetwork;
/*     */ import universalelectricity.core.electricity.ElectricityPack;
/*     */ import universalelectricity.core.vector.Vector3;
/*     */ import universalelectricity.prefab.implement.IRotatable;
/*     */ import universalelectricity.prefab.network.IPacketReceiver;
/*     */ import universalelectricity.prefab.network.PacketManager;
/*     */ import universalelectricity.prefab.tile.TileEntityElectricityReceiver;
/*     */ 
/*     */ public class TileEntityTransformer extends TileEntityElectricityReceiver
/*     */   implements IRotatable, IPacketReceiver
/*     */ {
/*  28 */   public boolean stepUp = false;
/*     */   public int type;
/*     */ 
/*     */   public void initiate()
/*     */   {
/*  35 */     if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) >= 8)
/*     */     {
/*  37 */       this.type = 8;
/*     */     }
/*  40 */     else if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) >= 4)
/*     */     {
/*  42 */       this.type = 4;
/*     */     }
/*     */     else
/*     */     {
/*  47 */       this.type = 0;
/*     */     }
/*     */ 
/*  50 */     ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() - this.type + 2), ForgeDirection.getOrientation(getBlockMetadata() - this.type + 2).getOpposite()));
/*  51 */     this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockTransformer.blockID);
/*     */   }
/*     */ 
/*     */   public void updateEntity()
/*     */   {
/*  57 */     super.updateEntity();
/*     */ 
/*  59 */     if (!this.worldObj.isRemote)
/*     */     {
/*  61 */       ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() - this.type + 2).getOpposite();
/*  62 */       TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
/*     */ 
/*  65 */       ForgeDirection outputDirection = ForgeDirection.getOrientation(getBlockMetadata() - this.type + 2);
/*  66 */       TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
/*     */ 
/*  68 */       ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);
/*  69 */       ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
/*     */ 
/*  71 */       if ((outputNetwork != null) && (inputNetwork == null))
/*     */       {
/*  73 */         outputNetwork.stopProducing(this);
/*     */       }
/*  75 */       else if ((outputNetwork == null) && (inputNetwork != null))
/*     */       {
/*  77 */         inputNetwork.stopRequesting(this);
/*     */       }
/*     */ 
/*  80 */       if ((outputNetwork != null) && (inputNetwork != null))
/*     */       {
/*  82 */         if (outputNetwork != inputNetwork)
/*     */         {
/*  84 */           if (outputNetwork.getRequest(new TileEntity[0]).getWatts() > 0.0D)
/*     */           {
/*  86 */             inputNetwork.startRequesting(this, outputNetwork.getRequest(new TileEntity[0]));
/*  87 */             ElectricityPack actualEnergy = inputNetwork.consumeElectricity(this);
/*     */ 
/*  89 */             if (actualEnergy.getWatts() > 0.0D)
/*     */             {
/*  91 */               double typeChange = 0.0D;
/*     */ 
/*  93 */               if (this.type == 0)
/*     */               {
/*  95 */                 typeChange = 60.0D;
/*     */               }
/*  97 */               else if (this.type == 4)
/*     */               {
/*  99 */                 typeChange = 120.0D;
/*     */               }
/* 101 */               else if (this.type == 8)
/*     */               {
/* 103 */                 typeChange = 240.0D;
/*     */               }
/*     */ 
/* 106 */               double newVoltage = actualEnergy.voltage + typeChange;
/*     */ 
/* 108 */               if (!this.stepUp)
/*     */               {
/* 110 */                 newVoltage = actualEnergy.voltage - typeChange;
/*     */               }
/*     */ 
/* 113 */               outputNetwork.startProducing(this, actualEnergy.getWatts() / newVoltage, newVoltage);
/*     */             }
/*     */             else
/*     */             {
/* 117 */               outputNetwork.stopProducing(this);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 122 */             inputNetwork.stopRequesting(this);
/* 123 */             outputNetwork.stopProducing(this);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 128 */           inputNetwork.stopRequesting(this);
/* 129 */           outputNetwork.stopProducing(this);
/*     */         }
/*     */       }
/*     */ 
/* 133 */       if (!this.worldObj.isRemote)
/*     */       {
/* 135 */         PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/* 144 */     return PacketManager.getPacket("ElecEx", this, new Object[] { Boolean.valueOf(this.stepUp), Integer.valueOf(this.type) });
/*     */   }
/*     */ 
/*     */   public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
/*     */   {
/*     */     try
/*     */     {
/* 152 */       this.stepUp = dataStream.readBoolean();
/* 153 */       this.type = dataStream.readInt();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 157 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 167 */     super.readFromNBT(par1NBTTagCompound);
/* 168 */     this.stepUp = par1NBTTagCompound.getBoolean("stepUp");
/* 169 */     this.type = par1NBTTagCompound.getInteger("type");
/*     */   }
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 178 */     super.writeToNBT(par1NBTTagCompound);
/* 179 */     par1NBTTagCompound.setBoolean("stepUp", this.stepUp);
/* 180 */     par1NBTTagCompound.setInteger("type", this.type);
/*     */   }
/*     */ 
/*     */   public ForgeDirection getDirection()
/*     */   {
/* 186 */     return ForgeDirection.getOrientation(getBlockMetadata() - this.type);
/*     */   }
/*     */ 
/*     */   public void setDirection(ForgeDirection facingDirection)
/*     */   {
/* 192 */     this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
/*     */   }
/*     */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.common.tile.TileEntityTransformer
 * JD-Core Version:    0.6.2
 */