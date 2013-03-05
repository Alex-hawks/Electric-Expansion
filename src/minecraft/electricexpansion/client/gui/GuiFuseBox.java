package electricexpansion.client.gui;

import universalelectricity.core.electricity.ElectricInfo;
import electricexpansion.common.containers.ContainerFuseBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class GuiFuseBox extends GuiContainer
{
	public final TileEntityFuseBox tileEntity;
	
	public GuiFuseBox(InventoryPlayer par1InventoryPlayer, TileEntityFuseBox tileEntity)
	{
		super(new ContainerFuseBox(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity; 
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
		String displayVoltage = ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(new Object[0]), ElectricInfo.ElectricUnit.VOLTAGE);

		this.fontRenderer.drawString("Voltage: " + displayVoltage, 65, 55, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

}
