package electricexpansion.common;

import java.util.logging.Level;

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
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.misc.InsulationRecipes;
import electricexpansion.common.misc.WireMillRecipes;

public class RecipeRegistery
{
    // EE Blocks
    private static final Block blockInsulatedWire = ElectricExpansionItems.blockInsulatedWire;
    private static final Block blockRawWire = ElectricExpansionItems.blockRawWire;
    private static final Block blockWireBlock = ElectricExpansionItems.blockWireBlock;
    private static final Block blockSwitchWire = ElectricExpansionItems.blockSwitchWire;
    private static final Block blockSwitchWireBlock = ElectricExpansionItems.blockSwitchWireBlock;
    private static final Block blockLogisticsWire = ElectricExpansionItems.blockLogisticsWire;
    private static final Block blockRedstoneWire = ElectricExpansionItems.blockRedstonePaintedWire;
    
    private static final Block blockBatBox = ElectricExpansionItems.blockAdvBatteryBox;
    private static final Block blockWireMill = ElectricExpansionItems.blockWireMill;
    private static final Block blockMultimeter = ElectricExpansionItems.blockMultimeter;
    private static final Block blockDistribution = ElectricExpansionItems.blockDistribution;
    private static final Block blockInsulationMachine = ElectricExpansionItems.blockInsulationMachine;
    
    // EE Items
    private static final Item itemParts = ElectricExpansionItems.itemParts;
    private static final Item itemUpgrade = ElectricExpansionItems.itemUpgrade;
    private static final Item itemMultimeter = ElectricExpansionItems.itemMultimeter;
    
    // EE ItemElectrics
    private static final ItemElectric itemAdvBat = ElectricExpansionItems.itemAdvBat;
    private static final ItemElectric itemEliteBat = ElectricExpansionItems.itemEliteBat;
    private static final ItemElectric itemUltimateBat = ElectricExpansionItems.itemUltimateBat;
    
    // InterMod Blocks
    private static ItemStack camo; // Will be ICBM Camouflage if ICBM is installed
    
    private static final ItemStack insulationIS = new ItemStack(itemParts, 1, 6);
    
