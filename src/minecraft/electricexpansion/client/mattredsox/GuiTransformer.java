package net.minecraft.src.Transformer;

import net.minecraft.src.*;
import net.minecraft.src.universalelectricity.*;

import org.lwjgl.opengl.GL11;

public class GuiTransformer extends GuiScreen {
	private TileEntityTransformer transformer;

	protected int xSize = 176;

	protected int ySize = 166;

	public GuiTransformer(TileEntityTransformer transformer) {
		super();
		this.transformer = transformer;
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 1 && transformer.secondaryCoil < transformer.getMaxCoils()) {
			++transformer.secondaryCoil;
		} else if (par1GuiButton.id == 2 && transformer.secondaryCoil > 1) {
			--transformer.secondaryCoil;
		} else if (par1GuiButton.id == 3 && transformer.primaryCoil < transformer.getMaxCoils()) {
			++transformer.primaryCoil;
		} else if (par1GuiButton.id == 4 && transformer.primaryCoil > 1) {
			--transformer.primaryCoil;
		}
		
		transformer.worldObj.markBlockNeedsUpdate(transformer.xCoord, transformer.yCoord, transformer.zCoord);
		if(this.transformer.blockMetadata == 2) {
			transformer.worldObj.markBlockNeedsUpdate(transformer.xCoord, transformer.yCoord - 1, transformer.zCoord);
			transformer.worldObj.markBlockNeedsUpdate(transformer.xCoord, transformer.yCoord - 2, transformer.zCoord);
		}else if(this.transformer.blockMetadata == 1) {
			transformer.worldObj.markBlockNeedsUpdate(transformer.xCoord, transformer.yCoord - 1, transformer.zCoord);
		}
		super.actionPerformed(par1GuiButton);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void drawBackgroundLayer(float par1, int par2, int par3) {
		int var4 = this.mc.renderEngine.getTexture("/transformer/textures/TransformerGUI.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
	protected void drawForegroundLayer() {
		this.drawCenteredString(mc.fontRenderer, UniversalElectricity.getVoltDisplay((int) transformer.incommingvoltage), (this.width - (this.xSize - 140)) / 2, (this.height - (this.ySize - 110)) / 2, 0xFFFFFF);
		this.drawCenteredString(mc.fontRenderer, UniversalElectricity.getVoltDisplay((int) transformer.outgoingvoltage), (this.width - (this.xSize - 210)) / 2, (this.height - (this.ySize - 110)) / 2, 0xFFFFFF);
		this.drawCenteredString(mc.fontRenderer, Integer.toString(transformer.primaryCoil), (this.width - (this.xSize - 135)) / 2, (this.height - (this.ySize - 70)) / 2, 0xFFFFFF);
		this.drawCenteredString(mc.fontRenderer, Integer.toString(transformer.secondaryCoil), (this.width - (this.xSize - 215)) / 2, (this.height - (this.ySize - 70)) / 2, 0xFFFFFF);
		this.drawCenteredString(mc.fontRenderer, "\2477Output", (this.width + 115) / 2, (this.height - (this.ySize - 150)) / 2, 0xFFFFFF);
		this.drawCenteredString(mc.fontRenderer, "\247cInput", (this.width - (this.xSize - 62)) / 2, (this.height - (this.ySize - 150)) / 2, 0xFFFFFF);
		if(transformer.getBlockMetadata() == 0){
			this.drawCenteredString(mc.fontRenderer, "\247fTier 1", (this.width - (this.xSize - 320)) / 2, (this.height - (this.ySize - 10)) / 2, 0xFFFFFF);
		}else if(transformer.getBlockMetadata() == 1){
			this.drawCenteredString(mc.fontRenderer, "\247eTier 2", (this.width - (this.xSize - 320)) / 2, (this.height - (this.ySize - 10)) / 2, 0xFFFFFF);
		}else if(transformer.getBlockMetadata() == 2){
			this.drawCenteredString(mc.fontRenderer, "\2479Tier 3", (this.width - (this.xSize - 320)) / 2, (this.height - (this.ySize - 10)) / 2, 0xFFFFFF);
		}

		mc.fontRenderer.drawString("Transformer", (this.width - (this.xSize - 110)) / 2, (this.height - (this.ySize - 20)) / 2, 0x333333);
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawBackgroundLayer(par3, par1, par2);
		this.drawForegroundLayer();
		super.drawScreen(par1, par2, par3);
	}

	public void initGui() {
		super.initGui();
		this.controlList.add(new GuiButton(3, (this.width - (this.xSize - 12)) / 2, (this.height - (this.ySize - 50)) / 2, 50, 20, "Increase"));
		this.controlList.add(new GuiButton(4, (this.width - (this.xSize - 12)) / 2, (this.height - (this.ySize - 100)) / 2, 50, 20, "Decrease"));
		this.controlList.add(new GuiButton(1, (this.width + 65) / 2, (this.height - (this.ySize - 50)) / 2, 50, 20, "Increase"));
		this.controlList.add(new GuiButton(2, (this.width + 65) / 2, (this.height - (this.ySize - 100)) / 2, 50, 20, "Decrease"));
	}

	protected void keyTyped(char par1, int par2) {
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.thePlayer.closeScreen();
		}
	}

	public void updateScreen() {
		super.updateScreen();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}
	}
}
