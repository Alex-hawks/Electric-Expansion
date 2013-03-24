package electricexpansion.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.prefab.block.BlockConductor;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.helpers.TileEntityConductorBase;
import electricexpansion.common.misc.EETab;

public class BlockInsulatedWire extends BlockConductor
{
	public BlockInsulatedWire(int id, int meta)
	{
		super(id, Material.cloth);
		this.setUnlocalizedName("InsulatedWire");
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
	
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityInsulatedWire();
	}
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 5; var4++)
			par3List.add(new ItemStack(par1, 1, var4));
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
	
	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityInsulatedWire tileEntity = (TileEntityInsulatedWire) par1World.getBlockTileEntity(x, y, z);
		
		if (!par1World.isRemote)
		{
			
			if (par5EntityPlayer.inventory.getCurrentItem() != null)
			{
				if (par5EntityPlayer.inventory.getCurrentItem().getItem() instanceof ItemDye)
				{
					
					int dyeColor = par5EntityPlayer.inventory.getCurrentItem().getItemDamageForDisplay();
					
					tileEntity.colorByte = (byte) dyeColor;
					
					par5EntityPlayer.inventory.getCurrentItem().stackSize = par5EntityPlayer.inventory.getCurrentItem().stackSize - 1;
					
					PacketManager.sendPacketToClients(PacketManager.getPacket(ElectricExpansion.CHANNEL, tileEntity, (byte) 0, tileEntity.colorByte));
					
					((IConductor) tileEntity).updateAdjacentConnections();
					
					this.updateWireSwitch(par1World, x, y, z);
					
					return true;
					
				}
				
			}
			
		}
		
		return false;
		
	}
	
	private void updateWireSwitch(World world, int x, int y, int z)
	{
		TileEntityInsulatedWire tileEntity = (TileEntityInsulatedWire) world.getBlockTileEntity(x, y, z);
		
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
				
				if(tileEntity1 instanceof IConductor)
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
			this.minX = (te.connectedBlocks[4] != null)? 0F 	: 	0.3F;
			this.minY = (te.connectedBlocks[0] != null)? 0F 	: 	0.3F;
			this.minZ = (te.connectedBlocks[2] != null)? 0F 	: 	0.3F;
			this.maxX = (te.connectedBlocks[5] != null)? 1F 	: 	0.7F;
			this.maxY = (te.connectedBlocks[1] != null)? 1F 	: 	0.7F;
			this.maxZ = (te.connectedBlocks[3] != null)? 1F 	: 	0.7F;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		
	}

}
