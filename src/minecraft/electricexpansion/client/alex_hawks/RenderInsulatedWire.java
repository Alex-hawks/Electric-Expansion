package electricexpansion.client.alex_hawks;

import org.lwjgl.opengl.GL11;

import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.cables.TileEntityInsulatedWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireOff;
import universalelectricity.basiccomponents.ModelCopperWire;
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
    public void renderAModelAt(TileEntity tileEntity, double x, double y, double z, float f)
    {
    	String textureToUse = null;
    	int ID = tileEntity.getBlockType().blockID;
    	int meta = tileEntity.getBlockMetadata();
        if(meta != -1)
        {
        	if(ID == ElectricExpansion.insulatedWire)
        	{
        		if(meta == 0)
        			textureToUse = EECommonProxy.ATEXTURES + "CopperWire.png";
        		else if(meta == 1)
        			textureToUse = EECommonProxy.ATEXTURES + "InsulatedTinWire.png";
        		else if(meta == 2)
        			textureToUse = EECommonProxy.ATEXTURES + "InsulatedSilverWire.png";
        		else if(meta == 3)
        			textureToUse = EECommonProxy.ATEXTURES + "InsulatedHVWire.png";
        	}
        	else if(ID == ElectricExpansion.onSwitchWire)
        	{
        		if(meta == 0)
        			textureToUse = EECommonProxy.ATEXTURES + "CopperSwitchWireOn.png";
        		else if(meta == 1)
        			textureToUse = EECommonProxy.ATEXTURES + "TinSwitchWireOn.png";
        		else if(meta == 2)
        			textureToUse = EECommonProxy.ATEXTURES + "SilverSwitchWireOn.png";
        		else if(meta == 3)
        			textureToUse = EECommonProxy.ATEXTURES + "HVSwitchWireOn.png";
        	}	
        	else if(ID == ElectricExpansion.offSwitchWire)
        	{
        		if(meta == 0)
        			textureToUse = EECommonProxy.ATEXTURES + "CopperSwitchWireOff.png";
        		else if(meta == 1)
        			textureToUse = EECommonProxy.ATEXTURES + "TinSwitchWireOff.png";
        		else if(meta == 2)
        			textureToUse = EECommonProxy.ATEXTURES + "SilverSwitchWireOff.png";
        		else if(meta == 3)
        			textureToUse = EECommonProxy.ATEXTURES + "HVSwitchWireOff.png";
        	}	
        }
        
    	//Texture file
    	bindTextureByName(textureToUse);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);

        if (tileEntity instanceof TileEntityInsulatedWire)
        {
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[0] != null) {model.renderBottom();}
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[1] != null) {model.renderTop();}
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[2] != null) {model.renderBack();}
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[3] != null) {model.renderFront();}
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[4] != null) {model.renderLeft();}
        if (((TileEntityInsulatedWire)tileEntity).connectedBlocks[5] != null) {model.renderRight();}
        }
        
        else if (tileEntity instanceof TileEntitySwitchWire)
        {
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[0] != null) {model.renderBottom();}
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[1] != null) {model.renderTop();}
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[2] != null) {model.renderBack();}
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[3] != null) {model.renderFront();}
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[4] != null) {model.renderLeft();}
        if (((TileEntitySwitchWire)tileEntity).connectedBlocks[5] != null) {model.renderRight();}
        }
        
        else if (tileEntity instanceof TileEntitySwitchWireOff)
        {
        	//model.renderNothing();
        }
        model.renderMiddle();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
    this.renderAModelAt(tileEntity, var2, var4, var6, var8);
    }
}
