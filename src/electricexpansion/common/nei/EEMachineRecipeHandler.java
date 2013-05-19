package electricexpansion.common.nei;

import java.util.Map;

import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class EEMachineRecipeHandler extends TemplateRecipeHandler
{
    
    @Override
    public abstract String getRecipeName();
    
    @Override
    public abstract String getOverlayIdentifier();
    
    @Override
    public abstract Class getGuiClass();
    
    @Override
    public abstract void loadTransferRects();
    
    public abstract double getWattsPerTick();
    
    public abstract Map<ItemStack, int[]> getRecipes();
    
    @Override
    public String getGuiTexture()
    {
        return "/mods/electricexpansion/textures/gui/GuiEEMachine.png";
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        
        if (outputId.equals(this.getOverlayIdentifier()))
        {
            boolean woolAdded = false;
            for (Map.Entry<ItemStack, int[]> recipe : this.getRecipes().entrySet())
            {
                if (recipe.getKey().itemID == 35)
                {
                    if (!woolAdded)
                    {
                        arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                        woolAdded = true;
                    }
                    continue;
                }
                arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
        
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        boolean woolAdded = false;
        for (Map.Entry<ItemStack, int[]> recipe : this.getRecipes().entrySet())
        {
            ItemStack item = new ItemStack(recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2]);
            if (recipe.getKey().itemID == 35)
            {
                if (!woolAdded && NEIServerUtils.areStacksSameTypeCrafting(item, result))
                {
                    arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                    woolAdded = true;
                }
                continue;
            }
            if (NEIServerUtils.areStacksSameTypeCrafting(item, result))
            {
                arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                
            }
        }
        
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        boolean woolAdded = false;
        for (Map.Entry<ItemStack, int[]> recipe : this.getRecipes().entrySet())
        {
            
            if (recipe.getKey().itemID == 35)
            {
                
                if (!woolAdded && NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient))
                {
                    arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
                    woolAdded = true;
                }
                continue;
            }
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient))
            {
                arecipes.add(new EEMachineRecipe(recipe, this.getWattsPerTick()));
            }
            
        }
        
    }
    
    @Override
    public void drawExtras(GuiContainerManager gui, int recipe)
    {
        drawProgressBar(gui, 72, 16, 176, 0, 22, 13, 48, 0);
        drawProgressBar(gui, 30, 9, 176, 13, 4, 10, 48, 1);
        double energy = ((EEMachineRecipe) arecipes.get(recipe)).getEnergy();
        String energyString = "Uses ";
        if (energy >= 2000000)
        {
            energyString += String.valueOf(energy / 1000000) + " MJ";
        }
        else if (energy >= 2000)
        {
            energyString += String.valueOf(energy / 1000) + " kJ";
        }
        else
        {
            energyString += String.valueOf(energy) + " J";
        }
        gui.drawTextCentered(energyString, 115, 42, 0xFFFFFFFF);
    }
    
    public class EEMachineRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        
        private PositionedStack input;
        private PositionedStack output;
        private double energy;
        
        public double getEnergy()
        {
            return energy;
        }
        
        @Override
        public PositionedStack getResult()
        {
            return output;
        }
        
        @Override
        public PositionedStack getIngredient()
        {
            if (input.item.itemID == 35)
            { // hax for rotating colored wool stax
                int cycle = cycleticks / 48;
                PositionedStack stack = input.copy();
                stack.item.setItemDamage(cycle % 14);
                return stack;
            }
            return input;
        }
        
        public EEMachineRecipe(Map.Entry<ItemStack, int[]> recipe, double wattsPerTick)
        {
            this.input = new PositionedStack(recipe.getKey(), 50, 14);
            this.output = new PositionedStack(new ItemStack(recipe.getValue()[0], recipe.getValue()[1], recipe.getValue()[2]), 103, 14);
            this.energy = recipe.getValue()[3] * wattsPerTick;
        }
    }
    
}
