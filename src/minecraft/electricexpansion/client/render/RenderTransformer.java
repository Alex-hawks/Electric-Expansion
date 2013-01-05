package electricexpansion.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelTransformer;
import electricexpansion.common.ElectricExpansion;

@SideOnly(Side.CLIENT)
public class RenderTransformer extends TileEntitySpecialRenderer
{
	private ModelTransformer model;
	
	public RenderTransformer()
	{
		this.model = new ModelTransformer();
	}

	public static final int TIER_1_META = 0;
	public static final int TIER_2_META = 4;
	public static final int TIER_3_META = 8;
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var3, double var4, float var5)
	{
		//TODO: I edited this off Github, didn't test. Also, refactor var2 var3... into x,y,z
		RenderFloatingText.renderFloatingText("Matt Failed", var2, var3, var4, 0);
		
		int metadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		if (metadata >= TIER_3_META)
		{
			bindTextureByName(ElectricExpansion.MATT_TEXTURE_PATH + "transformer3.png");
		}

		else if (metadata >= TIER_2_META)
		{
			bindTextureByName(ElectricExpansion.MATT_TEXTURE_PATH + "transformer2.png");
		}

		else
		{
			bindTextureByName(ElectricExpansion.MATT_TEXTURE_PATH + "transformer1.png");
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) var2 + 0.5F, (float) var3 + 1.5F, (float) var4 + 0.5F);

		if (metadata >= TIER_3_META)
		{
			switch (metadata - TIER_3_META)
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

		}
		else if (metadata >= TIER_2_META)
		{
			switch (metadata - TIER_2_META)
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
		}

		else
		{
			switch (metadata - TIER_1_META)
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
		}

		GL11.glScalef(1.0F, -1F, -1F);
		this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();

	}
}
