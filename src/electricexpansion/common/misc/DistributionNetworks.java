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
    private static final float maxJoules = 5_000;
    public static final byte maxFrequencies = (byte) 128;
    private Map<String, float[]> playerFrequencies = new HashMap<String, float[]>();
    
    public float getJoules(String player, byte frequency)
    {
        if (player != null)
        {
            if (!this.playerFrequencies.containsKey(player))
            {
                this.playerFrequencies.put(player, new float[128]);
            }
            return this.playerFrequencies.get(player)[frequency];
        }
        return 0;
    }
    
    public void setJoules(String player, short frequency, float newJoules)
    {
        if (player != null)
        {
            if (!this.playerFrequencies.containsKey(player))
            {
                this.playerFrequencies.put(player, new float[128]);
            }
            this.playerFrequencies.get(player)[frequency] = newJoules;
        }
    }
    
    public void addJoules(String player, short frequency, float addedJoules)
    {
        if (player != null)
        {
            if (!this.playerFrequencies.containsKey(player))
            {
                this.playerFrequencies.put(player, new float[128]);
            }
            this.playerFrequencies.get(player)[frequency] = this.playerFrequencies.get(player)[frequency] + addedJoules;
        }
    }
    
    public void removeJoules(String player, short frequency, float removedJoules)
    {
        try
        {
            if (player != null)
            {
                this.playerFrequencies.get(player)[frequency] = this.playerFrequencies.get(player)[frequency] - removedJoules;
            }
        }
        catch (Exception e)
        {
        }
    }
    
    public static float getMaxJoules()
    {
        return maxJoules;
    }
    
    public void onWorldSave(WorldEvent event)
    {
        String folder = "";
        if (this.server.isDedicatedServer())
        {
            folder = this.server.getFolderName();
        }
        else
        {
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" + File.separator + this.server.getFolderName();
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
                
                String[] players = new String[this.playerFrequencies.size()];
                players = this.playerFrequencies.keySet().toArray(players);
                
                for (int i = 0; i < this.playerFrequencies.size(); i++)
                {
                    File var3 = new File(file + File.separator + players[i] + "_tmp.dat");
                    File var4 = new File(file + File.separator + players[i] + ".dat");
                    File var5 = new File(file + File.separator + players[i] + "_Backup.dat");
                    NBTTagCompound nbt = new NBTTagCompound();
                    for (int j = 0; j < this.playerFrequencies.get(players[i]).length; j++)
                    {
                        if (this.playerFrequencies.get(players[i])[j] > 0)
                        {
                            nbt.setDouble(j + "", this.playerFrequencies.get(players[i])[j]);
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
        {
            this.playerFrequencies.clear();
        }
    }
    
    public void onWorldLoad()
    {
        try
        {
            for (File playerFile : this.ListSaves())
            {
                if (playerFile.exists())
                {
                    String name = playerFile.getName();
                    if (!name.contains("_Backup"))
                    {
                        if (name.endsWith(".dat"))
                        {
                            name = name.substring(0, name.length() - 4);
                        }
                        
                        this.playerFrequencies.put(name, new float[128]);
                        for (int i = 0; i < 128; i++)
                        {
                            try
                            {
                                this.playerFrequencies.get(name)[i] = CompressedStreamTools.readCompressed(new FileInputStream(playerFile)).getFloat(i + "");
                            }
                            catch (Exception e)
                            {
                                this.playerFrequencies.get(name)[i] = 0;
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
        String[] players = new String[this.playerFrequencies.size()];
        players = this.playerFrequencies.keySet().toArray(players);
        String playerString = "";
        for (String player : players)
        {
            playerString = playerString + ", " + player;
        }
        ElectricExpansion.EELogger.warning(playerString);
    }
    
    public File[] ListSaves()
    {
        String folder = "";
        if (this.server.isDedicatedServer())
        {
            folder = this.server.getFolderName() + File.separator + "ElectricExpansion";
        }
        else if (!this.server.isDedicatedServer())
        {
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" + File.separator + this.server.getFolderName() + File.separator + "ElectricExpansion";
        }
        
        File folderToUse = new File(folder);
        File[] listOfFiles = folderToUse.listFiles();
        
        return listOfFiles;
    }
}