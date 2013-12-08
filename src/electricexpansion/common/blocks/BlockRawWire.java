package electricexpansion.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.helpers.BlockWireBase;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockRawWire extends BlockWireBase
{
    public BlockRawWire(int id)
    {
        super(id, Material.cloth);
        this.setUnlocalizedName("RawWire");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.1F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setCreativeTab(EETab.INSTANCE);
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
    
//    @Override
//    public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity entity)
//    {
//        if (entity instanceof EntityLiving)
//        {
//            TileEntityRawWire tileEntity = (TileEntityRawWire) par1World.getBlockTileEntity(x, y, z);
//            
//            if (tileEntity.getNetwork().getProduced().getWatts() > 0)
//            {
//                ((EntityLiving) entity).attackEntityFrom(CustomDamageSource.electrocution, EnumWireMaterial.values()[par1World.getBlockMetadata(x, y, z)].electrocutionDamage);
//            }
//        }
//    }
    
    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityRawWire();
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
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
            this.minX = te.getAdjacentConnections()[4] != null ? 0F : 0.3F;
            this.minY = te.getAdjacentConnections()[0] != null ? 0F : 0.3F;
            this.minZ = te.getAdjacentConnections()[2] != null ? 0F : 0.3F;
            this.maxX = te.getAdjacentConnections()[5] != null ? 1F : 0.7F;
            this.maxY = te.getAdjacentConnections()[1] != null ? 1F : 0.7F;
            this.maxZ = te.getAdjacentConnections()[3] != null ? 1F : 0.7F;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {

    }
}
