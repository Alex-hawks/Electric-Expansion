package electricexpansion.api;

import electricexpansion.api.tile.EnergyCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.block.IElectricalStorage;

public interface IWirelessPowerMachine extends IElectricalStorage
{
    public EnergyCoordinates getFrequency();
    
    public void setFrequency(EnergyCoordinates newFrequency);
    
    public String getType();
    
    void removeJoules(float removedEnergy);
    
    void setPlayer(EntityPlayer player);
}
