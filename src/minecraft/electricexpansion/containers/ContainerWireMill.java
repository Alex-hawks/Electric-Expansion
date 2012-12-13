package electricexpansion.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import electricexpansion.tile.TileEntityWireMill;

public class ContainerWireMill extends Container
{
	private TileEntityWireMill tileEntity;

	public ContainerWireMill(InventoryPlayer par1InventoryPlayer, TileEntityWireMill tileEntity)
	{
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new universalelectricity.prefab.SlotElectricItem(tileEntity, 0, 55, 49)); // Electric
																											// Input
																											// Slot
		this.addSlotToContainer(new Slot(tileEntity, 1, 55, 25)); // To be drawn into wire
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 108, 25)); // Drawing
																										// result

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		tileEntity.openChest();
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{
		super.onCraftGuiClosed(entityplayer);
		tileEntity.closeChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}

}
