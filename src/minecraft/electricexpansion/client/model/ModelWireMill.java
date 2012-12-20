package electricexpansion.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWireMill extends ModelBase
{
	// fields
	ModelRenderer base;
	ModelRenderer plug;
	ModelRenderer part1;
	ModelRenderer part2;
	ModelRenderer part3;
	ModelRenderer part4;
	ModelRenderer support;
	ModelRenderer support2;
	ModelRenderer container;
	ModelRenderer support3;
	ModelRenderer support4;
	ModelRenderer gear1rot;
	ModelRenderer gear2rot;
	ModelRenderer output;

	public ModelWireMill()
	{
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(0F, 0F, 0F, 16, 1, 16);
		base.setRotationPoint(-8F, 23F, -8F);
		base.setTextureSize(128, 128);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		plug = new ModelRenderer(this, 0, 19);
		plug.addBox(0F, 0F, 0F, 5, 12, 12);
		plug.setRotationPoint(-7F, 11F, -4F);
		plug.setTextureSize(128, 128);
		plug.mirror = true;
		setRotation(plug, 0F, 0F, 0F);
		part1 = new ModelRenderer(this, 0, 20);
		part1.addBox(0F, -1F, 0F, 1, 2, 1);
		part1.setRotationPoint(-8F, 14F, 0F);
		part1.setTextureSize(128, 128);
		part1.mirror = true;
		setRotation(part1, 0F, 0F, 0F);
		part2 = new ModelRenderer(this, 0, 20);
		part2.addBox(0F, -1F, -3F, 1, 2, 1);
		part2.setRotationPoint(-8F, 14F, 0F);
		part2.setTextureSize(128, 128);
		part2.mirror = true;
		setRotation(part2, 0F, 0F, 0F);
		part3 = new ModelRenderer(this, 0, 20);
		part3.addBox(0F, -2F, -1F, 1, 1, 2);
		part3.setRotationPoint(-8F, 14F, -1F);
		part3.setTextureSize(128, 128);
		part3.mirror = true;
		setRotation(part3, 0F, 0F, 0F);
		part4 = new ModelRenderer(this, 0, 20);
		part4.addBox(0F, 1F, -1F, 1, 1, 2);
		part4.setRotationPoint(-8F, 14F, -1F);
		part4.setTextureSize(128, 128);
		part4.mirror = true;
		setRotation(part4, 0F, 0F, 0F);
		support = new ModelRenderer(this, 0, 46);
		support.addBox(0F, 0F, 0F, 5, 5, 4);
		support.setRotationPoint(-7F, 18F, -8F);
		support.setTextureSize(128, 128);
		support.mirror = true;
		setRotation(support, 0F, 0F, 0F);
		support2 = new ModelRenderer(this, 19, 46);
		support2.addBox(0F, 0F, 0F, 5, 8, 4);
		support2.setRotationPoint(-7F, 11F, -4F);
		support2.setTextureSize(128, 128);
		support2.mirror = true;
		setRotation(support2, -0.5235988F, 0F, 0F);
		container = new ModelRenderer(this, 48, 36);
		container.addBox(0F, 0F, 0F, 10, 15, 5);
		container.setRotationPoint(-2F, 8F, 3F);
		container.setTextureSize(128, 128);
		container.mirror = true;
		setRotation(container, 0F, 0F, 0F);
		support3 = new ModelRenderer(this, 80, 20);
		support3.addBox(0F, 0F, 0F, 2, 12, 4);
		support3.setRotationPoint(6F, 11F, -1F);
		support3.setTextureSize(128, 128);
		support3.mirror = true;
		setRotation(support3, 0F, 0F, 0F);
		support4 = new ModelRenderer(this, 80, 20);
		support4.addBox(0F, 0F, 0F, 2, 12, 4);
		support4.setRotationPoint(-2F, 11F, -1F);
		support4.setTextureSize(128, 128);
		support4.mirror = true;
		setRotation(support4, 0F, 0F, 0F);
		gear1rot = new ModelRenderer(this, 67, 13);
		gear1rot.addBox(0F, -1F, -1F, 6, 2, 2);
		gear1rot.setRotationPoint(0F, 14F, 1F);
		gear1rot.setTextureSize(128, 128);
		gear1rot.mirror = true;
		setRotation(gear1rot, 0.7853982F, 0F, 0F);
		gear2rot = new ModelRenderer(this, 67, 13);
		gear2rot.addBox(0F, -1F, -1F, 6, 2, 2);
		gear2rot.setRotationPoint(0F, 17F, 1F);
		gear2rot.setTextureSize(128, 128);
		gear2rot.mirror = true;
		setRotation(gear2rot, 0.7853982F, 0F, 0F);
		output = new ModelRenderer(this, 36, 20);
		output.addBox(0F, 0F, 0F, 10, 4, 11);
		output.setRotationPoint(-2F, 19F, -8F);
		output.setTextureSize(128, 128);
		output.mirror = true;
		setRotation(output, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		base.render(f5);
		plug.render(f5);
		part1.render(f5);
		part2.render(f5);
		part3.render(f5);
		part4.render(f5);
		support.render(f5);
		support2.render(f5);
		container.render(f5);
		support3.render(f5);
		support4.render(f5);
		gear1rot.render(f5);
		gear2rot.render(f5);
		output.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
