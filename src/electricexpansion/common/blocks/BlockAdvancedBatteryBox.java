package electricexpansion.common.blocks;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.items.ItemLinkCard;
import electricexpansion.common.misc.EETab;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.misc.EnumAdvBattBoxMode;

public class BlockAdvancedBatteryBox extends BlockAdvanced
{
    private HashMap<String, Icon> icons = new HashMap<String, Icon>();
    
    public BlockAdvancedBatteryBox(int id, int textureIndex)
    {
        super(id, UniversalElectricity.machine);
        this.setStepSound(soundMetalFootstep);
        this.setCreativeTab(EETab.INSTANCE);
        this.setUnlocalizedName("advbatbox");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons.put("top", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineTop"));
        this.icons.put("out", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineOutput"));
        this.icons.put("input", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "machineInput"));
        this.icons.put("tier1", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "batBoxT1"));
        this.icons.put("tier2", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "batBoxT2"));
        this.icons.put("tier3", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "batBoxT3"));
        this.icons.put("tier4", par1IconRegister.registerIcon(ElectricExpansion.TEXTURE_NAME_PREFIX + "batBoxT4"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) iBlockAccess.getBlockTileEntity(x, y, z);
        if (side == te.getOutput().ordinal() && te.getOutputMode() != EnumAdvBattBoxMode.OFF && te.getOutputMode() != EnumAdvBattBoxMode.QUANTUM)
            return this.icons.get("out");
        else if (side == te.getInput().ordinal() && te.getInputMode() != EnumAdvBattBoxMode.OFF && te.getInputMode() != EnumAdvBattBoxMode.QUANTUM)
            return this.icons.get("input");
        
        if (side == 0 || side == 1)
            return this.icons.get("top");
        
        if (te.getMaxJoules() <= 8000000)
            return this.icons.get("tier1");
        else if (te.getMaxJoules() <= 12000000)
            return this.icons.get("tier2");
        else if (te.getMaxJoules() <= 16000000)
            return this.icons.get("tier3");
        else if (te.getMaxJoules() > 16000000)
            return this.icons.get("tier4");
        
        return this.icons.get("tier1");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
            return this.icons.get("top");
        else if (side == metadata + 2)
            return this.icons.get("out");
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            return this.icons.get("input");
        else
            return this.icons.get("tier1");
    }
    
    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemLinkCard)
            {
                ItemStack is = player.getCurrentEquippedItem();
                if (((ItemLinkCard) is.getItem()).getHasLinkData(is))
                {
                    TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) world.getBlockTileEntity(x, y, z);
                    if (!te.isLinkCardValid(is))
                    {
                        te.setLinkCard(is);
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }
                }
            }
            else
            {
                player.openGui(ElectricExpansion.instance, 0, world, x, y, z);
            }
        }
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
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityAdvancedBatteryBox();
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
        
        if (tileEntity instanceof TileEntityAdvancedBatteryBox)
        {
            TileEntityAdvancedBatteryBox te = (TileEntityAdvancedBatteryBox) tileEntity;
            double max = te.getMaxJoules();
            double current = te.getJoules();
            return (int) (current / max * 15);
        }
        
        return 0;
    }
}
