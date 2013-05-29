package electricexpansion.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

@SideOnly(Side.CLIENT)
public class GuiAdvancedBatteryBox extends GuiContainer
{
	private TileEntityAdvancedBatteryBox tileEntity;

	private int guiTopLeftX;
	private int guiTopLeftY;

	private ArrayList<Byte> validModes;

	public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox te)
	{
		super(new ContainerAdvancedBatteryBox(par1InventoryPlayer, te));
		this.tileEntity = te;
		this.validModes = te.getAvailableModes();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		super.initGui();

		this.xSize = 220;

		this.guiTopLeftX = (this.width - this.xSize) / 2;
		this.guiTopLeftY = (this.height - this.ySize) / 2;

		this.buttonList.clear();

		this.buttonList.add(new GuiButton(0, this.guiTopLeftX + 180, this.guiTopLeftY + 66, 35, 20, this.tileEntity.getInput().name()));
		this.buttonList.add(new GuiButton(1, this.guiTopLeftX + 180, this.guiTopLeftY + 132, 35, 20, this.tileEntity.getOutput().name()));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 4, 6, 4210752);
		String displayJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getJoules(), ElectricUnit.JOULES);
		String displayMaxJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getMaxJoules(), ElectricUnit.JOULES);
		String displayInputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getInputVoltage(), ElectricUnit.VOLTAGE);
		String displayOutputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);

		if (this.tileEntity.isDisabled())
		{
			displayMaxJoules = "Disabled";
		}

		this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 25, 4210752);
		this.fontRenderer.drawString(displayMaxJoules, 70, 35, 4210752);
		this.fontRenderer.drawString("Output: " + displayOutputVoltage, 40, 55, 4210752);
		this.fontRenderer.drawString("Input: " + displayInputVoltage, 40, 65, 4210752);

		((GuiButton) this.buttonList.get(0)).displayString = this.tileEntity.getInput().name();
		((GuiButton) this.buttonList.get(1)).displayString = this.tileEntity.getOutput().name();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiAdvancedBatteryBox.getTexture());

		this.xSize = 220;

		this.guiTopLeftX = (this.width - this.xSize) / 2;
		this.guiTopLeftY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.guiTopLeftX, this.guiTopLeftY, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(this.guiTopLeftX + 197, guiTopLeftY + 41, this.tileEntity.getInputMode() * 17, 169, 16, 16);
		((GuiButton) this.buttonList.get(0)).enabled = (this.tileEntity.getInputMode() != 0); // Disable when mode 0

		this.drawTexturedModalRect(this.guiTopLeftX + 197, guiTopLeftY + 107, this.tileEntity.getOutputMode() * 17, 186, 16, 16);
		((GuiButton) this.buttonList.get(1)).enabled = (this.tileEntity.getOutputMode() != 0); // Disable when mode 0

		int scale = (int) (this.tileEntity.getJoules() / this.tileEntity.getMaxJoules() * 72.0D);
		this.drawTexturedModalRect(this.guiTopLeftX + 64, this.guiTopLeftY + 46, 0, 166, scale, 3);
	}

	@Override
	protected void mouseClicked(int x, int y, int buttonID)
	{
		super.mouseClicked(x, y, buttonID);

		if (x >= this.guiTopLeftX + 197 && x <= this.guiTopLeftX + 212)
		{
			if (y >= this.guiTopLeftY + 41 && y <= this.guiTopLeftY + 56)
			{
				int newMode = (this.validModes.indexOf(this.tileEntity.getInputMode()) + 1) % this.validModes.size();
				this.tileEntity.setInputMode((byte) this.validModes.get(newMode));
				return;
			}

			if (y >= this.guiTopLeftY + 107 && y <= this.guiTopLeftY + 122)
			{
				int newMode = (this.validModes.indexOf(this.tileEntity.getOutputMode()) + 1) % this.validModes.size();
				this.tileEntity.setOutputMode((byte) this.validModes.get(newMode));
				return;
			}
		}
	}

	@Override
	public void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
			case 0:
				this.tileEntity.setInputNext();
				break;
			case 1:
				this.tileEntity.setOutputNext();
				break;
			default:
				break;
		}
	}

	public static String getTexture()
	{
		return ElectricExpansion.GUI_PATH + "GuiBatBox.png";
	}
}
