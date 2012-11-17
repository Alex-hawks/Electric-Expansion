package electricexpansion.client.alex_hawks.gui;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.containers.ContainerDistribution;
import electricexpansion.alex_hawks.containers.ContainerInductionReciever;
import electricexpansion.alex_hawks.containers.ContainerInductionSender;
import electricexpansion.alex_hawks.machines.TileEntityDistribution;
import electricexpansion.alex_hawks.machines.TileEntityInductionReciever;
import electricexpansion.alex_hawks.machines.TileEntityInductionSender;
import electricexpansion.api.WirelessPowerMachine;

public class GuiWPT extends GuiContainer
{
	private WirelessPowerMachine tileEntity;
	private Class<? extends WirelessPowerMachine> tileEntityType;

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
		this.fontRenderer.drawString(((IInventory)this.tileEntity).getInvName(), 60, 6, 4210752);
		this.fontRenderer.drawString("Current Frequency: " + ((WirelessPowerMachine)tileEntity).getFrequency(), 10, 28, 4210752);
		String displayText = "";
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(EECommonProxy.ATEXTURES + "/WireMill.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.getJoules() > 0)
		{
			int scale = (int)(((double)this.tileEntity.getJoules() / this.tileEntity.getMaxJoules()) * 72);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
	}
}