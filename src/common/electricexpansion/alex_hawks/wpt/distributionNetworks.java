package electricexpansion.alex_hawks.wpt;

public class distributionNetworks 
{
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

}
