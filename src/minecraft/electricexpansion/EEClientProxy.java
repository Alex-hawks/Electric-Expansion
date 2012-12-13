package electricexpansion;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.cables.TileEntityInsulatedWire;
import electricexpansion.cables.TileEntityRawWire;
import electricexpansion.cables.TileEntitySwitchWire;
import electricexpansion.cables.TileEntitySwitchWireBlock;
import electricexpansion.cables.TileEntityWireBlock;
import electricexpansion.render.RenderHandler;
import electricexpansion.render.RenderInsulatedWire;
import electricexpansion.render.RenderRawWire;
import electricexpansion.render.RenderTransformer;
import electricexpansion.render.RenderWireMill;
import electricexpansion.tile.TileEntityAdvBatteryBox;
import electricexpansion.tile.TileEntityDistribution;
import electricexpansion.tile.TileEntityInductionReciever;
import electricexpansion.tile.TileEntityInductionSender;
import electricexpansion.tile.TileEntityMultimeter;
import electricexpansion.tile.TileEntityTransformer;
import electricexpansion.tile.TileEntityWireMill;

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

