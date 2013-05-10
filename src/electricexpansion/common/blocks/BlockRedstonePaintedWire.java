package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EENetwork;
import electricexpansion.common.misc.EETab;

public class BlockRedstonePaintedWire extends BlockAdvanced implements ITileEntityProvider
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
            return te.mode ? 0 : ((EENetwork) te.getNetwork()).rsLevel;
        }
        return 0;
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockTileEntity(x, y, z) instanceof TileEntityRedstonePaintedWire)
        {
            TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
            return te.mode ? 0 : ((EENetwork) te.getNetwork()).rsLevel;
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
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 5; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
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
                ((IConductor) tileEntity).updateAdjacentConnections();
                this.updateWireSwitch(world, x, y, z);
            }
        }
        
    }
    
    private void updateWireSwitch(World world, int x, int y, int z)
    {
        TileEntityRedstonePaintedWire tileEntity = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
        
        TileEntity tileEntity1;
        
        if (!world.isRemote && tileEntity != null)
        {
            
            for (byte i = 0; i < 6; i++)
            {
                switch (i)
                {
                    case 0:
                        tileEntity1 = world.getBlockTileEntity(x + 1, y, z);
                        break;
                    case 1:
                        tileEntity1 = world.getBlockTileEntity(x - 1, y, z);
                        break;
                    case 2:
                        tileEntity1 = world.getBlockTileEntity(x, y + 1, z);
                        break;
                    case 3:
                        tileEntity1 = world.getBlockTileEntity(x, y - 1, z);
                        break;
                    case 4:
                        tileEntity1 = world.getBlockTileEntity(x, y, z + 1);
                        break;
                    case 5:
                        tileEntity1 = world.getBlockTileEntity(x, y, z - 1);
                        break;
                    default:
                        tileEntity1 = world.getBlockTileEntity(x, y, z);
                }
                
                if (tileEntity1 instanceof IConductor)
                {
                    ((IConductor) tileEntity1).updateAdjacentConnections();
                    tileEntity1.worldObj.markBlockForUpdate(tileEntity1.xCoord, tileEntity1.yCoord, tileEntity1.zCoord);
                }
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
            this.minX = te.connectedBlocks[4] != null ? 0F : 0.3F;
            this.minY = te.connectedBlocks[0] != null ? 0F : 0.3F;
            this.minZ = te.connectedBlocks[2] != null ? 0F : 0.3F;
            this.maxX = te.connectedBlocks[5] != null ? 1F : 0.7F;
            this.maxY = te.connectedBlocks[1] != null ? 1F : 0.7F;
            this.maxZ = te.connectedBlocks[3] != null ? 1F : 0.7F;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    }
    
    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
        if (ElectricExpansion.debugRecipes)
        {
            if (te.getNetwork() != null)
            {
                player.addChatMessage("NetRsLevel: " + ((EENetwork) te.getNetwork()).rsLevel);
            }
            else 
            {
                player.addChatMessage("NetRsLevel: NETWORK INVALID");
            }
            player.addChatMessage("WldRsLevel: " + world.getBlockPowerInput(x, y, z));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntityRedstonePaintedWire te = (TileEntityRedstonePaintedWire) world.getBlockTileEntity(x, y, z);
        
        te.mode = !te.mode;
        
        if (!world.isRemote)
            player.addChatMessage(StatCollector.translateToLocal("rsCable.message").replace("<>", 
                    (te.mode ? StatCollector.translateToLocal("rsCable.input") : StatCollector.translateToLocal("rsCable.output"))));
        return true;
    }
    
    
}
