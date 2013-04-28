package electricexpansion.common.nei;

import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.common.misc.WireMillRecipes;
import electricexpansion.common.tile.TileEntityWireMill;

public class WireMillRecipeHandler extends EEMachineRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Wire Mill";
	}
	@Override
    public String getOverlayIdentifier()
    {
        return "wireMill";
    }

	@Override
	public String getGuiTexture() {
		return "/mods/electricexpansion/textures/gui/GuiEEMachine.png";
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(77, 27, 22, 12), "wireMill", new Object[0]));
	}
	
	@Override
	public Class getGuiClass()
	{
		return GuiWireMill.class;
	}
	@Override
	public double getWattsPerTick() {
		return TileEntityWireMill.WATTS_PER_TICK;
	}
	@Override
	public Map<ItemStack, int[]> getRecipes() {
		return WireMillRecipes.INSTANCE.getRecipesForNEI();
	}
	
}
