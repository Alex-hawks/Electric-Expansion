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
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.UpdateNotifier;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
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
import electricexpansion.common.blocks.BlockAdvancedBatteryBox;
import electricexpansion.common.blocks.BlockInsulatedWire;
import electricexpansion.common.blocks.BlockMultimeter;
import electricexpansion.common.blocks.BlockRawWire;
import electricexpansion.common.blocks.BlockSwitchWire;
import electricexpansion.common.blocks.BlockSwitchWireBlock;
import electricexpansion.common.blocks.BlockTransformer;
import electricexpansion.common.blocks.BlockWPT;
import electricexpansion.common.blocks.BlockWireBlock;
import electricexpansion.common.blocks.BlockWireMill;
import electricexpansion.common.itemblocks.ItemBlockInsulatedWire;
import electricexpansion.common.itemblocks.ItemBlockRawWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWire;
import electricexpansion.common.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.common.itemblocks.ItemBlockWPT;
import electricexpansion.common.itemblocks.ItemBlockWireBlock;
import electricexpansion.common.items.ItemAdvancedBattery;
import electricexpansion.common.items.ItemBase;
import electricexpansion.common.items.ItemEliteBattery;
import electricexpansion.common.items.ItemParts;
import electricexpansion.common.items.ItemTransformerCoil;
import electricexpansion.common.items.ItemUpgrade;
import electricexpansion.common.wpt.DistributionNetworks;

