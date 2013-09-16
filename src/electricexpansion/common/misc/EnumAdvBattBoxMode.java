package electricexpansion.common.misc;

/**
 * 0: OFF: none 
 * 1: BASIC: Electricity (UE, IC2, and RP2 if/when permission is obtained and RP2 is up to date) 
 * 2: PNEUMATIC: Pneumatic (Intelligence depends on upgrade. BuildCraft, ThermalExpansion)
 * 3: QUANTUM: Quantum (Depends on Upgrade, Replaces Quantum Battery Box soon)
 * 4: MEKANISM: Universal Cables (Depends on upgrade. Mekanism) (Unavailable for now) 
 * 5: FACTORIZATION: Factorization Cables (Depends on upgrade. Factorization) (Unavailable for now) 
 * 6: UNIVERSAL (Depends on Upgrade(s), Requires availability of Modes: 1, 2, 4 if Mekanism is installed, 5 if Factorization is installed) (Unavailable for now)
 */
public enum EnumAdvBattBoxMode
{
	OFF,
	BASIC,
	PNEUMATIC,
	QUANTUM, 
	MEKANISM,
	FACTORIZATION, 
	UNIVERSAL,
	
	UNKNOWN;

	EnumAdvBattBoxMode() { }

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
		    
		    default: return UNKNOWN;
		}
	}
}
