package electricexpansion.api;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.block.IElectricityStorage;

public interface IWirelessPowerMachine extends IElectricityStorage
{
    public byte getFrequency();
    
    public void setFrequency(byte newFrequency);
    
    public String getType();
    
    void removeJoules(double outputWatts);
    
    void setPlayer(EntityPlayer player);
}
