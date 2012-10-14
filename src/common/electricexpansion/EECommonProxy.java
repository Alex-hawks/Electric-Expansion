package electricexpansion;

import cpw.mods.fml.common.registry.GameRegistry;

import electricexpansion.alex_hawks.cables.*;
import electricexpansion.client.mattredsox.*;
import electricexpansion.mattredsox.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class EECommonProxy extends universalelectricity.prefab.CommonProxy
{
	//Mattredsox's Textures
	public static final String MattFILE_PATH = "/electricexpansion/textures/mattredsox/";
	public static final String MattBLOCK_TEXTURE_FILE = MattFILE_PATH + "blocks.png";
	public static final String MattBLOCK1_TEXTURE_FILE = MattFILE_PATH + "blocks1.png";
	public static final String MattItem_TEXTURE_FILE = MattFILE_PATH + "items.png";
        
	//Alex_hawks' Textures
	public static String ATEXTURES = "electricexpansion/textures/alex_hawks/";
	public static String AITEMS = "/electricexpansion/textures/alex_hawks/items.png";
	public static String ABLOCK = "/electricexpansion/textures/alex_hawks/block.png";
	
	@Override
	public void init()
	{
		//Alex_hawks' Tile entity registrations
		GameRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire");
		GameRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire");
		GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWireOff.class, "TileEntitySwitchWireOff");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlockOff.class, "TileEntitySwitchWireBlockOff");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWireBlock.class, "TileEntityRedstoneWireBlock");
		
		//Mattredsox's Tile entity registrations
		GameRegistry.registerTileEntity(TileEntityAdvBatteryBox.class, "TileEntityAdv");
		GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TileEntityUp");
		GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TileEntityVoltDet");
		GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TileEntityDown");
		GameRegistry.registerTileEntity(TileEntityFuse.class, "TileEntityFuse");
		GameRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill");
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new GUIAdvBatteryBox(player.inventory, (TileEntityAdvBatteryBox)tileEntity);
				case 1: return new GuiVoltDetector(player.inventory, (TileEntityVoltDetector)tileEntity);
				case 2: return new GuiWireMill(player.inventory, (TileEntityWireMill)tileEntity);

			}
        }
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new ContainerAdvBatteryBox(player.inventory, ((TileEntityAdvBatteryBox)tileEntity));
				case 1: return new ContainerVoltDetector();
				case 2: return new ContainerWireMill(player.inventory, (TileEntityWireMill)tileEntity);
			}
        }
		return null;
	}
}
