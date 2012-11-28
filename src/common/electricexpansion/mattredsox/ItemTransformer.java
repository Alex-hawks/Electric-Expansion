package net.minecraft.src.Transformer;

import java.util.*;

import net.minecraft.src.*;
import net.minecraft.src.forge.*;

public class ItemTransformer extends Item implements ITextureProvider {

	private int spawnID;

	public ItemTransformer(int par1, Block par2Block) {
		super(par1);
		this.spawnID = par2Block.blockID;
		this.setHasSubtypes(true);
	}

	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this, 1, 0));
		itemList.add(new ItemStack(this, 1, 1));
		itemList.add(new ItemStack(this, 1, 2));
	}

	public int getIconFromDamage(int par1) {
		switch (par1) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 2;
		}
		return 0;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		switch (itemstack.getItemDamage()) {
		case 0:
			return "Transformer";
		case 1:
			return "Tier 2 upgrade";
		case 2:
			return "Tier 3 upgrade";
		}
		return null;
	}

	public int getMetadata(int par1) {
		return par1;
	}

	@Override
	public String getTextureFile() {
		return TransformerCore.textureFile;
	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7) {
		int var8 = par3World.getBlockId(par4, par5, par6);

		if (var8 == Block.snow.blockID) {
			par7 = 1;
		} else if (var8 != Block.vine.blockID && var8 != Block.tallGrass.blockID && var8 != Block.deadBush.blockID) {
			if (par7 == 0) {
				--par5;
			}

			if (par7 == 1) {
				++par5;
			}

			if (par7 == 2) {
				--par6;
			}

			if (par7 == 3) {
				++par6;
			}

			if (par7 == 4) {
				--par4;
			}

			if (par7 == 5) {
				++par4;
			}
		}

		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6)) {
			return false;
		} else if (par1ItemStack.stackSize == 0) {
			return false;
		} else {
			if (par3World.canBlockBePlacedAt(this.spawnID, par4, par5, par6, false, par7) && this.getMetadata(par1ItemStack.getItemDamage()) == 0) {
				Block var9 = Block.blocksList[this.spawnID];

	            if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.spawnID, this.getMetadata(par1ItemStack.getItemDamage())))
	            {
	                if (par3World.getBlockId(par4, par5, par6) == this.spawnID)
	                {
	                    Block.blocksList[this.spawnID].onBlockPlaced(par3World, par4, par5, par6, par7);
	                    Block.blocksList[this.spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
	                }

	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var9.stepSound.getStepSound(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
	                --par1ItemStack.stackSize;
	            }
			}else if(par3World.canBlockBePlacedAt(this.spawnID, par4, par5, par6, false, par7) && par3World.getBlockId(par4, par5 - 1, par6) == TransformerCore.blocktransformer.blockID && par3World.getBlockMetadata(par4, par5 - 1, par6) == 0 && this.getMetadata(par1ItemStack.getItemDamage()) == 1)
			{
				Block var9 = Block.blocksList[this.spawnID];

	            if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.spawnID, this.getMetadata(par1ItemStack.getItemDamage())))
	            {
	                if (par3World.getBlockId(par4, par5, par6) == this.spawnID)
	                {
	                    Block.blocksList[this.spawnID].onBlockPlaced(par3World, par4, par5, par6, par7);
	                    Block.blocksList[this.spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
	                }

	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var9.stepSound.getStepSound(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
	                --par1ItemStack.stackSize;
	            }
			}else if(par3World.canBlockBePlacedAt(this.spawnID, par4, par5, par6, false, par7) && par3World.getBlockId(par4, par5 - 1, par6) == TransformerCore.blocktransformer.blockID && par3World.getBlockMetadata(par4, par5 - 1, par6) == 1 && this.getMetadata(par1ItemStack.getItemDamage()) == 2)
			{
				Block var9 = Block.blocksList[this.spawnID];

	            if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.spawnID, this.getMetadata(par1ItemStack.getItemDamage())))
	            {
	                if (par3World.getBlockId(par4, par5, par6) == this.spawnID)
	                {
	                    Block.blocksList[this.spawnID].onBlockPlaced(par3World, par4, par5, par6, par7);
	                    Block.blocksList[this.spawnID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
	                }

	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var9.stepSound.getStepSound(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
	                --par1ItemStack.stackSize;
	            }
			}else if(par3World.canBlockBePlacedAt(this.spawnID, par4, par5 + 1, par6, false, par7) && par3World.getBlockId(par4, par5 - 1, par6) == TransformerCore.blocktransformer.blockID && par3World.getBlockMetadata(par4, par5 - 1, par6) == 0  && par3World.getBlockId(par4, par5, par6) == TransformerCore.blocktransformer.blockID && par3World.getBlockMetadata(par4, par5, par6) == 1 && this.getMetadata(par1ItemStack.getItemDamage()) == 2)
			{
				Block var9 = Block.blocksList[this.spawnID];

	            if (par3World.setBlockAndMetadataWithNotify(par4, par5 + 1, par6, this.spawnID, this.getMetadata(par1ItemStack.getItemDamage())))
	            {
	                if (par3World.getBlockId(par4, par5 + 1, par6) == this.spawnID)
	                {
	                    Block.blocksList[this.spawnID].onBlockPlaced(par3World, par4, par5 + 1, par6, par7);
	                    Block.blocksList[this.spawnID].onBlockPlacedBy(par3World, par4, par5 + 1, par6, par2EntityPlayer);
	                }

	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 1 + 0.5F), (double)((float)par6 + 0.5F), var9.stepSound.getStepSound(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
	                --par1ItemStack.stackSize;
	            }
			}
			
			return true;
		}
	}

}