package electricexpansion.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelInsulatedWire extends ModelBase
{
	ModelRenderer Middle;
	ModelRenderer Right;
	ModelRenderer Left;
	ModelRenderer Back;
	ModelRenderer Front;
	ModelRenderer Top;
	ModelRenderer Bottom;

	public ModelInsulatedWire()
	{
		textureWidth = 64;
		textureHeight = 32;

		Middle = new ModelRenderer(this, 0, 0);
		Middle.addBox(0F, 0F, 0F, 4, 4, 4);
		Middle.setRotationPoint(-2F, 14F, -2F);
		Middle.setTextureSize(textureWidth, textureHeight);
		Middle.mirror = true;
		setRotation(Middle, 0F, 0F, 0F);
		Right = new ModelRenderer(this, 22, 0);
		Right.addBox(0F, 0F, 0F, 6, 4, 4);
		Right.setRotationPoint(2F, 14F, -2F);
		Right.setTextureSize(textureWidth, textureHeight);
		Right.mirror = true;
		setRotation(Right, 0F, 0F, 0F);
		Left = new ModelRenderer(this, 44, 0);
		Left.addBox(0F, 0F, 0F, 6, 4, 4);
		Left.setRotationPoint(-8F, 14F, -2F);
		Left.setTextureSize(textureWidth, textureHeight);
		Left.mirror = true;
		setRotation(Left, 0F, 0F, 0F);
		Back = new ModelRenderer(this, 0, 10);
		Back.addBox(0F, 0F, 0F, 4, 4, 6);
		Back.setRotationPoint(-2F, 14F, 2F);
		Back.setTextureSize(textureWidth, textureHeight);
		Back.mirror = true;
		setRotation(Back, 0F, 0F, 0F);
		Front = new ModelRenderer(this, 0, 22);
		Front.addBox(0F, 0F, 0F, 4, 4, 6);
		Front.setRotationPoint(-2F, 14F, -8F);
		Front.setTextureSize(textureWidth, textureHeight);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		Top = new ModelRenderer(this, 22, 22);
		Top.addBox(0F, 0F, 0F, 4, 6, 4);
		Top.setRotationPoint(-2F, 8F, -2F);
		Top.setTextureSize(textureWidth, textureHeight);
		Top.mirror = true;
		setRotation(Top, 0F, 0F, 0F);
		Bottom = new ModelRenderer(this, 22, 10);
		Bottom.addBox(0F, 0F, 0F, 4, 6, 4);
		Bottom.setRotationPoint(-2F, 18F, -2F);
		Bottom.setTextureSize(textureWidth, textureHeight);
		Bottom.mirror = true;
		setRotation(Bottom, 0F, 0F, 0F);
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

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
