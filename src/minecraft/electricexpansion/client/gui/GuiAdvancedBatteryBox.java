package electricexpansion.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerAdvBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;

@SideOnly(Side.CLIENT)
public class GuiAdvancedBatteryBox extends GuiContainer
{
	private TileEntityAdvancedBatteryBox tileEntity;
	private int containerWidth;
	private int containerHeight;

	public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox AdvBatteryBox)
	{
		super(new ContainerAdvBatteryBox(par1InventoryPlayer, AdvBatteryBox));
		this.tileEntity = AdvBatteryBox;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
		String displayJoules = ElectricInfo.getDisplayShort(this.tileEntity.getJoules(new Object[0]), ElectricInfo.ElectricUnit.JOULES);
		String displayMaxJoules = ElectricInfo.getDisplayShort(this.tileEntity.getMaxJoules(new Object[0]), ElectricInfo.ElectricUnit.JOULES);
		String displayInputVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getInputVoltage(), ElectricInfo.ElectricUnit.VOLTAGE);
		String displayOutputVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(new Object[0]), ElectricInfo.ElectricUnit.VOLTAGE);

		if (this.tileEntity.isDisabled())
		{
			displayMaxJoules = "Disabled";
		}

		this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 25, 4210752);
		this.fontRenderer.drawString(displayMaxJoules, 70, 35, 4210752);
		this.fontRenderer.drawString("Voltage: " + displayOutputVoltage, 65, 55, 4210752);
		this.fontRenderer.drawString("Input: " + displayInputVoltage, 65, 65, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("electricexpansiontexturesBatBox.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);

		this.containerWidth = ((this.width - this.xSize) / 2);
		this.containerHeight = ((this.height - this.ySize) / 2);
		drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
		int scale = (int)(this.tileEntity.getJoules(new Object[0]) / this.tileEntity.getMaxJoules(new Object[0]) * 72.0D);
		drawTexturedModalRect(this.containerWidth + 64, this.containerHeight + 46, 176, 0, scale, 20);
	}
}
