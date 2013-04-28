package electricexpansion.common.blocks;

import java.util.HashMap;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityRedstoneNetworkCore;

public class BlockRedstoneNetworkCore extends BlockAdvanced
{
    private HashMap<String, Icon> icons = new HashMap<String, Icon>();
    
    public BlockRedstoneNetworkCore(int par1)
    {
        super(par1, Material.iron);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("RsNetCore");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons.put("top", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "rsMachine"));
        this.icons.put("out", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "rsMachineOutput"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
            return (side == metadata) ? this.icons.get("out") : this.icons.get("top");
    }

    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
    {
        int angle = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        switch (angle)
        {
            case 0:
                par1World.setBlock(x, y, z, this.blockID, 5, 0);
                break;
            case 1:
                par1World.setBlock(x, y, z, this.blockID, 3, 0);
                break;
            case 2:
                par1World.setBlock(x, y, z, this.blockID, 4, 0);
                break;
            case 3:
                par1World.setBlock(x, y, z, this.blockID, 2, 0);
                break;
        }
        
        ((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
        par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }

    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        
        int change = 0;
        
        // Re-orient the block
        switch (metadata)
        {
            case 0:         // down
                change = 1;
                break;
            case 1:         // up
                change = 2;
                break;
            case 2:         // north
                change = 5;
                break;
            case 3:         // south
                change = 4;
                break;
            case 4:         // west
                change = 0;
                break;
            case 5:         // east
                change = 3;
                break;
        }
        
        par1World.setBlock(x, y, z, this.blockID, change, 0);
        par1World.markBlockForRenderUpdate(x, y, z);
        
        ((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
        
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityRedstoneNetworkCore();
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = this.idPicked(world, x, y, z);
        
        if (id == 0)
            return null;
        
        Item item = Item.itemsList[id];
        if (item == null)
            return null;
        
        return new ItemStack(id, 1, 0);
    }
}
