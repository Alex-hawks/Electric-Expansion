package electricexpansion.alex_hawks.containers;

import electricexpansion.alex_hawks.machines.TileEntityDistribution;
import electricexpansion.alex_hawks.machines.TileEntityInductionSender;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerDistribution extends Container 
{
	private TileEntityDistribution tileEntity;

	public ContainerDistribution(InventoryPlayer par1InventoryPlayer, TileEntityDistribution tileEntity2)
	{
		this.tileEntity = tileEntity2;
		
		int var3;
		for (var3 = 0; var3 < 3; ++var3)
			for (int var4 = 0; var4 < 9; ++var4)
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
		for (var3 = 0; var3 < 9; ++var3)
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));

		tileEntity2.openChest();
	}
	
	public void onCraftGuiClosed(EntityPlayer entityplayer)
	{tileEntity.closeChest();}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{return this.tileEntity.isUseableByPlayer(par1EntityPlayer);}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{return null;}
}