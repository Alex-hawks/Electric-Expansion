package electricexpansion.additionalcables.blocks;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import java.lang.String;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import universalelectricity.prefab.BlockConductor;

import electricexpansion.EECommonProxy;
import electricexpansion.additionalcables.cables.TileEntityRawWire;
import electricexpansion.additionalcables.cables.TileEntityWireBlock;

public class BlockWireBlock extends BlockConductor
{
	public BlockWireBlock(int id, int meta)
    {
        super(id, Material.rock);
        this.setBlockName("HiddenWire");
        this.setStepSound(soundStoneFootstep);
        this.setResistance(0.2F);
        this.setRequiresSelfNotify();
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    @Override
    public int damageDropped(int i)
    {
    	return i;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }
    
    @Override
    public int getRenderType()
    {
        return 0;
    }
    
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityWireBlock();
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
    
    public String getTextureFile()
    {
        return EECommonProxy.BLOCK;
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
