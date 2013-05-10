package electricexpansion.common;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
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
import cpw.mods.fml.relauncher.Side;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockBasic;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockInsulationMachine;
import electricexpansion.common.blocks.BlockLogisticsWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockQuantumBatteryBox;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockRedstoneNetworkCore;
import electricexpansion.common.blocks.BlockRedstonePaintedWire;
import electricexpansion.common.blocks.BlockSwitchWire;
import electricexpansion.common.blocks.BlockSwitchWireBlock;
import electricexpansion.common.blocks.BlockTransformer;
import electricexpansion.common.blocks.BlockWireBlock;
import electricexpansion.common.blocks.BlockWireMill;
import electricexpansion.common.itemblocks.ItemBlockInsulatedWire;
import electricexpansion.common.itemblocks.ItemBlockLogisticsWire;
import electricexpansion.common.itemblocks.ItemBlockRawWire;
import electricexpansion.common.itemblocks.ItemBlockRedstonePaintedWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.common.itemblocks.ItemBlockTransformer;
import electricexpansion.common.itemblocks.ItemBlockWireBlock;
import electricexpansion.common.items.ItemAdvancedBattery;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemLinkCard;
import electricexpansion.common.items.ItemMultimeter;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemUltimateBattery;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.misc.DistributionNetworks;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.misc.EventHandler;

