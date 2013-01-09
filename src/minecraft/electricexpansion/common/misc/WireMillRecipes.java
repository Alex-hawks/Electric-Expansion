package electricexpansion.common.misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WireMillRecipes
{
	private static final WireMillRecipes drawingBase = new WireMillRecipes();

	private static HashMap<String, Integer> inputToRecipe = new HashMap<String, Integer>();
	private static HashMap<Integer, ItemStack> recipeToInput = new HashMap<Integer, ItemStack>();
	private static HashMap<Integer, ItemStack> recipeToOutput = new HashMap<Integer, ItemStack>();
	private static HashMap<Integer, Integer> recipeToTicks = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> recipeToInputQTY = new HashMap<Integer, Integer>();

	/**
	 * Used to call methods addDrawing and getDrawingResult.
	 */
	public static final WireMillRecipes drawing()
	{
		return drawingBase;
	}

	/**
	 * Adds a drawing recipe.
	 * 
	 * @param input As an ItemStack
	 * @param output As an ItemStack
	 * @param ticks The ticks required for the recipe, seconds * 20.
	 */
	public static void addDrawing(ItemStack input, ItemStack output, int ticks)
	{
		try
		{
			if (input != null && output != null && ticks > 0)
			{
				boolean j = true;
				int nextRecipeID = recipeToOutput.size();
				inputToRecipe.put(stackSizeToOne(input) + "", nextRecipeID);
				recipeToInput.put(nextRecipeID, stackSizeToOne(input));
				recipeToOutput.put(nextRecipeID, output);
				recipeToTicks.put(nextRecipeID, ticks);
				recipeToInputQTY.put(nextRecipeID, input.stackSize);
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
	 * @param input As Forge Ore-Dict. ID
	 * @param output As an ItemStack
	 * @param ticks The ticks required for the recipe, seconds * 20.
	 */
	public static void addDrawing(String input, ItemStack output, int ticks)
	{
		for (ItemStack input2 : OreDictionary.getOres(input))
			addDrawing(input2, output, ticks);
	}

	/**
	 * Used to get the resulting ItemStack from a source ItemStack
	 * 
	 * @param item The Source ItemStack
	 * @return The result ItemStack
	 */
	public ItemStack getDrawingResult(ItemStack input)
	{
		try
		{
			int recipeID = 0;
			recipeID = inputToRecipe.get(stackSizeToOne(input) + "");
			if (input.stackSize >= recipeToInputQTY.get(recipeID))
				return (ItemStack) this.recipeToOutput.get(recipeID);
			else
				return null;
		}
		catch (NullPointerException e)
		{
			return (ItemStack) null;
		}
	}

	public static int getInputQTY(ItemStack input)
	{
		try
		{
			int recipeID = 0;
			recipeID = inputToRecipe.get(stackSizeToOne(input) + "");
			if (input.stackSize >= recipeToInputQTY.get(recipeID))
				return (int) recipeToInputQTY.get(recipeID);
			else
				return (Integer) null;
		}
		catch (NullPointerException e)
		{
			return 0;
		}
	}

	/**
	 * Used to get the required ticks for a source ItemStack
	 * 
	 * @param item The Source ItemStack
	 * @return The processing time, in ticks
	 */
	public static Integer getDrawingTicks(ItemStack input)
	{
		try
		{
			int recipeID = 0;
			recipeID = inputToRecipe.get(stackSizeToOne(input) + "");
			if (input.stackSize >= recipeToInputQTY.get(recipeID))
				return (Integer) recipeToTicks.get(recipeID);
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
	 * @param i An ItemStack
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

	public static Map getRecipesForNEI()
	{
		Map<ItemStack, int[]> recipes = new HashMap<ItemStack, int[]>();
		// int[] is (0:ID of output; 1: StackSize; 2: Metadata; 3:ticks required)
		// requiredEnergy = ticks required * 500 (TileEntityWireMill.WATTS_PER_TICK
		for (int i = 0; i < recipeToInput.size(); i++)
		{
			ItemStack input = stackSizeChange(recipeToInput.get(i), recipeToInputQTY.get(i));
			int[] output = { recipeToOutput.get(i).itemID, recipeToOutput.get(i).stackSize, recipeToOutput.get(i).getItemDamage(), getDrawingTicks(recipeToInput.get(i)) };
			recipes.put(input, output);
		}
		return recipes;
	}
}