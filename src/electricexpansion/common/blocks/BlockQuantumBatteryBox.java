package electricexpansion.common.blocks;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;

public class BlockQuantumBatteryBox extends BlockAdvanced
{
    private HashMap<String, Icon> icons = new HashMap<String, Icon>();
    
    public BlockQuantumBatteryBox(int id)
    {
        super(id, Material.iron);
        this.setUnlocalizedName("Distribution");
        this.setStepSound(soundMetalFootstep);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setCreativeTab(EETab.INSTANCE);
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    @Override
    public int damageDropped(int i)
    {
        return 0;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }
    
    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == metadata + 2)
            return this.icons.get("output");
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            return this.icons.get("input");
        else
            return this.icons.get("default");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons.put("output", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "darkMachineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "darkMachineInput"));
        this.icons.put("default", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "darkMachineTop"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }
    
    @Override
    public TileEntity createTileEntity(World var1, int meta)
    {
        return new TileEntityQuantumBatteryBox();
    }
    
    /**
     * Called when the block is right clicked by the player
     */
    
    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (!par1World.isRemote)
        {
            boolean isPlayerOp = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().areCommandsAllowed(par5EntityPlayer.getCommandSenderName());
            if (par5EntityPlayer.username == ((TileEntityQuantumBatteryBox) par1World.getBlockTileEntity(x, y, z)).getOwningPlayer() || isPlayerOp)
            {
                par5EntityPlayer.openGui(ElectricExpansion.instance, 4, par1World, x, y, z);
                return true;
            }
            return true;
        }
        return true;
    }
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack itemStack)
    {
        int angle = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        
        switch (angle)
        {
            case 0:
                par1World.setBlock(x, y, z, this.blockID, 3, 0);
                break;
            case 1:
                par1World.setBlock(x, y, z, this.blockID, 1, 0);
                break;
            case 2:
                par1World.setBlock(x, y, z, this.blockID, 2, 0);
                break;
            case 3:
                par1World.setBlock(x, y, z, this.blockID, 0, 0);
                break;
        }
        
        if (par5EntityLiving instanceof EntityPlayer && (TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z) instanceof TileEntityQuantumBatteryBox)
        {
            ((TileEntityQuantumBatteryBox) par1World.getBlockTileEntity(x, y, z)).setPlayer((EntityPlayer) par5EntityLiving);
        }
        
        ((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
        par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
    }
    
    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        
        int change = 0;
        
        // Re-orient the block
        switch (metadata)
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
    
    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }
    
    public int getComparatorInputOverride(World par1World, int x, int y, int z, int meta)
    {
        
        TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);
        
        if (tileEntity instanceof TileEntityQuantumBatteryBox)
        {
            TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) tileEntity;
            double max = te.getMaxJoules();
            double current = te.getJoules();
            return (int) (current / max * 15);
        }
        
        return 0;
    }
}