@Mod(modid = ElectricExpansion.MOD_ID, name = ElectricExpansion.MOD_NAME, version = ElectricExpansion.VERSION, dependencies = "after:BasicComponents;after:AtomicScience;after:ICBM|Contraption;after:MineFactoryReloaded;after:IC2", useMetadata = true)
@NetworkMod(channels = { ElectricExpansion.CHANNEL }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)
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
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVIS_VERSION + "." + BUILD_VERSION;
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
    
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "pl_PL" };
    
    public static OreGenBase silverOreGeneration;
    
    public static final Configuration CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/ElectricExpansion.cfg"));
    public static boolean configLoaded = false;
    
    public static boolean debugRecipes = true;
    public static boolean useHashCodes = true;
    private static boolean useUeVoltageSensitivity = false;
    
    public static DistributionNetworks DistributionNetworksInstance;
    
    public static Logger EELogger = Logger.getLogger("ElectricExpansion");
    
    @Instance("ElectricExpansion")
    public static ElectricExpansion instance;
    
    @SidedProxy(clientSide = "electricexpansion.client.ClientProxy", serverSide = "electricexpansion.common.CommonProxy")
    public static CommonProxy proxy;
    
    public static void log(Level level, String msg, String... replacements)
    {
        for (String replace : replacements)
        {
            msg = msg.replace("%s", replace);
        }
        EELogger.log(level, msg);
    }
    
    public static boolean configLoad(Configuration config)
    {
        config.load();
        
        ElectricExpansionItems.blockRawWire = new BlockRawWire(config.getBlock("Uninsulated_Wire", BLOCK_ID_PREFIX).getInt());
        ElectricExpansionItems.blockInsulatedWire = new BlockInsulatedWire(config.getBlock("Insulated_Wire", BLOCK_ID_PREFIX + 1).getInt());
        ElectricExpansionItems.blockWireBlock = new BlockWireBlock(config.getBlock("Wire_Block", BLOCK_ID_PREFIX + 2).getInt());
        ElectricExpansionItems.blockSwitchWire = new BlockSwitchWire(config.getBlock("Switch_Wire", BLOCK_ID_PREFIX + 3).getInt());
        ElectricExpansionItems.blockSwitchWireBlock = new BlockSwitchWireBlock(config.getBlock("Switch_Wire_Block", BLOCK_ID_PREFIX + 4).getInt());
        ElectricExpansionItems.blockRedstonePaintedWire = new BlockRedstonePaintedWire(config.getBlock("Redstone_Wire", BLOCK_ID_PREFIX + 5).getInt());
        // +6
        ElectricExpansionItems.blockAdvBatteryBox = new BlockAdvancedBatteryBox(config.getBlock("Advanced_Battery_Box", BLOCK_ID_PREFIX + 7).getInt(), 0);
        ElectricExpansionItems.blockMultimeter = new BlockMultimeter(config.getBlock("Multimeter", BLOCK_ID_PREFIX + 8).getInt(), 0);
        ElectricExpansionItems.blockSilverOre = new BlockBasic(config.getBlock("Silver Ore", BLOCK_ID_PREFIX + 9).getInt(), Material.rock, EETab.INSTANCE, 2F, "SilverOre");
        ElectricExpansionItems.blockInsulationMachine = new BlockInsulationMachine(config.getBlock("Insulation_Refiner", BLOCK_ID_PREFIX + 10).getInt());
        ElectricExpansionItems.blockWireMill = new BlockWireMill(config.getBlock("Wire_Mill", BLOCK_ID_PREFIX + 11).getInt());
        ElectricExpansionItems.blockTransformer = new BlockTransformer(config.getBlock("Transformer", BLOCK_ID_PREFIX + 12).getInt());
        ElectricExpansionItems.blockDistribution = new BlockQuantumBatteryBox(config.getBlock("Wireless_Transfer_Machines", BLOCK_ID_PREFIX + 13).getInt());
        ElectricExpansionItems.blockLead = new BlockBasic(config.getBlock("Lead_Block", BLOCK_ID_PREFIX + 14).getInt(), Material.iron, EETab.INSTANCE, 2F, "LeadBlock");
        ElectricExpansionItems.blockLogisticsWire = new BlockLogisticsWire(config.getBlock("Logistics_Wire", BLOCK_ID_PREFIX + 15).getInt(), 0);
        // ElectricExpansionItems.blockFuseBox = new BlockFuseBox(config.getBlock("Fuse_Box", BLOCK_ID_PREFIX + 16).getInt());
        ElectricExpansionItems.blockRedstoneNetworkCore = new BlockRedstoneNetworkCore(config.getBlock("RS_Network_Core", BLOCK_ID_PREFIX + 16).getInt());
        
        ElectricExpansionItems.itemUpgrade = new ItemUpgrade(config.getItem("Advanced_Bat_Box_Upgrade", ITEM_ID_PREFIX).getInt(), 0);
        ElectricExpansionItems.itemEliteBat = new ItemEliteBattery(config.getItem("Elite_Battery", ITEM_ID_PREFIX + 1).getInt());
        ElectricExpansionItems.itemUltimateBat = new ItemUltimateBattery(config.getItem("Ultimate_Battery", ITEM_ID_PREFIX + 2).getInt());
        ElectricExpansionItems.itemParts = new ItemParts(config.getItem("Parts", ITEM_ID_PREFIX + 3).getInt(), 0);
        ElectricExpansionItems.itemLinkCard = new ItemLinkCard(config.getItem("Link_Card", ITEM_ID_PREFIX + 3).getInt());
        ElectricExpansionItems.itemAdvBat = new ItemAdvancedBattery(config.getItem("Advanced_Battery", ITEM_ID_PREFIX + 5).getInt());
        // itemFuse = new ItemFuse(config.getItem("Fuses", ITEM_ID_PREFIX + 6).getInt());
        // +7
        ElectricExpansionItems.itemMultimeter = new ItemMultimeter(config.getItem("Item_Multimeter", ITEM_ID_PREFIX + 8).getInt());
        
        GameRegistry.registerBlock(ElectricExpansionItems.blockSilverOre, ItemBlock.class, "blockSilverOre", ElectricExpansion.MOD_ID);
        
        silverOreGeneration = new OreGenReplaceStone("Silver Ore", "oreSilver", new ItemStack(ElectricExpansionItems.blockSilverOre), 36, 10, 3).enable(config);
        
        debugRecipes = config.get("General", "Debug_Recipes", false, "Set to true for debug Recipes. This is considdered cheating.").getBoolean(debugRecipes);
        useHashCodes = config.get("General", "Use_Hashcodes", true, "Set to true to make clients use hash codes for the Quantum Battery Box Owner data.").getBoolean(useHashCodes);
        useUeVoltageSensitivity = config.get("General", "Use_UeVoltageSensitivity", false, "Set to true to use the setting in the UE config file for Voltage Sensitivity.").getBoolean(useUeVoltageSensitivity);
        
        if (config.hasChanged())
            config.save();
        
        configLoaded = true;
        return true; // returns true to configLoaded VAR
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
        
        meta.url = "http://universalelectricity.com/electric%20expansion";
        
        meta.logoFile = "/EELogo.png";
        meta.version = ElectricExpansion.VERSION + "." + BUILD_VERSION;
        meta.authorList = Arrays.asList(new String[] { "Mattredsox & Alex_hawks" });
        meta.credits = "Please see the website.";
        meta.autogenerated = false;
        
        if (!configLoaded)
        {
            configLoad(CONFIG);
        }
        log(Level.INFO, "PreInitializing ElectricExpansion v." + VERSION);
        
        GameRegistry.registerBlock(ElectricExpansionItems.blockAdvBatteryBox, ItemBlock.class, "blockAdvBatteryBox", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockWireMill, ItemBlock.class, "blockWireMill", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockInsulationMachine, ItemBlock.class, "blockInsulationMachine", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockMultimeter, ItemBlock.class, "blockMultimeter", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockLead, ItemBlock.class, "blockLead", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockTransformer, ItemBlockTransformer.class, "blockTransformer", ElectricExpansion.MOD_ID);
        
        GameRegistry.registerBlock(ElectricExpansionItems.blockDistribution, ItemBlock.class, "blockDistribution", ElectricExpansion.MOD_ID);
        // GameRegistry.registerBlock(blockFuseBox, ItemBlock.class, "blockFuseBox", this.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockRedstoneNetworkCore, ItemBlock.class, "blockRsNetworkCore", ElectricExpansion.MOD_ID);
        
        GameRegistry.registerBlock(ElectricExpansionItems.blockRawWire, ItemBlockRawWire.class, "blockRawWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockInsulatedWire, ItemBlockInsulatedWire.class, "blockInsulatedWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockSwitchWire, ItemBlockSwitchWire.class, "blockSwitchWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockSwitchWireBlock, ItemBlockSwitchWireBlock.class, "blockSwitchWireBlock", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockWireBlock, ItemBlockWireBlock.class, "blockWireBlock", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockLogisticsWire, ItemBlockLogisticsWire.class, "blockLogisticsWire", ElectricExpansion.MOD_ID);
        GameRegistry.registerBlock(ElectricExpansionItems.blockRedstonePaintedWire, ItemBlockRedstonePaintedWire.class, "blockRedstonePaintedWire", ElectricExpansion.MOD_ID);
        
        OreDictionary.registerOre("blockLead", ElectricExpansionItems.blockLead);
        OreDictionary.registerOre("advancedBattery", ElectricExpansionItems.itemAdvBat);
        OreDictionary.registerOre("eliteBattery", ElectricExpansionItems.itemEliteBat);
        OreDictionary.registerOre("advancedBattery", ElectricExpansionItems.itemAdvBat);
        OreDictionary.registerOre("transformer", ElectricExpansionItems.blockTransformer);
        OreDictionary.registerOre("wireMill", ElectricExpansionItems.blockWireMill);
        OreDictionary.registerOre("multimeter", ElectricExpansionItems.blockMultimeter);
        OreDictionary.registerOre("itemMultimeter", ElectricExpansionItems.itemMultimeter);
        OreDictionary.registerOre("ingotElectrum", new ItemStack(ElectricExpansionItems.itemParts, 1, 2));
        OreDictionary.registerOre("ingotLead", new ItemStack(ElectricExpansionItems.itemParts, 1, 7));
        OreDictionary.registerOre("coil", new ItemStack(ElectricExpansionItems.itemParts, 1, 8));
        OreDictionary.registerOre("ingotSilver", new ItemStack(ElectricExpansionItems.itemParts, 1, 9));
        
        OreDictionary.registerOre("copperWire", new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 0));
        OreDictionary.registerOre("tinWire", new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 1));
        OreDictionary.registerOre("silverWire", new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 2));
        OreDictionary.registerOre("aluminumWire", new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 3));
        OreDictionary.registerOre("superconductor", new ItemStack(ElectricExpansionItems.blockInsulatedWire, 1, 4));
        OreDictionary.registerOre("insulation", RecipeRegistery.getInsulationIS());
        
        NetworkRegistry.instance().registerGuiHandler(this, ElectricExpansion.proxy);
        
        if (!Loader.isModLoaded("BasicComponents"))
        {
            EELogger.fine("Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
        }
    }
    
    @Init
    public void load(FMLInitializationEvent event)
    {
        log(Level.INFO, "Initializing ElectricExpansion v." + VERSION);
        proxy.init();
        
        RecipeRegistery.crafting();
        EETab.INSTANCE.setItemStack(new ItemStack(ElectricExpansionItems.blockTransformer));
        
        int languages = 0;
        
        /**
         * Load all languages.
         */
        for (String language : LANGUAGES_SUPPORTED)
        {
            LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", language, false);
            
            if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
            {
                try
                {
                    String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");
                    
                    for (String child : children)
                    {
                        if (child != null && child != "")
                        {
                            LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", child, false);
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
        
        EELogger.info("Loaded " + languages + " Official and " + unofficialLanguages + " unofficial languages");
        
        if (!useUeVoltageSensitivity)
        {
            UniversalElectricity.isVoltageSensitive = true;
            EELogger.finest("Successfully toggled Voltage Sensitivity!");
        }
        
        OreGenerator.addOre(silverOreGeneration);
        UniversalElectricity.isNetworkActive = true;
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        log(Level.INFO, "PostInitializing ElectricExpansion v." + VERSION);
        
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        RecipeRegistery.drawing();
        RecipeRegistery.insulation();
    }
    
    @ServerStarting
    public void onServerStarting(FMLServerStartingEvent event)
    {
        ElectricExpansion.DistributionNetworksInstance = new DistributionNetworks();
    }
    
    private static File[] ListFilesInMinecraftDir(Side side, String dir)
    {
        if (side.isClient())
        {
            dir = Minecraft.getMinecraftDir() + File.separator + dir;
        }
        
        File folder = new File(dir);
        
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
            Side side = MinecraftServer.getServer().isDedicatedServer() ? Side.SERVER : Side.CLIENT;
            for (File langFile : ListFilesInMinecraftDir(side, "mods" + File.separator + "ElectricExpansionLanguages"))
            {
                if (langFile.exists())
                {
                    String name = langFile.getName();
                    if (name.endsWith(".lang"))
                    {
                        String lang = name.substring(0, name.length() - 4);
                        LanguageRegistry.instance().loadLocalization(langFile.toString(), lang, false);
                        unofficialLanguages++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            // the folder is likely empty, so what...
        }
        return unofficialLanguages;
    }
}
