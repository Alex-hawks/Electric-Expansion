package electricexpansion.alex_hawks.misc;

import java.awt.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class WireMillRecipes
{
	private static final WireMillRecipes drawingBase = new WireMillRecipes();

	/** The arrayList of drawing results. 
	  *	drawingList[0] input, output
	  *	drawingList[1] input, watts needed
	  */
	private HashMap[] drawingList = new HashMap[2];

	/**
	 * Used to call methods addSmelting and getSmeltingResult.
	 */
	public static final WireMillRecipes drawing()
	{
		return drawingBase;
	}

	/**
	 * Adds a drawing recipe.
	 */
	public void addDrawing(ItemStack input, ItemStack output, double watts)
	{
		this.drawingList[0].put(input, output);
		this.drawingList[1].put(input, watts);
	}

	public HashMap[] getDrawingList()
	{
		return this.drawingList;
	}

	/**
	 * Used to get the resulting ItemStack from a source ItemStack
	 * @param item The Source ItemStack
	 * @return The result ItemStack
	 */
	public ItemStack getDrawingResult(ItemStack input) 
	{
		if (input == null)	
			return null;
		return (ItemStack)this.drawingList[0].get(input);
	}

	/**
	 * Used to get the required watts from a source ItemStack
	 * @param item The Source ItemStack
	 * @return The result ItemStack
	 */
	public double getDrawingWatts(ItemStack input) 
	{
		if (input == null)	
			return (Double)null;
		return new Double(this.drawingList[1].get(input).toString());
	}
}