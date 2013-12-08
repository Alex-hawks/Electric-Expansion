package electricexpansion.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
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
        TileEntityTransformer te = (TileEntityTransformer) tileEntity;
        int metadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        String status = StatCollector.translateToLocal(te.stepUp ? "function.transformer.stepUp" : "function.transformer.stepDown");
        String name = StatCollector.translateToLocal(ElectricExpansionItems.blockTransformer.getUnlocalizedName() + "." + (int) Math.pow(2, metadata +1) + "x.name");
        
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        MovingObjectPosition movingPosition = player.rayTrace(5, 1f);
        
        
        
        if (movingPosition != null)
        {
            if (new Vector3(tileEntity).equals(new Vector3(movingPosition)))
            {
                RenderFloatingText.renderFloatingText(status, (float) ((float) x + .5), (float) y - 1, (float) ((float) z + .5));
                RenderFloatingText.renderFloatingText(name, (float) ((float) x + .5), (float) y - .70F, (float) ((float) z + .5));
            }
        }
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
        
        switch (metadata)
        {
            case 0:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_1;
                break;
            case 1:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_2;
                break;
            case 2:
                this.textureToUse = TextureLocations.MODEL_TRANSFORMER_3;
                break;
        }

        this.bindTexture(this.textureToUse);
        this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        this.model.renderCores(te.getInput(), te.getOutput(), 0.0625F);
        
        this.bindTexture(TextureLocations.MODEL_TRANSFORMER_INPUT);
        this.model.renderIO(te.getInput(), 0.0625F);
        
        this.bindTexture(TextureLocations.MODEL_TRANSFORMER_OUTPUT);
        this.model.renderIO(te.getOutput(), 0.0625F);
        
        GL11.glPopMatrix();
    }
}
