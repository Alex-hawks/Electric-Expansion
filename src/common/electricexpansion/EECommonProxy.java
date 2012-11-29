package electricexpansion;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.alex_hawks.cables.TileEntityInsulatedWire;
import electricexpansion.alex_hawks.cables.TileEntityRawWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireBlock;
import electricexpansion.alex_hawks.cables.TileEntityWireBlock;
import electricexpansion.alex_hawks.containers.ContainerWireMill;
import electricexpansion.alex_hawks.machines.TileEntityDistribution;
import electricexpansion.alex_hawks.machines.TileEntityInductionReciever;
import electricexpansion.alex_hawks.machines.TileEntityInductionSender;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;
import electricexpansion.client.alex_hawks.gui.GuiWireMill;
import electricexpansion.client.mattredsox.GUIAdvBatteryBox;
import electricexpansion.client.mattredsox.GuiVoltDetector;
import electricexpansion.mattredsox.ContainerAdvBatteryBox;
import electricexpansion.mattredsox.ContainerVoltDetector;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;
import electricexpansion.mattredsox.tileentities.TileEntityVoltDetector;

public class EECommonProxy implements IGuiHandler
{
	//Mattredsox's Textures
	public static final String MattFILE_PATH = "/electricexpansion/textures/mattredsox/";
	public static final String MattBLOCK_TEXTURE_FILE = MattFILE_PATH + "blocks.png";
	public static final String MattItem_TEXTURE_FILE = MattFILE_PATH + "items.png";
        
	//Alex_hawks' Textures
	public static final String ATEXTURES = "/electricexpansion/textures/alex_hawks/";
	public static final String AITEMS = "/electricexpansion/textures/alex_hawks/items.png";
	public static final String ABLOCK = "/electricexpansion/textures/alex_hawks/block.png";
	
	public void init()
	{
		//Alex_hawks' Tile entity registrations
		GameRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire");
		GameRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire");
		GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
		GameRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire");
		GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
		GameRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWire.class, "TileEntityRedstoneWire");
		//GameRegistry.registerTileEntity(TileEntityRedstoneWireBlock.class, "TileEntityRedstoneWireBlock");
		
		//Mattredsox's Tile entity registrations
		GameRegistry.registerTileEntity(TileEntityAdvBatteryBox.class, "TileEntityAdvBox");
		//GameRegistry.registerTileEntity(TileEntityUPTransformer.class, "TileEntityUpTrans");
		GameRegistry.registerTileEntity(TileEntityVoltDetector.class, "TileEntityVoltDet");
//		GameRegistry.registerTileEntity(TileEntityDOWNTransformer.class, "TileEntityDownTrans");
//		GameRegistry.registerTileEntity(TileEntityFuse.class, "TileEntityFuse");
		GameRegistry.registerTileEntity(TileEntityDistribution.class, "TileEntityDistribution");
		GameRegistry.registerTileEntity(TileEntityInductionReciever.class, "TileEntityInductionReciever");
		GameRegistry.registerTileEntity(TileEntityInductionSender.class, "TileEntityInductionSender");
	}
	
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
	//	if (tileEntity != null)
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
	
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
	//	if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new ContainerAdvBatteryBox(player.inventory, ((TileEntityAdvBatteryBox)tileEntity));
				case 1: return new ContainerVoltDetector(((TileEntityVoltDetector)tileEntity));
				case 2: return new ContainerWireMill(player.inventory, (TileEntityWireMill)tileEntity);
			}
        }
		return null;
	}
}
