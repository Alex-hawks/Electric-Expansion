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
import electricexpansion.common.containers.ContainerAdvBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;

@SideOnly(Side.CLIENT)
public class GuiAdvancedBatteryBox extends GuiContainer
{
    private TileEntityAdvancedBatteryBox tileEntity;
    private int containerWidth;
    private int containerHeight;
    
    public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox AdvBatteryBox)
    {
        super(new ContainerAdvBatteryBox(par1InventoryPlayer, AdvBatteryBox));
        this.tileEntity = AdvBatteryBox;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
        String displayJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getJoules(), ElectricUnit.JOULES);
        String displayMaxJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getMaxJoules(),
                ElectricUnit.JOULES);
        String displayInputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getInputVoltage(),
                ElectricUnit.VOLTAGE);
        String displayOutputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getVoltage(),
                ElectricUnit.VOLTAGE);
        
        if (this.tileEntity.isDisabled())
        {
            displayMaxJoules = "Disabled";
        }
        
        this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 25, 4210752);
        this.fontRenderer.drawString(displayMaxJoules, 70, 35, 4210752);
        this.fontRenderer.drawString("Voltage: " + displayOutputVoltage, 65, 55, 4210752);
        this.fontRenderer.drawString("Input: " + displayInputVoltage, 65, 65, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2,
                4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiAdvancedBatteryBox.getTexture());
        
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
        int scale = (int) (this.tileEntity.getJoules() / this.tileEntity.getMaxJoules() * 72.0D);
        this.drawTexturedModalRect(this.containerWidth + 64, this.containerHeight + 46, 176, 0, scale, 20);
    }
    
    public static String getTexture()
    {
        return ElectricExpansion.GUI_PATH + "GuiBatBox.png";
    }
}
