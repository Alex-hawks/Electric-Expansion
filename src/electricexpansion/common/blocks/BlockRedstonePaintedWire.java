package electricexpansion.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.helpers.BlockWireBase;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockRedstonePaintedWire extends BlockWireBase implements ITileEntityProvider
{   
    public BlockRedstonePaintedWire(int id)
    {
        super(id, Material.cloth);
        this.setUnlocalizedName("RedstonePaintedWire");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.1F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setCreativeTab(EETab.INSTANCE);
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }
    
    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockTileEntity(x, y, z) instanceof TileEntityRedstonePaintedWire)
        {
            TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
            return te.getRsLevel();
        }
        return 0;
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockTileEntity(x, y, z) instanceof TileEntityRedstonePaintedWire)
        {
            TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
            return te.getIrsLevel();
        }
        return 0;
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
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityRedstonePaintedWire();
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        
        if (tileEntity != null)
        {
            if (tileEntity instanceof IConductor)
            {
                ((IConductor) tileEntity).refresh();
                this.updateWireSwitch(world, x, y, z);
            }
        }
        
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z)
    {
        TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityConductorBase)
        {
            TileEntityConductorBase te = (TileEntityConductorBase) tileEntity;
            this.minX = te.getAdjacentConnections()[4] != null ? 0F : 0.3F;
            this.minY = te.getAdjacentConnections()[0] != null ? 0F : 0.3F;
            this.minZ = te.getAdjacentConnections()[2] != null ? 0F : 0.3F;
            this.maxX = te.getAdjacentConnections()[5] != null ? 1F : 0.7F;
            this.maxY = te.getAdjacentConnections()[1] != null ? 1F : 0.7F;
            this.maxZ = te.getAdjacentConnections()[3] != null ? 1F : 0.7F;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    }
        
}
