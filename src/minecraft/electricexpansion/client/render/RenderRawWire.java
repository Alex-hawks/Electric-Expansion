package electricexpansion.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import electricexpansion.api.CableInterfaces.IPanelElectricMachine;
import electricexpansion.client.model.ModelRawWire;
import electricexpansion.common.CommonProxy;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.helpers.TileEntityConductorBase;

@SideOnly(Side.CLIENT)
public class RenderRawWire extends TileEntitySpecialRenderer
{
	private ModelRawWire model;

	public RenderRawWire()
	{
		model = new ModelRawWire();
	}

	public void renderAModelAt(TileEntityRawWire t, double x, double y, double z, float f)
	{
		String textureToUse = null;
		int meta = t.getBlockMetadata();
		if (meta != -1)
		{
			if (meta == 0)
				textureToUse = CommonProxy.ATEXTURES + "RawCopperWire.png";
			else if (meta == 1)
				textureToUse = CommonProxy.ATEXTURES + "RawTinWire.png";
			else if (meta == 2)
				textureToUse = CommonProxy.ATEXTURES + "RawSilverWire.png";
			else if (meta == 3)
				textureToUse = CommonProxy.ATEXTURES + "RawHVWire.png";
			else if (meta == 4)
				textureToUse = CommonProxy.ATEXTURES + "RawEndiumWire.png";
		}

		// Texture file
		bindTextureByName(textureToUse);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		
		TileEntityConductorBase tileEntity = (TileEntityConductorBase) t;
		boolean[] connectedSides = tileEntity.visuallyConnected;

		/*
		 * TileEntity[] neighbors = new TileEntity[6];
		 * 
		 * for (int i = 0; i < 6; i++) if (connectedSides[i]) neighbors[i] = connectedSides[i];
		 * int[] metaConnected = new int[6]; for (int i = 0; i < 6; i++) if (connectedSides[i])
		 * metaConnected[i] = connectedSides[i].blockMetadata;
		 * 
		 * for (int i = 2; i < 6; i++) if (connectedSides[i]) if (neighbors[i] instanceof
		 * IPanelElectricMachine) model.renderBottom();
		 */

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
			/*
			 * if (neighbors[2] instanceof IPanelElectricMachine && ((IPanelElectricMachine)
			 * connectedSides[2]).canConnectToBase(metaConnected[2],
			 * ForgeDirection.getOrientation(3))) model.renderPanelBack(); else
			 */
			model.renderBack();
		}
		if (connectedSides[3])
		{
			/*
			 * if (neighbors[3] instanceof IPanelElectricMachine && ((IPanelElectricMachine)
			 * connectedSides[3]).canConnectToBase(metaConnected[3],
			 * ForgeDirection.getOrientation(2))) model.renderPanelFront(); else
			 */
			model.renderFront();
		}
		if (connectedSides[4])
		{
			/*
			 * if (neighbors[4] instanceof IPanelElectricMachine && ((IPanelElectricMachine)
			 * connectedSides[4]).canConnectToBase(metaConnected[4],
			 * ForgeDirection.getOrientation(5))) model.renderPanelLeft(); else
			 */
			model.renderLeft();
		}
		if (connectedSides[5])
		{
			/*
			 * if (neighbors[5] instanceof IPanelElectricMachine && ((IPanelElectricMachine)
			 * connectedSides[5]).canConnectToBase(metaConnected[5],
			 * ForgeDirection.getOrientation(4))) model.renderPanelRight(); else
			 */
			model.renderRight();
		}

		model.renderMiddle();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt((TileEntityRawWire) tileEntity, var2, var4, var6, var8);
	}
}