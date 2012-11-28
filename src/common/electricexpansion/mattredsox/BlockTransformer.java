package net.minecraft.src.Transformer;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import universalelectricity.prefab.implement.IRotatable;
import basiccomponents.BasicComponents;
import electricexpansion.client.mattredsox.GuiTransformer;
import electricexpansion.mattredsox.tileentities.TileEntityAdvBatteryBox;

public class BlockTransformer extends BlockMachine {
	
	public static final int meta = 0;

	
	public BlockTransformer(int id) {
		super("Transformer", id, Material.wood);
		this.setStepSound(soundMetalFootstep);
		this.setRequiresSelfNotify();
	}
	

	@Override
    public boolean canBlockStay(World par1World, int x, int y, int z)
    {
        if (par1World.getBlockMetadata(x, y, z) == 0)
        {
            return true;
        }
        else if (par1World.getBlockMetadata(x, y, z) == 1 && par1World.getBlockTileEntity(x, y - 1, z) != null && par1World.getBlockMetadata(x, y - 1, z) == 0 && par1World.getBlockTileEntity(x, y - 1, z) instanceof TileEntityTransformer)
        {
            return true;
        }
        else if (par1World.getBlockMetadata(x, y, z) == 2 && par1World.getBlockTileEntity(x, y - 2, z) != null && par1World.getBlockMetadata(x, y - 2, z) == 0 && par1World.getBlockTileEntity(x, y - 2, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y, z) == 2 && par1World.getBlockTileEntity(x, y - 1, z) != null && par1World.getBlockMetadata(x, y - 1, z) == 1 && par1World.getBlockTileEntity(x, y - 1, z) instanceof TileEntityTransformer)
        {
            return true;
        }else
        {
            return false;
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
			return new TileEntityTransformer();
	}
/*	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
		float var9 = 0.375F;
		float var10 = 0.625F;
		if (UniversalElectricity.getOrientationFromSide(((TileEntityTransformer)getBlockEntity()).facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(((TileEntityTransformer)getBlockEntity()).facingDirection, (byte) 2) == 2) {
			return AxisAlignedBB.getBoundingBox((double) ((float) x + var9), (double) y, (double) z, (double) ((float) x + var10), (double) ((float) y + 0.9F), (double) ((float) z + 1.0F));
		}else{
			return AxisAlignedBB.getBoundingBox((double) x, (double) y, (double) ((float) z + var9), (double) ((float) x + 1.0F), (double) ((float) y + 0.9F), (double) ((float) z + var9));
		}
	}*/

	@Override
	public int getRenderType() {
		return TransformerCore.TransformerRenderID;
	}

	@Override
	public String getTextureFile() {
		return BasicComponents.blockTextureFile;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return TransformerCore.itemtransformer.shiftedIndex;
	}

	public void isBeingPowered(World par1World, int x, int y, int z) {
		TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneReceptor) {
			if (par1World.isBlockGettingPowered(x, y, z) || par1World.isBlockIndirectlyGettingPowered(x, y, z)) {
				((IRedstoneReceptor) tileEntity).onPowerOn();
			} else {
				((IRedstoneReceptor) tileEntity).onPowerOff();
			}
		}
	}
    
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

    @Override
	public boolean machineActivated(World par1World, int x, int y, int z,
			EntityPlayer par5EntityPlayer) {
		if (!par1World.isRemote) {

			TileEntity tileEntity = par1World.getBlockTileEntity(x, y, z);

			if (tileEntity != null && !par5EntityPlayer.isSneaking() && !(par5EntityPlayer.getCurrentEquippedItem() != null && (par5EntityPlayer.getCurrentEquippedItem().isItemEqual(new ItemStack(TransformerCore.itemtransformer, 1, 1)) || par5EntityPlayer.getCurrentEquippedItem().isItemEqual(new ItemStack(TransformerCore.itemtransformer, 1, 2)))) && par1World.getBlockMetadata(x, y, z) == 0) {
			if(par1World.getBlockTileEntity(x, y + 2, z)  != null && par1World.getBlockTileEntity(x, y + 2, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y + 2, z) == 2)
			{
				ModLoader.openGUI(par5EntityPlayer, new GuiTransformer((TileEntityTransformer) par1World.getBlockTileEntity(x, y + 2, z)));
			}else if(par1World.getBlockTileEntity(x, y + 1, z)  != null && par1World.getBlockTileEntity(x, y + 1, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y + 1, z) == 1)
			{
				ModLoader.openGUI(par5EntityPlayer, new GuiTransformer((TileEntityTransformer) par1World.getBlockTileEntity(x, y + 1, z)));
			}else{
				ModLoader.openGUI(par5EntityPlayer, new GuiTransformer((TileEntityTransformer) tileEntity));
			}
			return true;
			}
		}
		

		return false;
	}

