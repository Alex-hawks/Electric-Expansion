package electricexpansion.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Random;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import electricexpansion.common.ElectricExpansion;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;

public abstract class MachineRecipeHandler extends TemplateRecipeHandler
{
	public class CachedBinaryRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		public PositionedStack input;
		public ArrayList<PositionedStack> fuels;
		public PositionedStack output;
		int[] outputA;

		int energy;

		public PositionedStack getIngredient()
		{
			return this.input;
		}

		public PositionedStack getResult()
		{
			return this.output;
		}

		public ArrayList<PositionedStack> getOtherStacks()
		{
			return getCycledIngredients(cycleticks / 20, fuels);
		}

		public CachedBinaryRecipe(Map.Entry<ItemStack, int[]> recipe)
		{
			super();
			this.input = new PositionedStack(recipe.getKey(), 50, 14);

			this.outputA = recipe.getValue();

			this.energy = this.outputA[3] * 500;

			ItemStack outputIS = new ItemStack(outputA[0], outputA[1], outputA[2]);

			this.output = new PositionedStack(outputIS, 103, 14);
			ItemStack[] fuels = getFuels();
			this.fuels = new ArrayList<PositionedStack>();
			if (fuels.length > 0)
				this.fuels.add(new PositionedStack(getFuels(), 50, 38));
		}
	}

	public abstract String getRecipeName();

	public abstract String getRecipeId();

	public abstract String getGuiTexture();

	public abstract String getOverlayIdentifier();

	public abstract Set<Map.Entry<ItemStack, int[]>> getRecipeList();

	public abstract ItemStack[] getFuels();

	public void drawBackground(GuiContainerManager guimanager, int i)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		guimanager.bindTextureByName(getGuiTexture());
		guimanager.drawTexturedModalRect(0, 13, 5, 24, 20, 52);
		guimanager.drawTexturedModalRect(20, 0, 25, 11, 120, 65);
	}

	@Override
	public void drawExtras(GuiContainerManager guimanager, int i)
	{
		Integer time = ((CachedBinaryRecipe) this.arecipes.get(i)).energy;
		
		//Fix to weird wool not showing watts
		if (((CachedBinaryRecipe) this.arecipes.get(i)).outputA[0] == Item.silk.itemID)
		{
			time = 300 * 500;
		}
		
		//Fix to weird supercondcuctor not showing watts
		if (((CachedBinaryRecipe) this.arecipes.get(i)).outputA[0] == ElectricExpansion.blockRawWire.blockID && ((CachedBinaryRecipe) this.arecipes.get(i)).outputA[2] == 4)
		{
			time = 24000 * 500;

		}

		guimanager.drawText(72, 35, "Energy Needed:");
		guimanager.drawText(82, 45, ElectricInfo.getDisplay((double) time, ElectricUnit.WATT));
	}

	public void loadTransferRects()
	{
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(54, 24, 25, 16), getRecipeId(), new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getRecipeId()))
		{
			for (Map.Entry<ItemStack, int[]> irecipe : getRecipeList())
			{
				this.arecipes.add(new CachedBinaryRecipe(irecipe));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result)
	{
		for (Map.Entry<ItemStack, int[]> irecipe : getRecipeList())
		{

			int[] outputA = irecipe.getValue();

			ItemStack outputIS = new ItemStack(outputA[0], outputA[1], outputA[2]);

			if (NEIServerUtils.areStacksSameTypeCrafting(outputIS, result))
			{
				this.arecipes.add(new CachedBinaryRecipe(irecipe));
			}
		}
	}

	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (Map.Entry<ItemStack, int[]> irecipe : getRecipeList())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getKey(), ingredient))
			{
				this.arecipes.add(new CachedBinaryRecipe(irecipe));
			}
		}
	}
}