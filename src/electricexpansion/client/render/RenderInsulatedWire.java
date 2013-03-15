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
		String textureToUse = ElectricExpansion.TEXTURE_NAME_PREFIX;
		int blockID = t.worldObj.getBlockId(t.xCoord, t.yCoord, t.zCoord);
		int metadata = t.worldObj.getBlockMetadata(t.xCoord, t.yCoord, t.zCoord);

		if (metadata != -1)
		{
			if (blockID == ElectricExpansion.blockInsulatedWire.blockID)
			{
				System.out.print("Insulated Wire: ");
				switch (metadata)
				{
					case 0:
						textureToUse = textureToUse +  "InsulatedCopperWire.png";
						break;
					case 1:
						textureToUse = textureToUse +  "InsulatedTinWire.png";
						break;
					case 2:
						textureToUse = textureToUse +  "InsulatedSilverWire.png";
						break;
					case 3:
						textureToUse = textureToUse +  "InsulatedHVWire.png";
						break;
					case 4:
						textureToUse = textureToUse +  "InsulatedSCWire.png";
						break;
				}
				System.out.println(textureToUse);
			}

			else if (blockID == ElectricExpansion.blockLogisticsWire.blockID)
			{

				switch (metadata)
				{
					case 0:
						textureToUse = textureToUse +  "CopperLogisticsWire.png";
						break;
					case 1:
						textureToUse = textureToUse +  "TinLogisticsWire.png";
						break;
					case 2:
						textureToUse = textureToUse +  "SilverLogisticsWire.png";
						break;
					case 3:
						textureToUse = textureToUse +  "HVLogisticsWire.png";
						break;
					case 4:
						textureToUse = textureToUse +  "SCLogisticsWire.png";
						break;
				}

			}

			else if (blockID == ElectricExpansion.blockSwitchWire.blockID)
			{
				if (t.getWorldObj().isBlockIndirectlyGettingPowered(t.xCoord, t.yCoord, t.zCoord))
				{
					switch (metadata)
					{
						case 0:
							textureToUse = textureToUse +  "CopperSwitchWireOn.png";
							break;
						case 1:
							textureToUse = textureToUse +  "TinSwitchWireOn.png";
							break;
						case 2:
							textureToUse = textureToUse +  "SilverSwitchWireOn.png";
							break;
						case 3:
							textureToUse = textureToUse +  "HVSwitchWireOn.png";
							break;
						case 4:
							textureToUse = textureToUse +  "SCSwitchWireOn.png";
							break;
					}
				}

				else
				{
					switch (metadata)
					{
						case 0:
							textureToUse = textureToUse + "CopperSwitchWireOff.png";
							break;
						case 1:
							textureToUse = textureToUse + "TinSwitchWireOff.png";
							break;
						case 2:
							textureToUse = textureToUse + "SilverSwitchWireOff.png";
							break;
						case 3:
							textureToUse = textureToUse + "HVSwitchWireOff.png";
							break;
						case 4:
							textureToUse = textureToUse + "SCSwitchWireOff.png";
							break;
					}
				}
			}
		}

		TileEntityConductorBase tileEntity = (TileEntityConductorBase) t;
		boolean[] connectedSides = tileEntity.visuallyConnected;

		if (textureToUse != null && textureToUse != "" && textureToUse != ElectricExpansion.TEXTURE_NAME_PREFIX)
		{
			bindTextureByName(textureToUse);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		if (tileEntity instanceof TileEntityInsulatedWire || tileEntity instanceof TileEntityLogisticsWire)
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

		else if (tileEntity instanceof TileEntitySwitchWire)
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

		if (tileEntity instanceof TileEntityInsulatedWire)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
			GL11.glScalef(1.0F, -1F, -1F);

			this.bindTextureByName(ElectricExpansion.TEXTURE_NAME_PREFIX + "WirePaintOverlay.png");

			byte colorByte = ((TileEntityInsulatedWire) tileEntity).colorByte;

			switch (colorByte)
			{
				case -1:
					GL11.glColor4f(0.2F, 0.2F, 0.2F, 1f);
					break;
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