	@Override
	public void onBlockAdded(World par1World, int x, int y, int z) {
		this.isBeingPowered(par1World, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving) {
		
		IRotatable tileEntity = (IRotatable) par1World.getBlockTileEntity(x, y, z);
		IRotatable tileEntity2;
		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if(par1World.getBlockTileEntity(x, y - 1, z)  != null && par1World.getBlockTileEntity(x, y - 1, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y - 1, z) == 0)
		{
			tileEntity2 = (IRotatable) par1World.getBlockTileEntity(x, y - 1, z);
			tileEntity.setDirection(tileEntity2.getDirection());

		}else if(par1World.getBlockTileEntity(x, y - 2, z)  != null && par1World.getBlockTileEntity(x, y - 2, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y - 2, z) == 0)
		{
			tileEntity2 = (IRotatable) par1World.getBlockTileEntity(x, y - 2, z);
			tileEntity.setDirection(tileEntity2.getDirection());

		}else{
			tileEntity = (IRotatable) par1World.getBlockTileEntity(x, y, z);
			switch (angle) {
			case 0:
				tileEntity.setDirection((byte) 5);
				break;
			case 1:
				tileEntity.setDirection((byte) 3);
				break;
			case 2:
				tileEntity.setDirection((byte) 4);
				break;
			case 3:
				tileEntity.setDirection((byte) 2);
				break;
			}
		}		
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

	}

	@Override
	public void onNeighborBlockChange(World par1World, int x, int y, int z, int par5) {
        if (!this.canBlockStay(par1World, x, y, z))
        {
            this.dropBlockAsItem(par1World, x, y, z, par1World.getBlockMetadata(x, y, z), 0);
            par1World.setBlockWithNotify(x, y, z, 0);
        }
		this.isBeingPowered(par1World, x, y, z);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
		if(par1World.getBlockMetadata(x, y, z) != 0)
		{
			return false;
		}
			
		IRotatable tileEntity = (IRotatable) par1World.getBlockTileEntity(x, y, z);

		if(par1World.getBlockTileEntity(x, y + 1, z)  != null && par1World.getBlockTileEntity(x, y + 1, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y + 1, z) == 1)
		{
			IRotatable tileEntity1 = (IRotatable) par1World.getBlockTileEntity(x, y + 1, z);
			switch (tileEntity.getDirection()) {
			case 2:
				tileEntity1.setDirection((byte) 5);
				break;
			case 5:
				tileEntity1.setDirection((byte) 3);
				break;
			case 3:
				tileEntity1.setDirection((byte) 4);
				break;
			case 4:
				tileEntity1.setDirection((byte) 2);
				break;
			}
			par1World.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			par1World.markBlockNeedsUpdate(x, y + 1, z);
		}
		
		if(par1World.getBlockTileEntity(x, y + 2, z)  != null && par1World.getBlockTileEntity(x, y + 2, z) instanceof TileEntityTransformer && par1World.getBlockMetadata(x, y + 2, z) == 2)
		{
			IRotatable tileEntity2 = (IRotatable) par1World.getBlockTileEntity(x, y + 2, z);
			switch (tileEntity.getDirection()) {
			case 2:
				tileEntity2.setDirection((byte) 5);
				break;
			case 5:
				tileEntity2.setDirection((byte) 3);
				break;
			case 3:
				tileEntity2.setDirection((byte) 4);
				break;
			case 4:
				tileEntity2.setDirection((byte) 2);
				break;
			}
			par1World.notifyBlocksOfNeighborChange(x, y + 2, z, this.blockID);
			par1World.markBlockNeedsUpdate(x, y + 2, z);
		}
		
		switch (tileEntity.getDirection()) {
		case 2:
			tileEntity.setDirection((byte) 5);
			break;
		case 5:
			tileEntity.setDirection((byte) 3);
			break;
		case 3:
			tileEntity.setDirection((byte) 4);
			break;
		case 4:
			tileEntity.setDirection((byte) 2);
			break;
		}

		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		par1World.markBlockNeedsUpdate(x, y, z);

		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
