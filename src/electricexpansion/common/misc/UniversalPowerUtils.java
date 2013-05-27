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
    public static final double UE_RATIO = 100;
    public static final double IC2_RATIO = 2.5;
    public static final double MEK_RATIO = 4;
    public static final double RC_RATIO = 5;
    public static final double FZ_RATIO = 5; // not used yet
    public static final double BC_RATIO = 1;
    public static final double RP_RATIO = 0; // not used yet
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
        protected double electricAmps = 0;
        
        public ElectricityPack toUEPack(double givenValue, ElectricUnit givenType)
        {
            switch (givenType)
            {
                case VOLTAGE:
                    return ElectricityPack.getFromWatts(scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO), givenValue);
                case AMPERE:
                    return new ElectricityPack(givenValue, (scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)) / givenValue);
                default:
                    return null;
            }
        }
        
        public ElectricityPack toUEPack(ElectricUnit givenType)
        {
            switch (givenType)
            {
                case VOLTAGE:
                    return ElectricityPack.getFromWatts(scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO), this.electricVolts);
                case AMPERE:
                    return new ElectricityPack(this.electricAmps, (scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO)) / this.electricAmps);
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
        
        public double toMinecraftJoules()
        {
            return scaledEnergy * BC_RATIO;
        }
        
        public double getScaledEnergy()
        {
            return this.scaledEnergy;
        }
        
        public double getVolts()
        {
            return electricVolts;
        }
        
        public double getAmps()
        {
            return electricAmps;
        }
        
        public double toUEWatts()
        {
            return this.scaledEnergy * (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO);
        }
    }
    
    public final class UEElectricPack extends GenericPack
    {
        public UEElectricPack(double amps, double volts)
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

        public UEElectricPack(double joules)
        {
            this.electricVolts = 1;
            this.electricAmps = joules;
            this.unscaledEnergy = joules;
            this.scaledEnergy = this.unscaledEnergy / (Loader.isModLoaded("Mekanism") ? MEK_RATIO : UE_RATIO);
        }
    }
    
    public final class IC2TickPack extends GenericPack
    {
        public IC2TickPack(int euPerPacket, int packetsPerTick)
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
        public BCPack(double minecraftJoules)
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
