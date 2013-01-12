package electricexpansion.api;

import java.io.File;

import net.minecraft.item.ItemStack;

public interface IItemFuse
{
	double getMaxVolts(ItemStack itemStack);
	
	File getTextureForRender(ItemStack itemStack);
	
	byte getTier(ItemStack itemStack);
	
	void onFuseTrip(ItemStack itemStack);

	boolean isValidFuse(ItemStack itemStack);
}
