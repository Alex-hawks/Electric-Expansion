package electricexpansion.common.nei;

import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.tile.TileEntityInsulatingMachine;

public class InsulatingMachineRecipeHandler extends EEMachineRecipeHandler
{
    
    @Override
    public String getRecipeName()
    {
        return "Insulation Refiner";
    }
    
    @Override
    public String getOverlayIdentifier()
    {
        return "insulation";
    }
    
    @Override
    public String getGuiTexture()
    {
        return "/mods/electricexpansion/textures/gui/GuiEEMachine.png";
    }
    
    @Override
    public void loadTransferRects()
    {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(77, 27, 22, 12), "insulation", new Object[0]));
    }
    
    @Override
    public Class getGuiClass()
    {
        return GuiInsulationMachine.class;
    }
    
    public double getWattsPerTick()
    {
        return TileEntityInsulatingMachine.WATTS_PER_TICK;
    }
    
    @Override
    public Map<ItemStack, int[]> getRecipes()
    {
        return InsulationRecipes.INSTANCE.getRecipesForNEI();
        
    }
    
}
