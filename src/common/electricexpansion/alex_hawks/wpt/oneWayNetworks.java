package electricexpansion.alex_hawks.wpt;

import java.util.List;

import electricexpansion.ElectricExpansion;
import electricexpansion.alex_hawks.machines.TileEntityWPTSender;

public class oneWayNetworks 
{
	private List[] senders = new List[32768];
	private List[] recievers = new List[32768];
	
	public void setSenderFreq(short oldFreq, short newFreq, TileEntityWPTSender sender)
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
	
	public void setRecieverFreq(short oldFreq, short newFreq, TileEntityWPTSender sender)
	{
		try
		{
			if(recievers[oldFreq].contains(sender))
				recievers[oldFreq].remove(sender);
			if(!(recievers[newFreq].contains(sender)))
				recievers[newFreq].add(sender);
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
				ElectricExpansion.EELogger.severe("Must provide the reciever in question");
				e.printStackTrace();
			}
		}
	}

}
