package electricexpansion.common.misc;

import static electricexpansion.common.misc.EnumPowerConversion.*;

import java.util.logging.Level;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import electricexpansion.common.ElectricExpansion;

public class PowerConversionUtils
{
    public static final PowerConversionUtils INSTANCE = new PowerConversionUtils();
    
    private PowerConversionUtils()
    {
    }
    
    public abstract class GenericPack
    {
        /** @see EnumPowerConversion#UNSCALED */
        protected double unscaledEnergy;
        
        public ElectricityPack toUEPack(float givenValue, ElectricUnit givenType)
        {
            switch (givenType)
            {
                case VOLTAGE:
                    return ElectricityPack.getFromWatts((float) (UNSCALED.convertToOtherUnit(JOULES_UE, unscaledEnergy)), givenValue);
                case AMPERE:
                    return new ElectricityPack(givenValue, (float) (UNSCALED.convertToOtherUnit(JOULES_UE, unscaledEnergy) / givenValue));
                default:
                    return null;
            }
        }
        
        public double toEU()
        {
            return UNSCALED.convertToOtherUnit(ENERGY_UNIT, unscaledEnergy);
        }
        
        public FluidStack toRCSteam()
        {
            return FluidRegistry.getFluidStack("steam", (int) (Math.floor(UNSCALED.convertToOtherUnit(FLUID_STEAM, unscaledEnergy) + 0.5)));
        }
        
        public FluidStack toLava()
        {
            return FluidRegistry.getFluidStack("lava", (int) (Math.floor(UNSCALED.convertToOtherUnit(FLUID_LAVA, unscaledEnergy) + 0.5)));
        }
        
        public float toMinecraftJoules()
        {
            return (float) UNSCALED.convertToOtherUnit(PNEUMATIC, unscaledEnergy);
        }
        
        public float toUEWatts()
        {
            return (float) (UNSCALED.convertToOtherUnit(JOULES_UE, unscaledEnergy));
        }
    }
    
    public final class UEElectricPack extends GenericPack
    {
        public UEElectricPack(float amps, float volts)
        {
            this.unscaledEnergy = JOULES_UE.convertToOtherUnit(UNSCALED, amps * volts);
        }
        
        public UEElectricPack(ElectricityPack pack)
        {
            this(pack.amperes, pack.voltage);
        }

        public UEElectricPack(float joules)
        {
            this.unscaledEnergy = JOULES_UE.convertToOtherUnit(UNSCALED, joules);
        }
    }
    
    public final class IC2Pack extends GenericPack
    {
        public IC2Pack(double euPerPacket, int packetsPerTick)
        {
            this.unscaledEnergy = ENERGY_UNIT.convertToOtherUnit(UNSCALED, euPerPacket * packetsPerTick);
        }
    }
    
    public final class RailCraftSteamStack extends GenericPack
    {
        public final FluidStack steam;
        
        public RailCraftSteamStack(FluidStack stack)
        {
            if (stack.isFluidEqual(FluidRegistry.getFluidStack("steam", 0)))
            {
                this.steam = stack;
                this.unscaledEnergy = FLUID_STEAM.convertToOtherUnit(UNSCALED, stack.amount);
            }
            else
            {
                ElectricExpansion.log(Level.SEVERE, "Someone gave the Electric Expansion Universal Power class an invalid LiquidStack.");
                ElectricExpansion.log(Level.SEVERE, "Report this to Alex_hawks, and he will point you to the mod author in question.");
                throw new IllegalArgumentException();
            }
        }
    }
    
    public final class LavaStack extends GenericPack
    {
        public final FluidStack lava;
        
        public LavaStack(FluidStack stack)
        {
            if (stack.isFluidEqual(FluidRegistry.getFluidStack("lava", 0)))
            {
                this.lava = stack;
                this.unscaledEnergy = FLUID_LAVA.convertToOtherUnit(UNSCALED, stack.amount);
            }
            else
            {
                ElectricExpansion.log(Level.SEVERE, "Someone gave the Electric Expansion Universal Power class an invalid LiquidStack.");
                ElectricExpansion.log(Level.SEVERE, "Report this to Alex_hawks, and he will point you to the mod author in question.");
                throw new IllegalArgumentException();
            }
        }
    }
    
    public final class BCPack extends GenericPack
    {
        public BCPack(float minecraftJoules)
        {
            this.unscaledEnergy = PNEUMATIC.convertToOtherUnit(UNSCALED, minecraftJoules);
        }
    }
    
    public final class EmptyPack extends GenericPack
    {
        public EmptyPack()
        {
        }
    }
}
