/*     */ package electricexpansion.common.tile;
/*     */ 
/*     */ import com.google.common.io.ByteArrayDataInput;
/*     */ import electricexpansion.api.IItemFuse;
/*     */ import electricexpansion.common.ElectricExpansion;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
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
/*     */ public class TileEntityFuseBox extends TileEntityElectricityReceiver
/*     */   implements IRotatable, IPacketReceiver
/*     */ {
/*  29 */   private ItemStack inventory = null;
/*     */ 
/*     */   public void initiate()
/*     */   {
/*  34 */     ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getBlockMetadata() + 2), ForgeDirection.getOrientation(getBlockMetadata() + 2).getOpposite()));
/*  35 */     this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ElectricExpansion.blockTransformer.blockID);
/*     */   }
/*     */ 
/*     */   public void updateEntity()
/*     */   {
/*  41 */     super.updateEntity();
/*     */ 
/*  43 */     if (!this.worldObj.isRemote)
/*     */     {
/*  45 */       if (hasFuse())
/*     */       {
/*  47 */         ForgeDirection inputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2).getOpposite();
/*  48 */         TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);
/*     */ 
/*  50 */         ForgeDirection outputDirection = ForgeDirection.getOrientation(getBlockMetadata() + 2);
/*  51 */         TileEntity outputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), outputDirection);
/*     */ 
/*  53 */         ElectricityNetwork inputNetwork = ElectricityNetwork.getNetworkFromTileEntity(inputTile, inputDirection);
/*  54 */         ElectricityNetwork outputNetwork = ElectricityNetwork.getNetworkFromTileEntity(outputTile, outputDirection);
/*     */ 
/*  56 */         if ((outputNetwork != null) && (inputNetwork != null) && (outputNetwork != inputNetwork))
/*     */         {
/*  58 */           ElectricityPack request = outputNetwork.getRequest(new TileEntity[0]);
/*  59 */           inputNetwork.startRequesting(this, request);
/*     */ 
/*  61 */           ElectricityPack recieved = inputNetwork.consumeElectricity(this);
/*     */ 
/*  63 */           outputNetwork.startProducing(this, recieved);
/*     */ 
/*  65 */           if (recieved.voltage > ((IItemFuse)this.inventory.getItem()).getMaxVolts(this.inventory))
/*     */           {
/*  67 */             ((IItemFuse)this.inventory.getItem()).onFuseTrip(this.inventory);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  72 */           outputNetwork.stopProducing(this);
/*  73 */           inputNetwork.stopRequesting(this);
/*     */         }
/*     */       }
/*     */ 
/*  77 */       if (!this.worldObj.isRemote)
/*     */       {
/*  79 */         PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12.0D);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Packet getDescriptionPacket()
/*     */   {
/*  87 */     return PacketManager.getPacket("ElecEx", this, new Object[0]);
/*     */   }
/*     */ 
/*     */   public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void readFromNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 108 */     super.readFromNBT(par1NBTTagCompound);
/*     */   }
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound par1NBTTagCompound)
/*     */   {
/* 117 */     super.writeToNBT(par1NBTTagCompound);
/*     */   }
/*     */ 
/*     */   public ForgeDirection getDirection()
/*     */   {
/* 123 */     return ForgeDirection.getOrientation(getBlockMetadata());
/*     */   }
/*     */ 
/*     */   public void setDirection(ForgeDirection facingDirection)
/*     */   {
/* 129 */     this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
/*     */   }
/*     */ 
/*     */   public boolean hasFuse()
/*     */   {
/* 134 */     if (this.inventory != null)
/*     */     {
/* 136 */       if ((this.inventory.getItem() instanceof IItemFuse))
/*     */       {
/* 138 */         return ((IItemFuse)this.inventory.getItem()).isValidFuse(this.inventory);
/*     */       }
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.common.tile.TileEntityFuseBox
 * JD-Core Version:    0.6.2
 */