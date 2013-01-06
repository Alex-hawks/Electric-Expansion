package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerDistribution;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;

@SideOnly(Side.CLIENT)
public class GuiQuantumBatteryBox extends GuiContainer
{
	private TileEntityQuantumBatteryBox tileEntity;

	private GuiTextField textFieldFrequency;

	private int containerWidth;
	private int containerHeight;

	public GuiQuantumBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityQuantumBatteryBox tileEntity)
	{
		super(new ContainerDistribution(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		int var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.textFieldFrequency = new GuiTextField(this.fontRenderer, 6, 45, 49, 13);
		this.textFieldFrequency.setMaxStringLength(5);
		this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.textFieldFrequency.drawTextBox();

		String displayJoules = ElectricInfo.getDisplayShort(tileEntity.getJoulesForDisplay(), ElectricUnit.JOULES);

		this.fontRenderer.drawString(this.tileEntity.getInvName(), 42, 6, 4210752);
		this.fontRenderer.drawString("Current Frequency: " + tileEntity.getFrequency(), 10, 20, 4210752);
		this.fontRenderer.drawString("Current Storage: " + displayJoules, 10, 30, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(ElectricExpansion.ALEX_TEXTURE_PATH + "WPTGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.getJoulesForDisplay() > 0)
		{
			int scale = (int) (((double) this.tileEntity.getJoulesForDisplay() / this.tileEntity.getMaxJoules()) * 100);
			this.drawTexturedModalRect(containerWidth + 59, containerHeight + 51, 0, 169, scale, 5);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.textFieldFrequency.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		this.textFieldFrequency.textboxKeyTyped(par1, par2);

		try
		{
			short newFrequency = (short) Math.max(Short.parseShort(this.textFieldFrequency.getText()), 0);
			this.tileEntity.setFrequency(newFrequency);
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void updateScreen()
	{
		if (!this.textFieldFrequency.isFocused())
			this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
	}
}