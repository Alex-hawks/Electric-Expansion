package electricexpansion.alex_hawks.blocks;

import java.util.List;
import java.util.Random;

import universalelectricity.prefab.BlockConductor;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWire;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireOff;

public class BlockSwitchWireOff extends BlockConductor
{
	public BlockSwitchWireOff(int id, int meta) 
	{
        super(id, Material.cloth);
        this.setBlockName("SwitchWireOff");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setRequiresSelfNotify();
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntitySwitchWireOff();
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return super.idDropped(par1, par2Random, par3);
	}

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public int damageDropped(int i)
    {
    	return i;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
        
    @Override
    public String getTextureFile()
    {
        return EECommonProxy.AITEMS;
    }
   
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
    	if(side == -1)
    		return false;
    	else return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
