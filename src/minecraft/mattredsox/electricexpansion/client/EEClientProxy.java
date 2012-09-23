package mattredsox.electricexpansion.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.basiccomponents.RenderCopperWire;
import universalelectricity.basiccomponents.TileEntityCopperWire;
import cpw.mods.fml.client.registry.ClientRegistry;
import mattredsox.electricexpansion.EECommonProxy;
import mattredsox.electricexpansion.TileEntityBigBatteryBox;

public class EEClientProxy extends EECommonProxy{
	@Override
	public void init(){
	//	ClientRegistry.registerTileEntity(TileEntityHVWire.class, "TileEntityHVWire", new RenderHVWire());
	}

}
