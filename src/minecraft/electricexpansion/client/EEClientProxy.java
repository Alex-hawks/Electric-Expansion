package electricexpansion.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.basiccomponents.RenderCopperWire;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import cpw.mods.fml.client.registry.ClientRegistry;


public class EEClientProxy extends electricexpansion.EECommonProxy{
	@Override
	public void init(){
	//	ClientRegistry.registerTileEntity(TileEntityHVWire.class, "TileEntityHVWire", new RenderHVWire());
	}

}
