package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.client.ClientProxy;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityTransformer;

public class BlockTransformer extends BlockAdvanced
{
    // TODO rewrite for changes to the TileEntity class
    public BlockTransformer(int id)
    {
        super(id, UniversalElectricity.machine);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("transformer");
    }
        
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityTransformer(metadata);
    }
    
    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && world.getBlockTileEntity(x, y, z) instanceof TileEntityTransformer)
        {
            TileEntityTransformer te = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
            if (side > 1)
                if (hitY > 0.5)
                    te.setOutput(ForgeDirection.getOrientation(side));
                else
                    te.setInput(ForgeDirection.getOrientation(side));
            else
                if (hitZ > 0.5)
                    te.setOutput(ForgeDirection.getOrientation(side));
                else
                    te.setInput(ForgeDirection.getOrientation(side));
        }
        return true;
    }
    
    @Override
    public boolean onSneakUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!par1World.isRemote)
        {
            TileEntityTransformer tileEntity = (TileEntityTransformer) par1World.getBlockTileEntity(x, y, z);
            
            tileEntity.stepUp = !tileEntity.stepUp;
        }
        
        return true;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.RENDER_ID;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 3; i++)
        {
            par3List.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        
        return new ItemStack(ElectricExpansionItems.blockTransformer, 1, metadata);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(ElectricExpansion.PREFIX + "darkMachine");
    }
    
    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
    
    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }
}