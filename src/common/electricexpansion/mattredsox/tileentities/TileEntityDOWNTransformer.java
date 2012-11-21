package electricexpansion.mattredsox.tileentities;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricityManager;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import universalelectricity.core.implement.IJouleStorage;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import electricexpansion.mattredsox.blocks.BlockDOWNTransformer;

public class TileEntityDOWNTransformer extends TileEntityElectricityReceiver implements IJouleStorage, IRedstoneProvider
{	
	private double joules = 0;


	private boolean isFull = false;


	public double voltin;

	public TileEntityDOWNTransformer()
	{
		super();
	}

	@Override
	public double wattRequest()
	{
		if (!this.isDisabled())
		{
			return ElectricInfo.getWatts(this.getMaxJoules()) - ElectricInfo.getWatts(this.joules);
		}

		return 0;
	}

	@Override
	public boolean canReceiveFromSide(ForgeDirection side)
	{return side == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();}

	@Override
	public boolean canConnect(ForgeDirection side)
	{return canReceiveFromSide(side) || side == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);}

	@Override
	public void onReceive(TileEntity sender, double amps, double voltage, ForgeDirection side)
	{ 
		voltin = voltage;
		if(!this.isDisabled())
		{
			this.setJoules(this.joules+ElectricInfo.getJoules(amps, voltage, 1));
		}
	}

	@Override
	public void updateEntity() 
	{
		super.updateEntity();

			//Power redstone if the battery box is full
			boolean isFullThisCheck = false;

			if (this.joules >= this.getMaxJoules())
			{
				isFullThisCheck = true;
			}

			if (this.isFull != isFullThisCheck)
			{
				this.isFull = isFullThisCheck;
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
			}

			//Output electricity
			if (this.joules > 0)
			{
				TileEntity tileEntity = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() + 2));

				TileEntity connector = Vector3.getConnectorFromSide(this.worldObj, Vector3.get(this), ForgeDirection.getOrientation(this.getBlockMetadata() + 2));

                {
                	//Output UE electricity
                	if (connector instanceof IConductor)
					{
						double joulesNeeded = ElectricityManager.instance.getElectricityRequired(((IConductor) connector).getNetwork());
						double transferAmps = Math.max(Math.min(Math.min(ElectricInfo.getAmps(joulesNeeded, this.getVoltage()), ElectricInfo.getAmps(this.joules, this.getVoltage())), 80), 0);
						if (!this.worldObj.isRemote)
						{
							ElectricityManager.instance.produceElectricity(this, (IConductor) connector, transferAmps, this.getVoltage());
						}
						this.setJoules(this.joules - ElectricInfo.getJoules(transferAmps, this.getVoltage()));
                    } 
                }
			}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.joules = par1NBTTagCompound.getDouble("electricityStored");
		this.voltin = par1NBTTagCompound.getDouble("voltIn");
	}
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("electricityStored", this.joules);
		par1NBTTagCompound.setDouble("voltIn", this.voltin);
	}

	public String getInvName()
	{return "Down Transformer";}
	
	@Override
	public boolean isPoweringTo(ForgeDirection side)
	{return this.isFull;}

	@Override
	public boolean isIndirectlyPoweringTo(ForgeDirection side)
	{return isPoweringTo(side);}

	@Override
	public double getJoules(Object... data)
	{return this.joules;}

	@Override
	public void setJoules(double joules, Object... data)
	{this.joules = Math.max(Math.min(joules, this.getMaxJoules()), 0);}

	@Override
	public double getMaxJoules(Object... data)
	{return 1000;}

	@Override
	public double getVoltage() {
		return voltin / 2;
	}

}
