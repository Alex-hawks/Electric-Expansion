package additionalcables;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

import additionalcables.cables.TileEntityInsulatedWire;
import additionalcables.cables.TileEntityRawWire;
import additionalcables.cables.TileEntityRedstoneWire;
import additionalcables.cables.TileEntitySwitchWire;
import additionalcables.cables.TileEntitySwitchWireBlock;
import additionalcables.cables.TileEntitySwitchWireBlockOff;
import additionalcables.cables.TileEntitySwitchWireOff;
import additionalcables.cables.TileEntityWireBlock;
import additionalcables.client.RenderInsulatedWire;
import additionalcables.client.RenderRawWire;

import universalelectricity.prefab.CommonProxy;

public class ACCommonProxy extends CommonProxy {
	public static String TEXTURES = "/additionalcables/textures/";
	public static String ITEMS = "/additionalcables/textures/items.png";
	public static String BLOCK = "/additionalcables/textures/block.png";
	
	@Override
	public void init()
	{
		GameRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire");
		GameRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire");
		GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlockOff.class, "TileEntitySwitchWireBlockOff");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWireBlock.class, "TileEntityRedstoneWireBlock");
	}
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	//Client stuff
	public static void registerRenderInformation() {
		//Nothing here as this is the server side proxy
	}
}