package electricexpansion.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemMultimeter extends Item
{
	public ItemMultimeter(int par1)
	{
		super(par1);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName("itemMultimeter");
	}

	@Override
	public String getTextureFile()
	{
		return ElectricExpansion.ITEM_FILE;
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

				player.addChatMessage("Electric Expansion: " + ElectricInfo.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", " + ElectricInfo.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", " + ElectricInfo.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
			}
			else
			{
				if (tileEntity instanceof IJouleStorage)
				{
					IJouleStorage tileStorage = (IJouleStorage) tileEntity;
					player.addChatMessage("Electric Expansion: " + ElectricInfo.getDisplay(tileStorage.getJoules(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplay(tileStorage.getMaxJoules(), ElectricUnit.JOULES));
				}
				if (tileEntity instanceof IVoltage)
				{
					player.addChatMessage("Electric Expansion: " + ElectricInfo.getDisplay(((IVoltage) tileEntity).getVoltage(), ElectricUnit.VOLTAGE));
				}

				return true;
			}
		}

		return false;
	}

}
