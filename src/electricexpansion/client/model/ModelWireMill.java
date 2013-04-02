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
        this.textureWidth = 128;
        this.textureHeight = 128;
        
        this.base = new ModelRenderer(this, 0, 0);
        this.base.addBox(0F, 0F, 0F, 16, 1, 16);
        this.base.setRotationPoint(-8F, 23F, -8F);
        this.base.setTextureSize(128, 128);
        this.base.mirror = true;
        this.setRotation(this.base, 0F, 0F, 0F);
        this.plug = new ModelRenderer(this, 0, 19);
        this.plug.addBox(0F, 0F, 0F, 5, 12, 12);
        this.plug.setRotationPoint(-7F, 11F, -4F);
        this.plug.setTextureSize(128, 128);
        this.plug.mirror = true;
        this.setRotation(this.plug, 0F, 0F, 0F);
        this.part1 = new ModelRenderer(this, 0, 20);
        this.part1.addBox(0F, -1F, 0F, 1, 2, 1);
        this.part1.setRotationPoint(-8F, 14F, 0F);
        this.part1.setTextureSize(128, 128);
        this.part1.mirror = true;
        this.setRotation(this.part1, 0F, 0F, 0F);
        this.part2 = new ModelRenderer(this, 0, 20);
        this.part2.addBox(0F, -1F, -3F, 1, 2, 1);
        this.part2.setRotationPoint(-8F, 14F, 0F);
        this.part2.setTextureSize(128, 128);
        this.part2.mirror = true;
        this.setRotation(this.part2, 0F, 0F, 0F);
        this.part3 = new ModelRenderer(this, 0, 20);
        this.part3.addBox(0F, -2F, -1F, 1, 1, 2);
        this.part3.setRotationPoint(-8F, 14F, -1F);
        this.part3.setTextureSize(128, 128);
        this.part3.mirror = true;
        this.setRotation(this.part3, 0F, 0F, 0F);
        this.part4 = new ModelRenderer(this, 0, 20);
        this.part4.addBox(0F, 1F, -1F, 1, 1, 2);
        this.part4.setRotationPoint(-8F, 14F, -1F);
        this.part4.setTextureSize(128, 128);
        this.part4.mirror = true;
        this.setRotation(this.part4, 0F, 0F, 0F);
        this.support = new ModelRenderer(this, 0, 46);
        this.support.addBox(0F, 0F, 0F, 5, 5, 4);
        this.support.setRotationPoint(-7F, 18F, -8F);
        this.support.setTextureSize(128, 128);
        this.support.mirror = true;
        this.setRotation(this.support, 0F, 0F, 0F);
        this.support2 = new ModelRenderer(this, 19, 46);
        this.support2.addBox(0F, 0F, 0F, 5, 8, 4);
        this.support2.setRotationPoint(-7F, 11F, -4F);
        this.support2.setTextureSize(128, 128);
        this.support2.mirror = true;
        this.setRotation(this.support2, -0.5235988F, 0F, 0F);
        this.container = new ModelRenderer(this, 48, 36);
        this.container.addBox(0F, 0F, 0F, 10, 15, 5);
        this.container.setRotationPoint(-2F, 8F, 3F);
        this.container.setTextureSize(128, 128);
        this.container.mirror = true;
        this.setRotation(this.container, 0F, 0F, 0F);
        this.support3 = new ModelRenderer(this, 80, 20);
        this.support3.addBox(0F, 0F, 0F, 2, 12, 4);
        this.support3.setRotationPoint(6F, 11F, -1F);
        this.support3.setTextureSize(128, 128);
        this.support3.mirror = true;
        this.setRotation(this.support3, 0F, 0F, 0F);
        this.support4 = new ModelRenderer(this, 80, 20);
        this.support4.addBox(0F, 0F, 0F, 2, 12, 4);
        this.support4.setRotationPoint(-2F, 11F, -1F);
        this.support4.setTextureSize(128, 128);
        this.support4.mirror = true;
        this.setRotation(this.support4, 0F, 0F, 0F);
        this.gear1rot = new ModelRenderer(this, 67, 13);
        this.gear1rot.addBox(0F, -1F, -1F, 6, 2, 2);
        this.gear1rot.setRotationPoint(0F, 14F, 1F);
        this.gear1rot.setTextureSize(128, 128);
        this.gear1rot.mirror = true;
        this.setRotation(this.gear1rot, 0.7853982F, 0F, 0F);
        this.gear2rot = new ModelRenderer(this, 67, 13);
        this.gear2rot.addBox(0F, -1F, -1F, 6, 2, 2);
        this.gear2rot.setRotationPoint(0F, 17F, 1F);
        this.gear2rot.setTextureSize(128, 128);
        this.gear2rot.mirror = true;
        this.setRotation(this.gear2rot, 0.7853982F, 0F, 0F);
        this.output = new ModelRenderer(this, 36, 20);
        this.output.addBox(0F, 0F, 0F, 10, 4, 11);
        this.output.setRotationPoint(-2F, 19F, -8F);
        this.output.setTextureSize(128, 128);
        this.output.mirror = true;
        this.setRotation(this.output, 0F, 0F, 0F);
    }
    
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.base.render(f5);
        this.plug.render(f5);
        this.part1.render(f5);
        this.part2.render(f5);
        this.part3.render(f5);
        this.part4.render(f5);
        this.support.render(f5);
        this.support2.render(f5);
        this.container.render(f5);
        this.support3.render(f5);
        this.support4.render(f5);
        this.gear1rot.render(f5);
        this.gear2rot.render(f5);
        this.output.render(f5);
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
