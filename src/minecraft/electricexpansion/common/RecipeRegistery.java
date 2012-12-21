package electricexpansion.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.common.misc.WireMillRecipes;

public class RecipeRegistery
{
	private static final Block blockSwitchWire = ElectricExpansion.blockSwitchWire;
	private static final Block blockSwitchWireBlock = ElectricExpansion.blockSwitchWireBlock;
	private static final Block blockBatBox = ElectricExpansion.blockAdvBatteryBox;
	private static final Block blockWireMill = ElectricExpansion.blockWireMill;
	private static final Block blockRawWire = ElectricExpansion.blockRawWire;
	private static final Block blockInsulatedWire = ElectricExpansion.blockInsulatedWire;
	private static final Block blockWireBlock = ElectricExpansion.blockWireBlock;
	private static final Item itemParts = ElectricExpansion.itemParts;
	private static final Item itemEliteBat = ElectricExpansion.itemEliteBat;
	private static final Item itemUpgrade = ElectricExpansion.itemUpgrade;
	private static final Block blockTransformer = ElectricExpansion.blockTransformer;
	private static final Block blockMultimeter = ElectricExpansion.blockMultimeter;


	public static void crafting()
	{
		// Uninsulated Wire Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 0), new Object[] { " @ ", " @ ", " @ ", '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 1), new Object[] { " @ ", " @ ", " @ ", '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 2), new Object[] { " @ ", " @ ", " @ ", '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 3), new Object[] { " @ ", " @ ", " @ ", '@', "ingotAluminium" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 12, 3), new Object[] { " @ ", " @ ", " @ ", '@', "ingotEndium" }));

		// Recipes for supporting other UE add-ons, the slack way...
		if (OreDictionary.getOres("copperWire").size() > 0)
		{
			GameRegistry.addShapelessRecipe(OreDictionary.getOres("copperWire").get(0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0) });
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { "copperWire" }));

		// Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Block.cloth });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Block.cloth });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Block.cloth });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Block.cloth });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Block.cloth });

		// Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 0), new Object[] { "#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 1), new Object[] { "#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 2), new Object[] { "#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 3), new Object[] { "#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotAluminium" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 12, 4), new Object[] { "#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotEndium" }));

		// Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Block.cloth, Block.stone });

		// Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.stone });

		// Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Block.cloth, Block.lever });

		// Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.lever });

		// Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the
		// corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Block.cloth, Block.stone, Block.lever });

		// Switch Wire Block Recipes (From Block.stone, Block,lever and the corresponding Insulated
		// Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.stone, Block.lever });

		// Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockWireBlock, 1, 0), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockWireBlock, 1, 1), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockWireBlock, 1, 2), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockWireBlock, 1, 3), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockWireBlock, 1, 4), Block.lever });

		// Machines
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWireMill), new Object[] { "#$#", "!%!", "@!@", '!', "motor", '#', "plateSteel", '@', "plateBronze", '$', "basicCircuit", '%', new ItemStack(itemParts, 1, 0) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBatBox), new Object[] { "!!!", "@@@", "!$!", '!', "battery", '@', "copperWire", '$', "eliteCircuit" }));

		// Tier 1 Upgrade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 0), new Object[] { "$!$", "!@!", "#!#", '!', "battery", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "basicCircuit", '$', "plateBronze" }));

		// Tier 2 Upgrade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 1), new Object[] { "$!$", "!@!", "#!#", '!', ElectricExpansion.itemEliteBat.getUncharged(), '@', new ItemStack(itemUpgrade, 1, 0), '#', "advancedCircuit", '$', "plateSteel" }));

		// Tier 3 Upgrade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 2), new Object[] { "#!#", "!@!", "#!#", '!', ElectricExpansion.itemEliteBat.getUncharged(), '@', new ItemStack(itemUpgrade, 1, 1), '#', "eliteCircuit" }));

		// Adv Battery
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.itemAdvBat), new Object[] { " T ", "TRT", "TRT", 'T', "ingotSteel", 'R', Item.lightStoneDust }));

		// Elite Battery
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEliteBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateSteel", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', ElectricExpansion.itemLead, '$', Item.ghastTear }));

		// Parts
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 0), new Object[] { " # ", "! !", " ! ", '!', Item.ingotIron, '#', Item.diamond }));

		// Lead Block
		GameRegistry.addRecipe(new ItemStack(ElectricExpansion.blockLead, 1), new Object[] { "@@@", "@@@", "@@@", '@', ElectricExpansion.itemLead });
		GameRegistry.addShapelessRecipe(new ItemStack(ElectricExpansion.itemLead, 9), new Object[] { ElectricExpansion.blockLead });

		//Tranformer
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockTransformer), new Object[] { "$ $", "!@!", "###", '!', ElectricExpansion.itemCoil, '$', "plateBronze", '@', "basicCircuit", '#', "plateSteel" }));

		//Multimeter
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0) , '%', "basicCircuit", '^', Block.glass, '@', Item.stick }));

	}

	public static void drawing()
	{
		WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 60);
		WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 60);
		WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 60);
		WireMillRecipes.addDrawing("ingotAluminium", new ItemStack(blockRawWire, 3, 3), 60);
		WireMillRecipes.addDrawing("ingotEndium", new ItemStack(blockRawWire, 6, 4), 60);

		WireMillRecipes.addDrawing(new ItemStack(Block.cloth), new ItemStack(Item.silk, 4), 30);
	}
}
