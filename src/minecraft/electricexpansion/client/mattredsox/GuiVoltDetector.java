package electricexpansion.client.mattredsox;

import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;


import org.lwjgl.opengl.GL11;

import electricexpansion.mattredsox.TileEntityVoltDetector;

import universalelectricity.UniversalElectricity;
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;

public class GuiVoltDetector extends GuiScreen {

	private TileEntityVoltDetector voltDetect;
	private ForgeDirection dir;

	protected int xSize = 176;

	protected int ySize = 166;

	public GuiVoltDetector(InventoryPlayer inventory, TileEntityVoltDetector voltDetect) {
		super();
		this.voltDetect = voltDetect;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void drawBackgroundLayer(float par1, int par2, int par3) {
		int var4 = this.mc.renderEngine.getTexture("/electricexpansion/VoltDetectorGUI.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
	protected void drawForegroundLayer() {
		this.drawCenteredString(mc.fontRenderer, ElectricInfo.getDisplay(voltDetect.voltsin, ElectricUnit.VOLTAGE), (this.width - (this.xSize - 180)) / 2, (this.height - (this.ySize - 80)) / 2, 0xFFFFFF);

		mc.fontRenderer.drawString("Volt Detector", (this.width - (this.xSize - 110)) / 2, (this.height - (this.ySize - 20)) / 2, 0x333333);
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawBackgroundLayer(par3, par1, par2);
		this.drawForegroundLayer();
		super.drawScreen(par1, par2, par3);
	}

	public void initGui() {
		super.initGui();
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