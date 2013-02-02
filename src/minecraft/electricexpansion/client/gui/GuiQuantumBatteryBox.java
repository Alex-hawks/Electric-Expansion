package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;
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
	
	private byte frequency;

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
		this.textFieldFrequency.setMaxStringLength(3);
		this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");

		this.controlList.clear();

		this.controlList.add(new GuiButton(0, var1 + 6, var2 + 60, 50, 20, "Set"));
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
		if(tileEntity.getOwningPlayer() != null)
			this.fontRenderer.drawString("Player: " + tileEntity.getOwningPlayer(), 65, 66, 4210752);
		else
			this.fontRenderer.drawString("I have no owner. BUG!", 62, 66, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(ElectricExpansion.TEXTURE_PATH + "WPTGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.getJoulesForDisplay() > 0)
		{
			int scale = (int) (((double) this.tileEntity.getJoulesForDisplay() / this.tileEntity.getMaxJoules()) * 72);
			this.drawTexturedModalRect(containerWidth + 70, containerHeight + 51, 0, 166, scale, 5);
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
		if(par2 == 28)
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, this.frequency));
		this.textFieldFrequency.textboxKeyTyped(par1, par2);

		try
		{
			byte newFrequency = (byte) Math.max(Byte.parseByte(this.textFieldFrequency.getText()), 0);
			this.frequency = newFrequency;
		}
		catch (Exception e)
		{
		}
	}

	public void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
			case 0:
				PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, this.frequency));
				break;
			default:
		}
	}

	@Override
	public void updateScreen()
	{
		if (!this.textFieldFrequency.isFocused())
			this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
	}
}