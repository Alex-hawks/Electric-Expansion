package electricexpansion.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import universalelectricity.core.item.ItemElectric;
import universalelectricity.prefab.RecipeHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.misc.WireMillRecipes;

public class RecipeRegistery
{
	private static final Block blockSwitchWire = ElectricExpansion.blockSwitchWire;
	private static final Block blockLogisticsWire = ElectricExpansion.blockLogisticsWire;
	private static final Block blockSwitchWireBlock = ElectricExpansion.blockSwitchWireBlock;
	private static final Block blockBatBox = ElectricExpansion.blockAdvBatteryBox;
	private static final Block blockWireMill = ElectricExpansion.blockWireMill;
	private static final Block blockRawWire = ElectricExpansion.blockRawWire;
	private static final Block blockInsulatedWire = ElectricExpansion.blockInsulatedWire;
	private static final Block blockWireBlock = ElectricExpansion.blockWireBlock;
	private static final Block blockMultimeter = ElectricExpansion.blockMultimeter;
	private static final Block blockDistribution = ElectricExpansion.blockDistribution;
	private static final Item itemParts = ElectricExpansion.itemParts;
	private static final ItemElectric itemAdvBat = ElectricExpansion.itemAdvBat;
	private static final ItemElectric itemEliteBat = ElectricExpansion.itemEliteBat;
	private static final ItemElectric itemUltimateBat = ElectricExpansion.itemUltimateBat;
	private static final Item itemUpgrade = ElectricExpansion.itemUpgrade;
	private static final Item itemMultimeter = ElectricExpansion.itemMultimeter;

	private static final ItemStack insulationIS = new ItemStack(itemParts, 1, 6);

	private static Block basicCompWire;
	private static ItemStack HVIngot;

