package electricexpansion.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.model.ModelInsulatedWire;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntityLogisticsWire;
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

		if (metadata != -1) {
			if (blockID == ElectricExpansion.blockInsulatedWire.blockID) 
			{
				if (metadata == 0)
					textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
							+ "InsulatedCopperWire.png";
				else if (metadata == 1)
					textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
							+ "InsulatedTinWire.png";
				else if (metadata == 2)
					textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
							+ "InsulatedSilverWire.png";
				else if (metadata == 3)
					textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
							+ "InsulatedHVWire.png";
				else if (metadata == 4)
					textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
							+ "InsulatedSCWire.png";
			}

			else if (blockID == ElectricExpansion.blockSwitchWire.blockID || blockID == ElectricExpansion.blockLogisticsWire.blockID) 
			{
				if (t.getWorldObj().isBlockIndirectlyGettingPowered(t.xCoord, t.yCoord, t.zCoord))
				{
					switch (metadata)
					{
					case 0:
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "CopperSwitchWireOn.png";
						break;
					case 1:
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "TinSwitchWireOn.png";
						break;
					case 2:
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "SilverSwitchWireOn.png";
						break;
					case 3:
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "HVSwitchWireOn.png";
						break;
					case 4:
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "SCSwitchWireOn.png";
						break;
					}
				}

				else {
					if (metadata == 0)
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "CopperSwitchWireOff.png";
					else if (metadata == 1)
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "TinSwitchWireOff.png";
					else if (metadata == 2)
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "SilverSwitchWireOff.png";
					else if (metadata == 3)
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "HVSwitchWireOff.png";
					else if (metadata == 4)
						textureToUse = ElectricExpansion.ALEX_TEXTURE_PATH
								+ "SCSwitchWireOff.png";
				}
			}
		}

		if (textureToUse != null) {
			bindTextureByName(textureToUse);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		TileEntityConductorBase tileEntity = (TileEntityConductorBase) t;
		boolean[] connectedSides = tileEntity.visuallyConnected;

		if (tileEntity instanceof TileEntityInsulatedWire)
		{
			if (connectedSides[0]) {
				model.renderBottom();
			}
			if (connectedSides[1]) {
				model.renderTop();
			}
			if (connectedSides[2]) {
				model.renderBack();
			}
			if (connectedSides[3]) {
				model.renderFront();
			}
			if (connectedSides[4]) {
				model.renderLeft();
			}
			if (connectedSides[5]) {
				model.renderRight();
			}
		}
		
		else if (tileEntity instanceof TileEntitySwitchWire || tileEntity instanceof TileEntityLogisticsWire) 
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
		model.renderMiddle();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt(tileEntity, var2, var4, var6, var8);
	}
}
