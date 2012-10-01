package additionalcables.client;

import org.lwjgl.opengl.GL11;

import universalelectricity.basiccomponents.BasicComponents;
import universalelectricity.basiccomponents.ModelCopperWire;

import additionalcables.ACCommonProxy;
import additionalcables.AdditionalCables;
import additionalcables.cables.TileEntityInsulatedWire;
import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

public class RenderInsulatedWire extends TileEntitySpecialRenderer 
{
	private ModelCopperWire model;

    public RenderInsulatedWire()
    {
        model = new ModelCopperWire();
    }
    public void renderAModelAt(TileEntityInsulatedWire tileEntity, double x, double y, double z, float f)
    {
    	String textureToUse = null;
    	int ID = tileEntity.getBlockType().blockID;
    	int meta = tileEntity.getBlockMetadata();
        if(meta != -1)
        {
        	if(ID == AdditionalCables.insulatedWire)
        	{
        		if(meta == 0)
        			textureToUse = ACCommonProxy.TEXTURES + "CopperWire.png";
        		else if(meta == 1)
        			textureToUse = ACCommonProxy.TEXTURES + "InsulatedTinWire.png";
        		else if(meta == 2)
        			textureToUse = ACCommonProxy.TEXTURES + "InsulatedSilverWire.png";
        		else if(meta == 3)
        			textureToUse = ACCommonProxy.TEXTURES + "InsulatedHVWire.png";
        	}
        	else if(ID == AdditionalCables.onSwitchWire)
        	{
        		if(meta == 0)
        			textureToUse = ACCommonProxy.TEXTURES + "CopperSwitchWireOn.png";
        		else if(meta == 1)
        			textureToUse = ACCommonProxy.TEXTURES + "TinSwitchWireOn.png";
        		else if(meta == 2)
        			textureToUse = ACCommonProxy.TEXTURES + "SilverSwitchWireOn.png";
        		else if(meta == 3)
        			textureToUse = ACCommonProxy.TEXTURES + "HVSwitchWireOn.png";
        	}	
        	else if(ID == AdditionalCables.offSwitchWire)
        	{
        		if(meta == 0)
        			textureToUse = ACCommonProxy.TEXTURES + "CopperSwitchWireOff.png";
        		else if(meta == 1)
        			textureToUse = ACCommonProxy.TEXTURES + "TinSwitchWireOff.png";
        		else if(meta == 2)
        			textureToUse = ACCommonProxy.TEXTURES + "SilverSwitchWireOff.png";
        		else if(meta == 3)
        			textureToUse = ACCommonProxy.TEXTURES + "HVSwitchWireOff.png";
        	}	
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
        this.renderAModelAt((TileEntityInsulatedWire)tileEntity, var2, var4, var6, var8);
    }
}
