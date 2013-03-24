package electricexpansion.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.block.IVoltage;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;

public class ItemMultimeter extends ItemElectric
{
	public ItemMultimeter(int par1)
	{
		super(par1);
		this.setCreativeTab(EETab.INSTANCE);
		this.setUnlocalizedName("Multimeter");
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

				player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", " + ElectricityDisplay.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", " + ElectricityDisplay.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
			}
			else
			{
				if (tileEntity instanceof IElectricityStorage)
				{
					IElectricityStorage tileStorage = (IElectricityStorage) tileEntity;
					player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(tileStorage.getJoules(), ElectricUnit.JOULES) + "/" + ElectricityDisplay.getDisplay(tileStorage.getMaxJoules(), ElectricUnit.JOULES));
				}
				if (tileEntity instanceof IVoltage)
				{
					player.addChatMessage("Electric Expansion: " + ElectricityDisplay.getDisplay(((IVoltage) tileEntity).getVoltage(), ElectricUnit.VOLTAGE));
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public double getMaxJoules(ItemStack itemStack)
	{
		return 1_000_000;
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 35;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister par1IconRegister)
	{
		this.iconIndex = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item.", ElectricExpansion.TEXTURE_NAME_PREFIX));
	}
}
