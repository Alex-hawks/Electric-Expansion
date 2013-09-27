package electricexpansion.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.misc.TextureLocations;
import electricexpansion.client.model.ModelTransformer;
import electricexpansion.common.tile.TileEntityTransformer;

@SideOnly(Side.CLIENT)
public class RenderTransformer extends TileEntitySpecialRenderer
{
    private ModelTransformer model;
    private ResourceLocation textureToUse;
    
    public RenderTransformer()
    {
        this.model = new ModelTransformer();
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var5)
    {
        String status = ((TileEntityTransformer) tileEntity).stepUp ? "Step Up" : "Step Down";
        
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        
        MovingObjectPosition movingPosition = player.rayTrace(5, 1f);
        
        if (movingPosition != null)
        {
            if (new Vector3(tileEntity).equals(new Vector3(movingPosition)))
            {
                RenderFloatingText.renderFloatingText(status, (float) ((float) x + .5), (float) y - 1, (float) ((float) z + .5));
            }
        }
        
        int metadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        
        switch (metadata)
        {
            case 0:
            case 1:
            case 2:
            case 3:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_1;
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_2;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_3;
                break;
        }
        this.bindTexture(textureToUse);
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        
        switch (metadata % 4)
        {
            case 0:
                GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
                break;
            case 1:
                GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                break;
            case 2:
                GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
                break;
            case 3:
                GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                break;
        }
        
        GL11.glScalef(1.0F, -1F, -1F);
        this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
        
    }
}
