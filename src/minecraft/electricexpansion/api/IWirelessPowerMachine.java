package electricexpansion.api;

public interface IWirelessPowerMachine
{
	public short getFrequency();

	public void setFrequency(short newFrequency);

	public double getJoules(Object... data);

	public double getMaxJoules(Object... data);

	public String getType();
}
