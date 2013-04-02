package electricexpansion.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerInsulationMachine;
import electricexpansion.common.tile.TileEntityInsulatingMachine;

@SideOnly(Side.CLIENT)
public class GuiInsulationMachine extends GuiContainer
{
    private TileEntityInsulatingMachine tileEntity;
    
    private int containerWidth;
    private int containerHeight;
    
    public GuiInsulationMachine(InventoryPlayer par1InventoryPlayer, TileEntityInsulatingMachine tileEntity)
    {
        super(new ContainerInsulationMachine(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Insulation Refiner", 60, 6, 4210752);
        String displayText = "";
        
        if (this.tileEntity.isDisabled())
        {
            displayText = "Disabled!";
        }
        else if (this.tileEntity.getProcessTimeLeft() > 0)
        {
            displayText = "Working";
        }
        else
        {
            displayText = "Idle";
        }
        
        this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRenderer.drawString(
                "Voltage: " + ElectricityDisplay.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE),
                82, 56, 4210752);
        this.fontRenderer.drawString(
                "Require: "
                        + ElectricityDisplay.getDisplayShort(this.tileEntity.WATTS_PER_TICK * 20, ElectricUnit.WATT),
                82, 68, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2,
                4210752);
    }
    
    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiInsulationMachine.getTexture());
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
        
        if (this.tileEntity.getProcessTimeLeft() >= 0)
        {
            int scale = (int) ((double) this.tileEntity.getProcessTimeLeft() / this.tileEntity.getProcessingTime() * 23);
            this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 27, 176, 0, 23 - scale, 13);
        }
        
        if (this.tileEntity.getJoules() >= 0)
        {
            int scale = (int) (this.tileEntity.getJoules() / this.tileEntity.getMaxJoules() * 50);
            this.drawTexturedModalRect(this.containerWidth + 35, this.containerHeight + 20, 176, 13, 4, 50 - scale);
        }
    }
    
    public static String getTexture()
    {
        return ElectricExpansion.GUI_PATH + "GuiEEMachine.png";
    }
}