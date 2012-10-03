package electricexpansion.client.alex_hawks;

import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

public class ModelRawWire extends ModelBase
{
    //fields
    ModelRenderer Middle;
    ModelRenderer Right;
    ModelRenderer Left;
    ModelRenderer Back;
    ModelRenderer Front;
    ModelRenderer Top;
    ModelRenderer Bottom;

    public ModelRawWire()
    {
        textureWidth = 64;
        textureHeight = 32;
        Middle = new ModelRenderer(this, 0, 0);
        Middle.addBox(-1, 15, -1, 2, 2, 2);
        Middle.setRotationPoint(0, 0, 0);
        Middle.setTextureSize(64, 32);
        Middle.mirror = true;
        setRotation(Middle, 0, 0, 0);
        Right = new ModelRenderer(this, 21, 0);
        Right.addBox(1, 15, -1, 7, 2, 2);
        Right.setRotationPoint(0, 0, 0);
        Right.setTextureSize(64, 32);
        Right.mirror = true;
        setRotation(Right, 0, 0, 0);
        Back = new ModelRenderer(this, 0, 11);
        Back.addBox(-1, 15, 1, 2, 2, 7);
        Back.setRotationPoint(0, 0, 0);
        Back.setTextureSize(64, 32);
        Back.mirror = true;
        setRotation(Back, 0F, 0F, 0F);
        Left = new ModelRenderer(this, 21, 0);
        Left.addBox(-8, 15, -1, 7, 2, 2);
        Left.setRotationPoint(0, 0, 0);
        Left.setTextureSize(64, 32);
        Left.mirror = true;
        setRotation(Left, 0, 0, 0);
        Front = new ModelRenderer(this, 0, 11);
        Front.addBox(-1, 15, -8, 2, 2, 7);
        Front.setRotationPoint(0, 0, 0);
        Front.setTextureSize(64, 32);
        Front.mirror = true;
        setRotation(Front, 0, 0, 0);
        Bottom = new ModelRenderer(this, 21, 11);
        Bottom.addBox(-1, 17, -1, 2, 7, 2);
        Bottom.setRotationPoint(0, 0, 0);
        Bottom.setTextureSize(64, 32);
        Bottom.mirror = true;
        setRotation(Bottom, 0, 0, 0);
        Top = new ModelRenderer(this, 29, 11);
        Top.addBox(-1, 8, -1, 2, 7, 2);
        Top.setRotationPoint(0, 0, 0);
        Top.setTextureSize(64, 32);
        Top.mirror = true;
        setRotation(Top, 0, 0, 0);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        this.renderMiddle();
        this.renderBottom();
        this.renderTop();
        this.renderLeft();
        this.renderRight();
        this.renderBack();
        this.renderFront();
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
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
