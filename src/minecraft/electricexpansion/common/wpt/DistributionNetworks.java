package electricexpansion.common.wpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.google.common.io.Files;

import electricexpansion.common.ElectricExpansion;

public class DistributionNetworks
{
	public static DistributionNetworks instance;
	private static MinecraftServer server = MinecraftServer.getServer();
	private static double[] joules = new double[32768];
	private static final double maxJoules = 50000000;

	public static double getJoules(short frequency)
	{
		return joules[frequency];
	}

	public static double setJoules(short frequency, double newJoules)
	{
		joules[frequency] = newJoules;
		return joules[frequency];
	}

	public static double addJoules(short frequency, double addedJoules)
	{
		joules[frequency] = joules[frequency] + addedJoules;
		return joules[frequency];
	}

	public static double getMaxJoules()
	{
		return maxJoules;
	}

	public static void onWorldSave(World world)
	{
		String folder = "";
		if (server.isDedicatedServer())
		{
			folder = server.getFolderName();
		}
		else if (!world.isRemote)
		{
			folder = Minecraft.getMinecraftDir() + File.separator + "saves" + File.separator + server.getFolderName();
		}

		if (!world.isRemote)
		{
			try
			{
				File file = new File(folder + File.separator + "ElectricExpansion");
				if (!file.exists())
				{
					file.mkdirs();
				}

				File var3 = new File(file + File.separator + "QuantumStorage_tmp_.dat");
				File var4 = new File(file + File.separator + "QuantumStorage.dat");
				File var5 = new File(file + File.separator + "QuantumStorageBackup.dat");
				NBTTagCompound nbt = new NBTTagCompound();
				for (int i = 0; i < joules.length; i++)
				{
					if (joules[i] > 0)
					{
						nbt.setDouble(i + "", joules[i]);
						CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(var3));
					}
				}
				if (var5.exists())
				{
					var5.delete();
				}
				if (var4.exists())
				{
					var4.renameTo(var5);
				}
				var3.renameTo(var4);
			}
			catch (Exception e)
			{
				ElectricExpansion.EELogger.severe("Failed to save the Quantum Battery Box Electricity Storage Data!");
			}
		}
	}

	public static void onWorldLoad(World world)
	{
		String folder = "";
		if (!world.isRemote && server.isDedicatedServer())
		{
			folder = server.getFolderName();
		}
		else if (!world.isRemote)
		{
			folder = Minecraft.getMinecraftDir() + File.separator + "saves" + File.separator + server.getFolderName();
		}

		File oldFile = null;
		if (!world.isRemote && !server.isDedicatedServer())
		{
			oldFile = new File(Minecraft.getMinecraftDir() + File.separator + server.getFolderName() + File.separator + "ElectricExpansion", "QuantumStorage.dat");
		}
		try
		{
			if (oldFile != null && oldFile.exists())
				Files.move(oldFile, new File(folder + File.separator + "ElectricExpansion" + File.separator + "QuantumStorage.dat"));
		}
		catch (Exception e1)
		{
			ElectricExpansion.EELogger.fine("Old Quantum Battery Box save data doesn't exist. This is not something to worry about.");
		}

		if (!world.isRemote)
		{
			try
			{
				File var2 = new File(folder + File.separator + "ElectricExpansion", "QuantumStorage.dat");

				if (var2.exists())
				{
					for (int i = 0; i < joules.length; i++)
					{
						try
						{
							joules[i] = CompressedStreamTools.readCompressed(new FileInputStream(var2)).getDouble(i + "");
						}
						catch (Exception e)
						{
							joules[i] = 0;
						}
					}
				}
			}
			catch (Exception e)
			{
				ElectricExpansion.EELogger.severe("Failed to load the Quantum Battery Box Electricity Storage Data!");
			}
		}
	}
}