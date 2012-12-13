package electricexpansion.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
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
import electricexpansion.client.render.RenderHandler;
import electricexpansion.client.render.RenderInsulatedWire;
import electricexpansion.client.render.RenderRawWire;
import electricexpansion.client.render.RenderTransformer;
import electricexpansion.client.render.RenderWireMill;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;
import electricexpansion.mattredsox.tileentities.TileEntityMultimeter;
import electricexpansion.mattredsox.tileentities.TileEntityTransformer;

public class EEClientProxy extends electricexpansion.EECommonProxy
{

	//@Override
	public static void registerRenderers() 
	{
/*		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);
		
		MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);*/
	}

	public static int RENDER_ID;
	
	@Override
	public void init()
	{
		 RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderHandler());

		//Alex's Tile Entity Renderer registrations
		ClientRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill", new RenderWireMill());
		ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
		ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
		GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
		GameRegistry.registerTileEntity(TileEntityDistribution.class, "TileEntityDistribution");
		GameRegistry.registerTileEntity(TileEntityInductionReciever.class, "TileEntityInductionReciever");
		GameRegistry.registerTileEntity(TileEntityInductionSender.class, "TileEntityInductionSender");
		
		//Mattredsox's Tile entity registrations
		ClientRegistry.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer", new RenderTransformer());
		GameRegistry.registerTileEntity(TileEntityAdvBatteryBox.class, "TileEntityAdvBox");
		GameRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityVoltDet");

		
		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);
		
		MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);
	}

}

