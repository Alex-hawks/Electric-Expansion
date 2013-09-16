package electricexpansion.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.client.model.ModelInsulatedWire;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.helpers.TileEntityConductorBase;

@SideOnly(Side.CLIENT)
public class RenderInsulatedWire extends TileEntitySpecialRenderer
{
    private static final ModelInsulatedWire model = new ModelInsulatedWire();
    
    public void renderAModelAt(TileEntity t, double x, double y, double z, float f)
    {
        String textureToUse = null;
        int blockID = t.worldObj.getBlockId(t.xCoord, t.yCoord, t.zCoord);
        int metadata = t.worldObj.getBlockMetadata(t.xCoord, t.yCoord, t.zCoord);
        
        if (metadata != -1)
        {
            if (blockID == ElectricExpansionItems.blockInsulatedWire.blockID)
            {
                switch (metadata)
                {
                    case 0:
                        textureToUse= "InsulatedCopperWire.png";
                        break;
                    case 1:
                        textureToUse= "InsulatedTinWire.png";
                        break;
                    case 2:
                        textureToUse= "InsulatedSilverWire.png";
                        break;
                    case 3:
                        textureToUse = "InsulatedHVWire.png";
                        break;
                    case 4:
                        textureToUse = "InsulatedSCWire.png";
                        break;
                }
            }
            
            else if (blockID == ElectricExpansionItems.blockLogisticsWire.blockID)
            {
                
                switch (metadata)
                {
                    case 0:
                        textureToUse = "CopperLogisticsWire.png";
                        break;
                    case 1:
                        textureToUse = "TinLogisticsWire.png";
                        break;
                    case 2:
                        textureToUse = "SilverLogisticsWire.png";
                        break;
                    case 3:
                        textureToUse = "HVLogisticsWire.png";
                        break;
                    case 4:
                        textureToUse = "SCLogisticsWire.png";
                        break;
                }
                
            }
            
            else if (blockID == ElectricExpansionItems.blockSwitchWire.blockID)
            {
                if (t.getWorldObj().isBlockIndirectlyGettingPowered(t.xCoord, t.yCoord, t.zCoord))
                {
                    switch (metadata)
                    {
                        case 0:
                            textureToUse = "CopperSwitchWireOn.png";
                            break;
                        case 1:
                            textureToUse = "TinSwitchWireOn.png";
                            break;
                        case 2:
                            textureToUse = "SilverSwitchWireOn.png";
                            break;
                        case 3:
                            textureToUse = "HVSwitchWireOn.png";
                            break;
                        case 4:
                            textureToUse = "SCSwitchWireOn.png";
                            break;
                    }
                }
                
                else
                {
                    switch (metadata)
                    {
                        case 0:
                            textureToUse = "CopperSwitchWireOff.png";
                            break;
                        case 1:
                            textureToUse = "TinSwitchWireOff.png";
                            break;
                        case 2:
                            textureToUse = "SilverSwitchWireOff.png";
                            break;
                        case 3:
                            textureToUse = "HVSwitchWireOff.png";
                            break;
                        case 4:
                            textureToUse = "SCSwitchWireOff.png";
                            break;
                    }
                }
            }
            
            else if (blockID == ElectricExpansionItems.blockRedstonePaintedWire.blockID)
            {
                switch (metadata)
                {
                    case 0:
                        textureToUse = "CopperRSWire.png";
                        break;
                    case 1:
                        textureToUse = "TinRSWire.png";
                        break;
                    case 2:
                        textureToUse = "SilverRSWire.png";
                        break;
                    case 3:
                        textureToUse = "HVRSWire.png";
                        break;
                    case 4:
                        textureToUse = "SCRSWire.png";
                        break;
                }
            }
        }
        
        TileEntityConductorBase tileEntity = (TileEntityConductorBase) t;
        boolean[] connectedSides = tileEntity.getVisualConnections();
        
        if (textureToUse != null && textureToUse != "" && textureToUse != ElectricExpansion.MODEL_PATH)
            this.func_110628_a(new ResourceLocation(ElectricExpansion.DOMAIN, ElectricExpansion.MODEL_PATH + textureToUse));
        else
            return;
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        
        if (tileEntity instanceof TileEntitySwitchWire)
        {
            if (tileEntity.getWorldObj().isBlockIndirectlyGettingPowered(t.xCoord, t.yCoord, t.zCoord))
            {
                if (connectedSides[0])
                {
                    model.renderBottom();
                }
                if (connectedSides[1])
                {
                    model.renderTop();
                }
                if (connectedSides[2])
                {
                    model.renderBack();
                }
                if (connectedSides[3])
                {
                    model.renderFront();
                }
                if (connectedSides[4])
                {
                    model.renderLeft();
                }
                if (connectedSides[5])
                {
                    model.renderRight();
                }
            }
        }
        
        else
        {
            if (connectedSides[0])
            {
                model.renderBottom();
            }
            if (connectedSides[1])
            {
                model.renderTop();
            }
            if (connectedSides[2])
            {
                model.renderBack();
            }
            if (connectedSides[3])
            {
                model.renderFront();
            }
            if (connectedSides[4])
            {
                model.renderLeft();
            }
            if (connectedSides[5])
            {
                model.renderRight();
            }
        }
        
        model.renderMiddle();
        GL11.glPopMatrix();
        
        if (tileEntity instanceof TileEntityInsulatedWire)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
            GL11.glScalef(1.0F, -1F, -1F);
            
            this.func_110628_a(new ResourceLocation(ElectricExpansion.DOMAIN, ElectricExpansion.MODEL_PATH + "WirePaintOverlay.png"));
            
            byte colorByte = ((TileEntityInsulatedWire) tileEntity).getFrequency().getIndex();
            
            switch (colorByte)
            {
                case 0:
                    GL11.glColor4f(0.1F, 0.1F, 0.1F, 1f);
                    break;
                case 1:
                    GL11.glColor4f(1F, 0F, 0F, 1f);
                    break;
                case 2:
                    GL11.glColor4f(0F, 0.2F, 0F, 1f);
                    break;
                case 3:
                    GL11.glColor4f(0.2F, 0F, 0F, 1f);
                    break;
                case 4:
                    GL11.glColor4f(0F, 0F, 1.0F, 1f);
                    break;
                case 5:
                    GL11.glColor4f(0.6F, 0F, 0.4F, 1f);
                    break;
                case 6:
                    GL11.glColor4f(0.2F, 0.8F, 1.0F, 1f);
                    break;
                case 7:
                    GL11.glColor4f(0.6F, 0.6F, 0.6F, 1f);
                    break;
                case 8:
                    GL11.glColor4f(0.4F, 0.4F, 0.4F, 1f);
                    break;
                case 9:
                    GL11.glColor4f(1.0F, 0.2F, 0.6F, 1f);
                    break;
                case 10:
                    GL11.glColor4f(0.0F, 1F, 0.0F, 1f);
                    break;
                case 11:
                    GL11.glColor4f(1.0F, 1.0F, 0F, 1f);
                    break;
                case 12:
                    GL11.glColor4f(0.3F, 0.3F, 0.8F, 1f);
                    break;
                case 13:
                    GL11.glColor4f(0.8F, 0.2F, 0.4F, 1f);
                    break;
                case 14:
                    GL11.glColor4f(0.8F, 0.3F, 0F, 1f);
                    break;
                case 15:
                    GL11.glColor4f(1F, 1F, 1F, 1f);
                    break;
                    
                default:
                    GL11.glColor4f(0.2F, 0.2F, 0.2F, 1f);
                    break;
            }
            
            if (connectedSides[0])
            {
                model.renderBottom();
            }
            if (connectedSides[1])
            {
                model.renderTop();
            }
            if (connectedSides[2])
            {
                model.renderBack();
            }
            if (connectedSides[3])
            {
                model.renderFront();
            }
            if (connectedSides[4])
            {
                model.renderLeft();
            }
            if (connectedSides[5])
            {
                model.renderRight();
            }
            
            model.renderMiddle();
            GL11.glPopMatrix();
        }
        
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderAModelAt(tileEntity, var2, var4, var6, var8);
    }
}
