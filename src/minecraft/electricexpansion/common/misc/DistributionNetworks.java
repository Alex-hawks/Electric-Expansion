package electricexpansion.common.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.common.ElectricExpansion;

public class DistributionNetworks
{
	public static DistributionNetworks instance;
	private static MinecraftServer server = MinecraftServer.getServer();
	private static final double maxJoules = 5000000;
	public static final byte maxFrequencies = (byte) 128;
	private static Map<EntityPlayer, double[]> playerFrequencies = new HashMap<EntityPlayer, double[]>();

	public static double getJoules(EntityPlayer player, byte frequency)
	{
		try
		{
			if(player != null)
				return playerFrequencies.get(player)[frequency];
			else return 0;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	public static void setJoules(EntityPlayer player, short frequency, double newJoules)
	{
			if(player != null)
				playerFrequencies.get(player)[frequency] = newJoules;
	}

	public static void addJoules(EntityPlayer player, short frequency, double addedJoules)
	{
			if(player != null)
				playerFrequencies.get(player)[frequency] = playerFrequencies.get(player)[frequency] + addedJoules;
	}

	public static void removeJoules(EntityPlayer player, short frequency, double removedJoules)
	{
		try
		{
			if(player != null)
				playerFrequencies.get(player)[frequency] = playerFrequencies.get(player)[frequency] - removedJoules;
		}
		catch(Exception e){}
	}

	public static double getMaxJoules()
	{
		return maxJoules;
	}

	@SideOnly(Side.SERVER)
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

				String[] players = new String[playerFrequencies.size()];
				players = (String[])( playerFrequencies.keySet().toArray( players ) );

				for (int i = 0; i < playerFrequencies.size(); i ++)
				{
					File var3 = new File(file + File.separator + players[i] + "_tmp.dat");
					File var4 = new File(file + File.separator + players[i] + ".dat");
					File var5 = new File(file + File.separator + players[i] + "_Backup.dat");
					NBTTagCompound nbt = new NBTTagCompound();
					for (int j = 0; j < playerFrequencies.get(players[i]).length; j++)
					{
						if (playerFrequencies.get(players[i])[j] > 0)
						{
							nbt.setDouble(j + "", playerFrequencies.get(players[i])[j]);
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
			}
			catch (Exception e)
			{
				ElectricExpansion.EELogger.severe("Failed to save the Quantum Battery Box Electricity Storage Data!");
			}
		}
	}

	@SideOnly(Side.SERVER)
	public static void onWorldLoad()
	{
		try
		{
			for(File playerFile : ListSaves())
			{
				if (playerFile.exists())
				{
					String name = playerFile.getName();
					if (name.endsWith(".dat"))
						name = name.substring(0, name.length() - 4);

					for (int i = 0; i < playerFrequencies.get(name).length; i++)
					{
						try
						{
							playerFrequencies.get(name)[i] = CompressedStreamTools.readCompressed(new FileInputStream(playerFile)).getDouble(i + "");
						}
						catch (Exception e)
						{
							playerFrequencies.get(name)[i] = 0;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			ElectricExpansion.EELogger.severe("Failed to load the Quantum Battery Box Electricity Storage Data!");
		}
	}

	public static File[] ListSaves() 
	{		
		String folder = "";
		if (server.isDedicatedServer())
		{
			folder = server.getFolderName();
		}
		else if (!server.isDedicatedServer())
		{
			folder = Minecraft.getMinecraftDir() + File.separator + "saves" + File.separator + server.getFolderName();
		}

		String files;
		File folderToUse = new File(folder);
		File[] listOfFiles = folderToUse.listFiles(); 

		return listOfFiles;
	}
}