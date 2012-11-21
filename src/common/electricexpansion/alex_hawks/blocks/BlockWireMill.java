package electricexpansion.alex_hawks.blocks;

import universalelectricity.prefab.BlockMachine;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import electricexpansion.EECommonProxy;
import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.machines.TileEntityWireMill;

public class BlockWireMill extends BlockMachine
{
	private int orientation;
	public BlockWireMill(int par1)
	{
		super("WireMill", par1, Material.iron);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setTextureFile(EECommonProxy.ABLOCK);
	}
	
	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		if(this.orientation == 4)
			this.orientation = 0;
		this.orientation++;
		return true;
	}

	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		if (!par1World.isRemote)
		{
			par5EntityPlayer.openGui(ElectricExpansion.instance, 2, par1World, x, y, z);
			return true;
		}

		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityWireMill();
	}
	
	@Override
	public int getBlockTextureFromSide(int side)
	{
		if(side == 1)
			return 6;
		if(side == orientation + 2)
			return 8;
		return 7;	
	}

}