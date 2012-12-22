	package electricexpansion.common.items;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.ItemElectric;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityConductor;
import electricexpansion.common.CommonProxy;
import electricexpansion.common.ElectricExpansion;

public class ItemMultimeter extends Item
{
	public ItemMultimeter(int par1)
	{
		super(par1);
		this.iconIndex = 1;
		this.setItemName("itemMultimeter");
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public String getTextureFile()
	{
		return CommonProxy.MattItem_TEXTURE_FILE;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (worldObj.isRemote)
		return false;
		
		TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
		if (!(tile instanceof TileEntityConductor))
			return false;
		
		TileEntityConductor wireTile = (TileEntityConductor) tile;
		
		ElectricityPack getProduced = wireTile.getNetwork().getProduced();

		player.addChatMessage("Multimeter Reading Successful!");
		player.addChatMessage("Amperes: " + ElectricInfo.roundDecimals(getProduced.amperes) + "  Voltage: " + ElectricInfo.roundDecimals(getProduced.voltage) + "  Watts: " + ElectricInfo.roundDecimals(getProduced.getWatts()));			
		
		return true;
	}


}
