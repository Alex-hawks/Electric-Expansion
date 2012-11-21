package electricexpansion;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import universalelectricity.core.UEConfig;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
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
import electricexpansion.alex_hawks.blocks.BlockInsulatedWire;
import electricexpansion.alex_hawks.blocks.BlockRawWire;
import electricexpansion.alex_hawks.blocks.BlockSwitchWire;
import electricexpansion.alex_hawks.blocks.BlockSwitchWireBlock;
import electricexpansion.alex_hawks.blocks.BlockWPT;
import electricexpansion.alex_hawks.blocks.BlockWireBlock;
import electricexpansion.alex_hawks.blocks.BlockWireMill;
import electricexpansion.alex_hawks.itemblocks.ItemBlockInsualtedWire;
import electricexpansion.alex_hawks.itemblocks.ItemBlockRawWire;
import electricexpansion.alex_hawks.itemblocks.ItemBlockSwitchWire;
import electricexpansion.alex_hawks.itemblocks.ItemBlockSwitchWireBlock;
import electricexpansion.alex_hawks.itemblocks.ItemBlockWireBlock;
import electricexpansion.alex_hawks.items.ItemConnectorAlloy;
import electricexpansion.alex_hawks.items.ItemParts;
import electricexpansion.alex_hawks.misc.RecipeRegistrar;
import electricexpansion.alex_hawks.wpt.distributionNetworks;
import electricexpansion.mattredsox.blocks.BlockAdvBatteryBox;
import electricexpansion.mattredsox.blocks.BlockDOWNTransformer;
import electricexpansion.mattredsox.blocks.BlockUPTransformer;
import electricexpansion.mattredsox.blocks.BlockVoltDetector;
import electricexpansion.mattredsox.items.ItemBase;
import electricexpansion.mattredsox.items.ItemLeadTearBattery;
import electricexpansion.mattredsox.items.ItemUpgrade;

