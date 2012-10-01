package electricexpansion;

import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.Mattredsox.*;
import electricexpansion.additionalcables.cables.TileEntityInsulatedWire;
import electricexpansion.additionalcables.cables.TileEntityRawWire;
import electricexpansion.additionalcables.cables.TileEntitySwitchWire;
import electricexpansion.additionalcables.cables.TileEntitySwitchWireBlock;
import electricexpansion.additionalcables.cables.TileEntitySwitchWireBlockOff;
import electricexpansion.additionalcables.cables.TileEntitySwitchWireOff;
import electricexpansion.additionalcables.cables.TileEntityWireBlock;
import electricexpansion.client.Mattredsox.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.basiccomponents.TileEntityCopperWire;

public class EECommonProxy extends universalelectricity.prefab.CommonProxy
{
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity != null)
        {
			switch(ID)
			{
				case 0: return new GUIBigBatteryBox(player.inventory, ((TileEntityBigBatteryBox)tileEntity));
				case 1: return new GuiVoltDetector(player.inventory, (TileEntityVoltDetector)tileEntity);
				case 2: return new GuiEtcher(player.inventory, world, x, y, z);

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
				case 0: return new ContainerBigBatteryBox(player.inventory, ((TileEntityBigBatteryBox)tileEntity));
				case 1: return new ContainerVoltDetector();
				case 2: return new ContainerEtcher(player.inventory, world, x, y, z);

			}
        }
		
		return null;
	}
}
	
	

