package electricexpansion.client.alex_hawks;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;

public class RenderWireMill extends TileEntitySpecialRenderer
{
	private ModelWireMill model = new ModelWireMill();

	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
	{
		renderAModelAt((TileEntityWireMill) var1, var2, var4, var6, 1F);
	}

	private void renderAModelAt(TileEntityWireMill tileEntity, double x, double y, double z, float f)
	{
		int meta = tileEntity.getBlockMetadata();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		//bindTextureByName(EECommonProxy.ATEXTURES + "WireMill.png");
		bindTextureByName("electricexpansion/textures/alex_hawks/wiremill.png");

	  // switch(tileEntity.getBlockMetadata())
	   // {
		//    case 2: GL11.glRotatef(270, 0.0F, 1.0F, 0.0F); break;
		//	case 3: GL11.glRotatef(90, 0.0F, 1.0F, 0.0F); break;
		//	case 4: GL11.glRotatef(0, 0.0F, 1.0F, 0.0F); break;
		//	case 5: GL11.glRotatef(180, 0.0F, 1.0F, 0.0F); break;
	//    }

		GL11.glRotatef(180, 0f, 0f, 1f);
		model.render(null, 0.0625F, f, f, f, f, f);
		GL11.glPopMatrix();
	}
}