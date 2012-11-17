package electricexpansion.alex_hawks.misc;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import universalelectricity.prefab.RecipeHelper;
import basiccomponents.BasicComponents;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.ElectricExpansion;

public class RecipeRegistrar 
{	

	private static final Block blockSwitchWire = ElectricExpansion.blockSwitchWire;
	private static final Block blockSwitchWireBlock = ElectricExpansion.blockSwitchWireBlock;
	private static final Block blockAdvBox = ElectricExpansion.blockBigBatteryBox;
	private static final Block blockWireMill = ElectricExpansion.blockWireMill;
	private static final Block blockRawWire = ElectricExpansion.blockRawWire;
	private static final Block blockInsulatedWire = ElectricExpansion.blockInsulatedWire;
	private static final Block blockWireBlock = ElectricExpansion.blockWireBlock;
	private static final Block blockSwitchWireOff = ElectricExpansion.blockRawWire;
	private static final Block blockSwitchWireBlockOff = ElectricExpansion.blockRawWire;
	private static final Item itemParts = ElectricExpansion.itemParts;
	private static final Item itemLeadTearBat = ElectricExpansion.itemLeadTearBat;
	
	public static void crafting()
	{

		//Uninsulated Wire Recipes
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 0), new Object [] {" @ ", " @ ", " @ ", '@', "ingotCopper"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 1), new Object [] {" @ ", " @ ", " @ ", '@', "ingotTin"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 2), new Object [] {" @ ", " @ ", " @ ", '@', "ingotSilver"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 7, 3), new Object [] {" @ ", " @ ", " @ ", '@', "ingotAluminium"}));

		//Recipes for supporting other UE add-ons, the slack way...
		//	GameRegistry.addRecipe(new ShapelessOreRecipe("copperWire"), new Object[]{new ItemStack(blockInsulatedWire, 1, 0)}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{"copperWire"}));

		//Shapeless Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth}));

		//Shaped Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 0), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotCopper"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 1), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotTin"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 2), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotSilver"}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 7, 3), new Object [] {"#@#", "#@#", "#@#", '#', Block.cloth, '@', "ingotAluminium"}));

		//Wire Block Recipes (From insulation, Block.stone, and the corresponding Uninsulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone}));

		//Wire Block Recipes (From Block.stone, and the corresponding Insulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone}));

		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.lever}));

		//Shapeless Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireOff, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.lever}));

		//Switch Wire Block Recipes (From insulation, Block.stone, Block.lever and the corresponding Uninsulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockRawWire, 1, 0), Block.cloth, Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockRawWire, 1, 1), Block.cloth, Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockRawWire, 1, 2), Block.cloth, Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockRawWire, 1, 3), Block.cloth, Block.stone, Block.lever}));

		//Switch Wire Block Recipes (From Block.stone, Block,lever and the corresponding Insulated Wire)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockInsulatedWire, 1, 0), Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockInsulatedWire, 1, 1), Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockInsulatedWire, 1, 2), Block.stone, Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockInsulatedWire, 1, 3), Block.stone, Block.lever}));

		//Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 0), new Object[]{new ItemStack(blockWireBlock, 1, 0), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 1), new Object[]{new ItemStack(blockWireBlock, 1, 1), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 2), new Object[]{new ItemStack(blockWireBlock, 1, 2), Block.lever}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockSwitchWireBlockOff, 1, 3), new Object[]{new ItemStack(blockWireBlock, 1, 3), Block.lever}));

		//Machines
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockAdvBox), new Object [] {"!!!", "!@!", "#$#", '!', ElectricExpansion.itemSuperConduct.getUncharged(),'@', "batteryBox", '?', "battery"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(blockAdvBox, new Object [] {"!!!", "!@!", "#$#", '!', "battery",'@', "batteryBox", '?', "battery", '$', "eliteCircuit", '#', blockWireBlock}));
//		GameRegistry.addRecipe(new ItemStack(blockWireMill), new Object [] {"#$#", "!%!", "@!@", '!', "motor", '#', "plateSteel", '@', "plateBronze", '$', "basicCircuit", '%', new ItemStack(itemParts, 1, 0)});
		
		//Parts
		GameRegistry.addRecipe(new ItemStack(itemParts, 1, 0), new Object [] {" ! ", "! !", " ! ", '!', Item.ingotIron});	
				
	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLeadTearBat), new Object [] {"!@!", "#$#", "!@!", '!', "plateSteel", '@', "copperWire", '#', "ingotLead", '$', Item.ghastTear}));
				
				if(Loader.isModLoaded("BasicComponents")) 
				{
					RecipeHelper.removeRecipe(BasicComponents.batteryBox);
				}
		if(Loader.isModLoaded("BasicComponents")) 
			RecipeHelper.removeRecipe(BasicComponents.batteryBox);
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
