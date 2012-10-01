package electricexpansion;

import cpw.mods.fml.common.registry.GameRegistry;
import electricexpansion.Mattredsox.*;
import electricexpansion.client.Mattredsox.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.basiccomponents.TileEntityCopperWire;

public class EECommonProxy extends universalelectricity.prefab.CommonProxy
{

	@Override
	public void init()
	{
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
	
	
