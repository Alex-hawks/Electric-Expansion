package electricexpansion.client.mattredsox;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

import electricexpansion.mattredsox.ContainerEtcher;
public class GuiEtcher extends GuiContainer
{
public GuiEtcher(InventoryPlayer inventory, World world, int x, int y, int z)
{
         super(new ContainerEtcher(inventory, world, x, y, z));
}
/**
         * Draw the foreground layer for the GuiContainer (everything in front of the items)
         */
protected void drawGuiContainerForegroundLayer()
{
         this.fontRenderer.drawString("Etcher", 28, 6, 4210752);
         this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
}
/**
         * Draw the background layer for the GuiContainer (everything behind the items)
         */
protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
{
         int var4 = this.mc.renderEngine.getTexture("/gui/crafting.png"); // put the code to your path in here
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.renderEngine.bindTexture(var4);
         int var5 = (this.width - this.xSize) / 2;
         int var6 = (this.height - this.ySize) / 2;
         this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
}
}