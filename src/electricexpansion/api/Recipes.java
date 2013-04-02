package electricexpansion.api;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;

public class Recipes
{
    /**
     * Used to add a recipe to the Electric Expansion Insulation Machine
     * 
     * @param input
     *            As an ItemStack
     * @param output
     *            As an int of insulation QTY
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     * @return true if successful
     */
    public boolean addInsulationRecipe(ItemStack input, int output, int ticks)
    {
        if (Loader.isModLoaded("ElectricExpansion"))
        {
            try
            {
                electricexpansion.common.misc.InsulationRecipes.INSTANCE.addProcessing(input, output, ticks);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        else
            return false;
    }
    
    /**
     * Used to add a recipe to the Electric Expansion Insulation Machine
     * 
     * @param input
     *            As a Forge Ore-Dictionary ID
     * @param output
     *            As an int of insulation QTY
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     * @return true if successful
     */
    public boolean addInsulationRecipe(String input, int output, int ticks)
    {
        if (Loader.isModLoaded("ElectricExpansion"))
        {
            try
            {
                electricexpansion.common.misc.InsulationRecipes.INSTANCE.addProcessing(input, output, ticks);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        else
            return false;
    }
    
    /**
     * Used to add a recipe to the Electric Expansion Wire Mill
     * 
     * @param input
     *            As an ItemStack
     * @param output
     *            As an ItemStack
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     * @return true if successful
     */
    public boolean addDrawingRecipe(ItemStack input, ItemStack output, int ticks)
    {
        if (Loader.isModLoaded("ElectricExpansion"))
        {
            try
            {
                electricexpansion.common.misc.WireMillRecipes.INSTANCE.addProcessing(input, output, ticks);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        else
            return false;
    }
    
    /**
     * Used to add a recipe to the Electric Expansion Wire Mill
     * 
     * @param input
     *            As a Forge Ore-Dictionary ID
     * @param output
     *            As an ItemStack
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     * @return true if successful
     */
    public boolean addDrawingRecipe(String input, ItemStack output, int ticks)
    {
        if (Loader.isModLoaded("ElectricExpansion"))
        {
            try
            {
                electricexpansion.common.misc.WireMillRecipes.INSTANCE.addProcessing(input, output, ticks);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        else
            return false;
    }
}
