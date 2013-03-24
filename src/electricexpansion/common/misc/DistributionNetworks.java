package electricexpansion.common.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import electricexpansion.common.ElectricExpansion;

public class DistributionNetworks
{
	private MinecraftServer server = MinecraftServer.getServer();
	private static final double maxJoules = 5000000;
	public static final byte maxFrequencies = (byte) 128;
	private Map<String, double[]> playerFrequencies = new HashMap<String, double[]>();

	public double getJoules(String player, byte frequency)
	{
		if (player != null)
		{
			if (!playerFrequencies.containsKey(player))
				playerFrequencies.put(player, new double[128]);
			return playerFrequencies.get(player)[frequency];
		}
		return 0;
	}

	public void setJoules(String player, short frequency, double newJoules)
	{
		if (player != null)
		{
			if (!playerFrequencies.containsKey(player))
				playerFrequencies.put(player, new double[128]);
			playerFrequencies.get(player)[frequency] = newJoules;
		}
	}

	public void addJoules(String player, short frequency, double addedJoules)
	{
		if (player != null)
		{
			if (!playerFrequencies.containsKey(player))
				playerFrequencies.put(player, new double[128]);
			playerFrequencies.get(player)[frequency] = playerFrequencies.get(player)[frequency] + addedJoules;
		}
	}

	public void removeJoules(String player, short frequency, double removedJoules)
	{
		try
		{
			if (player != null)
				playerFrequencies.get(player)[frequency] = playerFrequencies.get(player)[frequency] - removedJoules;
		}
		catch (Exception e)
		{
		}
	}

	public static double getMaxJoules()
	{
		return maxJoules;
	}

	public void onWorldSave(WorldEvent event)
	{
		String folder = "";
		if (server.isDedicatedServer())
		{
			folder = server.getFolderName();
		}
		else
		{
			folder = Minecraft.getMinecraftDir() + File.separator + "saves" + File.separator + server.getFolderName();
		}

		if (!event.world.isRemote)
		{
			try
			{
				File file = new File(folder + File.separator + "ElectricExpansion");
				if (!file.exists())
				{
					file.mkdirs();
				}

				String[] players = new String[playerFrequencies.size()];
				players = (String[]) (playerFrequencies.keySet().toArray(players));

				for (int i = 0; i < playerFrequencies.size(); i++)
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
		if (event instanceof WorldEvent.Unload)
			playerFrequencies.clear();
	}

	public void onWorldLoad()
	{
		try
		{
			for (File playerFile : ListSaves())
			{
				if (playerFile.exists())
				{
					String name = playerFile.getName();
					if (!name.contains("_Backup"))
					{
						if (name.endsWith(".dat"))
							name = name.substring(0, name.length() - 4);

						playerFrequencies.put(name, new double[128]);
						for (int i = 0; i < 128; i++)
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
		}
		catch (Exception e)
		{
			ElectricExpansion.EELogger.warning("Failed to load the Quantum Battery Box Electricity Storage Data!");
			ElectricExpansion.EELogger.warning("If this is the first time loading the world after the mod was installed, there are no problems.");
		}
		String[] players = new String[playerFrequencies.size()];
		players = (String[]) (playerFrequencies.keySet().toArray(players));
		String playerString = "";
		for (int i = 0; i < players.length; i++)
			playerString = playerString + ", " + players[i];
		ElectricExpansion.EELogger.warning(playerString);
	}

	public File[] ListSaves()
	{
		String folder = "";
		if (server.isDedicatedServer())
		{
			folder = server.getFolderName() + File.separator + "ElectricExpansion";
		}
		else if (!server.isDedicatedServer())
		{
			folder = Minecraft.getMinecraftDir() + File.separator + "saves" + File.separator + server.getFolderName() + File.separator + "ElectricExpansion";
		}

		File folderToUse = new File(folder);
		File[] listOfFiles = folderToUse.listFiles();

		return listOfFiles;
	}
}