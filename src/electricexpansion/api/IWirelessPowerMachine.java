package electricexpansion.api;

import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.block.IElectricalStorage;

public interface IWirelessPowerMachine extends IElectricalStorage
{
    public byte getFrequency();
    
    public void setFrequency(byte newFrequency);
    
    public String getType();
    
    void removeJoules(float outputWatts);
    
    void setPlayer(EntityPlayer player);
}
