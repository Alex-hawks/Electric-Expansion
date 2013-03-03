/*    */ package electricexpansion.client.gui;
/*    */ 
/*    */ import cpw.mods.fml.relauncher.Side;
/*    */ import cpw.mods.fml.relauncher.SideOnly;
/*    */ import electricexpansion.common.containers.ContainerAdvBatteryBox;
/*    */ import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.client.renderer.RenderEngine;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.util.StatCollector;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import universalelectricity.core.electricity.ElectricInfo;
/*    */ import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ public class GuiAdvancedBatteryBox extends GuiContainer
/*    */ {
/*    */   private TileEntityAdvancedBatteryBox tileEntity;
/*    */   private int containerWidth;
/*    */   private int containerHeight;
/*    */ 
/*    */   public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox AdvBatteryBox)
/*    */   {
/* 27 */     super(new ContainerAdvBatteryBox(par1InventoryPlayer, AdvBatteryBox));
/* 28 */     this.tileEntity = AdvBatteryBox;
/*    */   }
/*    */ 
/*    */   protected void drawGuiContainerForegroundLayer(int par1, int par2)
/*    */   {
/* 37 */     this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
/* 38 */     String displayJoules = ElectricInfo.getDisplayShort(this.tileEntity.getJoules(new Object[0]), ElectricInfo.ElectricUnit.JOULES);
/* 39 */     String displayMaxJoules = ElectricInfo.getDisplayShort(this.tileEntity.getMaxJoules(new Object[0]), ElectricInfo.ElectricUnit.JOULES);
/* 40 */     String displayInputVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getInputVoltage(), ElectricInfo.ElectricUnit.VOLTAGE);
/* 41 */     String displayOutputVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(new Object[0]), ElectricInfo.ElectricUnit.VOLTAGE);
/*    */ 
/* 44 */     if (this.tileEntity.isDisabled())
/*    */     {
/* 46 */       displayMaxJoules = "Disabled";
/*    */     }
/*    */ 
/* 49 */     this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 25, 4210752);
/* 50 */     this.fontRenderer.drawString(displayMaxJoules, 70, 35, 4210752);
/* 51 */     this.fontRenderer.drawString("Voltage: " + displayOutputVoltage, 65, 55, 4210752);
/* 52 */     this.fontRenderer.drawString("Input: " + displayInputVoltage, 65, 65, 4210752);
/* 53 */     this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
/*    */   }
/*    */ 
/*    */   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
/*    */   {
/* 62 */     int var4 = this.mc.renderEngine.getTexture("/electricexpansion/textures/BatBox.png");
/* 63 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 64 */     this.mc.renderEngine.bindTexture(var4);
/*    */ 
/* 66 */     this.containerWidth = ((this.width - this.xSize) / 2);
/* 67 */     this.containerHeight = ((this.height - this.ySize) / 2);
/* 68 */     drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
/* 69 */     int scale = (int)(this.tileEntity.getJoules(new Object[0]) / this.tileEntity.getMaxJoules(new Object[0]) * 72.0D);
/* 70 */     drawTexturedModalRect(this.containerWidth + 64, this.containerHeight + 46, 176, 0, scale, 20);
/*    */   }
/*    */ }

/* Location:           C:\Users\Matt\Downloads\ElectricExpansion_v1.4.7.zip
 * Qualified Name:     electricexpansion.client.gui.GuiAdvancedBatteryBox
 * JD-Core Version:    0.6.2
 */