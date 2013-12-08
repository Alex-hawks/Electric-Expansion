package electricexpansion.common.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import electricexpansion.api.tile.EnergyCoordinates;
import electricexpansion.common.ElectricExpansion;

public class DistributionNetworks
implements Serializable
{
    private static final long serialVersionUID = 1501129754883884546L;
    
    private static final float maxJoules = 5_000;
    public static final byte maxFrequencies = (byte) 128;
    
    private transient static MinecraftServer server = MinecraftServer.getServer();
    private HashMap<EnergyCoordinates, Float> energyStorage = new HashMap<EnergyCoordinates, Float>();
    
    public float getJoules(EnergyCoordinates coords)
    {
        if (coords != null)
        {
            if (!this.energyStorage.containsKey(coords))
            {
                this.energyStorage.put(coords, 0f);
            }
            return this.energyStorage.get(coords);
        }
        return 0;
    }
    
    public void setJoules(EnergyCoordinates coords, float newJoules)
    {
        if (coords != null)
        {
            if (!this.energyStorage.containsKey(coords))
            {
                this.energyStorage.put(coords, 0f);
            }
            this.energyStorage.put(coords, newJoules);
        }
    }
    
    public void addJoules(EnergyCoordinates coords, float addedJoules)
    {
        if (coords != null)
        {
            if (!this.energyStorage.containsKey(coords))
            {
                this.energyStorage.put(coords, 0f);
            }
            this.energyStorage.put(coords, this.energyStorage.get(coords) + addedJoules);
        }
    }
    
    public float removeJoules(EnergyCoordinates coords, float removedJoules)
    {
        if (coords == null)
            return 0;
        if (removedJoules > this.energyStorage.get(coords))
            removedJoules = this.energyStorage.get(coords);
        if (!this.energyStorage.containsKey(coords))
            this.energyStorage.put(coords, 0f);
        this.energyStorage.put(coords, this.energyStorage.get(coords) - removedJoules);
        return removedJoules;
    }
    
    public static float getMaxJoules()
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
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" + File.separator + server.getFolderName();
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
                
                {
                    try
                    {
                        File var3 = new File(file + File.separator + "Quantum_Storage_tmp.ser");
                        File var4 = new File(file + File.separator + "Quantum_Storage.ser");
                        File var5 = new File(file + File.separator + "Quantum_Storage_Backup.ser");
                        
                        FileOutputStream fileOut = new FileOutputStream(var3);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(this);
                        out.close();
                        fileOut.close();
                        
                        if (var5.exists())
                        {
                            var5.delete();
                        }
                        if (var4.exists())
                        {
                            var4.renameTo(var5);
                        }
                        var3.renameTo(var4);
                    } catch(IOException i)
                    {
                        i.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                ElectricExpansion.EELogger.severe("Failed to save the Quantum Battery Box Electricity Storage Data!");
            }
        }
        if (event instanceof WorldEvent.Unload)
        {
            this.energyStorage.clear();
        }
    }
    
    public static DistributionNetworks onWorldLoad(WorldEvent event)
    {
        String folder = "";
        DistributionNetworks toReturn = null;
        
        if (server.isDedicatedServer())
        {
            folder = server.getFolderName();
        }
        else
        {
            folder = Minecraft.getMinecraft().mcDataDir + File.separator + "saves" + File.separator + server.getFolderName();
        }
        
        if (!event.world.isRemote)
        {
            try
            {
                File file = new File(folder + File.separator + "ElectricExpansion");
                File var3 = new File(file + File.separator + "Quantum_Storage.ser");
                
                FileInputStream fileIn = new FileInputStream(var3);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                toReturn = (DistributionNetworks) in.readObject();
                in.close();
                fileIn.close();
            }
            catch (Exception e)
            {
                ElectricExpansion.EELogger.warning("Failed to load the Quantum Battery Box Electricity Storage Data!");
                ElectricExpansion.EELogger.warning("If this is the first time loading the world after the mod was installed, there are no problems.");
                e.printStackTrace();
            }
        }
        
        return toReturn;
    }
}