package electricexpansion.alex_hawks.wpt;

import java.util.List;

import cpw.mods.fml.common.Mod.Instance;

import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.machines.TileEntityInductionReciever;
import electricexpansion.alex_hawks.machines.TileEntityInductionSender;

public class InductionNetworks 
{
	private static List[] senders = new List[32768];
	private static List[] recievers = new List[32768];
	
	public static void setSenderFreq(short oldFreq, short newFreq, TileEntityInductionSender sender)
	{
		try
		{
			if(senders[oldFreq].contains(sender))
				senders[oldFreq].remove(sender);
			if(!(senders[newFreq].contains(sender)))
				senders[newFreq].add(sender);
		}
		catch(NullPointerException e)
		{
			if(newFreq == (Short)null)
			{
				ElectricExpansion.EELogger.severe("Must provide new frequency");
				e.printStackTrace();
			}
			if(sender == null)
			{
				ElectricExpansion.EELogger.severe("Must provide the sender in question");
				e.printStackTrace();
			}
		}
	}
	
	public static void setRecieverFreq(short oldFreq, short newFreq, TileEntityInductionReciever reciever)
	{
		try
		{
			if(recievers[oldFreq].contains(reciever))
				recievers[oldFreq].remove(reciever);
			if(!(recievers[newFreq].contains(reciever)))
				recievers[newFreq].add(reciever);
		}
		catch(NullPointerException e)
		{
			if(newFreq == (Short)null)
			{
				ElectricExpansion.EELogger.severe("Must provide new frequency");
				e.printStackTrace();
			}
			if(reciever == null)
			{
				ElectricExpansion.EELogger.severe("Must provide the reciever in question");
				e.printStackTrace();
			}
		}
	}
	
	public List getSenders(short freq)
	{return senders[freq];}	
	
	public static List getRecievers(short freq)
	{return recievers[freq];}
}