	public static void crafting()
	{
		if (OreDictionary.getOres("ingotAluminium").size() > 0)
		{
			HVIngot = OreDictionary.getOres("ingotAluminium").get(0);
		}
		else
		{
			HVIngot = new ItemStack(itemParts, 1, 4);
			FurnaceRecipes.smelting().addSmelting(itemParts.itemID, 3, new ItemStack(itemParts, 4, 4), 0);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 3), new Object[] { "!#!", "#@#", "!#!", '!', Item.ingotIron, '#', "ingotSilver", '@', Item.eyeOfEnder }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 3), new Object[] { "!#!", "#@#", "!#!", '#', Item.ingotIron, '!', "ingotSilver", '@', Item.eyeOfEnder }));
		}

		// Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS });
		GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7) });

		// Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 0), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 1), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 2), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 3), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', HVIngot }));

		// Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated
		// Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), Block.stone });

		// Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated
		// Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), Block.lever });

		// Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the
		// corresponding Uninsulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, Block.stone, Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), Block.stone, Block.lever });

		// Uninsulated Wire Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 0), new Object[] { " @ ", " @ ", " @ ", '@', "ingotCopper" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 1), new Object[] { " @ ", " @ ", " @ ", '@', "ingotTin" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 2), new Object[] { " @ ", " @ ", " @ ", '@', "ingotSilver" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 3), new Object[] { " @ ", " @ ", " @ ", '@', HVIngot }));

		// Switch Wire Recipes (From a lever and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.lever });
		GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.lever });

		// Logistics Wire Recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 0), new Object[] { new ItemStack(blockSwitchWire, 1, 0), Item.redstone }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 1), new Object[] { new ItemStack(blockSwitchWire, 1, 1), Item.redstone }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 2), new Object[] { new ItemStack(blockSwitchWire, 1, 2), Item.redstone }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 3), new Object[] { new ItemStack(blockSwitchWire, 1, 3), Item.redstone }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 4), new Object[] { new ItemStack(blockSwitchWire, 1, 4), Item.redstone }));

		if (OreDictionary.getOres("battery").size() > 0)
		{

			for (int i = 0; i < OreDictionary.getOres("battery").size(); i++)

			{

				Item BCBattery = OreDictionary.getOres("battery").get(i).getItem();

				if (BCBattery instanceof ItemElectric)

				{

					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBatBox), new Object[] { "!%!", "@@@", "!$!", '%', "batteryBox", '!', new ItemStack(BCBattery, 1, -1), '@', new ItemStack(blockInsulatedWire, 1, 0), '$', "advancedCircuit" }));

					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 0), new Object[] { "$!$", "!@!", "#!#", '!', new ItemStack(BCBattery, 1, -1), '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "basicCircuit", '$', "plateBronze" }));

				}

			}

			GameRegistry.addShapelessRecipe(OreDictionary.getOres("copperWire").get(0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0) });

		}

		// Recipes for supporting other UE add-ons, the slack way...
		if (OreDictionary.getOres("copperWire").size() > 0)
			GameRegistry.addShapelessRecipe(OreDictionary.getOres("copperWire").get(0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0) });
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { "copperWire" }));

		// Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.stone });
		GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.stone });

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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0), '%', "basicCircuit", '^', Block.glass, '@', Item.stick }));
		if (OreDictionary.getOres("antimatterGram").size() > 0)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDistribution), new Object[] { "!!!", "!@!", "!!!", '@', new ItemStack(blockBatBox), '!', "antimatterGram" }));
		else
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDistribution), new Object[] { "!!!", "!@!", "!!!", '@', new ItemStack(blockBatBox), '!', new ItemStack(itemParts, 1, 1) }));

		// Transformers
		// T1
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.blockTransformer, 1, 0), new Object[] { 
			"@@@", "#x#", "$&$", '@', "plateSteel", '#', new ItemStack(ElectricExpansion.itemParts, 1, 6), 
			'$', ElectricExpansion.itemCoil, '&', "ingotCopper", 'x', "dyeGreen" }));

		// T2
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.blockTransformer, 1, 4), new Object[] { 
			"@!@", "#x#", "$&$", '!', new ItemStack(ElectricExpansion.blockTransformer, 1, 0), '@', "plateSteel", 
			'#', new ItemStack(ElectricExpansion.itemParts, 1, 6), '$', ElectricExpansion.itemCoil, '&', "ingotCopper", 'x', "dyeRed" }));

		// T3
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.blockTransformer, 1, 8), new Object[] { 
			"@!@", "# #", "$&$", '!', new ItemStack(ElectricExpansion.blockTransformer, 1, 4), '@', "plateSteel", 
			'#', new ItemStack(ElectricExpansion.itemParts, 1, 6), '$', ElectricExpansion.itemCoil, '&', "ingotCopper", 'x', "dyeGreen" }));

		// Upgrades
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 1), new Object[] { "$!$", "!@!", "#!#", '!', new ItemStack(ElectricExpansion.itemAdvBat, 1, -1), '@', new ItemStack(itemUpgrade, 1, 0), '#', "advancedCircuit", '$', "plateSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 2), new Object[] { "#!#", "!@!", "#!#", '!', new ItemStack(ElectricExpansion.itemEliteBat, 1, -1), '@', new ItemStack(itemUpgrade, 1, 1), '#', "eliteCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 3), new Object[] { "#!#", "!@!", "#!#", '!', "antimatterMilligram", '@', new ItemStack(itemUltimateBat, 1, -1), '#', "eliteCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 4), new Object[] { "#$#", "#!#", "#$#", '!', new ItemStack(ElectricExpansion.blockTransformer, 1, 4), '#', new ItemStack(blockInsulatedWire, 1, 0), '$', "basicCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 5), new Object[] { "#$#", "#!#", "#$#", '!', new ItemStack(ElectricExpansion.blockTransformer, 1, 8), '#', new ItemStack(blockInsulatedWire, 1, 3), '$', "eliteCircuit" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 6), new Object[] { "@$#", "@!#", "@$#", '!', new ItemStack(ElectricExpansion.blockTransformer, 1, 4), '#', new ItemStack(blockInsulatedWire, 1, 3), '@', new ItemStack(blockInsulatedWire, 1, 0), '$', "advancedCircuit" }));

		// Batteries
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemAdvBat), new Object[] { " T ", "TRT", "TRT", 'T', "ingotSilver", 'R', Item.lightStoneDust }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEliteBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateSteel", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "ingotLead", '$', Item.ghastTear }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUltimateBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateGold", '@', new ItemStack(blockInsulatedWire, 1, 4), '#', "antimatterMilligram", '$', "strangeMatter" }));

		// Parts
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 0), new Object[] { " # ", "! !", " ! ", '!', Item.ingotIron, '#', Item.diamond }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '!', Item.ingotGold, '#', "ingotSilver", '@', Item.eyeOfEnder }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '#', Item.ingotGold, '!', "ingotSilver", '@', Item.eyeOfEnder }));
		FurnaceRecipes.smelting().addSmelting(itemParts.itemID, 1, new ItemStack(itemParts, 4, 2), 0);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 5), new Object[] { "AA", "AA", 'A', Item.ingotGold }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansion.itemCoil), new Object[] { "AAA", "ABA", "AAA", 'B', Item.ingotIron, 'A', new ItemStack(blockInsulatedWire, 1, 0) }));

		// Silver Ore Smelting
		GameRegistry.addSmelting(ElectricExpansion.blockSilverOre.blockID, new ItemStack(ElectricExpansion.itemSilverIngot), 0.8F);

		// Storage Blocks
		GameRegistry.addRecipe(new ItemStack(ElectricExpansion.blockLead, 1), new Object[] { "@@@", "@@@", "@@@", '@', ElectricExpansion.itemLead });
		GameRegistry.addShapelessRecipe(new ItemStack(ElectricExpansion.itemLead, 9), new Object[] { ElectricExpansion.blockLead });

		// Tools
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0), '%', "advancedCircuit", '^', Block.glass, '@', Item.stick }));

		//Insulation
		GameRegistry.addShapelessRecipe(new ItemStack(itemParts, 1, 6), new Object[] { Block.cloth });
		GameRegistry.addShapelessRecipe(new ItemStack(itemParts, 4, 6), new Object[] { Item.leather });

		
		
		// Reflection to get Basic Components Wire
		if (Loader.isModLoaded("BasicComponents"))
		{
			try
			{
				basicCompWire = (Block) Class.forName("basiccomponents.common.BasicComponents").getField("blockCopperWire").get(Block.class);
			}
			catch (Exception e)
			{
				e.getStackTrace();
			}

			RecipeHelper.removeRecipe(new ItemStack(basicCompWire));

		}
	}

	public static void drawing()
	{
		if (!ElectricExpansion.debugRecipes)
		{
			WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 60);
			WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 60);
			WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 60);
			WireMillRecipes.addDrawing(HVIngot, new ItemStack(blockRawWire, 3, 3), 60);
			WireMillRecipes.addDrawing(new ItemStack(itemParts, 64, 2), new ItemStack(blockRawWire, 64, 4), 24000);

			for (int i = 0; i < 16; i++)
				WireMillRecipes.addDrawing(new ItemStack(Block.cloth, 10, i), new ItemStack(Item.silk, 40), 300);
		}
		if (ElectricExpansion.debugRecipes)
		{
			WireMillRecipes.addDrawing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 20);
			WireMillRecipes.addDrawing("ingotTin", new ItemStack(blockRawWire, 3, 1), 20);
			WireMillRecipes.addDrawing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 20);
			WireMillRecipes.addDrawing(HVIngot, new ItemStack(blockRawWire, 3, 3), 20);
			WireMillRecipes.addDrawing(new ItemStack(itemParts, 64, 2), new ItemStack(blockRawWire, 64, 4), 20);

			for (int i = 0; i < 16; i++)
				WireMillRecipes.addDrawing(new ItemStack(Block.cloth, 10, i), new ItemStack(Item.silk, 40), 30);
		}
	}
	
	public static void insulation()
	{
		InsulationRecipes.getProcessing().addProcessing(new ItemStack(Block.cloth), 1, 20);
		InsulationRecipes.getProcessing().addProcessing(new ItemStack(Item.leather), 3, 20);
	}
}