package electricexpansion.mattredsox;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityEtcher extends TileEntity implements IInventory
{
	public ItemStack[] craftingMatrix;

	public TileEntityEtcher()
	{
		craftingMatrix = new ItemStack[16];
	}

	@Override
	public void closeChest() {


	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if(craftingMatrix[i] != null)
		{
			if(craftingMatrix[i].stackSize <= j)
			{
				ItemStack itemstack = craftingMatrix[i];
				craftingMatrix[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = craftingMatrix[i].splitStack(j);
			if(craftingMatrix[i].stackSize == 0)
			{
				craftingMatrix[i] = null;
			}
			return itemstack1;
		} else
		{
			return null;
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public String getInvName()
	{
		return "Workbench";
	}

	public int getSizeInventory()
	{
		return craftingMatrix.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return craftingMatrix[i];
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) 
	{
		craftingMatrix[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}


}
