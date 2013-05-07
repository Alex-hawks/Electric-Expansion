package electricexpansion.client.gui;

import java.util.ArrayList;

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
    
    private int guiTopLeftX;
    private int guiTopLeftY;
    
    private byte displayInputMode;
    private byte displayOutputMode;
    
    private ArrayList<Byte> validModes;
    
    public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox te)
    {
        super(new ContainerAdvBatteryBox(par1InventoryPlayer, te));
        this.tileEntity = te;
        this.displayInputMode = te.getInputMode();
        this.displayOutputMode = te.getOutputMode();
        this.validModes = te.getAvailableModes();
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
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiAdvancedBatteryBox.getTexture());
        
        this.guiTopLeftX = (this.width - this.xSize) / 2;
        this.guiTopLeftY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.guiTopLeftX, this.guiTopLeftY, 0, 0, this.xSize / 4 * 5, this.ySize);
        int scale = (int) (this.tileEntity.getJoules() / this.tileEntity.getMaxJoules() * 72.0D);
        this.drawTexturedModalRect(this.guiTopLeftX + 64, this.guiTopLeftY + 46, 0, 166, scale, 3);
    }
    
    @Override
    protected void mouseClicked(int x, int y, int buttonID)
    {
        if (x >= this.guiTopLeftX + 197 && x <= this.guiTopLeftX + 212)
        {
            if (y >= this.guiTopLeftY + 10 && y <= this.guiTopLeftY + 25)
            {
                int currentMode = 0;
                for (int i = 0; i < this.validModes.size(); i++)
                {
                    if (this.validModes.get(i) == this.tileEntity.getInputMode())
                    {
                        currentMode = i;
                        break;
                    }
                }
                this.tileEntity.setInputMode((byte) (this.validModes.get(currentMode) + 1));
                this.displayInputMode = this.tileEntity.getInputMode();
                return;
            }
            if (y >= this.guiTopLeftY + 34 && y <= this.guiTopLeftY + 49)
            {
                int currentMode = 0;
                for (int i = 0; i < this.validModes.size(); i++)
                {
                    if (this.validModes.get(i) == this.tileEntity.getOutputMode())
                    {
                        currentMode = i;
                        break;
                    }
                }
                this.tileEntity.setOutputMode((byte) (this.validModes.get(currentMode) + 1));
                this.displayOutputMode = this.tileEntity.getOutputMode();
                return;
            }
        }
        else
            super.mouseClicked(x, y, buttonID); 
    }
    
    public static String getTexture()
    {
        return ElectricExpansion.GUI_PATH + "GuiBatBox.png";
    }
}
