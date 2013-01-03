package electricexpansion.common;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UEDamageSource;
import universalelectricity.prefab.UpdateNotifier;
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
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockLogisticsWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockRedstoneWire;
import electricexpansion.common.blocks.BlockSilverOre;
import electricexpansion.common.blocks.BlockSwitchWire;
import electricexpansion.common.blocks.BlockSwitchWireBlock;
import electricexpansion.common.blocks.BlockTransformer;
import electricexpansion.common.blocks.BlockWPT;
import electricexpansion.common.blocks.BlockWireBlock;
import electricexpansion.common.blocks.BlockWireMill;
import electricexpansion.common.itemblocks.ItemBlockInsulatedWire;
import electricexpansion.common.itemblocks.ItemBlockLogisticsWire;
import electricexpansion.common.itemblocks.ItemBlockRawWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.common.itemblocks.ItemBlockTransformer;
import electricexpansion.common.itemblocks.ItemBlockWPT;
import electricexpansion.common.itemblocks.ItemBlockWireBlock;
import electricexpansion.common.itemblocks.ItemBlockTransformer;
import electricexpansion.common.items.ItemAdvancedBattery;
import electricexpansion.common.items.ItemBase;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemInfiniteBattery;
import electricexpansion.common.items.ItemMultimeter;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.wpt.DistributionNetworks;

