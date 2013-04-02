package electricexpansion.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelWireMill;
import electricexpansion.common.ElectricExpansion;

@SideOnly(Side.CLIENT)
public class RenderWireMill extends TileEntitySpecialRenderer
{
    private ModelWireMill model;
    
    public RenderWireMill()
    {
        this.model = new ModelWireMill();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var3, double var4, float var5)
    {
        this.bindTextureByName(ElectricExpansion.MODEL_PATH + "wiremill.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2 + 0.5F, (float) var3 + 1.5F, (float) var4 + 0.5F);
        switch (var1.worldObj.getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord))
        {
            case 0:
                GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
                break;
            case 1:
                GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                break;
            case 2:
                GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                break;
            case 3:
                GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
                break;
        }
        
        GL11.glScalef(1.0F, -1F, -1F);
        this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
        
    }
    
}
