package electricexpansion.additionalcables.blocks;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;import electricexpansion.additionalcables.cables.TileEntityRedstoneWireBlock;
import electricexpansion.additionalcables.cables.TileEntityWireBlock;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import universalelectricity.prefab.BlockConductor;

public class BlockRedstoneWireBlock extends BlockConductor 
{
	public BlockRedstoneWireBlock(int id, int meta)
    {
        super(id, Material.rock);
        this.setBlockName("HiddenRedstoneWire");
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
        return new TileEntityRedstoneWireBlock();
    }
    
    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir)
    {
            return true;
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
