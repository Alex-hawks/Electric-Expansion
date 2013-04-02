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
        this.textureWidth = 64;
        this.textureHeight = 32;
        
        this.Middle = new ModelRenderer(this, 0, 0);
        this.Middle.addBox(0F, 0F, 0F, 2, 2, 2);
        this.Middle.setRotationPoint(-1F, 15F, -1F);
        this.Middle.setTextureSize(64, 32);
        this.Middle.mirror = true;
        this.setRotation(this.Middle, 0F, 0F, 0F);
        this.Right = new ModelRenderer(this, 22, 0);
        this.Right.addBox(0F, 0F, 0F, 7, 2, 2);
        this.Right.setRotationPoint(1F, 15F, -1F);
        this.Right.setTextureSize(64, 32);
        this.Right.mirror = true;
        this.setRotation(this.Right, 0F, 0F, 0F);
        this.Back = new ModelRenderer(this, 0, 10);
        this.Back.addBox(0F, 0F, 0F, 2, 2, 7);
        this.Back.setRotationPoint(-1F, 15F, 1F);
        this.Back.setTextureSize(64, 32);
        this.Back.mirror = true;
        this.setRotation(this.Back, 0F, 0F, 0F);
        this.Left = new ModelRenderer(this, 44, 0);
        this.Left.addBox(0F, 0F, 0F, 7, 2, 2);
        this.Left.setRotationPoint(-8F, 15F, -1F);
        this.Left.setTextureSize(64, 32);
        this.Left.mirror = true;
        this.setRotation(this.Left, 0F, 0F, 0F);
        this.Front = new ModelRenderer(this, 0, 22);
        this.Front.addBox(0F, 0F, 0F, 2, 2, 7);
        this.Front.setRotationPoint(-1F, 15F, -8F);
        this.Front.setTextureSize(64, 32);
        this.Front.mirror = true;
        this.setRotation(this.Front, 0F, 0F, 0F);
        this.Bottom = new ModelRenderer(this, 22, 10);
        this.Bottom.addBox(0F, 0F, 0F, 2, 7, 2);
        this.Bottom.setRotationPoint(-1F, 17F, -1F);
        this.Bottom.setTextureSize(64, 32);
        this.Bottom.mirror = true;
        this.setRotation(this.Bottom, 0F, 0F, 0F);
        this.Top = new ModelRenderer(this, 22, 22);
        this.Top.addBox(0F, 0F, 0F, 2, 7, 2);
        this.Top.setRotationPoint(-1F, 8F, -1F);
        this.Top.setTextureSize(64, 32);
        this.Top.mirror = true;
        this.setRotation(this.Top, 0F, 0F, 0F);
    }
    
    public void renderMiddle()
    {
        this.Middle.render(0.0625F);
    }
    
    public void renderBottom()
    {
        this.Bottom.render(0.0625F);
    }
    
    public void renderTop()
    {
        this.Top.render(0.0625F);
    }
    
    public void renderLeft()
    {
        this.Left.render(0.0625F);
    }
    
    public void renderRight()
    {
        this.Right.render(0.0625F);
    }
    
    public void renderBack()
    {
        this.Back.render(0.0625F);
    }
    
    public void renderFront()
    {
        this.Front.render(0.0625F);
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
