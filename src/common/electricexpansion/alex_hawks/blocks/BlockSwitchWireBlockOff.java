package electricexpansion.alex_hawks.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireBlockOff;
import universalelectricity.prefab.BlockConductor;

public class BlockSwitchWireBlockOff extends BlockConductor
{
	public BlockSwitchWireBlockOff(int id, int meta) 
	{
		super(id, Material.rock);
        this.setBlockName("SwitchWireBlock");
        this.setStepSound(soundStoneFootstep);
        this.setResistance(0.2F);
        this.setRequiresSelfNotify();
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setBlockName("SwitchWireBlockOff");
	}
	@Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }
    @Override
    public int damageDropped(int i)
    {
    	return i;
    }
    @Override
    public int getRenderType()
    {
        return 0;
    }
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
    	int texture = 255;
    	if(side == 1)
    	{
        	if(meta == 0)
        		texture = 16;
        	if(meta == 1)
        		texture = 17;    	
        	if(meta == 2)
        		texture = 18;
        	if(meta == 3)
        		texture = 19;
    	}
    	else if(side == 0)
    	{
        	if(meta == 0)
        		texture = 32;
        	if(meta == 1)
        		texture = 33;    	
        	if(meta == 2)
        		texture = 34;
        	if(meta == 3)
        		texture = 35;
    	}
    	else
    	{
    		if(meta == 0)
    			texture = 0;
    		if(meta == 1)
    			texture = 1;    	
    		if(meta == 2)
    			texture = 2;
    		if(meta == 3)
    			texture = 3;
    	}
    	if(texture != 255)
    		texture = texture + 64;
    	return texture;
    }
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntitySwitchWireBlockOff();
	}
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return super.idDropped(par1, par2Random, par3);
	}
	@Override
	public int idPicked(World par1World, int par2, int par3, int par4)
	{
		return super.idPicked(par1World, par2, par3, par4);
	}
    @Override
    public String getTextureFile()
    {
        return EECommonProxy.ABLOCK;
    }
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
    	if(side == -1)
    		return false;
    	else return true;
    }
}
