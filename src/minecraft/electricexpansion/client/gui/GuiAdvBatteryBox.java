package electricexpansion.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerAdvBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

@SideOnly(Side.CLIENT)
public class GuiAdvBatteryBox extends GuiContainer
{
	private TileEntityAdvancedBatteryBox tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GuiAdvBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox AdvBatteryBox)
	{
		super(new ContainerAdvBatteryBox(par1InventoryPlayer, AdvBatteryBox));
		this.tileEntity = AdvBatteryBox;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 15, 6, 4210752);
		String displayJoules = ElectricInfo.getDisplayShort(tileEntity.getJoules(), ElectricUnit.JOULES);
		String displayMaxJoules = ElectricInfo.getDisplayShort(tileEntity.getMaxJoules(), ElectricUnit.JOULES);

		if (this.tileEntity.isDisabled())
		{
			displayMaxJoules = "Disabled";
		}

		this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 30, 4210752);
		this.fontRenderer.drawString(displayMaxJoules, 70, 40, 4210752);
		this.fontRenderer.drawString("Voltage: " + (int) this.tileEntity.getVoltage(), 65, 60, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/electricexpansion/textures/mattredsox/BatBox.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
		int scale = (int) (((double) this.tileEntity.getJoules() / this.tileEntity.getMaxJoules()) * 72);
		this.drawTexturedModalRect(containerWidth + 64, containerHeight + 52, 176, 0, scale, 20);
	}
}
