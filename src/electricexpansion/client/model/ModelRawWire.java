package electricexpansion.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRawWire extends ModelBase
{
	ModelRenderer Middle;
	ModelRenderer Right;
	ModelRenderer Back;
	ModelRenderer Left;
	ModelRenderer Front;
	ModelRenderer Bottom;
	ModelRenderer Top;

	public ModelRawWire()
	{
		textureWidth = 64;
		textureHeight = 32;

		Middle = new ModelRenderer(this, 0, 0);
		Middle.addBox(0F, 0F, 0F, 2, 2, 2);
		Middle.setRotationPoint(-1F, 15F, -1F);
		Middle.setTextureSize(64, 32);
		Middle.mirror = true;
		setRotation(Middle, 0F, 0F, 0F);
		Right = new ModelRenderer(this, 22, 0);
		Right.addBox(0F, 0F, 0F, 7, 2, 2);
		Right.setRotationPoint(1F, 15F, -1F);
		Right.setTextureSize(64, 32);
		Right.mirror = true;
		setRotation(Right, 0F, 0F, 0F);
		Back = new ModelRenderer(this, 0, 10);
		Back.addBox(0F, 0F, 0F, 2, 2, 7);
		Back.setRotationPoint(-1F, 15F, 1F);
		Back.setTextureSize(64, 32);
		Back.mirror = true;
		setRotation(Back, 0F, 0F, 0F);
		Left = new ModelRenderer(this, 44, 0);
		Left.addBox(0F, 0F, 0F, 7, 2, 2);
		Left.setRotationPoint(-8F, 15F, -1F);
		Left.setTextureSize(64, 32);
		Left.mirror = true;
		setRotation(Left, 0F, 0F, 0F);
		Front = new ModelRenderer(this, 0, 22);
		Front.addBox(0F, 0F, 0F, 2, 2, 7);
		Front.setRotationPoint(-1F, 15F, -8F);
		Front.setTextureSize(64, 32);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		Bottom = new ModelRenderer(this, 22, 10);
		Bottom.addBox(0F, 0F, 0F, 2, 7, 2);
		Bottom.setRotationPoint(-1F, 17F, -1F);
		Bottom.setTextureSize(64, 32);
		Bottom.mirror = true;
		setRotation(Bottom, 0F, 0F, 0F);
		Top = new ModelRenderer(this, 22, 22);
		Top.addBox(0F, 0F, 0F, 2, 7, 2);
		Top.setRotationPoint(-1F, 8F, -1F);
		Top.setTextureSize(64, 32);
		Top.mirror = true;
		setRotation(Top, 0F, 0F, 0F);
	}

	public void renderMiddle()
	{
		Middle.render(0.0625F);
	}

	public void renderBottom()
	{
		Bottom.render(0.0625F);
	}

	public void renderTop()
	{
		Top.render(0.0625F);
	}

	public void renderLeft()
	{
		Left.render(0.0625F);
	}

	public void renderRight()
	{
		Right.render(0.0625F);
	}

	public void renderBack()
	{
		Back.render(0.0625F);
	}

	public void renderFront()
	{
		Front.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
