package electricexpansion.client.gui;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import electricexpansion.api.IItemFuse;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerFuseBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class GuiFuseBox extends GuiContainer
{
	public final TileEntityFuseBox tileEntity;
	
	private int containerWidth;
	private int containerHeight;

	public GuiFuseBox(InventoryPlayer par1InventoryPlayer, TileEntityFuseBox tileEntity)
	{
		super(new ContainerFuseBox(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity; 
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		int var4 = this.mc.renderEngine.getTexture(this.getTexture());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 8, 6, 4210752);
		String displayVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(new Object[0]), ElectricInfo.ElectricUnit.VOLTAGE);

		this.fontRenderer.drawString(StatCollector.translateToLocal("container.voltage") + ": " + displayVoltage, 65, 55, 4210752);
		if (this.tileEntity.getStackInSlot(0) != null)
		{
			ItemStack fuseStack = this.tileEntity.getStackInSlot(0);
			IItemFuse fuse = ((IItemFuse) fuseStack.getItem());
			this.fontRenderer.drawString(this.tileEntity.getStackInSlot(0).getItemName(), 30, 18, 4210752);
			this.fontRenderer.drawString(StatCollector.translateToLocal(fuse.getItemNameIS(fuseStack)), 30, 18, 4210752);
		}
	}
	
	public static String getTexture()
	{
		return ElectricExpansion.GUI_PATH + "GuiFuseBox.png";
	}

}
