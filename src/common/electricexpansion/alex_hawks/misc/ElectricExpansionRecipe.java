package electricexpansion.alex_hawks.misc;

import net.minecraft.src.ItemStack;

public class ElectricExpansionRecipe 
{
	public class WireMillRecipe
	{
		public ItemStack input;
		public ItemStack output;
		public double wattsForRecipe;
		
		public WireMillRecipe(ItemStack output, ItemStack input, double watts)
		{
			this.input = input;
			this.output = output;
			this.wattsForRecipe = watts;
		}

		public boolean isEqual(WireMillRecipe comparingRecipe)
		{
			if(this.input == comparingRecipe.input && this.output == comparingRecipe.output){return true;}
			else return false;
		}
	}
}
