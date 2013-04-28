package electricexpansion.common.misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WireMillRecipes
{
    /**
     * Used to call methods addDrawing and getDrawingResult.
     */
    public static final WireMillRecipes INSTANCE = new WireMillRecipes();
    
    private HashMap<String, Integer> inputToRecipe = new HashMap<String, Integer>();
    private HashMap<Integer, ItemStack> recipeToInput = new HashMap<Integer, ItemStack>();
    private HashMap<Integer, ItemStack> recipeToOutput = new HashMap<Integer, ItemStack>();
    private HashMap<Integer, Integer> recipeToTicks = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> recipeToInputQTY = new HashMap<Integer, Integer>();
    
    private WireMillRecipes()
    {
    }
    
    /**
     * Adds a drawing recipe.
     * 
     * @param input
     *            As an ItemStack
     * @param output
     *            As an ItemStack
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     */
    public void addProcessing(ItemStack input, ItemStack output, int ticks)
    {
        try
        {
            if (input != null && output != null && ticks > 0)
            {
                int nextRecipeID = this.recipeToOutput.size();
                this.inputToRecipe.put(stackSizeToOne(input) + "", nextRecipeID);
                this.recipeToInput.put(nextRecipeID, stackSizeToOne(input));
                this.recipeToOutput.put(nextRecipeID, output);
                this.recipeToTicks.put(nextRecipeID, ticks);
                this.recipeToInputQTY.put(nextRecipeID, input.stackSize);
            }
            else if (input == null)
                throw new IOException("Error: Input cannot be null.");
            else if (output == null)
                throw new IOException("Error: Output cannot be null.");
            else if (ticks <= 0)
                throw new IOException("Error: Ticks must be greater than 0.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds a drawing recipe.
     * 
     * @param input
     *            As Forge Ore-Dict. ID
     * @param output
     *            As an ItemStack
     * @param ticks
     *            The ticks required for the recipe, seconds * 20.
     */
    public void addProcessing(String input, ItemStack output, int ticks)
    {
        for (ItemStack input2 : OreDictionary.getOres(input))
        {
            this.addProcessing(input2, output, ticks);
        }
    }
    
    /**
     * Used to get the resulting ItemStack from a source ItemStack
     * 
     * @param item
     *            The Source ItemStack
     * @return The result ItemStack
     */
    public ItemStack getDrawingResult(ItemStack input)
    {
        try
        {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID))
                return this.recipeToOutput.get(recipeID);
            else
                return null;
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }
    
    public int getInputQTY(ItemStack input)
    {
        try
        {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID))
                return this.recipeToInputQTY.get(recipeID);
            else
                return 0;
        }
        catch (NullPointerException e)
        {
            return 0;
        }
    }
    
    /**
     * Used to get the required ticks for a source ItemStack
     * 
     * @param item
     *            The Source ItemStack
     * @return The processing time, in ticks
     */
    public Integer getDrawingTicks(ItemStack input)
    {
        try
        {
            int recipeID = 0;
            recipeID = this.inputToRecipe.get(stackSizeToOne(input) + "");
            if (input.stackSize >= this.recipeToInputQTY.get(recipeID))
                return this.recipeToTicks.get(recipeID);
            else
                return 0;
        }
        catch (NullPointerException e)
        {
            return 0;
        }
    }
    
    /**
     * 
     * @param i
     *            An ItemStack
     * @return The ItemStack, with StackSize set to 1
     */
    public static ItemStack stackSizeToOne(ItemStack i)
    {
        if (i != null)
            return new ItemStack(i.itemID, 1, i.getItemDamage());
        else
            return null;
    }
    
    public static ItemStack stackSizeChange(ItemStack i, int j)
    {
        if (i != null && j + "" != "")
            return new ItemStack(i.itemID, j, i.getItemDamage());
        else
            return null;
    }
    
    /**
     * A helper method for getting the recipes for NEI 
     * @return Map<inputItemStack, int[]>
     * int[] is (0:ID of output; 1: StackSize; 2: Metadata; 3: ticksRequired)
     * requiredEnergy = ticksRequired * {@link electricexpansion.common.tile.TileEntityWireMill#WATTS_PER_TICK WATTS_PER_TICK}
     */
    public Map<ItemStack, int[]> getRecipesForNEI()
    {
        Map<ItemStack, int[]> recipes = new HashMap<ItemStack, int[]>();
        for (int i = 0; i < this.recipeToInput.size(); i++)
        {
            ItemStack input = stackSizeChange(this.recipeToInput.get(i), this.recipeToInputQTY.get(i));
            int[] output = { this.recipeToOutput.get(i).itemID, this.recipeToOutput.get(i).stackSize, this.recipeToOutput.get(i).getItemDamage(), this.getDrawingTicks(input) };
            recipes.put(input, output);
        }
        return recipes;
    }
}