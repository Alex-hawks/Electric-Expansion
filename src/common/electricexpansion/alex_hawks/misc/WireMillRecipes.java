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

	/** The arrayList of drawing results. 
	  *	drawingList[0] input, output
	  *	drawingList[1] input, watts needed
	  */
	private static Map[] drawingList = new Map[2];

	/**
	 * Used to call methods addSmelting and getSmeltingResult.
	 */
	public static final WireMillRecipes drawing()
	{
		return drawingBase;
	}

	/**
	 * Adds a drawing recipe.
	 * @throws IOException 
	 */
	public static void addDrawing(ItemStack input, ItemStack output, double watts)
	{
		try
		{if(input != null && output != null && watts < 0)
		{
			drawingList[0].put(input, output);
			drawingList[1].put(input, watts);
		}
		else if (input == null) 
			throw new IOException("Error: Input cannot be null.");
		else if (output == null)
			throw new IOException("Error: Output cannot be null.");
		else if (watts >= 0)
			throw new IOException("Error: Watts must be greater than 0.");
		}
		catch(IOException e)
		{e.printStackTrace();}
	}
	/**
	 * This one supports Forge Ore-Dictionary.
	 * @param input
	 * @param output
	 * @param watts
	 * @throws IOException 
	 */
	public static void addDrawing(String input, ItemStack output, double watts)
	{
		for ( ItemStack input2 : OreDictionary.getOres(input))
			addDrawing(input2, output, watts);
	}

	public Map[] getDrawingList()
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
		try{return (ItemStack)this.drawingList[0].get(input);}
		catch(NullPointerException e) {return (ItemStack)null;}
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
	
	/**
	 * 
	 */
	private ItemStack stackSizeToOne(ItemStack item)
	{
		if(item != null)
			return new ItemStack(item.itemID, 1, item.getItemDamage());
		else return null;
	}
}