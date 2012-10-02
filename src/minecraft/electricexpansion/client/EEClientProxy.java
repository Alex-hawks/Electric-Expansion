package electricexpansion.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.basiccomponents.RenderCopperWire;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import cpw.mods.fml.client.registry.ClientRegistry;
import electricexpansion.additionalcables.cables.TileEntityInsulatedWire;
import electricexpansion.additionalcables.cables.TileEntityRawWire;
import electricexpansion.additionalcables.cables.TileEntityRedstoneWire;
import electricexpansion.additionalcables.cables.TileEntitySwitchWire;
import electricexpansion.additionalcables.cables.TileEntitySwitchWireOff;
import electricexpansion.client.additionalcables.client.RenderInsulatedWire;
import electricexpansion.client.additionalcables.client.RenderRawWire;



public class EEClientProxy extends electricexpansion.EECommonProxy{

	//@Override
	public static void registerRenderers() 
	{
		MinecraftForgeClient.preloadTexture(ITEMS);
		MinecraftForgeClient.preloadTexture(BLOCK);
	}
	
	@Override
	public void init(){
		ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
		ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff", new RenderInsulatedWire());	}

}
