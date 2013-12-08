package electricexpansion.common.nei;

import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.common.misc.WireMillRecipes;
import electricexpansion.common.tile.TileEntityWireMill;

public class WireMillRecipeHandler extends EEMachineRecipeHandler
{
    
    @Override
    public String getRecipeName()
    {
        return "Wire Mill";
    }
    
    @Override
    public String getOverlayIdentifier()
    {
        return "wireMill";
    }
    
    @Override
    public void loadTransferRects()
    {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(77, 19, 22, 12), "wireMill", new Object[0]));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass()
    {
        return GuiWireMill.class;
    }
    
    @Override
    public float getWattsPerTick()
    {
        return TileEntityWireMill.WATTS_PER_TICK;
    }
    
    @Override
    public Map<ItemStack, int[]> getRecipes()
    {
        return WireMillRecipes.INSTANCE.getRecipesForNEI();
    }
    
}
