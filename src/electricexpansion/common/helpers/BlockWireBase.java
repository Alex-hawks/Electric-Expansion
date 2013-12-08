package electricexpansion.common.helpers;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.prefab.block.BlockConductor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.misc.EnumWireFrequency;
import electricexpansion.common.misc.ItemUtils;

public abstract class BlockWireBase extends BlockConductor
{

    public BlockWireBase(int id, Material material)
    {
        super(id, material);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
                ((IConductor) tileEntity).refresh();
                this.updateWireSwitch(world, x, y, z);
            }
        }
        
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityConductorBase tileEntity = (TileEntityConductorBase) par1World.getBlockTileEntity(x, y, z);
        
        if (!par1World.isRemote)
        {
            if (player.inventory.getCurrentItem() != null)
            {
                EnumWireFrequency freq = ItemUtils.isDye(player.inventory.getCurrentItem());
                if (freq != EnumWireFrequency.NONE)
                {
                    tileEntity.setFrequency(freq);
                    
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.getCurrentItem().stackSize = player.inventory.getCurrentItem().stackSize - 1;
                    
                    ((IConductor) tileEntity).refresh();
                    
                    this.updateWireSwitch(par1World, x, y, z);
                    
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void updateWireSwitch(World world, int x, int y, int z)
    {
        TileEntityConductorBase tileEntity = (TileEntityConductorBase) world.getBlockTileEntity(x, y, z);
        
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
                    ((IConductor) tileEntity1).refresh();
                    tileEntity1.worldObj.markBlockForUpdate(tileEntity1.xCoord, tileEntity1.yCoord, tileEntity1.zCoord);
                }
            }
        }
    }

}
