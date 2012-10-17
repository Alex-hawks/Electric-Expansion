package electricexpansion.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import universalelectricity.basiccomponents.RenderCopperWire;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.alex_hawks.cables.TileEntityInsulatedWire;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;
import electricexpansion.alex_hawks.cables.TileEntityRedstoneWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireOff;
import electricexpansion.client.alex_hawks.RenderInsulatedWire;
import electricexpansion.client.alex_hawks.RenderRawWire;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;
import electricexpansion.mattredsox.tileentities.TileEntityDOWNTransformer;
import electricexpansion.mattredsox.tileentities.TileEntityFuse;
import electricexpansion.mattredsox.tileentities.TileEntityUPTransformer;
import electricexpansion.mattredsox.tileentities.TileEntityVoltDetector;

public class EEClientProxy extends electricexpansion.EECommonProxy
{

	//@Override
	public static void registerRenderers() 
	{
		MinecraftForgeClient.preloadTexture(AITEMS);
		MinecraftForgeClient.preloadTexture(ABLOCK);
	}
	
	@Override
	public void init()
	{
		//Mattredsox's Tile entity registrations
			GameRegistry.registerTileEntity(TileEntityAdvBatteryBox.class, "TileEntityAdvBox");
			GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TileEntityUpTrans");
			GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TileEntityVoltDet");
			GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TileEntityDownTrans");
			GameRegistry.registerTileEntity(TileEntityFuse.class, "TileEntityFuse");
		
			//Alex's Tile Entity Renderer registrations
			ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
			ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", new RenderInsulatedWire());
			ClientRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire", new RenderInsulatedWire());
			ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", new RenderInsulatedWire());
			ClientRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff", new RenderInsulatedWire());	
			
	}

}

