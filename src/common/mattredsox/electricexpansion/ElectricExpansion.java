package mattredsox.electricexpansion;

import java.io.File;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.*;
import universalelectricity.basiccomponents.BasicComponents;
import universalelectricity.electricity.ElectricityManager;
import universalelectricity.network.PacketManager;
import universalelectricity.recipe.RecipeManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
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


@Mod(modid = "ElectricExpansion", name = "Electric Expansion", version = "0.8.4", dependencies = "after:BasicComponents")
@NetworkMod(channels = { "ElecEx" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketManager.class)

public class ElectricExpansion
{

	public static int blockBigBoxid;
	public static int blockVoltDetid;
	public static int blockuptransid;
	public static int blockdowntransid;
	public static Block blockBigBatteryBox;
    public static Block blockVoltDet;
    public static Block blockUPTransformer;
    public static Block blockDOWNTransformer;
    public static final String FILE_PATH = "/electricexpansion/";

    
    public static ElectricExpansion instance;
    
    @SidedProxy(clientSide = "mattredsox.electricexpansion.client.EEClientProxy", serverSide = "mattredsox.electricexpansion.EECommonProxy")
	public static EECommonProxy proxy;
        

	public static Configuration config = new Configuration(new File(Loader.instance().getConfigDir(), "config/HMConfig.cfg"));


	public void preInit(FMLPreInitializationEvent event)
    {
		UniversalElectricity.registerMod(this, "Electric Expansion", "0.8.3");
		instance = this;

		config.load();
		
        blockBigBoxid = config.getOrCreateBlockIdProperty("LargerBatBox", 3004).getInt(3004);
        blockVoltDetid = config.getOrCreateBlockIdProperty("VoltageDetector", 3005).getInt(3005);
        blockuptransid = config.getOrCreateBlockIdProperty("UPTransformer", 3006).getInt(3006);
        blockdowntransid = config.getOrCreateBlockIdProperty("DOWNTransformer", 3007).getInt(3007);
    
        config.save();
		
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
		
	
    	proxy.preInit();

    }
    
    @Init
	public void load(FMLInitializationEvent evt)
    {
    	proxy.init(); 
    	blockBigBatteryBox= new BlockBigBatteryBox(3004, 0).setCreativeTab(CreativeTabs.tabDecorations);
		blockVoltDet = new BlockVoltDetector(3005, 0).setCreativeTab(CreativeTabs.tabDecorations);
		blockUPTransformer = new BlockUPTransformer(3006, 0).setCreativeTab(CreativeTabs.tabDecorations);
		blockDOWNTransformer = new BlockDOWNTransformer(3007, 0).setCreativeTab(CreativeTabs.tabDecorations);
    	//Register Blocks
    	GameRegistry.registerBlock(blockBigBatteryBox);
    	GameRegistry.registerBlock(blockUPTransformer);
    	GameRegistry.registerBlock(blockDOWNTransformer);
    	GameRegistry.registerBlock(blockVoltDet);
		GameRegistry.registerTileEntity(TileEntityBigBatteryBox.class, "TEBBB");
		GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TEUp");
		GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TEVD");
		GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TEDown");
		LanguageRegistry.addName(blockUPTransformer, "Up Transformer");
        LanguageRegistry.addName(blockBigBatteryBox, "Larger Battery Box");
        LanguageRegistry.addName(blockDOWNTransformer, "Down Transformer");
        LanguageRegistry.addName(blockVoltDet, "Voltage Detector");
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
            
    }

    
}