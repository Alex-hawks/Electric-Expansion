package electricexpansion.api;

/**
 * An enumerator for different wire materials.
 * 
 * @author Calclavia
 * 
 */

public enum EnumWireMaterial
{
	COPPER("Copper", 0.05f, 3), TIN("Tin", 0.04f, 2), SILVER("Silver", 0.02f, 1), ALUMINUM("Aluminum", 0.03f, 8), SUPERCONDUCTOR("Superconductor", 0, 5), UNKNOWN("Unknown", 0, 0);

	public final String name;
	public final float resistance;
	public final int electrocutionDamage;

	EnumWireMaterial(String name, float resistance, int electrocutionDamage)
	{
		this.name = name;
		this.resistance = resistance;
		this.electrocutionDamage = electrocutionDamage;
	}
}
