package electricexpansion.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IWirelessPowerMachine
{
	public byte getFrequency();

	public void setFrequency(byte newFrequency);

	public double getJoules(Object... data);

	public double getMaxJoules(Object... data);

	public String getType();

	void removeJoules(double outputWatts);
	
	void setPlayer(EntityPlayer player);
}
