package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import electricexpansion.EECommonProxy;
import electricexpansion.api.WirelessPowerMachine;
import electricexpansion.containers.ContainerDistribution;
import electricexpansion.containers.ContainerInductionReciever;
import electricexpansion.containers.ContainerInductionSender;
import electricexpansion.tile.TileEntityDistribution;
import electricexpansion.tile.TileEntityInductionReciever;
import electricexpansion.tile.TileEntityInductionSender;

@SideOnly(Side.CLIENT)
public class GuiWPT extends GuiContainer
{
	private WirelessPowerMachine tileEntity;
	private Class<? extends WirelessPowerMachine> tileEntityType;

	private GuiTextField textField;

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
		this.fontRenderer.drawString(Text, 7, 46, 0xffffff);

		this.fontRenderer.drawString(((IInventory) this.tileEntity).getInvName(), 60, 6, 4210752);
		this.fontRenderer.drawString("Current Frequency: " + ((WirelessPowerMachine) tileEntity).getFrequency(), 10, 28, 4210752);
		String displayText = "";
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
		this.textField = new GuiTextField(this.fontRenderer, 6, 45, 49, 13);
		textField.setMaxStringLength(10);
		textField.setFocused(false);

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
		int var4 = this.mc.renderEngine.getTexture(EECommonProxy.ATEXTURES + "WPTGui.png");
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
		Text = textField.getText();
	}
}