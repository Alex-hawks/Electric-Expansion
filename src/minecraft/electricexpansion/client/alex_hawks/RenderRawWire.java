package electricexpansion.client.alex_hawks;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;
import electricexpansion.api.CableConnectionInterfaces.IPanelElectricMachine;


public class RenderRawWire extends TileEntitySpecialRenderer
{
	private ModelRawWire model;

	public RenderRawWire()
	{
		model = new ModelRawWire();
	}
	public void renderAModelAt(TileEntityRawWire tileEntity, double x, double y, double z, float f)
	{
		String textureToUse = null;
		int meta = tileEntity.getBlockMetadata();
		if(meta != -1)
		{
			if(meta == 0)
				textureToUse = EECommonProxy.ATEXTURES + "RawCopperWire.png";
			else if(meta == 1)
				textureToUse = EECommonProxy.ATEXTURES + "RawTinWire";
			else if(meta == 2)
				textureToUse = EECommonProxy.ATEXTURES + "RawSilverWire";
			else if(meta == 3)
				textureToUse = EECommonProxy.ATEXTURES + "RawHVWire";
		}

		//Texture file
		bindTextureByName(textureToUse);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		int[] metaConnected = new int[6];
		Block[] neighbors = new Block[6];
		for(int i=0; i<6; i++)
			if(tileEntity.connectedBlocks[i] != null)
				neighbors[i] = tileEntity.connectedBlocks[i].getBlockType();
		for(int i=0; i<6; i++)
			if(tileEntity.connectedBlocks[i] != null)
				metaConnected[i] = tileEntity.connectedBlocks[i].blockMetadata;
		for(int i=2; i<6; i++)
			if(tileEntity.connectedBlocks[i] != null)
				if(neighbors[i] instanceof IPanelElectricMachine)
					model.renderPanel();
		
		if (tileEntity.connectedBlocks[0] != null)
		{model.renderBottom();}
		if (tileEntity.connectedBlocks[1] != null)
		{model.renderTop();}
		if (tileEntity.connectedBlocks[2] != null)
		{
			if (neighbors[2] instanceof IPanelElectricMachine)
				if(((IPanelElectricMachine)neighbors[2]).canConnectToBase(metaConnected[2], ForgeDirection.getOrientation(3)))
					model.renderPanelBack();
			else model.renderBack();
		}
		if (tileEntity.connectedBlocks[3] != null)
		{
			if (neighbors[3] instanceof IPanelElectricMachine)
				if(((IPanelElectricMachine)neighbors[3]).canConnectToBase(metaConnected[3], ForgeDirection.getOrientation(2)))
					model.renderPanelFront();
			else model.renderFront();
		}
		if (tileEntity.connectedBlocks[4] != null)
		{
			if (neighbors[4] instanceof IPanelElectricMachine)
				if(((IPanelElectricMachine)neighbors[4]).canConnectToBase(metaConnected[4], ForgeDirection.getOrientation(5)))
					model.renderPanelLeft();
			else model.renderLeft();
		}
		if (tileEntity.connectedBlocks[5] != null)
		{
			if (neighbors[5] instanceof IPanelElectricMachine)
				if(((IPanelElectricMachine)neighbors[5]).canConnectToBase(metaConnected[5], ForgeDirection.getOrientation(4)))
					model.renderPanelRight();
			else model.renderRight();
		}

		model.renderMiddle();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt((TileEntityRawWire)tileEntity, var2, var4, var6, var8);
	}
}