@Mod(modid = "ElectricExpansion", name = "Electric Expansion", version = ElectricExpansion.VERSION, useMetadata = true, dependencies = "after:BasicComponents")
@NetworkMod(channels = { ElectricExpansion.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class ElectricExpansion
{

	private static final int BLOCK_ID_PREFIX = 3980;
	private static final int ITEM_ID_PREFIX = 15970;

	public static final String CHANNEL = "ElexEx";

	public static final String LANGUAGE_PATH = "/electricexpansion/languages/";
	private static final String[] LANGUAGE_SUPPORTED = new String[] { "en_US" };

	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 4;
	public static final int REVISION_VERSION = 8;
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;

	// private, these are the default options.
	// Blocks
	private static final int rawWireID = BLOCK_ID_PREFIX;
	private static final int insulatedWireID = BLOCK_ID_PREFIX + 1;
	private static final int wireBlockID = BLOCK_ID_PREFIX + 2;
	private static final int switchWireID = BLOCK_ID_PREFIX + 3;
	private static final int switchWireBlockID = BLOCK_ID_PREFIX + 4;
	// private static final int redstoneWireID = BLOCK_ID_PREFIX + 5;
	// private static final int redstoneWireBlockID = BLOCK_ID_PREFIX + 6;
	private static final int blockAdvBatteryBoxID = BLOCK_ID_PREFIX + 7;
	private static final int blockMultiMeterID = BLOCK_ID_PREFIX + 8;
	private static final int blockWireMillID = BLOCK_ID_PREFIX + 11;
	private static final int blockTransformerID = BLOCK_ID_PREFIX + 12;
	private static final int blockBatBoxID = BLOCK_ID_PREFIX + 13;
	private static final int blockWPTID = BLOCK_ID_PREFIX + 14;
	private static final int blockLeadID = BLOCK_ID_PREFIX + 15;
	// Items
	private static final int itemUpgradeID = ITEM_ID_PREFIX;
	private static final int itemEliteBatID = ITEM_ID_PREFIX + 1;
	private static final int connectorAlloyID = ITEM_ID_PREFIX + 2;
	private static final int itemPartsID = ITEM_ID_PREFIX + 3;
	private static final int itemLeadID = ITEM_ID_PREFIX + 4;
	private static final int itemAdvBatID = ITEM_ID_PREFIX + 5;
	private static final int itemAdvancedBatID = ITEM_ID_PREFIX + 6;
	private static final int itemTransformerCoilID = ITEM_ID_PREFIX + 7;
	// Other
	private static final int superConductorUpkeepDefault = 500;

	// Runtime Values
	// Blocks
	public static int rawWire;
	public static int insulatedWire;
	public static int wireBlock;
	public static int SwitchWire;
	public static int SwitchWireBlock;
	// public static int redstoneWire;
	// public static int redstoneWireBlock;
	public static int AdvBatteryBox;
	public static int MultiMeter;
	public static int wireMill;
	public static int Transformer;
	public static int batBox;
	public static int WPT;
	public static int LeadBlock;
	// Items
	public static int Upgrade;
	public static int EliteBat;
	public static int ConnectionAlloy;
	public static int Parts;
	public static int Lead;
	public static int AdvBat;
	public static int TransformerCoil;

	public static final Configuration CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/ElectricExpansion.cfg"));
	public static boolean configLoaded = configLoad(CONFIG);

	// Blocks
	public static final Block blockRawWire = new BlockRawWire(rawWire, 0);
	public static final Block blockInsulatedWire = new BlockInsulatedWire(insulatedWire, 0);
	public static final Block blockWireBlock = new BlockWireBlock(wireBlock, 0);
	public static final Block blockSwitchWire = new BlockSwitchWire(SwitchWire, 0);
	public static final Block blockSwitchWireBlock = new BlockSwitchWireBlock(SwitchWireBlock, 0);
	// public static final Block blockRedstoneWire = new BlockRedstoneWire(redstoneWire, 0);
	// public static final Block blockRedstoneWireBlock = new
	// BlockRedstoneWireBlock(redstoneWireBlock, 0);

	public static final Block blockAdvBatteryBox = new BlockAdvancedBatteryBox(AdvBatteryBox, 0).setCreativeTab(UETab.INSTANCE);
	public static final Block blockMultimeter = new BlockMultimeter(MultiMeter, 0).setBlockName("multimeter");
	public static final Block blockWireMill = new BlockWireMill(wireMill).setBlockName("wiremill");
	public static final Block blockTransformer = new BlockTransformer(Transformer, 0).setCreativeTab(UETab.INSTANCE).setBlockName("Transformer");
	public static final Block blockWPT = new BlockWPT(WPT, 0);
	public static final Block blockLead = new Block(blockLeadID, 255, Material.iron).setCreativeTab(UETab.INSTANCE).setHardness(2F).setBlockName("LeadBlock").setTextureFile(CommonProxy.ABLOCK);

	// Items
	public static final Item itemParts = new ItemParts(Parts, 0);
	public static final Item itemUpgrade = new ItemUpgrade(Upgrade, 0).setItemName("Upgrade");
	public static final ItemElectric itemEliteBat = new ItemEliteBattery(EliteBat);
	public static final ItemElectric itemAdvBat = new ItemAdvancedBattery(AdvBat);
	public static final Item itemLead = new ItemBase(Lead, 0).setCreativeTab(UETab.INSTANCE).setItemName("LeadIngot");
	public static final Item itemTransformerCoil = new ItemTransformerCoil(TransformerCoil).setCreativeTab(UETab.INSTANCE).setItemName("transformerCoil");

	public static Logger EELogger = Logger.getLogger("ElectricExpansion");
	public static boolean[] startLogLogged = { false, false, false, false };

	@Instance("ElectricExpansion")
	public static ElectricExpansion instance;

	@SidedProxy(clientSide = "electricexpansion.client.ClientProxy", serverSide = "electricexpansion.common.CommonProxy")
	public static CommonProxy proxy;

	public static boolean configLoad(Configuration CONFIG)
	{
		CONFIG.load();
		rawWire = CONFIG.getBlock("Uninsulated_Wire", rawWireID).getInt();
		insulatedWire = CONFIG.getBlock("Insualted_Wire", insulatedWireID).getInt();
		wireBlock = CONFIG.getBlock("Wire_Block", wireBlockID).getInt();
		SwitchWire = CONFIG.getBlock("Switch_Wire", switchWireID).getInt();
		SwitchWireBlock = CONFIG.getBlock("Switch_Wire_Block", switchWireBlockID).getInt();
		// Redstone'd Insulated Cable
		// Redstone'd Cable Blocks

		AdvBatteryBox = CONFIG.getBlock("Advanced_Battery_Box", blockAdvBatteryBoxID).getInt();
		MultiMeter = CONFIG.getBlock("Multimeter", blockMultiMeterID).getInt();
		wireMill = CONFIG.getBlock("Wire_Mill", blockWireMillID).getInt();
		Transformer = CONFIG.getBlock("Transformer", blockTransformerID).getInt();
		WPT = CONFIG.getBlock("Wireless_Transfer_Machines", blockWPTID).getInt();
		LeadBlock = CONFIG.getBlock("Lead_Block", blockLeadID).getInt();

		Upgrade = CONFIG.getItem("Advanced_Bat_Box_Upgrade", itemUpgradeID).getInt();
		EliteBat = CONFIG.getItem("Elite_Battery", itemEliteBatID).getInt();
		ConnectionAlloy = CONFIG.getItem("Connection_Alloy", connectorAlloyID).getInt();
		Parts = CONFIG.getItem("Parts", itemPartsID).getInt();
		Lead = CONFIG.getItem("Lead_Ingot", itemLeadID).getInt();
		AdvBat = CONFIG.getItem("Advanced_Battery", itemAdvBatID).getInt();
		TransformerCoil = CONFIG.getItem("Transformer_Coil", itemTransformerCoilID).getInt();
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
		UniversalElectricity.register(this, 1, 2, 0, false);

		if (!configLoaded)
		{
			configLoad(CONFIG);
		}
		if (startLogLogged[1] != true)
		{
			StartLog("preInit");
		}

		UpdateNotifier.INSTANCE.checkUpdate("Electric Expansion", VERSION, "http://www.calclavia.com/downloads/ee/updatebuild.txt");

		GameRegistry.registerBlock(blockAdvBatteryBox);
		GameRegistry.registerBlock(blockWireMill);
		GameRegistry.registerBlock(blockMultimeter);
		GameRegistry.registerBlock(blockLead);
		GameRegistry.registerBlock(blockTransformer);

		GameRegistry.registerBlock(blockWPT, ItemBlockWPT.class);

		GameRegistry.registerBlock(blockRawWire, ItemBlockRawWire.class);
		GameRegistry.registerBlock(blockInsulatedWire, ItemBlockInsulatedWire.class);
		GameRegistry.registerBlock(blockSwitchWire, ItemBlockSwitchWire.class);
		GameRegistry.registerBlock(blockSwitchWireBlock, ItemBlockSwitchWireBlock.class);
		GameRegistry.registerBlock(blockWireBlock, ItemBlockWireBlock.class);

		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		if (!Loader.isModLoaded("BasicComponents"))
		{
			System.out.println("Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
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
		UETab.setItemStack(new ItemStack(this.blockAdvBatteryBox));

		for (String language : LANGUAGE_SUPPORTED)
		{
			LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", language, false);
		}

	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		if (startLogLogged[3] != true)
		{
			StartLog("postInit");
		}
		OreDictionary.registerOre("ingotLead", this.itemLead);
		OreDictionary.registerOre("leadBlock", this.blockLead);
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
					event.entity.dropItem(itemLead.shiftedIndex, numberOfDrops);
				}
			}
		}
	}

	@ForgeSubscribe
	public void onWorldSave(WorldEvent.Save event)
	{
		DistributionNetworks.onWorldSave(event.world);
	}

	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		DistributionNetworks.onWorldLoad(event.world);
	}
}
