package electricexpansion.common.misc;

public enum EnumAdvBattBoxMode
{
	OFF((byte) 0), BASIC((byte) 1), PNEUMATIC((byte) 2), QUANTUM((byte) 3), 
		MEKANISM((byte) 4), FACTORIZATION((byte) 5), UNIVERSAL((byte) 6);
	private static int count = 7;
	private int value;

	EnumAdvBattBoxMode(byte value)
	{
		this.value = value;
	}

	public static EnumAdvBattBoxMode fromValue(byte value) // .values() is apparently very inefficient
	{ 
		switch (value)
		{
		case 0:  return OFF;
		case 1:  return BASIC;
		case 2:  return PNEUMATIC;
		case 3:  return QUANTUM;
		case 4:  return MEKANISM;
		case 5:  return FACTORIZATION;
		case 6:  return UNIVERSAL;
		default: throw new IllegalArgumentException();
		}
	}
}
