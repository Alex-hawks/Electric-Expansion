package electricexpansion.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.implement.IVoltage;
import universalelectricity.prefab.UETab;
import electricexpansion.common.ElectricExpansion;

public class ItemMultimeter extends Item
{
	public ItemMultimeter(int par1)
	{
		super(par1);
		this.iconIndex = 11;
		this.setItemName("itemMultimeter");
		this.setCreativeTab(UETab.INSTANCE);
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.MATT_ITEM_TEXTURE_FILE;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (!worldObj.isRemote)
		{
			TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);

			if (tileEntity instanceof IConductor)
			{
				IConductor wireTile = (IConductor) tileEntity;

				ElectricityPack getProduced = wireTile.getNetwork().getProduced();

				player.addChatMessage(ElectricInfo.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", " + ElectricInfo.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", " + ElectricInfo.getDisplay(getProduced.getWatts(), ElectricUnit.WATT));
			}
			else
			{
				if (tileEntity instanceof IJouleStorage)
				{
					IJouleStorage tileStorage = (IJouleStorage) tileEntity;
					player.addChatMessage(ElectricInfo.getDisplay(tileStorage.getJoules(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplay(tileStorage.getJoules(), ElectricUnit.JOULES));
				}
				if (tileEntity instanceof IVoltage)
				{
					player.addChatMessage(ElectricInfo.getDisplay(((IVoltage) tileEntity).getVoltage(), ElectricUnit.VOLTAGE));
				}

				return true;
			}
		}

		return false;
	}

}
