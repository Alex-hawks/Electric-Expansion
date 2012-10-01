package additionalcables.blocks;

import java.util.Random;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import additionalcables.AdditionalCables;
import additionalcables.cables.TileEntitySwitchWireOff;

public class BlockSwitchWireOff extends BlockSwitchWire 
{
	public BlockSwitchWireOff(int id, int meta) 
	{
		super(id, meta);
		this.setBlockName("SwitchWireOff");
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntitySwitchWireOff();
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return super.idDropped(par1, par2Random, par3);
	}
}
