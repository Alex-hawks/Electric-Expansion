package electricexpansion.client.mattredsox;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import electricexpansion.mattredsox.*;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;

import universalelectricity.BasicComponents;
import universalelectricity.basiccomponents.TileEntityBatteryBox;
import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;


public class GUIAdvBatteryBox extends GuiContainer
{
    private TileEntityAdvBatteryBox tileEntity;

    private int containerWidth;
    private int containerHeight;
    
    public GUIAdvBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvBatteryBox batteryBox)
    {
        super(new ContainerAdvBatteryBox(par1InventoryPlayer, batteryBox));
        this.tileEntity = batteryBox;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 15, 6, 4210752);
        String displayJoules = ElectricInfo.getDisplay(tileEntity.getJoules(), ElectricUnit.WATT_HOUR, 3, true);
        String displayMaxJoules = ElectricInfo.getDisplaySimple(tileEntity.getMaxJoules(), ElectricUnit.WATT_HOUR, 0);

        if (this.tileEntity.isDisabled())
        {
            displayMaxJoules = "Disabled";
        }

        this.fontRenderer.drawString(displayJoules + " of", 98 - displayJoules.length(), 30, 4210752);
        this.fontRenderer.drawString(displayMaxJoules, 83, 40, 4210752);
        this.fontRenderer.drawString("Voltage: " + (int)this.tileEntity.getVoltage(), 90, 60, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(BasicComponents.FILE_PATH + "BatteryBox.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        
        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        int scale = (int)(((double)this.tileEntity.getJoules() / this.tileEntity.getMaxJoules()) * 72);
        this.drawTexturedModalRect(containerWidth + 87, containerHeight + 51, 176, 0, scale, 20);
    }
    
    @Override
    public void onGuiClosed()
    {
    }
}
