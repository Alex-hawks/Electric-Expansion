package electricexpansion.alex_hawks.blocks;

import java.util.Random;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.alex_hawks.blocks.BlockSwitchWireBlock;
import electricexpansion.alex_hawks.cables.TileEntitySwitchWireBlockOff;

public class BlockSwitchWireBlockOff extends BlockSwitchWireBlock 
{
	public BlockSwitchWireBlockOff(int id, int meta) {
		super(id, meta);
        this.setBlockName("SwitchWireBlockOff");
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
}
