package mattredsox.electricexpansion;

import java.io.File;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.UniversalElectricity;
import universalelectricity.basiccomponents.BCCommonProxy;
import universalelectricity.basiccomponents.BlockBCOre;
import universalelectricity.basiccomponents.BlockBasicMachine;
import universalelectricity.basiccomponents.BlockCopperWire;
import universalelectricity.basiccomponents.BlockOilFlowing;
import universalelectricity.basiccomponents.BlockOilStill;
import universalelectricity.basiccomponents.ItemBasic;
import universalelectricity.basiccomponents.ItemBasicMachine;
import universalelectricity.basiccomponents.ItemBattery;
import universalelectricity.basiccomponents.ItemCircuit;
import universalelectricity.basiccomponents.ItemCopperWire;
import universalelectricity.basiccomponents.ItemOilBucket;
import universalelectricity.basiccomponents.ItemOre;
import universalelectricity.basiccomponents.ItemWrench;
import universalelectricity.basiccomponents.TileEntityBatteryBox;
import universalelectricity.basiccomponents.TileEntityCoalGenerator;
import universalelectricity.basiccomponents.TileEntityElectricFurnace;
import universalelectricity.network.PacketManager;
import universalelectricity.ore.OreGenBase;
import universalelectricity.ore.OreGenReplaceStone;
import universalelectricity.ore.OreGenerator;
import universalelectricity.recipe.RecipeManager;
import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;
import buildcraft.api.liquids.LiquidStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * The main class for managing Basic Component items and blocks.
 * @author Calclavia
 *
 */

@Mod(modid = "ElectricExpansion", name = "Electric Expansion", version = ElectricExpansion.VERSION, dependencies = "after:BasicComponents")
@NetworkMod(channels = { "ElecEx" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)

public class ElectricExpansion
{
    protected static final String VERSION = "0.0.5";

    public static final String FILE_PATH = "/electricexpansion/textures/";
	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/ElectricExpansion.cfg"));

    
    public static ElectricExpansion instance;
    
    @SidedProxy(clientSide = "mattredsox.electricexpansion.client.EEClientProxy", serverSide = "mattredsox.electricexpansion.EECommonProxy")
	public static EECommonProxy proxy;
    
    /**
     * Here is where all the Universal Components are defined. You may reference to these variables.
    */
    public static final int BLOCK_ID_PREFIX = 3004;
    public static final Block blockBigBatteryBox = new BlockBigBatteryBox(getBlockConfigID(CONFIGURATION, "LargerBatBox", BLOCK_ID_PREFIX), 0).setCreativeTab(CreativeTabs.tabDecorations);
    public static final Block blockVoltDet = new BlockVoltDetector(getBlockConfigID(CONFIGURATION, "VoltDetector", BLOCK_ID_PREFIX + 1), 0).setCreativeTab(CreativeTabs.tabDecorations);
    public static final Block blockUPTransformer = new BlockUPTransformer(getBlockConfigID(CONFIGURATION, "UPTransformer", BLOCK_ID_PREFIX + 2), 0).setCreativeTab(CreativeTabs.tabDecorations);
    public static final Block blockDOWNTransformer = new BlockDOWNTransformer(getBlockConfigID(CONFIGURATION, "DOWNTransformer", BLOCK_ID_PREFIX + 3), 0).setCreativeTab(CreativeTabs.tabDecorations);
    public static final Block blockEtcher = new BlockEtcher(getBlockConfigID(CONFIGURATION, "Etcher", BLOCK_ID_PREFIX + 4)).setCreativeTab(CreativeTabs.tabDecorations).setBlockName("blockEtcher");
    public static final Block blockFuse = new BlockFuse(getBlockConfigID(CONFIGURATION, "Relay", BLOCK_ID_PREFIX + 5), 0).setCreativeTab(CreativeTabs.tabDecorations).setBlockName("blockFuse");

    @PreInit
	public void preInit(FMLPreInitializationEvent event)
    {
		instance = this;
		
		MinecraftForgeClient.preloadTexture("/electricexpansion/blocks1.png");

		
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

    	//Register Blocks
	   	GameRegistry.registerBlock(blockBigBatteryBox);
    	GameRegistry.registerBlock(blockUPTransformer);
    	GameRegistry.registerBlock(blockDOWNTransformer);
    	GameRegistry.registerBlock(blockVoltDet);
    	GameRegistry.registerBlock(blockEtcher);
    	GameRegistry.registerBlock(blockFuse);

    	proxy.preInit();
    }
    
    @Init
	public void load(FMLInitializationEvent evt)
    {
    	proxy.init();
    					
		LanguageRegistry.addName(blockUPTransformer, "Up Transformer");
        LanguageRegistry.addName(blockBigBatteryBox, "Larger Battery Box");
        LanguageRegistry.addName(blockDOWNTransformer, "Down Transformer");
        LanguageRegistry.addName(blockVoltDet, "Voltage Detector");
        LanguageRegistry.addName(blockEtcher, "Etcher");
        LanguageRegistry.addName(blockFuse, "120 Volt Relay");

		GameRegistry.registerTileEntity(TileEntityBigBatteryBox.class, "TEBBB");
		GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TEUp");
		GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TEVD");
		GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TEDown");
		GameRegistry.registerTileEntity(TileEntityFuse.class, "TEFuse120");

		//Recipes
		//Oil Bucket
	//	RecipeManager.addRecipe(new ItemStack(blockOre), new Object [] {"CCC", "CBC", "CCC", 'B', Item.bucketWater, 'C', Item.coal});
    }

    public static int getBlockConfigID(Configuration configuration, String name, int defaultID)
    {
        configuration.load();
        int id = defaultID;

        id = Integer.parseInt(configuration.getOrCreateIntProperty(name, Configuration.CATEGORY_BLOCK, defaultID).value);

        if (id <= 136)
        {
            return defaultID;
        }
          
        configuration.save();
        return id;
    }
}