@Mod(modid = "ElectricExpansion", name = ElectricExpansion.NAME, version = ElectricExpansion.VERSION, useMetadata = true, certificateFingerprint = "b34077d98c710152e788c277173c3d474769c3e6", dependencies = "after:BasicComponents")
@NetworkMod(channels = { ElectricExpansion.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class ElectricExpansion
{

	private static final int BLOCK_ID_PREFIX = 3980;
	private static final int ITEM_ID_PREFIX = 15970;

	public static final String CHANNEL = "ElecEx";

	public static final String NAME = "Electric Expansion";

	public static final String RESOURCE_PATH = "/electricexpansion/";
	public static final String LANGUAGE_PATH = RESOURCE_PATH + "language/";
	public static final String TEXTURE_PATH = RESOURCE_PATH + "textures/";
	// Mattredsox's Textures
	public static final String MATT_TEXTURE_PATH = TEXTURE_PATH + "mattredsox/";
	public static final String MATT_BLOCK_TEXTURE_FILE = MATT_TEXTURE_PATH + "blocks.png";
	public static final String MATT_ITEM_TEXTURE_FILE = MATT_TEXTURE_PATH + "items.png";
	// Alex Textures
	public static final String ALEX_TEXTURE_PATH = TEXTURE_PATH + "alex_hawks/";
	public static final String ALEX_ITEMS_TEXTURE_FILE = ALEX_TEXTURE_PATH + "items.png";
	public static final String ALEX_BLOCK_TEXTURE_FILE = ALEX_TEXTURE_PATH + "block.png";

	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US" };

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 1;
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
	public static Block blockRedstoneWire;
	// public static final Block blockRedstoneWireBlock = new

	public static Block blockAdvBatteryBox;
	public static Block blockMultimeter;
	public static Block blockWireMill;
	public static Block blockTransformer;
	public static Block blockWPT;
	public static Block blockLead;
	public static Block blockSilverOre;
	// Items
	public static Item itemParts;
	public static Item itemUpgrade;
	public static ItemElectric itemEliteBat;
	public static ItemElectric itemAdvBat;
	public static Item itemLead;
	public static Item itemCoil;
	public static Item itemMultimeter;
	public static Item itemSilverIngot;
	public static Item itemInfBat;
	
	
	public static ItemStack transformer1;
	public static ItemStack transformer2;
	public static ItemStack transformer3;

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
		blockRedstoneWire = new BlockRedstoneWire(CONFIG.getBlock("Redstone_Wire", BLOCK_ID_PREFIX + 5).getInt(), 0);
		// +6 public static final Block blockRedstoneWireBlock = new
		// BlockRedstoneWireBlock(redstoneWireBlock, 0);
		blockAdvBatteryBox = new BlockAdvancedBatteryBox(CONFIG.getBlock("Advanced_Battery_Box", BLOCK_ID_PREFIX + 7).getInt(), 0).setCreativeTab(EETab.INSTANCE);
		blockMultimeter = new BlockMultimeter(CONFIG.getBlock("Multimeter", BLOCK_ID_PREFIX + 8).getInt(), 0).setBlockName("multimeter");
		blockSilverOre = new BlockSilverOre(CONFIG.getBlock("Silver Ore", BLOCK_ID_PREFIX + 9).getInt());
		// 10
		blockWireMill = new BlockWireMill(CONFIG.getBlock("Wire_Mill", BLOCK_ID_PREFIX + 11).getInt()).setBlockName("wiremill");
		blockTransformer = new BlockTransformer(CONFIG.getBlock("Transformer", BLOCK_ID_PREFIX + 12).getInt()).setCreativeTab(EETab.INSTANCE).setBlockName("transformer");
		blockWPT = new BlockWPT(CONFIG.getBlock("Wireless_Transfer_Machines", BLOCK_ID_PREFIX + 13).getInt(), 0);
		blockLead = new Block(CONFIG.getBlock("Lead_Block", BLOCK_ID_PREFIX + 14).getInt(), 255, Material.iron).setCreativeTab(EETab.INSTANCE).setHardness(2F).setBlockName("LeadBlock").setTextureFile(ElectricExpansion.ALEX_BLOCK_TEXTURE_FILE);
		blockLogisticsWire = new BlockLogisticsWire(CONFIG.getBlock("Logistics_Wire", BLOCK_ID_PREFIX + 15).getInt(), 0);
		// Redstone'd Insulated Cable
		// Redstone'd Cable Blocks

		itemUpgrade = new ItemUpgrade(CONFIG.getItem("Advanced_Bat_Box_Upgrade", ITEM_ID_PREFIX).getInt(), 0).setItemName("Upgrade");
		itemEliteBat = new ItemEliteBattery(CONFIG.getItem("Elite_Battery", ITEM_ID_PREFIX + 1).getInt());
		// 2
		itemParts = new ItemParts(CONFIG.getItem("Parts", ITEM_ID_PREFIX + 3).getInt(), 0);
		itemLead = new ItemBase(CONFIG.getItem("Lead_Ingot", ITEM_ID_PREFIX + 4).getInt(), 0).setCreativeTab(EETab.INSTANCE).setItemName("LeadIngot");
		itemAdvBat = new ItemAdvancedBattery(CONFIG.getItem("Advanced_Battery", ITEM_ID_PREFIX + 5).getInt());
		// 6
		itemCoil = new ItemBase(CONFIG.getItem("Coil", ITEM_ID_PREFIX + 7).getInt(), 10).setCreativeTab(EETab.INSTANCE).setItemName("coil");
		itemMultimeter = new ItemMultimeter(CONFIG.getItem("Item_Multimeter", ITEM_ID_PREFIX + 8).getInt()).setCreativeTab(EETab.INSTANCE).setItemName("itemMultimeter");
		itemSilverIngot = new ItemBase(CONFIG.getItem("Silver_Ingot", ITEM_ID_PREFIX + 9).getInt(), 2).setItemName("silveringot");
		itemInfBat = new ItemInfiniteBattery(CONFIG.getItem("Infinite_Battery", ITEM_ID_PREFIX + 10).getInt()).setItemName("infinitebattery");

		silverOreGeneration = new OreGenReplaceStone("Silver Ore", "oreSilver", new ItemStack(blockSilverOre), 0, 0, 36, 18, 3, "pickaxe", 2).enable();

		transformer1 = ((BlockTransformer) blockTransformer).getTier1();
		transformer2 = ((BlockTransformer) blockTransformer).getTier2();
		transformer3 = ((BlockTransformer) blockTransformer).getTier3();
		
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
		UniversalElectricity.register(this, 1, 2, 2, false);

		if (!configLoaded)
		{
			configLoad(CONFIG);
		}
		if (startLogLogged[1] != true)
		{
			StartLog("preInit");
		}

		UpdateNotifier.INSTANCE.checkUpdate("Electric Expansion", VERSION, "http://www.calclavia.com/downloads/ee/updatebuild.txt");

		GameRegistry.registerBlock(blockAdvBatteryBox, "blockAdvBatteryBox");
		GameRegistry.registerBlock(blockWireMill, "blockWireMill");
		GameRegistry.registerBlock(blockMultimeter, "blockMultimeter");
		GameRegistry.registerBlock(blockLead, "blockLead");
		GameRegistry.registerBlock(blockTransformer, ItemBlockTransformer.class, "blockTransformer");
		GameRegistry.registerBlock(blockSilverOre, "blockSilverOre");
		GameRegistry.registerBlock(blockRedstoneWire, "blockRedstoneWire");

		GameRegistry.registerBlock(blockWPT, ItemBlockWPT.class, "blockWPT");

		GameRegistry.registerBlock(blockRawWire, ItemBlockRawWire.class, "blockRawWire");
		GameRegistry.registerBlock(blockInsulatedWire, ItemBlockInsulatedWire.class, "blockInsulatedWire");
		GameRegistry.registerBlock(blockSwitchWire, ItemBlockSwitchWire.class, "blockSwitchWire");
		GameRegistry.registerBlock(blockSwitchWireBlock, ItemBlockSwitchWireBlock.class, "blockSwitchWireBlock");
		GameRegistry.registerBlock(blockWireBlock, ItemBlockWireBlock.class, "blockWireBlock");
		GameRegistry.registerBlock(blockLogisticsWire, ItemBlockLogisticsWire.class, "blockLogisticsWire");

		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		if (!Loader.isModLoaded("BasicComponents"))
		{
			EELogger.fine("Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
		}

		UEDamageSource.electrocution.registerDeathMessage();
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
						if (child != "" || child != null)
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

			languages++;
		}

		System.out.println(NAME + ": Loaded " + languages + " languages.");

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
		OreDictionary.registerOre("ingotSilver", this.itemSilverIngot);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe
	public void onEntityDeath(LivingDeathEvent event)
	{
		if (event.entity != null)
		{
			if (event.entity instanceof EntitySkeleton && ((EntitySkeleton) event.entity).getSkeletonType() == 1)
			{
				{
					Random dropNumber = new Random();
					int numberOfDrops = dropNumber.nextInt(4);
					event.entity.dropItem(itemLead.itemID, numberOfDrops);
				}
			}
		}
	}

	@SideOnly(Side.SERVER)
	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		DistributionNetworks.onWorldSave(event.world);
	}

	@SideOnly(Side.SERVER)
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		DistributionNetworks.onWorldLoad();
	}
}
