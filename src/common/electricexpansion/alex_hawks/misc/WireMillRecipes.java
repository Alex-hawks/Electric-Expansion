package electricexpansion.alex_hawks.misc;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WireMillRecipes
{
	private static final WireMillRecipes drawingBase = new WireMillRecipes();

	private static HashMap<String, Integer> inputToRecipe = new HashMap<String, Integer>();
	private static HashMap<Integer, ItemStack> recipeToOutput = new HashMap<Integer, ItemStack>();
	private static HashMap<Integer, Double> recipeToWatts = new HashMap<Integer, Double>();
	
	/**
	 * Used to call methods addDrawing and getDrawingResult.
	 */
	public static final WireMillRecipes drawing()
	{
		return drawingBase;
	}

	public static void addDrawing(ItemStack input, ItemStack output, double watts, boolean leaveInputStackSize)
	{
		try
		{
			if(input != null && output != null && watts > 0)
			{
				boolean j = true;
				int nextRecipeID = recipeToOutput.size();
				if(!leaveInputStackSize)
					inputToRecipe.put(stackSizeToOne(input) + "", nextRecipeID);
				else if(leaveInputStackSize)
					inputToRecipe.put(input + "", nextRecipeID);
				recipeToOutput.put(nextRecipeID, output);
				recipeToWatts.put(nextRecipeID, watts);
			}
			else if (input == null) 
				throw new IOException("Error: Input cannot be null.");
			else if (output == null)
				throw new IOException("Error: Output cannot be null.");
			else if (watts <= 0)
				throw new IOException("Error: Watts must be greater than 0.");
		}
		catch(IOException e)
		{e.printStackTrace();}
	}
	/**
	 * Adds a drawing recipe.
	 * @param input As an ItemStack
	 * @param output As an ItemStack
	 * @param watts The Watts required for the recipe, Time  to process is directly proportional.
	 */
	public static void addDrawing(ItemStack input, ItemStack output, double watts)
	{addDrawing(input, output, watts, false);}
	/**
	 * This one supports Forge Ore-Dictionary.
	 * @param input
	 * @param output
	 * @param watts
	 */
	public static void addDrawing(String input, ItemStack output, double watts)
	{
		for ( ItemStack input2 : OreDictionary.getOres(input))
			addDrawing(input2, output, watts);
	}

	/**
	 * Used to get the resulting ItemStack from a source ItemStack
	 * @param item The Source ItemStack
	 * @return The result ItemStack
	 */
	public ItemStack getDrawingResult(ItemStack input, boolean leaveInputStackSize) 
	{
		try
		{
			int recipeID = 0;
			if(!leaveInputStackSize)
				recipeID = inputToRecipe.get(stackSizeToOne(input) + "");
			else if(leaveInputStackSize)
				recipeID = inputToRecipe.get(input + "");
			return (ItemStack)this.recipeToOutput.get(recipeID);
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			return (ItemStack)null;
		}
	}

	public ItemStack getDrawingResult(ItemStack input) 
	{return getDrawingResult(input, false);}

	public double getDrawingWatts(ItemStack input, boolean leaveInputStackSize) 
	{
		try
		{
			int recipeID = 0;
			if(!leaveInputStackSize)
				recipeID = inputToRecipe.get(stackSizeToOne(input) + "");
			else if(leaveInputStackSize)
				recipeID = inputToRecipe.get(input + "");
			return (Double)this.recipeToWatts.get(recipeID);
		}
		catch(NullPointerException e) 
		{
			e.printStackTrace();
			return (Double)null;
		}
	}
	/**
	 * Used to get the required watts from a source ItemStack
	 * @param item The Source ItemStack
	 * @return The result ItemStack
	 */
	public double getDrawingWatts(ItemStack input) 
	{return getDrawingWatts(input, false);}

	private static ItemStack stackSizeToOne(ItemStack item)
	{
		if(item != null)
			return new ItemStack(item.itemID, 1, item.getItemDamage());
		else return null;
	}
}