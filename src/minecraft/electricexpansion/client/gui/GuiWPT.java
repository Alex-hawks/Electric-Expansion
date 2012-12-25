package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.WirelessPowerMachine;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerDistribution;
import electricexpansion.common.containers.ContainerInductionReciever;
import electricexpansion.common.containers.ContainerInductionSender;
import electricexpansion.common.tile.TileEntityDistribution;
import electricexpansion.common.tile.TileEntityInductionReciever;
import electricexpansion.common.tile.TileEntityInductionSender;

@SideOnly(Side.CLIENT)
public class GuiWPT extends GuiContainer
{
	private WirelessPowerMachine tileEntity;
	private Class<? extends WirelessPowerMachine> tileEntityType;

	private GuiTextField textField;
	private GuiButton button;

	private String Text;

	private int containerWidth;
	private int containerHeight;

	public GuiWPT(InventoryPlayer par1InventoryPlayer, TileEntityInductionSender tileEntity)
	{
		super(new ContainerInductionSender(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.tileEntityType = TileEntityInductionSender.class;
	}

	public GuiWPT(InventoryPlayer par1InventoryPlayer, TileEntityInductionReciever tileEntity)
	{
		super(new ContainerInductionReciever(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.tileEntityType = TileEntityInductionReciever.class;
	}

	public GuiWPT(InventoryPlayer par1InventoryPlayer, TileEntityDistribution tileEntity)
	{
		super(new ContainerDistribution(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.tileEntityType = TileEntityDistribution.class;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		textField.drawTextBox();

		this.fontRenderer.drawString(((IInventory) this.tileEntity).getInvName(), 42, 6, 4210752);
		this.fontRenderer.drawString("Current Frequency: " + tileEntity.getFrequency(), 10, 28, 4210752);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		textField = new GuiTextField(this.fontRenderer, 6, 45, 49, 13);
		textField.setMaxStringLength(5);
		textField.setFocused(true);
		textField.setText(tileEntity.getFrequency() + "");

		button = new GuiButton(0, 6, 50, 50, 13, "Set");
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		textField.mouseClicked(par1, par2, par3);
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
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.getJoules() > 0)
		{
			int scale = (int) (((double) this.tileEntity.getJoules() / this.tileEntity.getMaxJoules()) * 72);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		textField.textboxKeyTyped(par1, par2);
	}

	@Override
	public void updateScreen()
	{
		int newFrequency = 0;
		if (this.textField.getText() != null && !(this.textField.getText().equals("")))
			newFrequency = Math.max(Integer.parseInt(this.textField.getText()), 0);
		if (newFrequency < 32768)
			tileEntity.setFrequency((short) newFrequency);
	}
}