package electricexpansion.common.blocks;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityFuseBox;

public class BlockFuseBox extends BlockAdvanced
{
    private HashMap<String, Icon> icons = new HashMap<String, Icon>();
    
    public BlockFuseBox(int id)
    {
        super(id, UniversalElectricity.machine);
        this.setUnlocalizedName("FuseBox");
        this.setCreativeTab(EETab.INSTANCE);
        this.setStepSound(soundMetalFootstep);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        int metadata = iBlockAccess.getBlockMetadata(x, y, z);
        
        if (side == 0 || side == 1)
            return this.icons.get("top");
        else if (side == metadata + 2)
            return this.icons.get("output");
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            return this.icons.get("input");
        else
            return this.icons.get("side");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons.put("top", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineTop"));
        this.icons
                .put("output", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineInput"));
        this.icons.put("side", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "fusebox"));
        // TODO Create above texture...
    }
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player, ItemStack itemStack)
    {
        int angle = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        switch (angle)
        {
            case 0:
                world.setBlock(x, y, z, this.blockID, 3, 0);
                break;
            case 1:
                world.setBlock(x, y, z, this.blockID, 1, 0);
                break;
            case 2:
                world.setBlock(x, y, z, this.blockID, 2, 0);
                break;
            case 3:
                world.setBlock(x, y, z, this.blockID, 0, 0);
                break;
        }
        
        ((TileEntityAdvanced) world.getBlockTileEntity(x, y, z)).initiate();
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }
    
    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        int original = metadata;
        
        int change = 0;
        
        // Re-orient the block
        switch (original)
        {
            case 0:
                change = 3;
                break;
            case 3:
                change = 1;
                break;
            case 1:
                change = 2;
                break;
            case 2:
                change = 0;
                break;
        }
        
        par1World.setBlock(x, y, z, this.blockID, change, 0);
        par1World.markBlockForRenderUpdate(x, y, z);
        
        ((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
        
        return true;
    }
    
    @Override
    public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World var1, int metadata)
    {
        return new TileEntityFuseBox();
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this.blockID, 1, 0));
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = this.idPicked(world, x, y, z);
        
        if (id == 0)
            return null;
        
        return new ItemStack(id, 1, 0);
    }
    
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side,
            float hitX, float hitY, float hitZ)
    {
        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(ElectricExpansion.instance, 6, par1World, x, y, z);
            return true;
        }
        
        return true;
    }
}