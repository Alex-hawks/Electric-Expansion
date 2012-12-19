package electricexpansion.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.containers.ContainerTransformer;
import electricexpansion.common.tile.TileEntityTransformer;

@SideOnly(Side.CLIENT)
public class GuiTransformer extends GuiContainer
{
	private TileEntityTransformer tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GuiTransformer(InventoryPlayer par1InventoryPlayer, TileEntityTransformer transformer)
	{
		super(new ContainerTransformer(par1InventoryPlayer, transformer));
		this.tileEntity = transformer;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 15, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture("/electricexpansion/textures/mattredsox/TransformerGUI.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
		// int scale = (int) (((double) this.tileEntity.getJoules() /
		// this.tileEntity.getMaxJoules()) * 72);
		// this.drawTexturedModalRect(containerWidth + 64, containerHeight + 51, 176, 0, scale, 20);
	}
}
