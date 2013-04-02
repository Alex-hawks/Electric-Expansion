package electricexpansion.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelRawWire;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.helpers.TileEntityConductorBase;

@SideOnly(Side.CLIENT)
public class RenderRawWire extends TileEntitySpecialRenderer
{
    private ModelRawWire model;
    
    public RenderRawWire()
    {
        this.model = new ModelRawWire();
    }
    
    public void renderAModelAt(TileEntityRawWire t, double x, double y,
            double z, float f)
    {
        String textureToUse = ElectricExpansion.MODEL_PATH;
        int meta = t.getBlockMetadata();
        if (meta != -1)
        {
            if (meta == 0)
            {
                textureToUse += "RawCopperWire.png";
            }
            else if (meta == 1)
            {
                textureToUse += "RawTinWire.png";
            }
            else if (meta == 2)
            {
                textureToUse += "RawSilverWire.png";
            }
            else if (meta == 3)
            {
                textureToUse += "RawHVWire.png";
            }
            else if (meta == 4)
            {
                textureToUse += "RawSCWire.png";
            }
        }
        
        // Texture file
        this.bindTextureByName(textureToUse);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        
        TileEntityConductorBase tileEntity = t;
        boolean[] connectedSides = tileEntity.visuallyConnected;
        
        if (connectedSides[0])
        {
            this.model.renderBottom();
        }
        if (connectedSides[1])
        {
            this.model.renderTop();
        }
        if (connectedSides[2])
        {
            this.model.renderBack();
        }
        if (connectedSides[3])
        {
            this.model.renderFront();
        }
        if (connectedSides[4])
        {
            this.model.renderLeft();
        }
        if (connectedSides[5])
        {
            this.model.renderRight();
        }
        
        this.model.renderMiddle();
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2,
            double var4, double var6, float var8)
    {
        this.renderAModelAt((TileEntityRawWire) tileEntity, var2, var4, var6,
                var8);
    }
}