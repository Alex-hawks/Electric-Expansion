package electricexpansion.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.tile.TileEntityMultimeter;

/**
 * Class used to render text onto the multimeter block.
 * 
 * @author Calclavia
 * 
 */
@SideOnly(Side.CLIENT)
public class RenderMultimeter extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float var8)
    {
        TileEntityMultimeter te = (TileEntityMultimeter) var1;
        ForgeDirection direction = te.getDirection(te.worldObj, te.xCoord, te.yCoord, te.zCoord);
        
        /**
         * Render from side 2 to 6. This means render all sides excluding top
         * and bottom.
         */
        for (int side = 0; side < 6; side++)
        {
            
            if (direction.ordinal() != side)
            {
                GL11.glPushMatrix();
                GL11.glPolygonOffset(-10, -10);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                
                float dx = 1F / 16;
                float dz = 1F / 16;
                float displayWidth = 1 - 2F / 16;
                float displayHeight = 1 - 2F / 16;
                GL11.glTranslatef((float) x, (float) y, (float) z);
                
                switch (side)
                {
                    case 1:
                        break;
                    case 0:
                        GL11.glTranslatef(1, 1, 0);
                        GL11.glRotatef(180, 1, 0, 0);
                        GL11.glRotatef(180, 0, 1, 0);
                        
                        break;
                    case 3:
                        GL11.glTranslatef(0, 1, 0);
                        GL11.glRotatef(0, 0, 1, 0);
                        GL11.glRotatef(90, 1, 0, 0);
                        
                        break;
                    case 2:
                        GL11.glTranslatef(1, 1, 1);
                        GL11.glRotatef(180, 0, 1, 0);
                        GL11.glRotatef(90, 1, 0, 0);
                        
                        break;
                    case 5:
                        GL11.glTranslatef(0, 1, 1);
                        GL11.glRotatef(90, 0, 1, 0);
                        GL11.glRotatef(90, 1, 0, 0);
                        
                        break;
                    case 4:
                        GL11.glTranslatef(1, 1, 0);
                        GL11.glRotatef(-90, 0, 1, 0);
                        GL11.glRotatef(90, 1, 0, 0);
                        
                        break;
                }
                
                GL11.glTranslatef(dx + displayWidth / 2, 1F, dz + displayHeight / 2);
                GL11.glRotatef(-90, 1, 0, 0);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                FontRenderer fontRenderer = this.getFontRenderer();
                int maxWidth = 1;
                
                String amperes = ElectricityDisplay.getDisplay(te.electricityReading.amperes, ElectricUnit.AMPERE);
                String voltage = ElectricityDisplay.getDisplay(te.electricityReading.voltage, ElectricUnit.VOLTAGE);
                String watt = ElectricityDisplay.getDisplay(te.electricityReading.getWatts(), ElectricUnit.WATT);
                
                maxWidth = Math.max(fontRenderer.getStringWidth(amperes), maxWidth);
                maxWidth = Math.max(fontRenderer.getStringWidth(voltage), maxWidth);
                maxWidth = Math.max(fontRenderer.getStringWidth(watt), maxWidth);
                maxWidth += 4;
                int lineHeight = fontRenderer.FONT_HEIGHT + 2;
                int requiredHeight = lineHeight * 1;
                float scaleX = displayWidth / maxWidth;
                float scaleY = displayHeight / requiredHeight;
                float scale = (float) (Math.min(scaleX, scaleY) * 0.8);
                GL11.glScalef(scale, -scale, scale);
                GL11.glDepthMask(false);
                
                int offsetX;
                int offsetY;
                int realHeight = (int) Math.floor(displayHeight / scale);
                int realWidth = (int) Math.floor(displayWidth / scale);
                
                offsetY = (realHeight - requiredHeight) / 2;
                offsetX = (realWidth - maxWidth) / 2 + 2 + 5;
                
                GL11.glDisable(GL11.GL_LIGHTING);
                fontRenderer.drawString(amperes, offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 - 1 * lineHeight, 1);
                fontRenderer.drawString(voltage, offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 0 * lineHeight, 1);
                fontRenderer.drawString(watt, offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 1 * lineHeight, 1);
                
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDepthMask(true);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPopMatrix();
            }
        }
    }
}