package electricexpansion;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.cables.TileEntityInsulatedWire;
import electricexpansion.cables.TileEntityRawWire;
import electricexpansion.cables.TileEntitySwitchWire;
import electricexpansion.cables.TileEntitySwitchWireBlock;
import electricexpansion.cables.TileEntityWireBlock;
import electricexpansion.containers.ContainerAdvBatteryBox;
import electricexpansion.containers.ContainerDistribution;
import electricexpansion.containers.ContainerInductionReciever;
import electricexpansion.containers.ContainerInductionSender;
import electricexpansion.containers.ContainerMultimeter;
import electricexpansion.containers.ContainerTransformer;
import electricexpansion.containers.ContainerWireMill;
import electricexpansion.gui.GUIAdvBatteryBox;
import electricexpansion.gui.GuiMultimeter;
import electricexpansion.gui.GuiTransformer;
import electricexpansion.gui.GuiWPT;
import electricexpansion.gui.GuiWireMill;
import electricexpansion.tile.TileEntityAdvBatteryBox;
import electricexpansion.tile.TileEntityDistribution;
import electricexpansion.tile.TileEntityInductionReciever;
import electricexpansion.tile.TileEntityInductionSender;
import electricexpansion.tile.TileEntityMultimeter;
import electricexpansion.tile.TileEntityTransformer;
import electricexpansion.tile.TileEntityWireMill;


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
		GameRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityVoltDet");
	GameRegistry.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer");
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
				case 1: return new GuiMultimeter((TileEntityMultimeter)tileEntity);
				case 2: return new GuiWireMill(player.inventory, (TileEntityWireMill)tileEntity);
				case 3: return new GuiTransformer(player.inventory, (TileEntityTransformer)tileEntity);
				case 4: 
				{
					if(tileEntity.getBlockMetadata()>= 0 && tileEntity.getBlockMetadata() < 4)
						return new GuiWPT(player.inventory, (TileEntityDistribution)tileEntity);
					if(tileEntity.getBlockMetadata()>= 4 && tileEntity.getBlockMetadata() < 8)
						return new GuiWPT(player.inventory, (TileEntityInductionSender)tileEntity);
					if(tileEntity.getBlockMetadata()>= 8 && tileEntity.getBlockMetadata() < 12)
						return new GuiWPT(player.inventory, (TileEntityInductionReciever)tileEntity);
				}
			}
        }
		return null;
	}
	
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new ContainerAdvBatteryBox(player.inventory, ((TileEntityAdvBatteryBox)tileEntity));
				case 1: return new ContainerMultimeter(((TileEntityMultimeter)tileEntity));
				case 2: return new ContainerWireMill(player.inventory, (TileEntityWireMill)tileEntity);
				case 3: return new ContainerTransformer(player.inventory, (TileEntityTransformer)tileEntity);
				case 4: 
				{
					if(tileEntity.getBlockMetadata()>= 0 && tileEntity.getBlockMetadata() < 4)
						return new ContainerDistribution(player.inventory, (TileEntityDistribution)tileEntity);
					if(tileEntity.getBlockMetadata()>= 4 && tileEntity.getBlockMetadata() < 8)
						return new ContainerInductionSender(player.inventory, (TileEntityInductionSender)tileEntity);
					if(tileEntity.getBlockMetadata()>= 8 && tileEntity.getBlockMetadata() < 12)
						return new ContainerInductionReciever(player.inventory, (TileEntityInductionReciever)tileEntity);
				}
			}
        }
		return null;
	}
}
