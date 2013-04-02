package electricexpansion.common;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.item.ItemElectric;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockBasic;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockInsulationMachine;
import electricexpansion.common.blocks.BlockLogisticsWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockQuantumBatteryBox;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockSwitchWire;
import electricexpansion.common.blocks.BlockSwitchWireBlock;
import electricexpansion.common.blocks.BlockTransformer;
import electricexpansion.common.blocks.BlockWireBlock;
import electricexpansion.common.blocks.BlockWireMill;
import electricexpansion.common.itemblocks.ItemBlockInsulatedWire;
import electricexpansion.common.itemblocks.ItemBlockLogisticsWire;
import electricexpansion.common.itemblocks.ItemBlockRawWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.common.itemblocks.ItemBlockTransformer;
import electricexpansion.common.itemblocks.ItemBlockWireBlock;
import electricexpansion.common.items.ItemAdvancedBattery;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemMultimeter;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemUltimateBattery;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.misc.DistributionNetworks;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.misc.EventHandler;

@Mod(modid = ElectricExpansion.MOD_ID, name = ElectricExpansion.MOD_NAME, version = ElectricExpansion.VERSION, dependencies = "after:BasicComponents;after:AtomicScience;after:ICBM|Contraption", useMetadata = true)
@NetworkMod(channels = { ElectricExpansion.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class ElectricExpansion
{
    // @Mod Prerequisites
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVIS_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    
    // @Mod
    public static final String MOD_ID = "ElectricExpansion";
    public static final String MOD_NAME = "Electric Expansion";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION
            + "." + REVIS_VERSION + "." + BUILD_VERSION;
    public static final String DEPENDENCIES = "after:UniversalElectricity;after:AtomicScience";
    public static final boolean USE_METADATA = true;
    
    // @NetworkMod
    public static final boolean USES_CLIENT = true;
    public static final boolean USES_SERVER = false;
    public static final String CHANNEL = "ElecEx";
    
    @Metadata(ElectricExpansion.MOD_ID)
    public static ModMetadata meta;
    
    private static final int BLOCK_ID_PREFIX = 3980;
    private static final int ITEM_ID_PREFIX = 15970;
    
    public static final String GUI_PATH = "/mods/electricexpansion/textures/gui/";
    public static final String MODEL_PATH = "/mods/electricexpansion/textures/models/";
    public static final String LANGUAGE_PATH = "/mods/electricexpansion/language/";
    
    public static final String TEXTURE_NAME_PREFIX = "electricexpansion:";
    
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US",
            "pl_PL" };
    
    public static OreGenBase silverOreGeneration;
    
    public static final Configuration CONFIG = new Configuration(new File(
            Loader.instance().getConfigDir(),
            "UniversalElectricity/ElectricExpansion.cfg"));
    public static boolean configLoaded = configLoad(CONFIG);
    
    public static Block blockRawWire;
    public static Block blockInsulatedWire;
    public static Block blockWireBlock;
    public static Block blockSwitchWire;
    public static Block blockSwitchWireBlock;
    public static Block blockLogisticsWire;
    // public static final Block blockRedstoneWire = new
    // BlockRedstoneWire(redstoneWire, 0);
    // public static final Block blockRedstoneWireBlock = new
    // BlockRedstoneWireBlock(redstoneWireBlock, 0);
    
    public static Block blockAdvBatteryBox;
    public static Block blockMultimeter;
    public static Block blockWireMill;
    public static Block blockTransformer;
    public static Block blockDistribution;
    public static Block blockLead;
    public static Block blockSilverOre;
    public static Block blockInsulationMachine;
    // public static Block blockFuseBox;
    // Items
    public static Item itemParts;
    public static Item itemUpgrade;
    public static ItemElectric itemEliteBat;
    public static ItemElectric itemAdvBat;
    public static ItemElectric itemUltimateBat;
    public static Item itemMultimeter;
    // public static Item itemFuse;
    
    static boolean debugRecipes;
    public static boolean useHashCodes;
    
    public static DistributionNetworks DistributionNetworksInstance;
    
    public static Logger EELogger = Logger.getLogger("ElectricExpansion");
    public static boolean[] startLogLogged = { false, false, false, false };
    
    @Instance("ElectricExpansion")
    public static ElectricExpansion instance;
    
    @SidedProxy(clientSide = "electricexpansion.client.ClientProxy", serverSide = "electricexpansion.common.CommonProxy")
    public static CommonProxy proxy;
    
    public static boolean configLoad(Configuration config)
    {
        config.load();
        
        blockRawWire = new BlockRawWire(config.getBlock("Uninsulated_Wire",
                BLOCK_ID_PREFIX).getInt(), 0);
        blockInsulatedWire = new BlockInsulatedWire(config.getBlock(
                "Insulated_Wire", BLOCK_ID_PREFIX + 1).getInt(), 0);
        blockWireBlock = new BlockWireBlock(config.getBlock("Wire_Block",
                BLOCK_ID_PREFIX + 2).getInt(), 0);
        blockSwitchWire = new BlockSwitchWire(config.getBlock("Switch_Wire",
                BLOCK_ID_PREFIX + 3).getInt(), 0);
        blockSwitchWireBlock = new BlockSwitchWireBlock(config.getBlock(
                "Switch_Wire_Block", BLOCK_ID_PREFIX + 4).getInt(), 0);
        // +5
        // +6
        blockAdvBatteryBox = new BlockAdvancedBatteryBox(config.getBlock(
                "Advanced_Battery_Box", BLOCK_ID_PREFIX + 7).getInt(), 0);
        blockMultimeter = new BlockMultimeter(config.getBlock("Multimeter",
                BLOCK_ID_PREFIX + 8).getInt(), 0);
        blockSilverOre = new BlockBasic(config.getBlock("Silver Ore",
                BLOCK_ID_PREFIX + 9).getInt(), Material.rock, EETab.INSTANCE,
                2F, "SilverOre");
        blockInsulationMachine = new BlockInsulationMachine(config.getBlock(
                "Insulation_Refiner", BLOCK_ID_PREFIX + 10).getInt());
        blockWireMill = new BlockWireMill(config.getBlock("Wire_Mill",
                BLOCK_ID_PREFIX + 11).getInt());
        blockTransformer = new BlockTransformer(config.getBlock("Transformer",
                BLOCK_ID_PREFIX + 12).getInt());
        blockDistribution = new BlockQuantumBatteryBox(config.getBlock(
                "Wireless_Transfer_Machines", BLOCK_ID_PREFIX + 13).getInt());
        blockLead = new BlockBasic(config.getBlock("Lead_Block",
                BLOCK_ID_PREFIX + 14).getInt(), Material.iron, EETab.INSTANCE,
                2F, "LeadBlock");
        blockLogisticsWire = new BlockLogisticsWire(config.getBlock(
                "Logistics_Wire", BLOCK_ID_PREFIX + 15).getInt(), 0);
        // blockFuseBox = new BlockFuseBox(config.getBlock("Fuse_Box",
        // BLOCK_ID_PREFIX + 16).getInt());
        
        itemUpgrade = new ItemUpgrade(config.getItem(
                "Advanced_Bat_Box_Upgrade", ITEM_ID_PREFIX).getInt(), 0);
        itemEliteBat = new ItemEliteBattery(config.getItem("Elite_Battery",
                ITEM_ID_PREFIX + 1).getInt());
        itemUltimateBat = new ItemUltimateBattery(config.getItem(
                "Ultimate_Battery", ITEM_ID_PREFIX + 2).getInt());
        itemParts = new ItemParts(config.getItem("Parts", ITEM_ID_PREFIX + 3)
                .getInt(), 0);
        // +4
        itemAdvBat = new ItemAdvancedBattery(config.getItem("Advanced_Battery",
                ITEM_ID_PREFIX + 5).getInt());
        // itemFuse = new ItemFuse(config.getItem("Fuses", ITEM_ID_PREFIX +
        // 6).getInt());
        // +7
        itemMultimeter = new ItemMultimeter(config.getItem("Item_Multimeter",
                ITEM_ID_PREFIX + 8).getInt());
        
        silverOreGeneration = new OreGenReplaceStone("Silver Ore", "oreSilver",
                new ItemStack(blockSilverOre, 1), 36, 10, 3).enable(config);
        
        debugRecipes = config.get("General", "Debug_Recipes", false,
                "Set to true for debug Recipes. This is considdered cheating.")
                .getBoolean(false);
        useHashCodes = config
                .get("General",
                        "Use_Hashcodes",
                        true,
                        "Set to true to make clients use hash codes for the Quantum Battery Box Owner data.")
                .getBoolean(true);
        
        if (config.hasChanged())
        {
            config.save();
        }
        
        configLoaded = true;
        return true; // returns true to configLoaded VAR
    }
    
    private static void StartLog(String string1)
    {
        
        String string2 = null;
        int j = 0;
        
        if (string1 != null && string1 == "preInit")
        {
            string2 = "PreInitializing";
            j = 1;
        }
        if (string1 != null && string1 == "Init")
        {
            string2 = "Initializing";
            j = 2;
        }
        if (string1 != null && string1 == "postInit")
        {
            string2 = "PostInitializing";
            j = 3;
        }
        
        EELogger.setParent(FMLLog.getLogger());
        EELogger.info(string2 + " ElectricExpansion v." + VERSION);
        startLogLogged[j] = true;
        if (startLogLogged[1] && startLogLogged[2] && startLogLogged[3])
        {
            startLogLogged[0] = true;
        }
    }
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        meta.modId = ElectricExpansion.MOD_ID;
        meta.name = ElectricExpansion.MOD_NAME;
        meta.description = "Electric Expansion is a Universal Electricity mod that focuses mainly on energy "
                + "storage and transfer as well as adding more cables for better energy transfer. "
                + "This mod will make Universal Electricity more complex and realistic. We try to "
                + "make all aspects as realistic as possible, whether that means the items and "
                + "block names or the processes and materials for each aspect of Electric Expansion.";
        
        meta.url = "http://universalelectricity.com/?m=electric_expansion";
        
        meta.logoFile = "/EELogo.png";
        meta.version = ElectricExpansion.VERSION;
        meta.authorList = Arrays
                .asList(new String[] { "Mattredsox & Alex_hawks" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;
        
        // UniversalElectricity.register(this, this.MAJOR_VERSION,
        // this.MINOR_VERSION, this.REVISION_VERSION, false);
        
        if (!configLoaded)
        {
            configLoad(CONFIG);
        }
        if (startLogLogged[1] != true)
        {
            StartLog("preInit");
        }
        
        GameRegistry.registerBlock(blockAdvBatteryBox, ItemBlock.class,
                "blockAdvBatteryBox", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockWireMill, ItemBlock.class,
                "blockWireMill", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockInsulationMachine, ItemBlock.class,
                "blockInsulationMachine", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockMultimeter, ItemBlock.class,
                "blockMultimeter", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockLead, ItemBlock.class, "blockLead",
                ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockTransformer,
                ItemBlockTransformer.class, "blockTransformer",
                ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockSilverOre, ItemBlock.class,
                "blockSilverOre", ElectricExpansion.MOD_ID);
        // GameRegistry.registerBlock(blockRedstoneWire, ItemBlock.class,
        // "blockRedstoneWire", this.MOD_ID);
        
        GameRegistry.registerBlock(blockDistribution, ItemBlock.class,
                "blockDistribution", ElectricExpansion.MOD_ID);
        // GameRegistry.registerBlock(blockFuseBox, ItemBlock.class,
        // "blockFuseBox", this.MOD_ID);
        
        GameRegistry.registerBlock(blockRawWire, ItemBlockRawWire.class,
                "blockRawWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockInsulatedWire,
                ItemBlockInsulatedWire.class, "blockInsulatedWire",
                ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockSwitchWire, ItemBlockSwitchWire.class,
                "blockSwitchWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockSwitchWireBlock,
                ItemBlockSwitchWireBlock.class, "blockSwitchWireBlock",
                ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockWireBlock, ItemBlockWireBlock.class,
                "blockWireBlock", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(blockLogisticsWire,
                ItemBlockLogisticsWire.class, "blockLogisticsWire",
                ElectricExpansion.MOD_ID);
        
        OreDictionary.registerOre("blockLead", ElectricExpansion.blockLead);
        OreDictionary.registerOre("advancedBattery",
                ElectricExpansion.itemAdvBat);
        OreDictionary.registerOre("eliteBattery",
                ElectricExpansion.itemEliteBat);
        OreDictionary.registerOre("advancedBattery",
                ElectricExpansion.itemAdvBat);
        OreDictionary.registerOre("transformer",
                ElectricExpansion.blockTransformer);
        OreDictionary.registerOre("wireMill", ElectricExpansion.blockWireMill);
        OreDictionary.registerOre("multimeter",
                ElectricExpansion.blockMultimeter);
        OreDictionary.registerOre("itemMultimeter",
                ElectricExpansion.itemMultimeter);
        OreDictionary.registerOre("ingotElectrum", new ItemStack(
                ElectricExpansion.itemParts, 1, 2));
        OreDictionary.registerOre("ingotLead", new ItemStack(
                ElectricExpansion.itemParts, 1, 7));
        OreDictionary.registerOre("coil", new ItemStack(
                ElectricExpansion.itemParts, 1, 8));
        OreDictionary.registerOre("ingotSilver", new ItemStack(
                ElectricExpansion.itemParts, 1, 9));
        
        OreDictionary.registerOre("copperWire", new ItemStack(
                ElectricExpansion.blockInsulatedWire, 1, 0));
        OreDictionary.registerOre("tinWire", new ItemStack(
                ElectricExpansion.blockInsulatedWire, 1, 1));
        OreDictionary.registerOre("silverWire", new ItemStack(
                ElectricExpansion.blockInsulatedWire, 1, 2));
        OreDictionary.registerOre("aluminumWire", new ItemStack(
                ElectricExpansion.blockInsulatedWire, 1, 3));
        OreDictionary.registerOre("superconductor", new ItemStack(
                ElectricExpansion.blockInsulatedWire, 1, 4));
        
        NetworkRegistry.instance().registerGuiHandler(this,
                ElectricExpansion.proxy);
        
        if (!Loader.isModLoaded("BasicComponents"))
        {
            EELogger.fine("Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
        }
    }
    
    @Init
    public void load(FMLInitializationEvent event)
    {
        if (startLogLogged[2] != true)
        {
            StartLog("Init");
        }
        proxy.init();
        
        RecipeRegistery.crafting();
        RecipeRegistery.drawing();
        RecipeRegistery.insulation();
        EETab.INSTANCE.setItemStack(new ItemStack(
                ElectricExpansion.blockTransformer));
        
        int languages = 0;
        
        /**
         * Load all languages.
         */
        for (String language : LANGUAGES_SUPPORTED)
        {
            LanguageRegistry.instance().loadLocalization(
                    LANGUAGE_PATH + language + ".properties", language, false);
            
            if (LanguageRegistry.instance().getStringLocalization("children",
                    language) != "")
            {
                try
                {
                    String[] children = LanguageRegistry.instance()
                            .getStringLocalization("children", language)
                            .split(",");
                    
                    for (String child : children)
                    {
                        if (child != "" && child != null)
                        {
                            LanguageRegistry.instance().loadLocalization(
                                    LANGUAGE_PATH + language + ".properties",
                                    child, false);
                            languages++;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        int unofficialLanguages = 0;
        unofficialLanguages = langLoad();
        
        EELogger.info("Loaded " + languages + " Official and "
                + unofficialLanguages + " unofficial languages");
        
        UniversalElectricity.isVoltageSensitive = true;
        
        EELogger.finest("Successfully toggled Voltage Sensitivity!");
        
        OreGenerator.addOre(silverOreGeneration);
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        if (startLogLogged[3] != true)
        {
            StartLog("postInit");
        }
        
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    
    @ServerStarting
    public void onServerStarting(FMLServerStartingEvent event)
    {
        ElectricExpansion.DistributionNetworksInstance = new DistributionNetworks();
    }
    
    private static File[] ListLanguages()
    {
        String folderDir = "";
        if (MinecraftServer.getServer().isDedicatedServer())
        {
            folderDir = "mods/" + "ElectricExpansionLanguages";
        }
        else if (!MinecraftServer.getServer().isDedicatedServer())
        {
            folderDir = Minecraft.getMinecraftDir() + File.separator + "mods"
                    + File.separator + "ElectricExpansionLanguages";
        }
        
        File folder = new File(folderDir);
        
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        
        File[] listOfFiles = folder.listFiles();
        
        return listOfFiles;
    }
    
    private static int langLoad()
    {
        int unofficialLanguages = 0;
        try
        {
            for (File langFile : ListLanguages())
            {
                if (langFile.exists())
                {
                    String name = langFile.getName();
                    if (name.endsWith(".lang"))
                    {
                        String lang = name.substring(0, name.length() - 4);
                        LanguageRegistry.instance().loadLocalization(
                                langFile.toString(), lang, false);
                        unofficialLanguages++;
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        return unofficialLanguages;
    }
}
