package electricexpansion.common.blocks;

import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityMultimeter;

public class BlockMultimeter extends BlockAdvanced
{
    private HashMap<String, Icon> icons = new HashMap<String, Icon>();
    
    public BlockMultimeter(int id, int textureIndex)
    {
        super(id, UniversalElectricity.machine);
        this.setStepSound(Block.soundMetalFootstep);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("multimeter");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        if (side == 3)
            return this.icons.get("front");
        else
            return this.icons.get("top");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        int metadata = par1IBlockAccess.getBlockMetadata(x, y, z);
        
        if (side == metadata)
            return this.icons.get("output");
        else
            return this.icons.get("top");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons.put("top", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineTop"));
        this.icons.put("output", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineOutput"));
        this.icons.put("machine", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machine"));
        this.icons.put("front", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "multimeter"));
    }
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
    {
        ElectricExpansion.log(Level.WARNING, "Pitch: ", par5EntityLiving.rotationPitch + "");
        
        int angle = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        world.setBlock(x, y, z, this.blockID, angle + 2, 0);
        ((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }
    
    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX,
            float hitY, float hitZ)
    {
        int original = world.getBlockMetadata(x, y, z);
        
        if (++original > 5)
            world.setBlock(x, y, z, this.blockID, 0, 0);
        else 
            world.setBlock(x, y, z, this.blockID, original, 0);
        
        world.markBlockForRenderUpdate(x, y, z);
        ((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
        return true;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public TileEntity createTileEntity(World var1, int metadata)
    {
        return new TileEntityMultimeter();
    }
}
