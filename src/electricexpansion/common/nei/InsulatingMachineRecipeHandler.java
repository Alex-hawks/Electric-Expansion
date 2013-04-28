package electricexpansion.common.nei;

import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.common.misc.InsulationRecipes;

public class InsulatingMachineRecipeHandler extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Insulation Refiner";
	}
	@Override
    public String getOverlayIdentifier()
    {
        return "insulation";
    }

	@Override
	public String getGuiTexture() {
		return "/mods/electricexpansion/textures/gui/GuiEEMachine.png";
	}
	@Override
	public void loadCraftingRecipes(String outputId, Object... results){
		
		
		if(outputId.equals("insulation")){
			boolean woolAdded = false;
			for(Map.Entry<ItemStack,int[]> recipe :  InsulationRecipes.INSTANCE.getRecipesForNEI().entrySet()){
				if(recipe.getKey().itemID == 35){
					if(!woolAdded){
						arecipes.add(new InsulationRecipe(recipe));
						woolAdded = true;
					}
					continue;
				}
				arecipes.add(new InsulationRecipe(recipe));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
	    }
		
	}

	@Override
	public void loadCraftingRecipes(ItemStack result){
		boolean woolAdded = false;
		for(Map.Entry<ItemStack,int[]> recipe :  InsulationRecipes.INSTANCE.getRecipesForNEI().entrySet()){	
			ItemStack item = new ItemStack(recipe.getValue()[0],recipe.getValue()[1],recipe.getValue()[2]);
			if(recipe.getKey().itemID == 35){
				if(!woolAdded && NEIServerUtils.areStacksSameTypeCrafting(item, result)){
						arecipes.add(new InsulationRecipe(recipe));
						woolAdded = true;
				}
				continue;
			}
			if(NEIServerUtils.areStacksSameTypeCrafting(item, result)){
				arecipes.add(new InsulationRecipe(recipe));

			}
		}

	}
    

    
    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
    	boolean woolAdded = false;
    	for(Map.Entry<ItemStack,int[]> recipe :  InsulationRecipes.INSTANCE.getRecipesForNEI().entrySet()){
			if(recipe.getKey().itemID == 35){

				if(!woolAdded &&NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)){
					arecipes.add(new InsulationRecipe(recipe));
					woolAdded = true;
				}
				continue;
			}
			else {
			if(NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)){
				arecipes.add(new InsulationRecipe(recipe));
			}
			}
		}
    	
    	
    }
    @Override
    public void drawExtras(GuiContainerManager gui, int recipe){
    	 drawProgressBar(gui, 72, 16, 176, 0, 22, 13, 48, 0);
    	 drawProgressBar(gui, 30, 9, 176, 13, 4, 10, 48, 1);
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
	
	public class InsulationRecipe extends TemplateRecipeHandler.CachedRecipe{
		
		private PositionedStack input;
		private PositionedStack output;
		
		
		@Override
		public PositionedStack getResult() {
			return output;
		}
		@Override
		public PositionedStack getIngredient(){
			if(input.item.itemID == 35) { // hax for colored wool
				int cycle = cycleticks / 48;
				PositionedStack stack = input.copy();
				stack.item.setItemDamage(cycle % 14);
                return stack;
			}
			return input;			
		}
		public InsulationRecipe(Map.Entry<ItemStack,int[]> recipe){
			this.input = new PositionedStack(recipe.getKey(),  50, 14);
			this.output = new PositionedStack(new ItemStack(recipe.getValue()[0],recipe.getValue()[1],recipe.getValue()[2]),  103, 14);
		}
		public InsulationRecipe(ItemStack input, ItemStack output){
			this.input = new PositionedStack(input,  50, 14);
			this.output = new PositionedStack(output,103, 14);
		}
	}
}
