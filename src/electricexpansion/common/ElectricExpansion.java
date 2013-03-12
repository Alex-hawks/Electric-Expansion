package electricexpansion.common;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
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
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
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
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockFuseBox;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockInsulationMachine;
import electricexpansion.common.blocks.BlockLogisticsWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockQuantumBatteryBox;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockSilverOre;
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
import electricexpansion.common.items.ItemBase;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemFuse;
import electricexpansion.common.items.ItemMultimeter;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemUltimateBattery;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.misc.DistributionNetworks;
import electricexpansion.common.misc.EETab;


@Mod(modid = ElectricExpansion.MOD_ID, name = ElectricExpansion.NAME, version = ElectricExpansion.VERSION, dependencies = "after:BasicComponents;after:AtomicScience")
@NetworkMod(channels = { ElectricExpansion.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class ElectricExpansion
{
	public static final String MOD_ID = "ElectricExpansion";

	private static final int BLOCK_ID_PREFIX = 3980;
	private static final int ITEM_ID_PREFIX = 15970;

	public static final String CHANNEL = "ElecEx";

	public static final String NAME = "Electric Expansion";

	public static final String RESOURCE_PATH 	= "/mods/electricexpansion/";
	public static final String LANGUAGE_PATH 	= RESOURCE_PATH + "language/";
	public static final String TEXTURE_PATH 	= RESOURCE_PATH + "textures/";
	public static final String ITEM_FILE 		= TEXTURE_PATH + "items.png";
	public static final String BLOCK_FILE 		= TEXTURE_PATH + "blocks.png";
	public static final String GUI_PATH			= TEXTURE_PATH + "gui/";
	public static final String MACHINE_PATH		= TEXTURE_PATH + "machine/";
	public static final String WIRE_PATH		= TEXTURE_PATH + "wire/";

	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "pl_PL" };

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 5;
	public static final int REVISION_VERSION = 0;
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;

	public static OreGenBase silverOreGeneration;

	public static final Configuration CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/ElectricExpansion.cfg"));
	public static boolean configLoaded = configLoad(CONFIG);

	public static Block blockRawWire;
	public static Block blockInsulatedWire;
	public static Block blockWireBlock;
	public static Block blockSwitchWire;
	public static Block blockSwitchWireBlock;
	public static Block blockLogisticsWire;
	// public static final Block blockRedstoneWire = new BlockRedstoneWire(redstoneWire, 0);
	// public static final Block blockRedstoneWireBlock = new BlockRedstoneWireBlock(redstoneWireBlock, 0);

	public static Block blockAdvBatteryBox;
	public static Block blockMultimeter;
	public static Block blockWireMill;
	public static Block blockTransformer;
	public static Block blockDistribution;
	public static Block blockLead;
	public static Block blockSilverOre;
	public static Block blockInsulationMachine;
	public static Block blockFuseBox;
	// Items
	public static Item itemParts;
	public static Item itemUpgrade;
	public static ItemElectric itemEliteBat;
	public static ItemElectric itemAdvBat;
	public static ItemElectric itemUltimateBat;
	public static Item itemLead;
	public static Item itemCoil;
	public static Item itemMultimeter;
	public static Item itemSilverIngot;
	public static Item itemInfBat;
	public static Item itemFuse;

	public static ItemStack transformer1;
	public static ItemStack transformer2;
	public static ItemStack transformer3;

	static boolean debugRecipes;
	public static boolean useHashCodes;

	public static DistributionNetworks DistributionNetworksInstance;

	public static Logger EELogger = Logger.getLogger("ElectricExpansion");
	public static boolean[] startLogLogged = { false, false, false, false };

	@Instance("ElectricExpansion")
	public static ElectricExpansion instance;

	@SidedProxy(clientSide = "electricexpansion.client.ClientProxy", serverSide = "electricexpansion.common.CommonProxy")
	public static CommonProxy proxy;

	public static boolean configLoad(Configuration CONFIG)
	{
		CONFIG.load();

		blockRawWire = new BlockRawWire(CONFIG.getBlock("Uninsulated_Wire", BLOCK_ID_PREFIX).getInt(), 0);
		blockInsulatedWire = new BlockInsulatedWire(CONFIG.getBlock("Insulated_Wire", BLOCK_ID_PREFIX + 1).getInt(), 0);
		blockWireBlock = new BlockWireBlock(CONFIG.getBlock("Wire_Block", BLOCK_ID_PREFIX + 2).getInt(), 0);
		blockSwitchWire = new BlockSwitchWire(CONFIG.getBlock("Switch_Wire", BLOCK_ID_PREFIX + 3).getInt(), 0);
		blockSwitchWireBlock = new BlockSwitchWireBlock(CONFIG.getBlock("Switch_Wire_Block", BLOCK_ID_PREFIX + 4).getInt(), 0);
		// +5
		// +6
		blockAdvBatteryBox = new BlockAdvancedBatteryBox(CONFIG.getBlock("Advanced_Battery_Box", BLOCK_ID_PREFIX + 7).getInt(), 0);
		blockMultimeter = new BlockMultimeter(CONFIG.getBlock("Multimeter", BLOCK_ID_PREFIX + 8).getInt(), 0);
		blockSilverOre = new BlockSilverOre(CONFIG.getBlock("Silver Ore", BLOCK_ID_PREFIX + 9).getInt());
		blockInsulationMachine = new BlockInsulationMachine(CONFIG.getBlock("Insulation_Refiner", BLOCK_ID_PREFIX + 10).getInt());
		blockWireMill = new BlockWireMill(CONFIG.getBlock("Wire_Mill", BLOCK_ID_PREFIX + 11).getInt());
		blockTransformer = new BlockTransformer(CONFIG.getBlock("Transformer", BLOCK_ID_PREFIX + 12).getInt());
		blockDistribution = new BlockQuantumBatteryBox(CONFIG.getBlock("Wireless_Transfer_Machines", BLOCK_ID_PREFIX + 13).getInt());
		blockLead = new Block(CONFIG.getBlock("Lead_Block", BLOCK_ID_PREFIX + 14).getInt(), Material.iron).setCreativeTab(EETab.INSTANCE).setHardness(2F).setUnlocalizedName("LeadBlock").setTextureFile(ElectricExpansion.BLOCK_FILE);
		blockLogisticsWire = new BlockLogisticsWire(CONFIG.getBlock("Logistics_Wire", BLOCK_ID_PREFIX + 15).getInt(), 0);
		blockFuseBox = new BlockFuseBox(CONFIG.getBlock("Fuse_Box", BLOCK_ID_PREFIX + 16).getInt());

		itemUpgrade = new ItemUpgrade(CONFIG.getItem("Advanced_Bat_Box_Upgrade", ITEM_ID_PREFIX).getInt(), 0);
		itemEliteBat = new ItemEliteBattery(CONFIG.getItem("Elite_Battery", ITEM_ID_PREFIX + 1).getInt());
		itemUltimateBat = new ItemUltimateBattery(CONFIG.getItem("Ultimate_Battery", ITEM_ID_PREFIX + 2).getInt());
		itemParts = new ItemParts(CONFIG.getItem("Parts", ITEM_ID_PREFIX + 3).getInt(), 0);
		itemLead = new ItemBase(CONFIG.getItem("Lead_Ingot", ITEM_ID_PREFIX + 4).getInt(), "LeadIngot");
		itemAdvBat = new ItemAdvancedBattery(CONFIG.getItem("Advanced_Battery", ITEM_ID_PREFIX + 5).getInt());
		itemFuse = new ItemFuse(CONFIG.getItem("Fuses", ITEM_ID_PREFIX + 6).getInt());
		itemCoil = new ItemBase(CONFIG.getItem("Coil", ITEM_ID_PREFIX + 7).getInt(), "coil");
		itemMultimeter = new ItemMultimeter(CONFIG.getItem("Item_Multimeter", ITEM_ID_PREFIX + 8).getInt());
		itemSilverIngot = new ItemBase(CONFIG.getItem("Silver_Ingot", ITEM_ID_PREFIX + 9).getInt(), "silveringot");

		silverOreGeneration = new OreGenReplaceStone("Silver Ore", "oreSilver", new ItemStack(blockSilverOre, 1), 36, 10, 3).enable(CONFIG);
		transformer1 = ((BlockTransformer) blockTransformer).getTier1();
		transformer2 = ((BlockTransformer) blockTransformer).getTier2();
		transformer3 = ((BlockTransformer) blockTransformer).getTier3();

		debugRecipes = CONFIG.get("General", "Debug_Recipes", false).getBoolean(false);
		useHashCodes = CONFIG.get("General", "Use_Hashcodes", true).getBoolean(true);

		CONFIG.save();

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
			startLogLogged[0] = true;
	}

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
//		UniversalElectricity.register(this, this.MAJOR_VERSION, this.MINOR_VERSION, this.REVISION_VERSION, false);

		if (!configLoaded)
		{
			configLoad(CONFIG);
		}
		if (startLogLogged[1] != true)
		{
			StartLog("preInit");
		}

		GameRegistry.registerBlock(blockAdvBatteryBox, ItemBlock.class, "blockAdvBatteryBox", this.MOD_ID);
		GameRegistry.registerBlock(blockWireMill, ItemBlock.class, "blockWireMill", this.MOD_ID);
		GameRegistry.registerBlock(blockInsulationMachine, ItemBlock.class, "blockInsulationMachine", this.MOD_ID);
		GameRegistry.registerBlock(blockMultimeter, ItemBlock.class, "blockMultimeter", this.MOD_ID);
		GameRegistry.registerBlock(blockLead, ItemBlock.class, "blockLead", this.MOD_ID);
		GameRegistry.registerBlock(blockTransformer, ItemBlockTransformer.class, "blockTransformer", this.MOD_ID);
		GameRegistry.registerBlock(blockSilverOre, ItemBlock.class, "blockSilverOre", this.MOD_ID);
//		GameRegistry.registerBlock(blockRedstoneWire, ItemBlock.class, "blockRedstoneWire", this.MOD_ID);

		GameRegistry.registerBlock(blockDistribution, ItemBlock.class, "blockDistribution", this.MOD_ID);
//		GameRegistry.registerBlock(blockFuseBox, ItemBlock.class, "blockFuseBox", this.MOD_ID);

		GameRegistry.registerBlock(blockRawWire, ItemBlockRawWire.class, "blockRawWire", this.MOD_ID);
		GameRegistry.registerBlock(blockInsulatedWire, ItemBlockInsulatedWire.class, "blockInsulatedWire", this.MOD_ID);
		GameRegistry.registerBlock(blockSwitchWire, ItemBlockSwitchWire.class, "blockSwitchWire", this.MOD_ID);
		GameRegistry.registerBlock(blockSwitchWireBlock, ItemBlockSwitchWireBlock.class, "blockSwitchWireBlock", this.MOD_ID);
		GameRegistry.registerBlock(blockWireBlock, ItemBlockWireBlock.class, "blockWireBlock", this.MOD_ID);
		GameRegistry.registerBlock(blockLogisticsWire, ItemBlockLogisticsWire.class, "blockLogisticsWire", this.MOD_ID);

		OreDictionary.registerOre("ingotLead", this.itemLead);
		OreDictionary.registerOre("blockLead", this.blockLead);
		OreDictionary.registerOre("advancedBattery", this.itemAdvBat);
		OreDictionary.registerOre("eliteBattery", this.itemEliteBat);
		OreDictionary.registerOre("advancedBattery", this.itemAdvBat);
		OreDictionary.registerOre("transformer", this.blockTransformer);
		OreDictionary.registerOre("coil", this.itemCoil);
		OreDictionary.registerOre("wireMill", this.blockWireMill);
		OreDictionary.registerOre("multimeter", this.blockMultimeter);
		OreDictionary.registerOre("itemMultimeter", this.itemMultimeter);

		OreDictionary.registerOre("copperWire", new ItemStack(blockInsulatedWire, 1, 0));
		OreDictionary.registerOre("tinWire", new ItemStack(blockInsulatedWire, 1, 1));
		OreDictionary.registerOre("silverWire", new ItemStack(blockInsulatedWire, 1, 2));
		OreDictionary.registerOre("aluminumWire", new ItemStack(blockInsulatedWire, 1, 3));
		OreDictionary.registerOre("superconductor", new ItemStack(blockInsulatedWire, 1, 4));

		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

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

		OreDictionary.registerOre("ingotSilver", this.itemSilverIngot);

		RecipeRegistery.crafting();
		RecipeRegistery.drawing();
		RecipeRegistery.insulation();
		EETab.setItemStack(new ItemStack(this.blockTransformer));

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
						if (child != "" && child != null)
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

		System.out.println(NAME + ": Loaded " + languages + " Official and " + unofficialLanguages + " unofficial languages");

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

		MinecraftForge.EVENT_BUS.register(this);
	}

	@ServerStarting
	public void onServerStarting(FMLServerStartingEvent event)
	{
		this.DistributionNetworksInstance = new DistributionNetworks();
	}

	@ForgeSubscribe
	public void onEntityDropItems(LivingDropsEvent event)
	{
		if (event.entity != null)
		{
			if (event.entity instanceof EntitySkeleton && ((EntitySkeleton) event.entity).getSkeletonType() == 1)
			{
				{
					Random dropNumber = new Random();
					int numberOfDrops = dropNumber.nextInt(4);
					event.entity.dropItem(itemLead.itemID, numberOfDrops);

					ItemStack leadIS = new ItemStack(this.itemLead, numberOfDrops);

					event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, leadIS.copy()));
				}
			}
		}
	}

	@ForgeSubscribe
	@SideOnly(Side.SERVER)
	public void onWorldSave(WorldEvent.Save event)
	{
		DistributionNetworksInstance.onWorldSave(event);
	}
	
	@ForgeSubscribe
	@SideOnly(Side.SERVER)
	public void onWorldLoad(WorldEvent.Load event)
	{
		DistributionNetworksInstance = new DistributionNetworks();
		DistributionNetworksInstance.onWorldLoad();
	}
	
	@ForgeSubscribe
	@SideOnly(Side.SERVER)
	public void onWorldUnload(WorldEvent.Unload event)
	{
		DistributionNetworksInstance.onWorldSave(event);
	}

	public static File[] ListLanguages()
	{
		String folderDir = "";
		if (MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = "mods/" + "ElectricExpansionLanguages";
		}
		else if (!MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = Minecraft.getMinecraftDir() + File.separator + "mods" + File.separator + "ElectricExpansionLanguages";
		}

		File folder = new File(folderDir);

		if (!folder.exists())
			folder.mkdirs();

		String files;
		File[] listOfFiles = folder.listFiles();

		return listOfFiles;
	}

	public static int langLoad()
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
						LanguageRegistry.instance().loadLocalization(langFile.toString(), lang, false);
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
