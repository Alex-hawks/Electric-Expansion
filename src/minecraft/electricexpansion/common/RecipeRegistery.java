package electricexpansion.common;

import universalelectricity.prefab.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Loader;
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
	private static final Item itemMultimeter = ElectricExpansion.itemMultimeter;

	private static Block basicCompWire;


	public static void crafting()
	{
		// Uninsulated Wire Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 0), new Object[] { " @ ", " @ ", " @ ", '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 1), new Object[] { " @ ", " @ ", " @ ", '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 2), new Object[] { " @ ", " @ ", " @ ", '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 3), new Object[] { " @ ", " @ ", " @ ", '@', "ingotAluminium" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 2, 3), new Object[] { " @ ", " @ ", " @ ", '@', new ItemStack(itemParts, 1, 2) }));

		// Recipes for supporting other UE add-ons, the slack way...
		if (OreDictionary.getOres("copperWire").size() > 0)
		{
			GameRegistry.addShapelessRecipe(OreDictionary.getOres("copperWire").get(0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0) });
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { "copperWire" }));

		// Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Item.leather });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Item.leather });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Item.leather });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Item.leather });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(Item.leather, 3)  });

		// Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 0), new Object[] { "#@#", "#@#", "#@#", '#', Item.leather, '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 1), new Object[] { "#@#", "#@#", "#@#", '#', Item.leather, '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 2), new Object[] { "#@#", "#@#", "#@#", '#', Item.leather, '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 3), new Object[] { "#@#", "#@#", "#@#", '#', Item.leather, '@', "ingotAluminium" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 2, 4), new Object[] { "#@#", "#@#", "#@#", '#', Item.leather, '@', new ItemStack(itemParts, 1, 2) }));

		// Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Item.leather, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Item.leather, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Item.leather, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Item.leather, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Item.leather, Block.stone });

		// Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.stone });

		// Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Item.leather, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Item.leather, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Item.leather, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Item.leather, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Item.leather, Block.lever });

		// Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.lever });

		// Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the
		// corresponding Uninsulated Wire)
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), Item.leather, Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), Item.leather, Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), Item.leather, Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), Item.leather, Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), Item.leather, Block.stone, Block.lever });

		// Switch Wire Block Recipes (From Block.stone, Block,lever and the corresponding Insulated
		// Wire)
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.stone, Block.lever });
	//	GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.stone, Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.stone, Block.lever });

		// Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
	//	GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockWireBlock, 1, 0), Block.lever });
	//	GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockWireBlock, 1, 1), Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockWireBlock, 1, 2), Block.lever });
		//GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockWireBlock, 1, 3), Block.lever });
	//	GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockWireBlock, 1, 4), Block.lever });

		// Machines
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWireMill), new Object[] { "#$#", "!%!", "@!@", '!', "motor", '#', "plateSteel", '@', "plateBronze", '$', "basicCircuit", '%', new ItemStack(itemParts, 1, 0) }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBatBox), new Object[] { "!!!", "@@@", "!$!", '!', "battery", '@', "copperWire", '$', "eliteCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0) , '%', "basicCircuit", '^', Block.glass, '@', Item.stick }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockTransformer), new Object[] { "$ $", "!@!", "###", '!', ElectricExpansion.itemCoil, '$', "plateBronze", '@', "basicCircuit", '#', "plateSteel" }));

		// Upgrades
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 0), new Object[] { "$!$", "!@!", "#!#", '!', "battery", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "basicCircuit", '$', "plateBronze" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 1), new Object[] { "$!$", "!@!", "#!#", '!', ElectricExpansion.itemEliteBat.getUncharged(), '@', new ItemStack(itemUpgrade, 1, 0), '#', "advancedCircuit", '$', "plateSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 2), new Object[] { "#!#", "!@!", "#!#", '!', ElectricExpansion.itemEliteBat.getUncharged(), '@', new ItemStack(itemUpgrade, 1, 1), '#', "eliteCircuit" }));

		// Batteries
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.itemAdvBat), new Object[] { " T ", "TRT", "TRT", 'T', "ingotSteel", 'R', Item.lightStoneDust }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEliteBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateSteel", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "ingotLead", '$', Item.ghastTear }));

		// Parts
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 0), new Object[] { " # ", "! !", " ! ", '!', Item.ingotIron, '#', Item.diamond }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '!', Item.ingotGold, '#', "ingotSilver", '@', Item.enderPearl }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '#', Item.ingotGold, '!', "ingotSilver", '@', Item.enderPearl }));
		FurnaceRecipes.smelting().addSmelting(itemParts.shiftedIndex, 1, new ItemStack(itemParts, 4, 3), 0);
		
		// Storage Blocks
		GameRegistry.addRecipe(new ItemStack(ElectricExpansion.blockLead, 1), new Object[] { "@@@", "@@@", "@@@", '@', ElectricExpansion.itemLead });
		GameRegistry.addShapelessRecipe(new ItemStack(ElectricExpansion.itemLead, 9), new Object[] { ElectricExpansion.blockLead });

		//Tools
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0) , '%', "advancedCircuit", '^', Block.glass, '@', Item.stick }));

		//Reflection to get Basic Components Wire
		if(Loader.isModLoaded("BasicComponents")) 
		{
			try {
				basicCompWire = (Block)Class.forName("basiccomponents.common.BasicComponents").getField("blockCopperWire").get(Block.class);
			} catch (Exception e) {
				e.getStackTrace();
			}	

			RecipeHelper.removeRecipe(new ItemStack(basicCompWire));

		}		
	}

	public static void drawing()
	{
		WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 60);
		WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 60);
		WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 60);
		WireMillRecipes.addDrawing("ingotAluminium", new ItemStack(blockRawWire, 3, 3), 60);
		WireMillRecipes.addDrawing(new ItemStack(itemParts, 64, 2), new ItemStack(blockRawWire, 64, 4), 24000);

		for(int i = 0; i < 16; i++)
			WireMillRecipes.addDrawing(new ItemStack(Block.cloth, 10, i), new ItemStack(Item.silk, 40), 300);
	}
}