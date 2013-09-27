package electricexpansion.common.misc;

public enum EnumPowerConversion
{
    /** 1 Charge is generated from 1 mB of Steam */
    CHARGE(100),
    /** Commonly accepted ratio between Pneumatic, and EU */
    ENERGY_UNIT(50),
    /** 20K MJ per Bucket in Combustion Engine */
    FLUID_LAVA(1),
    /** Ratio between Pneumatic and Steam as stated by CoverJaguar himself */
    FLUID_STEAM(100),
    /** Taken by decompiling RP2pr6, and looking at the decompiled code. Apart from this, no code towards compatibility will be done without permission. */
    JOULES_RP(12_500),
    // ^ One can dream...
    /** Built in to UE */
    JOULES_UE(20),
    /** The universal unit on which all the ratios are based */
    PNEUMATIC(20),
    /** Not scaled to any unit */
    UNSCALED(1);
    
    
    private float ratio;
    
    private EnumPowerConversion(float ratio)
    {
        this.ratio = ratio;
    }
    
    private EnumPowerConversion(double ratio)
    {
        this((float) ratio);
    }
    
    public float getRatio()
    {
        return this.ratio;
    }
    
    /**
     *  This method is called on the fromUnit
     *  <br />eg.
     *  <br /><code>&#8195&#8194 float convertedEnergy = fromUnit.convertUnit(toUnit, toConvert)</code>
     */
    public float convertToOtherUnit(EnumPowerConversion toUnit, float input)
    {
        return (input / this.ratio) * toUnit.ratio;
    }

    /** 
     * @see #convertToOtherUnit(EnumPowerConversion, float)
     */
    public double convertToOtherUnit(EnumPowerConversion toUnit, double input)
    {
        return (input / this.ratio) * toUnit.ratio;
    }
}
