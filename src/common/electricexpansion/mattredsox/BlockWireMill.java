package electricexpansion.mattredsox;

import electricexpansion.ElectricExpansion;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockWireMill extends BlockContainer
{
public BlockWireMill(int par1)
{
         super(par1, Material.wood);
         this.setCreativeTab(CreativeTabs.tabDecorations);
}
/**
         * Returns the block texture based on the side being looked at. Args: side
public int getBlockTextureFromSide(int side)
{
         switch(side)
         {
         case 0: return mod_LegoCrafting.LegoBottom;
         case 1: return mod_LegoCrafting.LegoTop;
         case 2:
         case 3:
         case 4:
         case 5: return mod_LegoCrafting.LegoSide;
         default : return 0;
         }

         */
@Override
public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
{
  if (!par1World.isRemote)
  {
      par5EntityPlayer.openGui(ElectricExpansion.instance, 2, par1World, par2, par3, par4);
      return true;
  }

  return true;
}
@Override
public TileEntity createNewTileEntity(World var1) {
	return new TileEntityWireMill();
}

}