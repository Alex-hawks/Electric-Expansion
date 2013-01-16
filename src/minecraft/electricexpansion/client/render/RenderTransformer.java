package electricexpansion.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelTransformer;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.tile.TileEntityTransformer;

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
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var5)
	{
		String status = "";

		if (((TileEntityTransformer) tileEntity).stepUp)
		{
			status = "Step Up";
		}
		else
		{
			status = "Step Down";
		}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		MovingObjectPosition movingPosition = player.rayTrace(5, 1f);

		if (movingPosition != null)
		{
			if (new Vector3(tileEntity).isEqual(new Vector3(movingPosition)))
			{
				RenderFloatingText.renderFloatingText(status, (float) ((float) x + .5), (float) y - 1, (float) ((float) z + .5));
			}
		}

		int metadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		if (metadata >= TIER_3_META)
		{
			bindTextureByName(ElectricExpansion.TEXTURE_PATH + "transformer3.png");
		}

		else if (metadata >= TIER_2_META)
		{
			bindTextureByName(ElectricExpansion.TEXTURE_PATH + "transformer2.png");
		}

		else
		{
			bindTextureByName(ElectricExpansion.TEXTURE_PATH + "transformer1.png");
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

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
