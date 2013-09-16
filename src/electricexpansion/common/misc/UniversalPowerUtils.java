package electricexpansion.common.misc;

import java.util.logging.Level;

import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import cpw.mods.fml.common.Loader;
import electricexpansion.common.ElectricExpansion;

public class UniversalPowerUtils
{
    public static final float UE_RATIO = 100;
    public static final float IC2_RATIO = 2.5f;
    public static final float MEK_RATIO = 4;
    public static final float RC_RATIO = 5;
    public static final float FZ_RATIO = 5; // not used yet
    public static final float BC_RATIO = 1;
    public static final float RP_RATIO = 0; // not used yet
    // One can dream... ^
    
    public static final UniversalPowerUtils INSTANCE = new UniversalPowerUtils();
    
    private UniversalPowerUtils()
    {
    }
    
    public abstract class GenericPack
    {
        /**
         * A hack to make it so I don't have to unnecessarily duplicate code
         */
        protected double scaledEnergy = 0;
        protected double unscaledEnergy = 0;
        
        /**
         * Volts, EU per packet...
         */
        protected double electricVolts = 1;
        /**
         * Amps, no. of EU packets
         */
        protected float electricAmps = 0;
        
        public ElectricityPack toUEPack(float givenValue, ElectricUnit givenType)
        {
            switch (givenType)
            {
                case VOLTAGE:
                    return ElectricityPack.getFromWatts((float) (scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)), givenValue);
                case AMPERE:
                    return new ElectricityPack(givenValue, (float) ((scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)) / givenValue));
                default:
                    return null;
            }
        }
        
        public ElectricityPack toUEPack(ElectricUnit givenType)
        {
            switch (givenType)
            {
                case VOLTAGE:
                    return ElectricityPack.getFromWatts(((float) scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)), (float) this.electricVolts);
                case AMPERE:
                    return new ElectricityPack(this.electricAmps, (float) ((scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)) / this.electricAmps));
                default:
                    return null;
            }
        }
        
        public int toEU()
        {
            return (int) (Math.floor(this.scaledEnergy * IC2_RATIO));
        }
        
        public LiquidStack toRCSteam()
        {
            return LiquidDictionary.getLiquid("Steam", (int) (Math.floor(scaledEnergy * RC_RATIO + 0.5)));
        }
        
        public float toMinecraftJoules()
        {
            return (float) (scaledEnergy * BC_RATIO);
        }
        
        public float getScaledEnergy()
        {
            return (float) this.scaledEnergy;
        }
        
        public float getVolts()
        {
            return (float) electricVolts;
        }
        
        public float getAmps()
        {
            return electricAmps;
        }
        
        public float toUEWatts()
        {
            return (float) (this.scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO));
        }
    }
    
    public final class UEElectricPack extends GenericPack
    {
        public UEElectricPack(float amps, float volts)
        {
            this.electricAmps = amps;
            this.electricVolts = volts;
            this.unscaledEnergy = amps * volts;
            this.scaledEnergy = this.unscaledEnergy / (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO);
        }
        
        public UEElectricPack(ElectricityPack pack)
        {
            this(pack.amperes, pack.voltage);
        }

        public UEElectricPack(float joules)
        {
            this.electricVolts = 1;
            this.electricAmps = joules;
            this.unscaledEnergy = joules;
            this.scaledEnergy = this.unscaledEnergy / (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO);
        }
    }
    
    public final class IC2Pack extends GenericPack
    {
        public IC2Pack(double euPerPacket, int packetsPerTick)
        {
            this.electricVolts = euPerPacket;
            this.electricAmps = packetsPerTick;
            this.unscaledEnergy = euPerPacket * packetsPerTick;
            this.scaledEnergy = this.unscaledEnergy / IC2_RATIO;
        }
    }
    
    public final class RailCraftSteamStack extends GenericPack
    {
        public final LiquidStack steam;
        
        public RailCraftSteamStack(LiquidStack stack)
        {
            if (stack.isLiquidEqual(LiquidDictionary.getLiquid("Steam", 0)))
            {
                this.steam = stack;
                this.unscaledEnergy = this.steam.amount;
                this.scaledEnergy = this.steam.amount / RC_RATIO;
            }
            else
            {
                ElectricExpansion.log(Level.SEVERE, "Someone gave the Electric Expansion Universal Power class an invalid LiquidStack.");
                ElectricExpansion.log(Level.SEVERE, "Report this to Alex_hawks, and he will point you to the mod author in question.");
                throw new NullPointerException();
            }
        }
    }
    
    public final class BCPack extends GenericPack
    {
        public BCPack(float minecraftJoules)
        {
            this.unscaledEnergy = minecraftJoules;
            this.scaledEnergy = minecraftJoules / BC_RATIO;
        }
    }
    
    public final class EmptyPack extends GenericPack
    {
        public EmptyPack()
        {
        }
    }
}
