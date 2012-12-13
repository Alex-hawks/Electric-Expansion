package electricexpansion.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.cables.TileEntityInsulatedWire;
import electricexpansion.cables.TileEntityRawWire;
import electricexpansion.cables.TileEntitySwitchWire;
import electricexpansion.cables.TileEntitySwitchWireBlock;
import electricexpansion.cables.TileEntityWireBlock;
import electricexpansion.client.render.RenderHandler;
import electricexpansion.client.render.RenderInsulatedWire;
import electricexpansion.client.render.RenderRawWire;
import electricexpansion.client.render.RenderTileEntityMultimeter;
import electricexpansion.client.render.RenderTransformer;
import electricexpansion.client.render.RenderWireMill;
import electricexpansion.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.tile.TileEntityDistribution;
import electricexpansion.tile.TileEntityInductionReciever;
import electricexpansion.tile.TileEntityInductionSender;
import electricexpansion.tile.TileEntityMultimeter;
import electricexpansion.tile.TileEntityTransformer;
import electricexpansion.tile.TileEntityWireMill;

@SideOnly(Side.CLIENT)
public class EEClientProxy extends electricexpansion.EECommonProxy
{

	// @Override
	public static void registerRenderers()
	{
		/*
		 * MinecraftForgeClient.preloadTexture(AITEMS); MinecraftForgeClient.preloadTexture(ABLOCK);
		 * 
		 * MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		 * MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);
		 */
	}

	public static int RENDER_ID;

	@Override
	public void init()
	{
		RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new RenderHandler());

		// Alex's Tile Entity Renderer registrations
		ClientRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill", new RenderWireMill());
		ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
		ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
		GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
		GameRegistry.registerTileEntity(TileEntityDistribution.class, "TileEntityDistribution");
		GameRegistry.registerTileEntity(TileEntityInductionReciever.class, "TileEntityInductionReciever");
		GameRegistry.registerTileEntity(TileEntityInductionSender.class, "TileEntityInductionSender");

		// Mattredsox's Tile entity registrations
		ClientRegistry.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer", new RenderTransformer());
		ClientRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityMultimeter", new RenderTileEntityMultimeter());
		GameRegistry.registerTileEntity(TileEntityAdvancedBatteryBox.class, "TileEntityAdvBox");

		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);

		MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);
	}

}