@Mod(modid="ElectricExpansion", name="Electric Expansion", version="0.2.3", dependencies = "after:BasicComponents;after:HawksMachinery", useMetadata = true)
@NetworkMod(channels = { "ElecEx" }, clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
public class ElectricExpansion {

	private static int[] versionArray = {0, 2, 3}; //Change EVERY release!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private static String version;
	private static final int BLOCK_ID_PREFIX = 3980;
	private static final int ITEM_ID_PREFIX = 15970;

	//private, these are the default options.
	//Blocks
	private static final int rawWireID = BLOCK_ID_PREFIX;
	private static final int insulatedWireID = BLOCK_ID_PREFIX + 1;
	private static final int wireBlockID = BLOCK_ID_PREFIX + 2;
	private static final int switchWireID = BLOCK_ID_PREFIX + 3;
	private static final int switchWireBlockID = BLOCK_ID_PREFIX + 4; 
	//private static final int redstoneWireID = BLOCK_ID_PREFIX + 5;
	//private static final int redstoneWireBlockID = BLOCK_ID_PREFIX + 6;
	private static final int blockBigBatteryBoxID = BLOCK_ID_PREFIX + 7;
	private static final int blockVoltDetID = BLOCK_ID_PREFIX + 8;
	private static final int blockUPTransformerID = BLOCK_ID_PREFIX + 9;
	private static final int blockDOWNTransformerID = BLOCK_ID_PREFIX + 10;
	private static final int blockWireMillID = BLOCK_ID_PREFIX + 11;
	private static final int blockFuseID = BLOCK_ID_PREFIX + 12;
	private static final int blockBatBoxID = BLOCK_ID_PREFIX + 13;
	private static final int blockWPTID = BLOCK_ID_PREFIX + 14;
	//Items
	private static final int itemUpgradeID = ITEM_ID_PREFIX;
	private static final int itemLeadTearBatID = ITEM_ID_PREFIX + 1;
	private static final int connectorAlloyID = ITEM_ID_PREFIX + 2;
	private static final int itemPartsID = ITEM_ID_PREFIX + 3;
	private static final int itemLeadID = ITEM_ID_PREFIX +4;
	//Other
	private static final int superConductorUpkeepDefault = 500;

	//Runtime Values
	//Blocks
	public static int rawWire;
	public static int insulatedWire;
	public static int wireBlock;
	public static int SwitchWire;
	public static int SwitchWireBlock;
	//public static int redstoneWire;
	//public static int redstoneWireBlock;
	public static int BigBatteryBox;
	public static int VoltDet;
	public static int UPTransformer;
	public static int DOWNTransformer;
	public static int wireMill;
	public static int Fuse;
	public static int batBox;
	public static int WPT;
	//Items
	public static int Upgrade;
	public static int LeadTearBat;
	public static int ConnectionAlloy;
	public static int Parts;
	public static int Lead;
	//Other
	public static double superConductorUpkeep;

	public static final Configuration CONFIG = new Configuration(new File("config/UniversalElectricity/ElectricExpansion.cfg"));
	public static boolean configLoaded = configLoad(CONFIG);

	//Blocks
	public static final Block blockRawWire = new BlockRawWire(rawWire, 0);
	public static final Block blockInsulatedWire = new BlockInsulatedWire(insulatedWire, 0);
	public static final Block blockWireBlock = new BlockWireBlock(wireBlock, 0);
	public static final Block blockSwitchWire = new BlockSwitchWire(SwitchWire, 0);
	public static final Block blockSwitchWireBlock = new BlockSwitchWireBlock(SwitchWireBlock, 0);
	//public static final Block blockRedstoneWire = new BlockRedstoneWire(redstoneWire, 0);
	//public static final Block blockRedstoneWireBlock = new BlockRedstoneWireBlock(redstoneWireBlock, 0);

	public static final Block blockBigBatteryBox = new BlockAdvBatteryBox(BigBatteryBox, 0).setCreativeTab(UETab.INSTANCE).setBlockName("AdvBatBox");
	public static final Block blockVoltDet = new BlockVoltDetector(VoltDet, 0).setCreativeTab(UETab.INSTANCE).setBlockName("VoltDet");
	public static final Block blockUPTransformer = new BlockUPTransformer(UPTransformer, 0).setCreativeTab(UETab.INSTANCE);
	public static final Block blockDOWNTransformer = new BlockDOWNTransformer(DOWNTransformer, 0).setCreativeTab(UETab.INSTANCE);
	public static final Block blockWireMill = new BlockWireMill(wireMill).setCreativeTab(UETab.INSTANCE).setBlockName("blockEtcher");
	//public static final Block blockFuse = new BlockFuse(Fuse, 0).setCreativeTab(UETab.INSTANCE).setBlockName("blockFuse");
	public static final Block blockWPT = new BlockWPT(WPT, 0);
	
	//Items
	public static final Item itemParts = new ItemParts(Parts, 0);
	public static final Item itemUpgrade = new ItemUpgrade(Upgrade, 0).setItemName("Upgrade");
	public static final ItemElectric itemLeadTearBat = new ItemLeadTearBattery(LeadTearBat, 0);
	public static final Item itemConnectorAlloy = new ItemConnectorAlloy(ConnectionAlloy, 0).setCreativeTab(UETab.INSTANCE);
	public static final Item itemLead = new ItemBase(Lead, 0).setCreativeTab(UETab.INSTANCE).setItemName("Lead");

	public static Logger EELogger = Logger.getLogger("ElectricExpansion");
	public static boolean[] startLogLogged = {false, false, false, false};

	@Instance("ElectricExpansion")
	public static ElectricExpansion instance;

	@SidedProxy(clientSide="electricexpansion.client.EEClientProxy", serverSide="electricexpansion.EECommonProxy")
	public static EECommonProxy proxy;

	public static boolean configLoad(Configuration i)
	{

		rawWire = UEConfig.getBlockConfigID(i, "Uninsulated_Wire", rawWireID);
		insulatedWire = UEConfig.getBlockConfigID(i, "Insualted_Wire", insulatedWireID);
		wireBlock = UEConfig.getBlockConfigID(i, "Wire_Block", wireBlockID);
		SwitchWire = UEConfig.getBlockConfigID(i, "Switch_Wire", switchWireID);
		SwitchWireBlock = UEConfig.getBlockConfigID(i, "Switch_Wire_Block", switchWireBlockID);
		//Redstone'd Insulated Cable
		//Redstone'd Cable Blocks

		BigBatteryBox = UEConfig.getBlockConfigID(i, "Advanced_Bat_Box", blockBigBatteryBoxID);
		VoltDet = UEConfig.getBlockConfigID(i, "Voltage_Detector", blockVoltDetID);
		UPTransformer = UEConfig.getBlockConfigID(i, "Up_Transformer", blockUPTransformerID);
		DOWNTransformer = UEConfig.getBlockConfigID(i, "Down_Transformer", blockDOWNTransformerID);
		wireMill = UEConfig.getBlockConfigID(i, "Wire_Mill", blockWireMillID);
		Fuse = UEConfig.getBlockConfigID(i, "Relay", blockFuseID);
		WPT = UEConfig.getBlockConfigID(i, "Wireless_Transfer_Machines", blockWPTID);

		Upgrade = UEConfig.getItemConfigID(i, "Advanced_Bat_Box_Upgrade", itemUpgradeID);
		LeadTearBat = UEConfig.getItemConfigID(i, "Lead_Tear_Battery", itemLeadTearBatID);
		ConnectionAlloy = UEConfig.getItemConfigID(i, "Connection_Alloy", itemUpgradeID);
		Parts = UEConfig.getItemConfigID(i, "Parts", itemPartsID);
		Lead = UEConfig.getItemConfigID(i, "Lead_Ingot", itemLeadID);

		superConductorUpkeep = (double)((UEConfig.getItemConfigID(i, "Super_Conductor_Upkeep", superConductorUpkeepDefault))/10);
		i.get(Configuration.CATEGORY_GENERAL, "Super_Conductor_Upkeep", superConductorUpkeepDefault).comment = "Divide by 10 to get the Watt upkeep cost, per second, for EACH Super-Conductor Cable's super-conducting function.";

		configLoaded = true;
		return true; //returns true to configLoaded VAR
	}

	private static void StartLog(String string1)
	{
		int i;
		version = "v";
		for (i=0; i<versionArray.length; i++)
			version = version + "." + versionArray[i];
		String string2 = null;
		int j = 0;

		if(string1 != null && string1 == "preInit")
		{
			string2 = "PreInitializing";
			j = 1;
		}
		if(string1 != null && string1 == "Init")
		{
			string2 = "Initializing";
			j = 2;
		}
		if(string1 != null && string1 == "postInit")
		{
			string2 = "PostInitializing";
			j = 3;
		}

		EELogger.setParent(FMLLog.getLogger());
		EELogger.info(string2 + " ElectricExpansion " + version);
		startLogLogged[j] = true;
		if(startLogLogged[1] && startLogLogged[2] && startLogLogged[3])
			startLogLogged[0] = true;
	}

	@PreInit
	public void preInit(FMLPreInitializationEvent event) 
	{
		UniversalElectricity.register(this, 1, 0, 0, false);

		if(!configLoaded){configLoad(CONFIG);}
		if(startLogLogged[1] != true){StartLog("preInit");}
		Item.itemsList[rawWire] = new ItemBlockRawWire(rawWire-256, blockRawWire);
		Item.itemsList[insulatedWire] = new ItemBlockInsualtedWire(insulatedWire-256, blockInsulatedWire);
		Item.itemsList[wireBlock] = new ItemBlockWireBlock(wireBlock-256, blockWireBlock);
		Item.itemsList[SwitchWire] = new ItemBlockSwitchWire(SwitchWire-256, blockSwitchWire);
		Item.itemsList[SwitchWireBlock] = new ItemBlockSwitchWireBlock(SwitchWireBlock-256, blockSwitchWireBlock);
		Item.itemsList[WPT] = new ItemBlockSwitchWireBlock(WPT-256, blockWPT);
		//Redstone'd Insulated Cable
		//Redstone'd Cable Blocks
		GameRegistry.registerBlock(blockBigBatteryBox);
		GameRegistry.registerBlock(blockDOWNTransformer);
		GameRegistry.registerBlock(blockUPTransformer);
		GameRegistry.registerBlock(blockWireMill);
		GameRegistry.registerBlock(blockVoltDet);


		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);

		if(!Loader.isModLoaded("BasicComponents")) 
			System.out.println("Basic Components NOT detected! Basic Components is REQUIRED for survival crafting and gameplay!");
	}

	@Init
	public void load(FMLInitializationEvent event) 
	{
		if(startLogLogged[2] != true){StartLog("Init");}
		proxy.init();

		RecipeRegistrar.crafting();
		RecipeRegistrar.drawing();
		UETab.setItemStack(new ItemStack(this.blockBigBatteryBox));

	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) 
	{
		if(startLogLogged[3] != true){StartLog("postInit");}

		//Set the Uninsulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Copper.name", "Uninsulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Tin.name", "Uninsulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Silver.name", "Uninsulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.HV.name", "Uninsulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Endium.name", "Uninsulated Endium Wire");
		//Set the Insulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Copper.name", "Insulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Tin.name", "Insulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Silver.name", "Insulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.HV.name", "Insulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Endium.name", "Insulated Endium Wire");
		//Set the Hidden Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Copper.name", "Hidden Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Tin.name", "Hidden Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Silver.name", "Hidden Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.HV.name", "Hidden HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Endium.name", "Hidden Endium Wire");
		//Set the Switch Cable (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Copper.name", "Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Tin.name", "Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Silver.name", "Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.HV.name", "HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Endium.name", "Endium Switch Wire");
		//Set the Switch Cable Block (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Copper.name", "Hidden Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Tin.name", "Hidden Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Silver.name", "Hidden Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.HV.name", "Hidden HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Endium.name", "Hidden Endium Switch Wire");

		//Machines
		LanguageRegistry.addName(blockUPTransformer, "Up Transformer");
		LanguageRegistry.addName(blockBigBatteryBox, "Advanced Battery Box");
		LanguageRegistry.addName(blockDOWNTransformer, "Down Transformer");
		LanguageRegistry.addName(blockVoltDet, "Voltage Detector");
		LanguageRegistry.addName(blockWireMill, "Wire Mill");
		//LanguageRegistry.addName(blockFuse, "120 Volt Relay");
		LanguageRegistry.addName(new ItemStack(blockWPT, 1, 0), "Quantum Battery Box");
		LanguageRegistry.addName(new ItemStack(blockWPT, 1, 4), "Induction Power Sender");
		LanguageRegistry.addName(new ItemStack(blockWPT, 1, 8), "Induction Power Reciever");

		//Upgrades
		LanguageRegistry.addName(new ItemStack(itemUpgrade, 1, 0), "Tier 1 Storage Upgrade");
		LanguageRegistry.addName(new ItemStack(itemUpgrade, 1, 1), "Tier 2 Storage Upgrade");
		LanguageRegistry.addName(new ItemStack(itemUpgrade, 1, 2), "Tier 3 Storage Upgrade");
		LanguageRegistry.addName(new ItemStack(itemUpgrade, 1, 3), "BC Compatibility Upgrade");
		LanguageRegistry.addName(new ItemStack(itemUpgrade, 1, 4), "IC2 Compatibility Upgrade");
		
		//Resources
		LanguageRegistry.addName(itemLead, "Lead Ingot");
		LanguageRegistry.addName(itemLeadTearBat, "Lead-Tear Battery");
		LanguageRegistry.addName(new ItemStack(itemParts, 1, 0), "Draw Plates");

		MinecraftForge.EVENT_BUS.register(this);
		
		MinecraftForgeClient.preloadTexture(EECommonProxy.MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(EECommonProxy.MattItem_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(EECommonProxy.AITEMS);
		MinecraftForgeClient.preloadTexture(EECommonProxy.ABLOCK);


	}

	@ForgeSubscribe
	public void onEntityDeath(LivingDeathEvent event)
	{
		if (event.entity != null)
		{
			if(event.entity instanceof EntitySkeleton && ((EntitySkeleton)event.entity).getSkeletonType() == 1)
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
	{distributionNetworks.onWorldSave();}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{distributionNetworks.onWorldLoad();}
}
