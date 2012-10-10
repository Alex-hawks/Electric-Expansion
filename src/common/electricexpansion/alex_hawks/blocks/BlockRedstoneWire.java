package electricexpansion.alex_hawks.blocks;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;
import electricexpansion.alex_hawks.cables.TileEntityRedstoneWire;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import universalelectricity.prefab.BlockConductor;

public class BlockRedstoneWire extends BlockConductor 
{
	public BlockRedstoneWire(int id, int meta) 
	{
		super(id, Material.cloth);
        this.setBlockName("RedstoneWire");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setRequiresSelfNotify();
        this.setCreativeTab(CreativeTabs.tabRedstone);
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
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityRedstoneWire();
    }
    
    @Override
    public String getTextureFile()
    {
        return EECommonProxy.AITEMS;
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
