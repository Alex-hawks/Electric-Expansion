package electricexpansion.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;

@SideOnly(Side.CLIENT)
public class GuiSwitchButton extends GuiButton
{
	private boolean isActive;

	public GuiSwitchButton(int par1, int par2, int par3, String par4Str, boolean initState)
	{
		this(par1, par2, par3, 200, 16, par4Str, initState);
	}

	public GuiSwitchButton(int par1, int par2, int par3, int par4, int par5, String par6Str, boolean initState)
	{
		super(par1, par2, par3, par4, par5, par6Str);
		this.width = 200;
		this.height = 16;
		this.enabled = true;
		this.drawButton = true;
		this.id = par1;
		this.xPosition = par2;
		this.yPosition = par3;
		this.width = par4;
		this.height = par5;
		this.displayString = par6Str;
		this.isActive = initState;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.drawButton)
		{
			FontRenderer var4 = par1Minecraft.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture(ElectricExpansion.TEXTURE_PATH + "SwitchButton.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

			int var5 = 0;

			if (this.isActive)
				var5 = 16;

			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, var5, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, var5, this.width / 2, this.height);
			this.mouseDragged(par1Minecraft, par2, par3);
			int var6 = 14737632;

			if (this.isActive)
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0x006400);

			else
				this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xFF0000);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.getHoverState(this.field_82253_i) == 2)
			this.isActive = !this.isActive;

		return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
	}

}
