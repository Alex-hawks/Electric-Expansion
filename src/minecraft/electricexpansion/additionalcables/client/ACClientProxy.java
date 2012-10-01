package additionalcables.client;

import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;

import additionalcables.ACCommonProxy;
import additionalcables.cables.TileEntityInsulatedWire;
import additionalcables.cables.TileEntityRawWire;
import additionalcables.cables.TileEntityRedstoneWire;
import additionalcables.cables.TileEntitySwitchWire;
import additionalcables.cables.TileEntitySwitchWireOff;

public class ACClientProxy extends ACCommonProxy 
{
	
	//@Override
	public static void registerRenderers() 
	{
		MinecraftForgeClient.preloadTexture(ITEMS);
		MinecraftForgeClient.preloadTexture(BLOCK);
	}
	@Override
	public void init()
	{
		ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
		ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff", new RenderInsulatedWire());
	}
}