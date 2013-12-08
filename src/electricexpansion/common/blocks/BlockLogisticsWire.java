package electricexpansion.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.helpers.BlockWireBase;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockLogisticsWire extends BlockWireBase
{
    public BlockLogisticsWire(int id, int meta)
    {
        super(id, Material.cloth);
        this.setUnlocalizedName("LogisticsWire");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.1F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setCreativeTab(EETab.INSTANCE);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
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
        return new TileEntityLogisticsWire();
    }
    
    @Override
    public boolean canProvidePower()
    {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z)
    {
        TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityConductorBase)
        {
            TileEntityConductorBase te = (TileEntityConductorBase) tileEntity;
            boolean[] connections = te.getVisualConnections();
            this.minX = connections[4] ? 0F : 0.3F;
            this.minY = connections[0] ? 0F : 0.3F;
            this.minZ = connections[2] ? 0F : 0.3F;
            this.maxX = connections[5] ? 1F : 0.7F;
            this.maxY = connections[1] ? 1F : 0.7F;
            this.maxZ = connections[3] ? 1F : 0.7F;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        
    }
    
}
