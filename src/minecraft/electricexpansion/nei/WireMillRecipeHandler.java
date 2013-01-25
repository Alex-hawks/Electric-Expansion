package electricexpansion.nei;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.WireMillRecipes;

public class WireMillRecipeHandler extends MachineRecipeHandler
{
	public Class getGuiClass()
	{
		return GuiWireMill.class;
	}

	public String getRecipeName()
	{
		return "Wire Mill";
	}

	public String getRecipeId()
	{
		return "electricexpansion.wiremill";
	}

	public String getGuiTexture()
	{
		return "/electricexpansion/textures/WireMillGUI.png";
	}

	public String getOverlayIdentifier()
	{
		return "electricexpansion.wiremill";
	}

	public Set<Map.Entry<ItemStack, int[]>> getRecipeList()
	{
		return WireMillRecipes.getRecipesForNEI().entrySet();
	}

	public ItemStack[] getFuels()
	{			
		return new ItemStack[] {new ItemStack(ElectricExpansion.itemAdvBat, 1), new ItemStack(ElectricExpansion.itemEliteBat, 1)};
	}

}