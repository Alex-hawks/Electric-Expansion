package electricexpansion.alex_hawks.misc;

import cpw.mods.fml.common.registry.GameRegistry;
import basiccomponents.BasicComponents;
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
	private static final Block blockCopperWire = BasicComponents.blockCopperWire;
	
	private static final Block blockRawWire = ElectricExpansion.blockRawWire;
	private static final Block blockInsulatedWire = ElectricExpansion.blockInsulatedWire;
	private static final Block blockWireBlock = ElectricExpansion.blockWireBlock;
	private static final Block blockSwitchWire = ElectricExpansion.blockSwitchWire;
	private static final Block blockSwitchWireBlock = ElectricExpansion.blockSwitchWireBlock;
	private static final Block blockAdvBox = ElectricExpansion.blockBigBatteryBox;
	private static final Block blockWireMill = ElectricExpansion.blockWireMill;

	private static final Item itemParts = ElectricExpansion.itemParts;
	
	public static void crafting()
	{
		//Uninsulated Wire Recipes
		GameRegistry.addRecipe(new ItemStack(blockRawWire, 7, 0), new Object [] {" @ ", " @ ", " @ ", '@', "ingotCopper"});
		GameRegistry.addRecipe(new ItemStack(blockRawWire, 7, 1), new Object [] {" @ ", " @ ", " @ ", '@', "ingotTin"});
		GameRegistry.addRecipe(new ItemStack(blockRawWire, 7, 2), new Object [] {" @ ", " @ ", " @ ", '@', "ingotSilver"});
		GameRegistry.addRecipe(new ItemStack(blockRawWire, 7, 3), new Object [] {" @ ", " @ ", " @ ", '@', "ingotAluminium"});

		//Recipes for supporting other UE add-ons, the slack way...
		GameRegistry.addShapelessRecipe(new ItemStack(blockCopperWire, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 0)});
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{new ItemStack(blockCopperWire, 1)});

		//Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth});
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth});
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth});
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth});
		
		//Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
		GameRegistry.addRecipe(new ItemStack(blockInsulatedWire, 7, 0), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotCopper"});
		GameRegistry.addRecipe(new ItemStack(blockInsulatedWire, 7, 1), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotTin"});
		GameRegistry.addRecipe(new ItemStack(blockInsulatedWire, 7, 2), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotSilver"});
		GameRegistry.addRecipe(new ItemStack(blockInsulatedWire, 7, 3), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotAluminium"});
		
		//Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone});
		
		//Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone});
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone});
		
		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.lever});
		
		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.lever});
		
		//Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone, Block.lever});
		
		//Switch Wire Block Recipes (From Block.stone, Block,lever and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone, Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone, Block.lever});
		
		//Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[]{new ItemStack(blockWireBlock, 1, 0), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[]{new ItemStack(blockWireBlock, 1, 1), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[]{new ItemStack(blockWireBlock, 1, 2), Block.lever});
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[]{new ItemStack(blockWireBlock, 1, 3), Block.lever});
	
		//Machines
		GameRegistry.addRecipe(new ItemStack(blockAdvBox), new Object [] {"!!!", "!@!", "#$#", '!', ElectricExpansion.itemSuperConduct.getUncharged(),'@', BasicComponents.batteryBox, '?', BasicComponents.itemBattery.getUncharged()});
//		RecipeManager.addRecipe(blockAdvBox, new Object [] {"!!!", "!@!", "#$#", '!', BasicComponents.itemBattery.getUnchargedItemStack(),'@', BasicComponents.batteryBox, '?', BasicComponents.itemBattery.getUnchargedItemStack(), '$', new ItemStack(BasicComponents.itemCircuit, 1, 2), '#', blockCopperWire}));
		GameRegistry.addRecipe(new ItemStack(blockWireMill), new Object [] {"#$#", "!%!", "@!@", '!', BasicComponents.itemMotor, '#', BasicComponents.itemSteelPlate, '@', BasicComponents.itemBronzePlate, '$', new ItemStack(BasicComponents.itemCircuit, 1, 0), '%', new ItemStack(itemParts, 1, 0)});
		
		//Parts
		GameRegistry.addRecipe(new ItemStack(itemParts, 1, 0), new Object [] {" ! ", "! !", " ! ", '!', Item.ingotIron});	
	}
	public static void drawing()
	{
		WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 60);
		WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 60);
		WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 60);
		WireMillRecipes.addDrawing("ingotAluminium", new ItemStack(blockRawWire, 3, 3), 60);
		
		WireMillRecipes.addDrawing(new ItemStack(Block.cloth), new ItemStack(Item.silk, 3), 40);
	}
}
