package electricexpansion.client.alex_hawks;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;
import electricexpansion.alex_hawks.misc.ContainerWireMill;

public class GuiWireMill extends GuiContainer
{
    private TileEntityWireMill tileEntity;

    private int containerWidth;
    private int containerHeight;
    
    public GuiWireMill(InventoryPlayer par1InventoryPlayer, TileEntityWireMill tileEntity)
    {
        super(new ContainerWireMill(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    /**
    * Draw the foreground layer for the GuiContainer (everything in front of the items)
    */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Wire Mill", 60, 6, 4210752);
        this.fontRenderer.drawString("Input:", 10, 28, 4210752);
        this.fontRenderer.drawString("Battery:", 10, 53, 4210752);
        String displayText = "";

        if (this.tileEntity.isDisabled())
        {
            displayText = "Disabled!";
        }
        else if (this.tileEntity.drawingTicks > 0)
        {
            displayText = "Working";
        }
        else
        {
            displayText = "Idle";
        }

        this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRenderer.drawString("Voltage: " + ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56, 4210752);
        this.fontRenderer.drawString("Require: " + ElectricInfo.getDisplayShort(this.tileEntity.WATTS_PER_TICK*20, ElectricUnit.WATT), 82, 68, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
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

        if (this.tileEntity.drawingTicks > 0)
        {
            int scale = (int)(((double)this.tileEntity.drawingTicks / this.tileEntity.getDrawingTime()) * 23);
            this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
        }
    }
}