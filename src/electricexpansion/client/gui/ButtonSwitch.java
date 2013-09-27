package electricexpansion.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.misc.TextureLocations;

@SideOnly(Side.CLIENT)
public class ButtonSwitch extends GuiButton
{
    private boolean isActive;
    
    public ButtonSwitch(int par1, int par2, int par3, String par4Str, boolean initState)
    {
        this(par1, par2, par3, 200, 16, par4Str, initState);
    }
    
    public ButtonSwitch(int par1, int par2, int par3, int par4, int par5, String par6Str, boolean initState)
    {
        super(par1, par2, par3, par4, par5, par6Str);
        this.width = 200;
        this.height = 16;
        this.enabled = true;
        this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = par6Str;
        this.isActive = initState;
    }
    
    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        FontRenderer fontrenderer = par1Minecraft.fontRenderer;
        par1Minecraft.getTextureManager().bindTexture(TextureLocations.BTN_SWITCH);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
        
        int var5 = 0;
        
        if (this.isActive)
        {
            var5 = 16;
        }
        
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, var5, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, var5, this.width / 2, this.height);
        this.mouseDragged(par1Minecraft, par2, par3);
        
        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.isActive ? 0x006400 : 0xFF0000);
    }
    
    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.getHoverState(this.field_82253_i) == 2)
        {
            this.isActive = !this.isActive;
        }
        
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
    
}
