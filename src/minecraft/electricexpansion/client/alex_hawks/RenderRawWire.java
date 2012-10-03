package electricexpansion.client.alex_hawks;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;


public class RenderRawWire extends TileEntitySpecialRenderer
{
    private ModelRawWire model;

    public RenderRawWire()
    {
        model = new ModelRawWire();
    }
    public void renderAModelAt(TileEntityRawWire tileEntity, double x, double y, double z, float f)
    {
    	String textureToUse = null;
    	int meta = tileEntity.getBlockMetadata();
        if(meta != -1)
        {
        	if(meta == 0)
        		textureToUse = EECommonProxy.ATEXTURES + "RawCopperWire.png";
        	else if(meta == 1)
        		textureToUse = EECommonProxy.ATEXTURES + "RawTinWire";
        	else if(meta == 2)
        		textureToUse = EECommonProxy.ATEXTURES + "RawSilverWire";
        	else if(meta == 3)
        		textureToUse = EECommonProxy.ATEXTURES + "RawHVWire";
        }
        
    	//Texture file
    	bindTextureByName(textureToUse);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);

        if (tileEntity.connectedBlocks[0] != null)
        {
            model.renderBottom();
        }

        if (tileEntity.connectedBlocks[1] != null)
        {
            model.renderTop();
        }

        if (tileEntity.connectedBlocks[2] != null)
        {
        	model.renderBack();
        }

        if (tileEntity.connectedBlocks[3] != null)
        {
        	model.renderFront();
        }

        if (tileEntity.connectedBlocks[4] != null)
        {
        	model.renderLeft();
        }

        if (tileEntity.connectedBlocks[5] != null)
        {
        	model.renderRight();
        }

        model.renderMiddle();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderAModelAt((TileEntityRawWire)tileEntity, var2, var4, var6, var8);
    }
}