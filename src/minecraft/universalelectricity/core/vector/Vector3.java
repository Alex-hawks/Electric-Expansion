package universalelectricity.core.vector;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityConnections;

/**
 * Vector3 Class is used for defining objects in a 3D space. Vector3 makes it easier to handle the
 * coordinates of objects. Instead of fumbling with x, y and z variables, all x, y and z variables
 * are stored in one class. Vector3.x, Vector3.y, Vector3.z.
 * 
 * @author Calclavia
 */

public class Vector3 extends Vector2 implements Cloneable
{
	public double z;

	public Vector3()
	{
		this(0, 0, 0);
	}

	public Vector3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Entity par1)
	{
		this.x = par1.posX;
		this.y = par1.posY;
		this.z = par1.posZ;
	}

	public Vector3(TileEntity par1)
	{
		this.x = par1.xCoord;
		this.y = par1.yCoord;
		this.z = par1.zCoord;
	}

	public Vector3(Vec3 par1)
	{
		this.x = par1.xCoord;
		this.y = par1.yCoord;
		this.z = par1.zCoord;
	}

	public Vector3(MovingObjectPosition par1)
	{
		this.x = par1.blockX;
		this.y = par1.blockY;
		this.z = par1.blockZ;
	}

	public Vector3(ChunkCoordinates par1)
	{
		this.x = par1.posX;
		this.y = par1.posY;
		this.z = par1.posZ;
	}

	/**
	 * Returns the coordinates as integers
	 */
	public int intX()
	{
		return (int) Math.floor(this.x);
	}

	public int intY()
	{
		return (int) Math.floor(this.y);
	}

	public int intZ()
	{
		return (int) Math.floor(this.z);
	}

	public boolean isEquals(Vector3 vector)
	{
		return this.x == vector.x && this.y == vector.y && this.z == vector.z;
	}

	/**
	 * Makes a new copy of this Vector. Prevents variable referencing problems.
	 */
	@Override
	public Vector3 clone()
	{
		return new Vector3(this.x, this.y, this.z);
	}

	@Deprecated
	public static Vector3 get(Entity par1)
	{
		return new Vector3(par1);
	}

	@Deprecated
	public static Vector3 get(TileEntity par1)
	{
		return new Vector3(par1);
	}

	@Deprecated
	public static Vector3 get(Vec3 par1)
	{
		return new Vector3(par1);
	}

	@Deprecated
	public static Vector3 get(MovingObjectPosition par1)
	{
		return new Vector3(par1);
	}

	@Deprecated
	public static Vector3 get(ChunkCoordinates par1)
	{
		return new Vector3(par1);
	}

	public int getBlockID(IBlockAccess world)
	{
		return world.getBlockId(this.intX(), this.intY(), this.intZ());
	}

	public int getBlockMetadata(IBlockAccess world)
	{
		return world.getBlockMetadata(this.intX(), this.intY(), this.intZ());
	}

	public TileEntity getTileEntity(IBlockAccess world)
	{
		return world.getBlockTileEntity(this.intX(), this.intY(), this.intZ());
	}

	public void setBlock(World world, int id, int metadata)
	{
		world.setBlockAndMetadata(this.intX(), this.intY(), this.intZ(), id, metadata);
	}

	public void setBlock(World world, int id)
	{
		world.setBlock(this.intX(), this.intY(), this.intZ(), id);
	}

	public void setBlockWithNotify(World world, int id, int metadata)
	{
		world.setBlockAndMetadataWithNotify(this.intX(), this.intY(), this.intZ(), id, metadata);
	}

	public void setBlockWithNotify(World world, int id)
	{
		world.setBlockWithNotify(this.intX(), this.intY(), this.intZ(), id);
	}

	/**
	 * Converts this Vector3 into a Vector2 by dropping the Y axis.
	 */
	public Vector2 toVector2()
	{
		return new Vector2(this.x, this.z);
	}

	/**
	 * Converts this vector three into a Minecraft Vec3 object
	 */
	public Vec3 toVec3()
	{
		return Vec3.createVectorHelper(this.x, this.y, this.z);
	}

	/**
	 * Compares two vectors and see if they are equal. True if so.
	 */
	public boolean isEqual(Vector3 vector3)
	{
		return (this.x == vector3.x && this.y == vector3.y && this.z == vector3.z);
	}

	/**
	 * Gets the distance between two vectors
	 * 
	 * @return The distance
	 */
	public static double distance(Vector3 par1, Vector3 par2)
	{
		double var2 = par1.x - par2.x;
		double var4 = par1.y - par2.y;
		double var6 = par1.z - par2.z;
		return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
	}

	public double distanceTo(Vector3 vector3)
	{
		double var2 = vector3.x - this.x;
		double var4 = vector3.y - this.y;
		double var6 = vector3.z - this.z;
		return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
	}

	public static Vector3 subtract(Vector3 par1, Vector3 par2)
	{
		return new Vector3(par1.x - par2.x, par1.y - par2.y, par1.z - par2.z);
	}

	public static Vector3 add(Vector3 par1, Vector3 par2)
	{
		return new Vector3(par1.x + par2.x, par1.y + par2.y, par1.z + par2.z);
	}

	public static Vector3 add(Vector3 par1, double par2)
	{
		return new Vector3(par1.x + par2, par1.y + par2, par1.z + par2);
	}

	public void add(Vector3 par1)
	{
		this.x += par1.x;
		this.y += par1.y;
		this.z += par1.z;
	}

	@Override
	public void add(double par1)
	{
		this.x += par1;
		this.y += par1;
		this.z += par1;
	}

	public void subtract(Vector3 amount)
	{
		this.x -= amount.x;
		this.y -= amount.y;
		this.z -= amount.z;
	}

	public void multiply(double amount)
	{
		this.x *= amount;
		this.y *= amount;
		this.z *= amount;
	}

	public void multiply(Vector3 vec)
	{
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
	}

	public static Vector3 multiply(Vector3 vec1, Vector3 vec2)
	{
		return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
	}

	public static Vector3 multiply(Vector3 vec1, double vec2)
	{
		return new Vector3(vec1.x * vec2, vec1.y * vec2, vec1.z * vec2);
	}

	public static Vector3 readFromNBT(String prefix, NBTTagCompound par1NBTTagCompound)
	{
		Vector3 tempVector = new Vector3();
		tempVector.x = par1NBTTagCompound.getDouble(prefix + "X");
		tempVector.y = par1NBTTagCompound.getDouble(prefix + "Y");
		tempVector.z = par1NBTTagCompound.getDouble(prefix + "Z");
		return tempVector;
	}

	/**
	 * Saves this Vector3 to disk
	 * 
	 * @param prefix - The prefix of this save. Use some unique string.
	 * @param par1NBTTagCompound - The NBT compound object to save the data in
	 */
	public void writeToNBT(String prefix, NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setDouble(prefix + "X", this.x);
		par1NBTTagCompound.setDouble(prefix + "Y", this.y);
		par1NBTTagCompound.setDouble(prefix + "Z", this.z);
	}

	@Override
	public Vector3 round()
	{
		return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
	}

	@Override
	public Vector3 floor()
	{
		return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
	}

	/**
	 * Gets all entities inside of this position in block space.
	 */
	public List<Entity> getEntitiesWithin(World worldObj, Class<? extends Entity> par1Class)
	{
		return (List<Entity>) worldObj.getEntitiesWithinAABB(par1Class, AxisAlignedBB.getBoundingBox(this.intX(), this.intY(), this.intZ(), this.intX() + 1, this.intY() + 1, this.intZ() + 1));
	}

	/**
	 * Gets a position relative to another position's side
	 * 
	 * @param position - The position
	 * @param side - The side. 0-5
	 * @return The position relative to the original position's side
	 */
	public void modifyPositionFromSide(ForgeDirection side, double amount)
	{
		switch (side.ordinal())
		{
			case 0:
				this.y -= amount;
				break;
			case 1:
				this.y += amount;
				break;
			case 2:
				this.z -= amount;
				break;
			case 3:
				this.z += amount;
				break;
			case 4:
				this.x -= amount;
				break;
			case 5:
				this.x += amount;
				break;
		}
	}

	public void modifyPositionFromSide(ForgeDirection side)
	{
		this.modifyPositionFromSide(side, 1);
	}

	public static TileEntity getTileEntityFromSide(World world, Vector3 position, ForgeDirection side)
	{
		position.modifyPositionFromSide(side);
		return world.getBlockTileEntity(position.intX(), position.intY(), position.intZ());
	}

	/**
	 * Gets a connector unit based on the given side.
	 */
	public static TileEntity getConnectorFromSide(World world, Vector3 position, ForgeDirection side)
	{
		TileEntity tileEntity = getTileEntityFromSide(world, position, side);

		if (ElectricityConnections.isConnector(tileEntity))
		{
			if (ElectricityConnections.canConnect(tileEntity, getOrientationFromSide(side, ForgeDirection.NORTH))) { return tileEntity; }
		}

		return null;
	}

	/**
	 * Finds the side of a block depending on it's facing direction from the given side. The side
	 * numbers are compatible with the function "getBlockTextureFromSideAndMetadata".
	 * 
	 * Bottom: 0; Top: 1; Back: 2; Front: 3; Left: 4; Right: 5;
	 * 
	 * @param front - The direction in which this block is facing/front. Use a number between 0 and
	 * 5. Default is 3.
	 * @param side - The side you are trying to find. A number between 0 and 5.
	 * @return The side relative to the facing direction.
	 */

	public static ForgeDirection getOrientationFromSide(ForgeDirection front, ForgeDirection side)
	{
		switch (front.ordinal())
		{
			case 0:
				switch (side.ordinal())
				{
					case 0:
						return ForgeDirection.getOrientation(3);
					case 1:
						return ForgeDirection.getOrientation(2);
					case 2:
						return ForgeDirection.getOrientation(1);
					case 3:
						return ForgeDirection.getOrientation(0);
					case 4:
						return ForgeDirection.getOrientation(5);
					case 5:
						return ForgeDirection.getOrientation(4);
				}

			case 1:
				switch (side.ordinal())
				{
					case 0:
						return ForgeDirection.getOrientation(4);
					case 1:
						return ForgeDirection.getOrientation(5);
					case 2:
						return ForgeDirection.getOrientation(0);
					case 3:
						return ForgeDirection.getOrientation(1);
					case 4:
						return ForgeDirection.getOrientation(2);
					case 5:
						return ForgeDirection.getOrientation(3);
				}

			case 2:
				switch (side.ordinal())
				{
					case 0:
						return ForgeDirection.getOrientation(0);
					case 1:
						return ForgeDirection.getOrientation(1);
					case 2:
						return ForgeDirection.getOrientation(3);
					case 3:
						return ForgeDirection.getOrientation(2);
					case 4:
						return ForgeDirection.getOrientation(5);
					case 5:
						return ForgeDirection.getOrientation(4);
				}

			case 3:
				return side;

			case 4:
				switch (side.ordinal())
				{
					case 0:
						return ForgeDirection.getOrientation(0);
					case 1:
						return ForgeDirection.getOrientation(1);
					case 2:
						return ForgeDirection.getOrientation(5);
					case 3:
						return ForgeDirection.getOrientation(4);
					case 4:
						return ForgeDirection.getOrientation(3);
					case 5:
						return ForgeDirection.getOrientation(2);
				}

			case 5:
				switch (side.ordinal())
				{
					case 0:
						return ForgeDirection.getOrientation(0);
					case 1:
						return ForgeDirection.getOrientation(1);
					case 2:
						return ForgeDirection.getOrientation(4);
					case 3:
						return ForgeDirection.getOrientation(5);
					case 4:
						return ForgeDirection.getOrientation(2);
					case 5:
						return ForgeDirection.getOrientation(3);
				}
		}

		return ForgeDirection.UNKNOWN;
	}

	@Override
	public String toString()
	{
		return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]";
	}
}