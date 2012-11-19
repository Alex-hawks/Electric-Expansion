package electricexpansion.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.alex_hawks.cables.TileEntityInsulatedWire;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireBlock;
import electricexpansion.alex_hawks.cables.TileEntityWireBlock;
import electricexpansion.alex_hawks.machines.TileEntityDistribution;
import electricexpansion.alex_hawks.machines.TileEntityInductionReciever;
import electricexpansion.alex_hawks.machines.TileEntityInductionSender;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;
import electricexpansion.client.alex_hawks.RenderInsulatedWire;
import electricexpansion.client.alex_hawks.RenderRawWire;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;
import electricexpansion.mattredsox.tileentities.TileEntityDOWNTransformer;
import electricexpansion.mattredsox.tileentities.TileEntityUPTransformer;
import electricexpansion.mattredsox.tileentities.TileEntityVoltDetector;

public class EEClientProxy extends electricexpansion.EECommonProxy
{

	//@Override
	public static void registerRenderers() 
	{
		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);
		
		MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);
	}
	
	@Override
	public void init()
	{
			//Alex's Tile Entity Renderer registrations
			ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
			ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
	//	ClientRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire", new RenderInsulatedWire());
			ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
			GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
			GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
			GameRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill");
			
			//Mattredsox's Tile entity registrations
			GameRegistry.registerTileEntity(TileEntityAdvBatteryBox.class, "TileEntityAdvBox");
			GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TileEntityUpTrans");
			GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TileEntityVoltDet");
			GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TileEntityDownTrans");
		//	GameRegistry.registerTileEntity(TileEntityFuse.class, "TileEntityFuse");
			GameRegistry.registerTileEntity(TileEntityDistribution.class, "TileEntityDistribution");
			GameRegistry.registerTileEntity(TileEntityInductionReciever.class, "TileEntityInductionReciever");
			GameRegistry.registerTileEntity(TileEntityInductionSender.class, "TileEntityInductionSender");
	}

}

