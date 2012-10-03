package electricexpansion.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.basiccomponents.RenderCopperWire;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import cpw.mods.fml.client.registry.ClientRegistry;
import electricexpansion.alex_hawks.cables.TileEntityInsulatedWire;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;
import electricexpansion.alex_hawks.cables.TileEntityRedstoneWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireOff;
import electricexpansion.client.alex_hawks.RenderInsulatedWire;
import electricexpansion.client.alex_hawks.RenderRawWire;

public class EEClientProxy extends electricexpansion.EECommonProxy{

	//@Override
	public static void registerRenderers() 
	{
		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);
	}
	
	@Override
	public void init(){
		ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
		ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
		ClientRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff", new RenderInsulatedWire());	}

}