    public static void crafting()
    {
        // To prevent being overpowered...
        if (Loader.isModLoaded("BasicComponents"))
        {
            for (ItemStack wire : OreDictionary.getOres("copperWire"))
                RecipeHelper.removeRecipe(wire);
        }
        
        FurnaceRecipes.smelting().addSmelting(itemParts.itemID, 3, new ItemStack(itemParts, 4, 4), 0);
        
        try
        {
            camo = GameRegistry.findItemStack("ICBM|Contraption", "camouflage", 1);
        }
        catch (NullPointerException e)
        {
            // So what, we didn't find it...
        }
        
        if (camo == null) // ICBM|Contraption Camouflage block not found
        {
            ElectricExpansion.log(Level.INFO, "Failed to detect ICBM|Contraption");
            ElectricExpansion.log(Level.INFO, "Using %s's items instead.", ElectricExpansion.MOD_NAME);
            camo = new ItemStack(itemParts, 1, 5);
            GameRegistry.addRecipe(new ShapelessOreRecipe(camo, new Object[] { Block.cloth, Item.bucketEmpty, Item.slimeBall, Item.redstone, "dyeRed", "dyeBlue", "dyeYellow", "dyeBlack", "dyeWhite" }));
        }
        
        registerRawCables();
        registerInsulatedCables();
        registerSwitchCables();
        registerLogisticsCables();
        registerCamoCables();
        registerCamoSwitchCables();
        //registerRedstoneCables();
        //they are broken in their special functions
        
        // Machines
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockWireMill), new Object[] { "#$#", "!%!", "@!@", '!', "motor", '#', "plateSteel", '@', "plateBronze", '$', "basicCircuit", '%', new ItemStack(itemParts, 1, 0) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', new ItemStack(blockInsulatedWire, 1, 0), '%', "basicCircuit", '^', Block.glass, '@', Item.stick }));
        for (ItemStack batt : OreDictionary.getOres("battery"))
        {
            ItemStack wildBatt = new ItemStack(batt.itemID, batt.stackSize, Integer.MIN_VALUE); 
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBatBox), new Object[] { "!!!", "@@@", "!#!", '!', wildBatt, '@', "copperWire", '#', "basicCircuit" }));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulationMachine), new Object[] { "!@!", "@#@", "!$!", '!', "plateSteel", '@', Block.obsidian, '#', Item.bucketLava, '$', Block.furnaceIdle }));
        
        if (OreDictionary.getOres("antimatterGram").size() > 0)
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDistribution), new Object[] { "!!!", "!@!", "!!!", '@', new ItemStack(blockBatBox), '!', "antimatterGram" }));
        }
        else
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockDistribution), new Object[] { "!!!", "!@!", "!!!", '@', new ItemStack(blockBatBox), '!', new ItemStack(itemParts, 1, 1) }));
        }
        
        // Transformers
        // T1
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansionItems.blockTransformer, 1, 0), new Object[] { "$&$", "#x#", "@@@", '@', "plateSteel", '#', new ItemStack(ElectricExpansionItems.itemParts, 1, 6), '$', new ItemStack(ElectricExpansionItems.itemParts, 1, 8), '&', "ingotCopper", 'x', "dyeGreen" }));
        
        // T2
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), new Object[] { "$&$", "#x#", "@!@", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 0), '@', "plateSteel", '#', new ItemStack(ElectricExpansionItems.itemParts, 1, 6), '$', new ItemStack(ElectricExpansionItems.itemParts, 1, 8), '&', "ingotCopper", 'x', "dyeRed" }));
        
        // T3
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansionItems.blockTransformer, 1, 8), new Object[] { "$&$", "#x#", "@!@", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '@', "plateSteel", '#', new ItemStack(ElectricExpansionItems.itemParts, 1, 6), '$', new ItemStack(ElectricExpansionItems.itemParts, 1, 8), '&', "ingotCopper", 'x', "dyeBlue" }));
        
        // Upgrades
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  0), new Object[] { "$!$", "!@!", "#!#", '!', new ItemStack(blockInsulatedWire, 1, 0), '@', new ItemStack(ElectricExpansionItems.itemAdvBat, 1, Integer.MIN_VALUE), '#', "advancedCircuit", '$', "plateSteel" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  1), new Object[] { "$!$", "%@%", "#%#", '!', new ItemStack(ElectricExpansionItems.itemAdvBat, 1, Integer.MIN_VALUE), '@', new ItemStack(itemUpgrade, 1, 0), '#', "advancedCircuit", '$', "plateSteel", '%', new ItemStack(blockInsulatedWire, 1, 0) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  2), new Object[] { "#!#", "%@%", "#%#", '!', new ItemStack(ElectricExpansionItems.itemEliteBat, 1, Integer.MIN_VALUE), '@', new ItemStack(itemUpgrade, 1, 1), '#', "eliteCircuit", '%', new ItemStack(blockInsulatedWire, 1, 0) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  3), new Object[] { "#!#", "!@!", "#!#", '!', "antimatterMilligram", '@', new ItemStack(itemUltimateBat, 1, Integer.MIN_VALUE), '#', "eliteCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  4), new Object[] { "#$#", "#!#", "#$#", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '#', new ItemStack(blockInsulatedWire, 1, 0), '$', "basicCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  5), new Object[] { "#$#", "#!#", "#$#", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 8), '#', new ItemStack(blockInsulatedWire, 1, 3), '$', "eliteCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  6), new Object[] { "@$#", "@!#", "@$#", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '#', new ItemStack(blockInsulatedWire, 1, 3), '@', new ItemStack(blockInsulatedWire, 1, 0), '$', "advancedCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  7), new Object[] { "#$#", "#!#", "#$#", '!', new ItemStack(ElectricExpansionItems.blockTransformer, 1, 4), '#', new ItemStack(blockInsulatedWire, 1, 2), '$', "basicCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  8), new Object[] { "!@!", "@#@", "!@!", '!', "plateTin", '@', "plateCopper", '#', "advancedCircuit" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1,  9), new Object[] { "!@!", "@#@", "!@!", '!', "plateTin", '@', "plateCopper", '#', new ItemStack(itemUpgrade, 1, 8) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 10), new Object[] { "!@!", "@#@", "!@!", '!', "plateTin", '@', "plateCopper", '#', new ItemStack(itemUpgrade, 1, 9) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 11), new Object[] { "!@!", "@#@", "!@!", '!', "plateTin", '@', "plateCopper", '#', new ItemStack(itemUpgrade, 1, 10) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 13), new Object[] { "!@!", "@#@", "!@!", '!', "antimatterMilligram", '@', "plateSteel", '#', new ItemStack(itemUpgrade, 1, 16) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUpgrade, 1, 16), new Object[] { "!@!", "@#@", "!@!", '!', "antimatterMilligram", '@', "plateSteel", '#', "eliteCircuit" }));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ElectricExpansionItems.itemLinkCard), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(Item.ingotIron), '@', new ItemStack(Item.paper), '#', "advancedCircuit" } ));

        // Batteries
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemAdvBat), new Object[] { " T ", "TRT", "TRT", 'T', "ingotSilver", 'R', Item.lightStoneDust }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEliteBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateSteel", '@', new ItemStack(blockInsulatedWire, 1, 0), '#', "ingotLead", '$', Item.ghastTear }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemUltimateBat), new Object[] { "!@!", "#$#", "!@!", '!', "plateGold", '@', new ItemStack(blockInsulatedWire, 1, 4), '#', "antimatterMilligram", '$', "strangeMatter" }));
        
        // Parts
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 0), new Object[] { " # ", "! !", " ! ", '!', Item.ingotIron, '#', Item.diamond }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '!', Item.ingotGold, '#', "ingotSilver", '@', Item.eyeOfEnder }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 1), new Object[] { "!#!", "#@#", "!#!", '#', Item.ingotGold, '!', "ingotSilver", '@', Item.eyeOfEnder }));
        FurnaceRecipes.smelting().addSmelting(itemParts.itemID, 1, new ItemStack(itemParts, 4, 2), 0);
        GameRegistry.addShapelessRecipe(new ItemStack(ElectricExpansionItems.itemParts, 9, 7), new Object[] { ElectricExpansionItems.blockLead });
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemParts, 1, 8), new Object[] { "AAA", "ABA", "AAA", 'B', Item.ingotIron, 'A', new ItemStack(blockInsulatedWire, 1, 0) }));
        GameRegistry.addSmelting(ElectricExpansionItems.blockSilverOre.blockID, new ItemStack(ElectricExpansionItems.itemParts, 1, 9), 0.8F);
        
        // Storage Blocks
        GameRegistry.addRecipe(new ItemStack(ElectricExpansionItems.blockLead, 1), new Object[] { "@@@", "@@@", "@@@", '@', new ItemStack(ElectricExpansionItems.itemParts, 1, 7) });
        
        // Tools
        for (ItemStack batt : OreDictionary.getOres("battery"))
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemMultimeter), new Object[] { "$^$", "!@!", "$%$", '!', "plateCopper", '$', "copperWire", '%', "advancedCircuit", '^', Block.glass, '@', batt }));
        }
    }
    
    public static void drawing()
    {
        if (!ElectricExpansion.debugRecipes)
        {
            WireMillRecipes.INSTANCE.addProcessing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 60);
            WireMillRecipes.INSTANCE.addProcessing("ingotTin", new ItemStack(blockRawWire, 3, 1), 60);
            WireMillRecipes.INSTANCE.addProcessing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 60);
            WireMillRecipes.INSTANCE.addProcessing("ingotAluminum", new ItemStack(blockRawWire, 3, 3), 60);
            WireMillRecipes.INSTANCE.addProcessing(new ItemStack(itemParts, 64, 2), new ItemStack(blockRawWire, 64, 4), 24000);
            
            for (int i = 0; i < 16; i++)
            {
                WireMillRecipes.INSTANCE.addProcessing(new ItemStack(Block.cloth, 10, i), new ItemStack(Item.silk, 40), 300);
            }
        }
        else
        {
            WireMillRecipes.INSTANCE.addProcessing("ingotCopper", new ItemStack(blockRawWire, 3, 0), 20);
            WireMillRecipes.INSTANCE.addProcessing("ingotTin", new ItemStack(blockRawWire, 3, 1), 20);
            WireMillRecipes.INSTANCE.addProcessing("ingotSilver", new ItemStack(blockRawWire, 3, 2), 20);
            WireMillRecipes.INSTANCE.addProcessing("ingotAluminum", new ItemStack(blockRawWire, 3, 3), 20);
            WireMillRecipes.INSTANCE.addProcessing(new ItemStack(itemParts, 64, 2), new ItemStack(blockRawWire, 64, 4), 20);
            
            for (int i = 0; i < 16; i++)
            {
                WireMillRecipes.INSTANCE.addProcessing(new ItemStack(Block.cloth, 10, i), new ItemStack(Item.silk, 40), 30);
            }
        }
    }
    
    public static void insulation()
    {
        int ticks = ElectricExpansion.debugRecipes ? 1 : 8;
        
        for (int i = 0; i < 16; i++)
        {
            InsulationRecipes.INSTANCE.addProcessing(new ItemStack(Block.cloth, 32, i), 32, ticks * 80);
        }
        
        InsulationRecipes.INSTANCE.addProcessing(new ItemStack(Item.leather, 8), 24, ticks * 25);
        InsulationRecipes.INSTANCE.addProcessing(new ItemStack(Item.rottenFlesh, 8), 16, ticks * 25);
        FurnaceRecipes.smelting().addSmelting(Item.leather.itemID, insulationIS, 0.7f);
        GameRegistry.addRecipe(new ShapelessOreRecipe(insulationIS, Block.cloth, Block.cloth, Block.cloth, Block.cloth));
        
        for (ItemStack is : OreDictionary.getOres("itemRubber"))
        {
            if (!is.isItemEqual(insulationIS))
            {
                ItemStack is2 = is.copy();
                is2.stackSize = 8;
                InsulationRecipes.INSTANCE.addProcessing(is2, is2.stackSize, ((int) Math.floor((is2.stackSize * ticks * 10.0 / 8.0) + 0.5)));
            }
        }
    }
    
    public static void registerRawCables()
    {
        // Uninsulated Wire Recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 0), new Object[] { " @ ", " @ ", " @ ", '@', "ingotCopper" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 1), new Object[] { " @ ", " @ ", " @ ", '@', "ingotTin" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 2), new Object[] { " @ ", " @ ", " @ ", '@', "ingotSilver" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRawWire, 6, 3), new Object[] { " @ ", " @ ", " @ ", '@', "ingotAluminum" }));
    }
    
    public static void registerInsulatedCables()
    {
        // Insulated Wire Recipes (From insulation, and the corresponding OreDictionary Ingots)
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 0), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotCopper" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 1), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotTin" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 2), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotSilver" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockInsulatedWire, 6, 3), new Object[] { "#@#", "#@#", "#@#", '#', insulationIS, '@', "ingotAluminum" }));
        
        // Insulated Wire Recipes (From insulation, and the corresponding Uninsulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS });
        GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS });
        GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS });
        GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS });
        GameRegistry.addShapelessRecipe(new ItemStack(blockInsulatedWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7) });
    }
    
    public static void registerSwitchCables()
    {
        // Switch Wire Recipes (From insulation, a lever, and the corresponding Uninsulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), Block.lever });
        
        // Switch Wire Recipes (From a lever and the corresponding Insulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWire, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.lever });
    }
    
    public static void registerLogisticsCables()
    {
        // Logistics Wire Recipes (From a unit of Redstone Dust, a lever, some insulation, and the
        // corresponding Uninsulated Wire)
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), Block.lever, Item.redstone }));
        
        // Logistics Wire Recipes (From a unit of Redstone Dust, a lever, and the corresponding
        // Insulated Wire)
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), Block.lever, Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), Block.lever, Item.redstone }));
        
        // Logistics Wire Recipes (From a unit of Redstone Dust, and the corresponding Switch Wire)
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 0), new Object[] { new ItemStack(blockSwitchWire, 1, 0), Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 1), new Object[] { new ItemStack(blockSwitchWire, 1, 1), Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 2), new Object[] { new ItemStack(blockSwitchWire, 1, 2), Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 3), new Object[] { new ItemStack(blockSwitchWire, 1, 3), Item.redstone }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockLogisticsWire, 1, 4), new Object[] { new ItemStack(blockSwitchWire, 1, 4), Item.redstone }));
    }
    
    public static void registerCamoCables()
    {
        // Wire Block Recipes (From insulation, Camouflage, and the corresponding Uninsulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), camo });
        
        // Wire Block Recipes (From Camouflage, and the corresponding Insulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), camo });
    }
    
    public static void registerCamoSwitchCables()
    {
        // Switch Wire Block Recipes (From insulation, Camouflage, Block.lever and the corresponding
        // Uninsulated Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockRawWire, 1, 0), insulationIS, camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockRawWire, 1, 1), insulationIS, camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockRawWire, 1, 2), insulationIS, camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockRawWire, 1, 3), insulationIS, camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockRawWire, 1, 4), new ItemStack(itemParts, 3, 7), camo, Block.lever });
        
        // Switch Wire Block Recipes (From Camouflage, Block.lever, and the corresponding Insulated
        // Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockInsulatedWire, 1, 0), camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockInsulatedWire, 1, 1), camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockInsulatedWire, 1, 2), camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockInsulatedWire, 1, 3), camo, Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockInsulatedWire, 1, 4), camo, Block.lever });
        
        // Switch Wire Block Recipes (From Camouflage, and the corresponding Switch Wire)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockSwitchWire, 1, 0), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockSwitchWire, 1, 1), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockSwitchWire, 1, 2), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockSwitchWire, 1, 3), camo });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockSwitchWire, 1, 4), camo });
        
        // Switch Wire Block Recipes (From Block.lever, and the corresponding Wire Block)
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 0), new Object[] { new ItemStack(blockWireBlock, 1, 0), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 1), new Object[] { new ItemStack(blockWireBlock, 1, 1), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 2), new Object[] { new ItemStack(blockWireBlock, 1, 2), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 3), new Object[] { new ItemStack(blockWireBlock, 1, 3), Block.lever });
        GameRegistry.addShapelessRecipe(new ItemStack(blockSwitchWireBlock, 1, 4), new Object[] { new ItemStack(blockWireBlock, 1, 4), Block.lever });
    }
    
    public static void registerRedstoneCables()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 0), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(blockInsulatedWire, 1, 0), '@', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 1), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(blockInsulatedWire, 1, 1), '@', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 2), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(blockInsulatedWire, 1, 2), '@', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 3), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(blockInsulatedWire, 1, 3), '@', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 4), new Object[] { "!@!", "@#@", "!@!", '!', new ItemStack(blockInsulatedWire, 1, 4), '@', Item.redstone, '#', Item.slimeBall }));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 0), new Object[] { "!@!", "@#@", "!@!", '@', new ItemStack(blockInsulatedWire, 1, 0), '!', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 1), new Object[] { "!@!", "@#@", "!@!", '@', new ItemStack(blockInsulatedWire, 1, 1), '!', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 2), new Object[] { "!@!", "@#@", "!@!", '@', new ItemStack(blockInsulatedWire, 1, 2), '!', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 3), new Object[] { "!@!", "@#@", "!@!", '@', new ItemStack(blockInsulatedWire, 1, 3), '!', Item.redstone, '#', Item.slimeBall }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRedstoneWire, 4, 4), new Object[] { "!@!", "@#@", "!@!", '@', new ItemStack(blockInsulatedWire, 1, 4), '!', Item.redstone, '#', Item.slimeBall }));
    }
    public static ItemStack getInsulationIS() { //need this for things
        return insulationIS;
    }
}