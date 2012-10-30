package electricexpansion.alex_hawks.misc;

import java.util.HashMap;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.ItemStack;

public class customRecipes 
{
	private static final customRecipes recipeBase = new customRecipes();

	private static HashMap<String[], Integer> inputToRecipe = new HashMap<String[], Integer>();
	private static HashMap<Integer, ItemStack> recipeToOutput = new HashMap<Integer, ItemStack>();
	private static HashMap<Integer, Integer> recipeToTicks = new HashMap<Integer, Integer>();
	private static HashMap<Integer, byte[]> recipeToInputQTY = new HashMap<Integer, byte[]>();

	public static final customRecipes processing()
	{return recipeBase;}

	public static void addRecipe(ItemStack[] input, ItemStack output, int ticks)
	{
		try
		{
			int nextRecipeID = recipeToOutput.size();
			String[] input2 = new String[input.length];
			byte[] inputQty = new byte[input.length];
			for(int i = 0; i < input.length; i++)
				input2[i] = stackSizeToOne(input[i]) + "";
			for(int i = 0; i < input.length; i++)
				inputQty[i] = (byte)input[i].stackSize;

			inputToRecipe.put(input2, nextRecipeID);
			recipeToOutput.put(nextRecipeID, output);
			recipeToTicks.put(nextRecipeID, ticks);
			recipeToInputQTY.put(nextRecipeID, inputQty);
		}
		catch(Exception e)
		{e.printStackTrace();}
	}

	public Integer getProcessingTicks(ItemStack[] input) 
	{
		try
		{
			String[] input2 = new String[input.length];
			int recipeID;
			boolean b = true;

			for(int i = 0; i < input.length; i++)
				input2[i] = stackSizeToOne(input[i]) + "";
			recipeID = inputToRecipe.get(input2);
			for(int i = 0; !b && i < input.length; i++)
				if(!(input[i].stackSize >= recipeToInputQTY.get(recipeID)[i]))
					b = false;
			if(b)
				return recipeToTicks.get(recipeID);
			else return (Integer)null;
		}
		catch(NullPointerException e) 
		{return (Integer)null;}
	}

	public int getInputQTY(ItemStack[] input, ItemStack wanted)
	{	
		try
		{
			String[] input2 = new String[input.length];
			int recipeID;
			boolean b = true;
			String wanted2 = wanted + "";

			for(int i = 0; i < input.length; i++)
				input2[i] = stackSizeToOne(input[i]) + "";
			recipeID = inputToRecipe.get(input2);
			for(int i = 0; !b && i < input.length; i++)
				if(!(input[i].stackSize >= recipeToInputQTY.get(recipeID)[i]))
					b = false;
			if(b)
			{
				byte[] qtyArray = recipeToInputQTY.get(recipeID);
				for(int i = 0; !b && i < input.length; i++)
				{
					if(input[i] == wanted)	
					{return qtyArray[i];}
				}
				throw new IllegalArgumentException("Error: arg2 must be in arg1[]");

			}
			else return (Integer)null;
		}
		catch(NullPointerException e) 
		{return (Integer)null;}
		catch(IllegalArgumentException e)
		{e.printStackTrace();return (Integer)null;}
	}

	public static ItemStack stackSizeToOne(ItemStack i)
	{
		if(i != null)
			return new ItemStack(i.itemID, 1, i.getItemDamage());
		else return null;
	} 
}
