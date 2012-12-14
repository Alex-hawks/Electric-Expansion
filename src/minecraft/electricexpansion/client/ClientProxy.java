package electricexpansion.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.client.render.RenderHandler;
import electricexpansion.client.render.RenderInsulatedWire;
import electricexpansion.client.render.RenderRawWire;
import electricexpansion.client.render.RenderMultimeter;
import electricexpansion.client.render.RenderTransformer;
import electricexpansion.client.render.RenderWireMill;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import electricexpansion.common.cables.TileEntityWireBlock;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityDistribution;
import electricexpansion.common.tile.TileEntityInductionReciever;
import electricexpansion.common.tile.TileEntityInductionSender;
import electricexpansion.common.tile.TileEntityMultimeter;
import electricexpansion.common.tile.TileEntityTransformer;
import electricexpansion.common.tile.TileEntityWireMill;

@SideOnly(Side.CLIENT)
public class ClientProxy extends electricexpansion.common.EECommonProxy
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
		ClientRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityMultimeter", new RenderMultimeter());
		GameRegistry.registerTileEntity(TileEntityAdvancedBatteryBox.class, "TileEntityAdvBox");

		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);

		MinecraftForgeClient.preloadTexture(MattBLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(MattItem_TEXTURE_FILE);
	}

}
