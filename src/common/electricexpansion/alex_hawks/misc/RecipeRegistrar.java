package electricexpansion.alex_hawks.misc;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import electricexpansion.ElectricExpansion;

public class RecipeRegistrar 
{
	
	private static final Block blockRawWire = ElectricExpansion.blockRawWire;
	private static final Block blockInsulatedWire = ElectricExpansion.blockInsulatedWire;
	private static final Block blockWireBlock = ElectricExpansion.blockWireBlock;
	private static final Block blockSwitchWireOff = ElectricExpansion.blockRawWire;
	private static final Block blockSwitchWireBlockOff = ElectricExpansion.blockRawWire;
	
	private static Block blockAdvBox = ElectricExpansion.blockBigBatteryBox;

	public static void crafting()
	{
		//Uninsulated Wire Recipes
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 0), new Object [] {" @ ", " @ ", " @ ", '@', "ingotCopper"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 1), new Object [] {" @ ", " @ ", " @ ", '@', "ingotTin"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 2), new Object [] {" @ ", " @ ", " @ ", '@', "ingotSilver"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 3), new Object [] {" @ ", " @ ", " @ ", '@', "ingotAluminium"}));
		
		//Recipes for supporting other UE add-ons, the slack way...
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe("copperWire"), new Object[]{new ItemStack(blockInsulatedWire, 1, 0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{"copperWire"}));

		//Shapeless Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth}));
		
		//Shaped Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 0), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotCopper"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 1), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotTin"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 2), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotSilver"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 3), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotAluminium"}));
		
		//Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone}));
		
		//Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone}));
		
		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.lever}));
		
		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.lever}));
		
		//Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the corresponding Uninsulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone, Block.lever}));
		
		//Switch Wire Block Recipes (From Block.stone, Block,lever and the corresponding Insulated Wire)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone, Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone, Block.lever}));
		
		//Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockWireBlock, 1, 0), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockWireBlock, 1, 1), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockWireBlock, 1, 2), Block.lever}));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockWireBlock, 1, 3), Block.lever}));
	
		
		
		//RecipeManager.addRecipe(blockAdvBox, new Object [] {"!!!", "!@!", "#$#", '!', BasicComponents.itemBattery.getUnchargedItemStack(),'@', BasicComponents.batteryBox, '?', BasicComponents.itemBattery.getUnchargedItemStack(), '$', new ItemStack(BasicComponents.itemCircuit, 1, 2), '#', blockCopperWire}));
	}
	public static void drawing()
	{
		WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 30000);
		WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 30000);
		WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 30000);
		WireMillRecipes.addDrawing("ingotAluminium", new ItemStack(blockRawWire, 3, 3), 30000);
		
		WireMillRecipes.addDrawing(new ItemStack(Block.cloth), new ItemStack(Item.silk, 3), 20000);
	}
}
