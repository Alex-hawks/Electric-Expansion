package electricexpansion.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTransformer extends ModelBase
{
    // fields
    ModelRenderer a;
    ModelRenderer b;
    ModelRenderer c;
    ModelRenderer d;
    ModelRenderer out2;
    ModelRenderer out1;
    ModelRenderer out3;
    ModelRenderer out4;
    ModelRenderer i;
    ModelRenderer j;
    ModelRenderer in1;
    ModelRenderer in2;
    ModelRenderer in3;
    ModelRenderer in4;
    
    public ModelTransformer()
    {
        this.textureWidth = 70;
        this.textureHeight = 45;
        
        this.a = new ModelRenderer(this, 0, 0);
        this.a.addBox(-8F, 0F, -8F, 16, 2, 16);
        this.a.setRotationPoint(0F, 22F, 0F);
        this.a.setTextureSize(70, 45);
        this.a.mirror = true;
        this.setRotation(this.a, 0F, 0F, 0F);
        this.b = new ModelRenderer(this, 0, 19);
        this.b.addBox(0F, 0F, -2F, 3, 11, 4);
        this.b.setRotationPoint(5F, 11F, 0F);
        this.b.setTextureSize(70, 45);
        this.b.mirror = true;
        this.setRotation(this.b, 0F, 0F, 0F);
        this.c = new ModelRenderer(this, 0, 19);
        this.c.addBox(0F, 0F, -2F, 3, 11, 4);
        this.c.setRotationPoint(-8F, 11F, 0F);
        this.c.setTextureSize(70, 45);
        this.c.mirror = true;
        this.setRotation(this.c, 0F, 0F, 0F);
        this.d = new ModelRenderer(this, 15, 19);
        this.d.addBox(0F, 0F, -2F, 16, 1, 4);
        this.d.setRotationPoint(-8F, 10F, 0F);
        this.d.setTextureSize(70, 45);
        this.d.mirror = true;
        this.setRotation(this.d, 0F, 0F, 0F);
        this.out2 = new ModelRenderer(this, 0, 35);
        this.out2.addBox(0F, 0F, -3F, 5, 0, 6);
        this.out2.setRotationPoint(-9F, 16F, 0F);
        this.out2.setTextureSize(70, 45);
        this.out2.mirror = true;
        this.setRotation(this.out2, 0F, 0F, 0F);
        this.out1 = new ModelRenderer(this, 0, 35);
        this.out1.addBox(0F, 0F, -3F, 5, 0, 6);
        this.out1.setRotationPoint(-9F, 15F, 0F);
        this.out1.setTextureSize(70, 45);
        this.out1.mirror = true;
        this.setRotation(this.out1, 0F, 0F, 0F);
        this.out3 = new ModelRenderer(this, 0, 35);
        this.out3.addBox(0F, 0F, -3F, 5, 0, 6);
        this.out3.setRotationPoint(-9F, 17F, 0F);
        this.out3.setTextureSize(70, 45);
        this.out3.mirror = true;
        this.setRotation(this.out3, 0F, 0F, 0F);
        this.out4 = new ModelRenderer(this, 0, 35);
        this.out4.addBox(0F, 0F, -3F, 5, 0, 6);
        this.out4.setRotationPoint(-9F, 18F, 0F);
        this.out4.setTextureSize(70, 45);
        this.out4.mirror = true;
        this.setRotation(this.out4, 0F, 0F, 0F);
        this.i = new ModelRenderer(this, 34, 35);
        this.i.addBox(0F, 0F, -1F, 2, 5, 2);
        this.i.setRotationPoint(-10F, 14F, 0F);
        this.i.setTextureSize(70, 45);
        this.i.mirror = true;
        this.setRotation(this.i, 0F, 0F, 0F);
        this.j = new ModelRenderer(this, 24, 35);
        this.j.addBox(0F, 0F, -1F, 2, 5, 2);
        this.j.setRotationPoint(8F, 14F, 0F);
        this.j.setTextureSize(70, 45);
        this.j.mirror = true;
        this.setRotation(this.j, 0F, 0F, 0F);
        this.in1 = new ModelRenderer(this, 0, 35);
        this.in1.addBox(0F, 0F, -3F, 5, 0, 6);
        this.in1.setRotationPoint(4F, 15F, 0F);
        this.in1.setTextureSize(70, 45);
        this.in1.mirror = true;
        this.setRotation(this.in1, 0F, 0F, 0F);
        this.in2 = new ModelRenderer(this, 0, 35);
        this.in2.addBox(0F, 0F, -3F, 5, 0, 6);
        this.in2.setRotationPoint(4F, 16F, 0F);
        this.in2.setTextureSize(70, 45);
        this.in2.mirror = true;
        this.setRotation(this.in2, 0F, 0F, 0F);
        this.in3 = new ModelRenderer(this, 0, 35);
        this.in3.addBox(0F, 0F, -3F, 5, 0, 6);
        this.in3.setRotationPoint(4F, 17F, 0F);
        this.in3.setTextureSize(70, 45);
        this.in3.mirror = true;
        this.setRotation(this.in3, 0F, 0F, 0F);
        this.in4 = new ModelRenderer(this, 0, 35);
        this.in4.addBox(0F, 0F, -3F, 5, 0, 6);
        this.in4.setRotationPoint(4F, 18F, 0F);
        this.in4.setTextureSize(70, 45);
        this.in4.mirror = true;
        this.setRotation(this.in4, 0F, 0F, 0F);
    }
    
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.a.render(f5);
        this.b.render(f5);
        this.c.render(f5);
        this.d.render(f5);
        this.out2.render(f5);
        this.out1.render(f5);
        this.out3.render(f5);
        this.out4.render(f5);
        this.i.render(f5);
        this.j.render(f5);
        this.in1.render(f5);
        this.in2.render(f5);
        this.in3.render(f5);
        this.in4.render(f5);
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
