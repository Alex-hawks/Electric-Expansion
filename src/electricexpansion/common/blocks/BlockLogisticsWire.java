package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.block.BlockConductor;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockLogisticsWire extends BlockConductor
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
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
            List par3List)
    {
        for (int var4 = 0; var4 < 5; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3,
            int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
            float par8, float par9)
    {
        TileEntityLogisticsWire tileEntity = (TileEntityLogisticsWire) par1World
                .getBlockTileEntity(par2, par3, par4);
        
        if (!par1World.isRemote)
        {
            PacketManager.sendPacketToClients(PacketManager.getPacket(
                    ElectricExpansion.CHANNEL, tileEntity, (byte) 3,
                    tileEntity.buttonStatus0, tileEntity.buttonStatus1,
                    tileEntity.buttonStatus2), tileEntity.worldObj,
                    new Vector3(tileEntity), 12);
            
        }
        else
        {
            PacketDispatcher.sendPacketToServer(PacketManager.getPacket(
                    ElectricExpansion.CHANNEL, tileEntity, (byte) 7, true));
            
            par5EntityPlayer.openGui(ElectricExpansion.instance, 3, par1World,
                    par2, par3, par4);
            return true;
        }
        
        return true;
        
    }
    
    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x,
            int y, int z, int side)
    {
        TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);
        
        if (tileEntity instanceof IRedstoneProvider)
            return ((IRedstoneProvider) tileEntity).isPoweringTo(ForgeDirection
                    .getOrientation(side)) ? 15 : 0;
        else
        {
            System.out.println("!tileEntity instanceof IRedstoneProvider");
        }
        
        return 0;
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int x,
            int y, int z, int side)
    {
        TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);
        
        if (tileEntity instanceof IRedstoneProvider)
            return ((IRedstoneProvider) tileEntity)
                    .isIndirectlyPoweringTo(ForgeDirection.getOrientation(side)) ? 15
                    : 0;
        else
        {
            System.out.println("!tileEntity instanceof IRedstoneProvider");
        }
        
        return 0;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess,
            int x, int y, int z)
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
    
}
