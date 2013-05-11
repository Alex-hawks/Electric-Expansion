package electricexpansion.common.misc;

import ic2.api.ElectricItem;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;
import electricexpansion.common.misc.UniversalPowerUtils.GenericPack;
import electricexpansion.common.misc.UniversalPowerUtils.UEElectricPack;

public class ChargeUtils
{
    public static final UE UE = new UE();
    public static final IC2 IC2 = new IC2();
    
    public static abstract class CommonUtil
    {
        /**
         * @param pack1
         * @param pack2
         * @return null if they are equal, or at least one of the packs is null
         */
        public static GenericPack getLargest(GenericPack pack1, GenericPack pack2)
        {
            if (pack1 == null || pack2 == null)
                return null;
            if (pack1.getScaledEnergy() == pack2.getScaledEnergy())
                return null;
            return pack1.getScaledEnergy() > pack2.getScaledEnergy() ? pack1 : pack2;
        }
        
        public static GenericPack getSmallest(GenericPack pack1, GenericPack pack2)
        {
            if (pack1 == null || pack2 == null)
                return null;
            if (pack1.getScaledEnergy() == pack2.getScaledEnergy())
                return null;
            return pack1.getScaledEnergy() < pack2.getScaledEnergy() ? pack1 : pack2;
        }
        
        /**
         * @return the leftover electricity from the action...
         */
        public abstract GenericPack charge(ItemStack toCharge, GenericPack pack);
        
        /**
         * @return the electricity drained from toDischarge...
         */
        public abstract GenericPack discharge(ItemStack toDischarge, GenericPack pack);
        
        public abstract boolean isFull(ItemStack toCheck);
        
        public abstract boolean isEmpty(ItemStack toCheck);
    }
    
    public static final class UE extends CommonUtil
    {
        @Override
        public GenericPack charge(ItemStack toCharge, GenericPack pack)
        {
            if (toCharge == null)
                return pack;
            else if (toCharge.getItem() == null)
                return pack;
            else if (toCharge.getItem() instanceof IItemElectric)
            {
                IItemElectric item = (IItemElectric) toCharge.getItem();
                
                UEElectricPack request = UniversalPowerUtils.INSTANCE.new UEElectricPack(item.getReceiveRequest(toCharge));
                GenericPack actualTransmitted = getSmallest(pack, request);
                
                item.onReceive(actualTransmitted.toUEPack(request.getVolts(), ElectricUnit.VOLTAGE), toCharge);
                
                return UniversalPowerUtils.INSTANCE.new UEElectricPack(ElectricityPack.getFromWatts(getLargest(pack, request).toUEWatts() - getSmallest(pack, request).toUEWatts(), pack.getVolts()));
            }
            else
                return pack;
        }
        
        @Override
        public GenericPack discharge(ItemStack toDischarge, GenericPack maxRequest)
        {
            if (toDischarge == null)
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
            else if (toDischarge.getItem() == null)
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
            else if (toDischarge.getItem() instanceof IItemElectric)
            {
                IItemElectric item = (IItemElectric) toDischarge.getItem();
                
                UEElectricPack request = UniversalPowerUtils.INSTANCE.new UEElectricPack(item.getReceiveRequest(toDischarge));
                GenericPack actualTransmitted = getSmallest(maxRequest, request);
                
                item.onReceive(actualTransmitted.toUEPack(request.getVolts(), ElectricUnit.VOLTAGE), toDischarge);

                return actualTransmitted;
            }
            else return UniversalPowerUtils.INSTANCE.new EmptyPack();
        }
        
        @Override
        public boolean isFull(ItemStack toCheck)
        {
            if (toCheck != null && toCheck.getItem() instanceof IItemElectric)
            {
                return ((IItemElectric) toCheck.getItem()).getJoules(toCheck) == ((IItemElectric) toCheck.getItem()).getMaxJoules(toCheck);
            }
            return false;
        }
        
        @Override
        public boolean isEmpty(ItemStack toCheck)
        {
            if (toCheck != null && toCheck.getItem() instanceof IItemElectric)
            {
                return ((IItemElectric) toCheck.getItem()).getJoules(toCheck) == 0D;
            }
            return false;
        }
    }
    
    public static final class IC2 extends CommonUtil
    {
        @Override
        public GenericPack charge(ItemStack toCharge, GenericPack pack)
        {
            try 
            {
                return UniversalPowerUtils.INSTANCE.new IC2TickPack(ElectricItem.charge(toCharge, pack.toEU(), 3, false, false), 1);
            }
            catch (Throwable e)
            {
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
            }
        }
        
        @Override
        public GenericPack discharge(ItemStack toDischarge, GenericPack pack)
        {
            try 
            {
                return UniversalPowerUtils.INSTANCE.new IC2TickPack(ElectricItem.discharge(toDischarge, pack.toEU(), 3, false, false), 1);
            }
            catch (Throwable e)
            {
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
            }
        }
        
        @Override
        public boolean isFull(ItemStack toCheck)
        {
            return ElectricItem.charge(toCheck, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true) == 0;
        }
        
        @Override
        public boolean isEmpty(ItemStack toCheck)
        {
            return ElectricItem.discharge(toCheck, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true) == 0;
        }
    }
}
