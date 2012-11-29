package electricexpansion.alex_hawks.wpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Mod.Instance;
import electricexpansion.ElectricExpansion;

public class distributionNetworks 
{;
	public static distributionNetworks instance;
	private static MinecraftServer server = MinecraftServer.getServer();
	private static double[] joules = new double[32768];
	private static final double maxJoules = 50000000;

	public static double getJoules(short frequency) 
	{return joules[frequency];}
	
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
	{return maxJoules;}

	public static void onWorldSave(World world)
	{
		String folder = "";
		if (server.isDedicatedServer()) {folder = server.getFolderName();}
		else if(!world.isRemote) {folder = Minecraft.getMinecraftDir() + File.separator + server.getFolderName();}
		
		if(!world.isRemote)
		{
			try
			{
				File file = new File(folder + File.separator + "ElectricExpansion");
				if(!file.exists())	{file.mkdirs();}
			
				File var3 = new File(file + File.separator + "QuantumStorage_tmp_.dat");
				File var4 = new File(file + File.separator + "QuantumStorage.dat");
				File var5 = new File(file + File.separator + "QuantumStorageBackup.dat");
				NBTTagCompound nbt = new NBTTagCompound();
				for(int i = 0; i < joules.length; i++)
				{
					if(joules[i] > 0)
					{
						nbt.setDouble(i + "", joules[i]);
						CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(var3));
					}
				}
			
				if (var4.exists()){var4.renameTo(var5);}
				var3.renameTo(var4);
			}
			catch(IOException e)
			{
				ElectricExpansion.EELogger.severe("Failed to save the Quantum Battery Box Electricity Storage Data!");
			}
		}
	}
	
	public static void onWorldLoad(World world)
	{
		String folder = "";
		if (!world.isRemote && server.isDedicatedServer()) {folder = server.getFolderName();}
		else if(!world.isRemote) {folder = Minecraft.getMinecraftDir() + File.separator + server.getFolderName();}
		
		if(!world.isRemote)
		{
			try
			{
				File var2 = new File(folder + File.separator + "ElectricExpansion", "QuantumStorage.dat");

				if (var2.exists())
				{
					for(int i = 0; i < joules.length; i++)
					{
						try	{joules[i] = CompressedStreamTools.readCompressed(new FileInputStream(var2)).getDouble(i + "");}
						catch(Exception e)	{joules[i] = 0;}
					}
				}
			}
			catch(Exception e)
			{
				ElectricExpansion.EELogger.severe("Failed to load the Quantum Battery Box Electricity Storage Data!");
			}
		}
	}
}
