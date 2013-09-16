package electricexpansion.common.misc;

import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
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
         * @return the largest, pack1 if they are both equal.
         */
        public static GenericPack getLargest(GenericPack pack1, GenericPack pack2)
        {
            if (pack1 == null || pack2 == null)
            {
                if (pack1 != null)
                    return pack1;
                if (pack2 != null)
                    return pack2;
                return null;
            }
            if (pack1.getScaledEnergy() == pack2.getScaledEnergy())
                return pack1;
            return pack1.getScaledEnergy() > pack2.getScaledEnergy() ? pack1 : pack2;
        }
        
        /**
         * @return the smallest, pack1 if they are both equal.
         */
        public static GenericPack getSmallest(GenericPack pack1, GenericPack pack2)
        {
            if (pack1 == null || pack2 == null)
            {
                if (pack1 != null)
                    return pack1;
                if (pack2 != null)
                    return pack2;
                return null;
            }
            if (pack1.getScaledEnergy() == pack2.getScaledEnergy())
                return pack1;
            return pack1.getScaledEnergy() < pack2.getScaledEnergy() ? pack1 : pack2;
        }
        
        /**
         * @return the electricity given to toCharge...
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
                
                UEElectricPack request = UniversalPowerUtils.INSTANCE.new UEElectricPack(item.getTransfer(toCharge));
                GenericPack actualTransmitted = getSmallest(pack, request);
                
                item.recharge(toCharge, actualTransmitted.toUEWatts(), true);
                
                return actualTransmitted;
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
                
                UEElectricPack request = UniversalPowerUtils.INSTANCE.new UEElectricPack(item.discharge(toDischarge, maxRequest.toUEWatts(), false));
                GenericPack actualTransmitted = getSmallest(maxRequest, request);
                
                item.discharge(toDischarge, actualTransmitted.toUEWatts(), true);
                
                return actualTransmitted;
            }
            else
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
        }
        
        @Override
        public boolean isFull(ItemStack toCheck)
        {
            if (toCheck != null && toCheck.getItem() instanceof IItemElectric)
            {
                return ((IItemElectric) toCheck.getItem()).getElectricityStored(toCheck) == ((IItemElectric) toCheck.getItem()).getMaxElectricityStored(toCheck);
            }
            return false;
        }
        
        @Override
        public boolean isEmpty(ItemStack toCheck)
        {
            if (toCheck != null && toCheck.getItem() instanceof IItemElectric)
            {
                return ((IItemElectric) toCheck.getItem()).getElectricityStored(toCheck) == 0D;
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
                return UniversalPowerUtils.INSTANCE.new IC2Pack(ElectricItem.manager.charge(toCharge, pack.toEU(), 3, false, false), 1);
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
                return UniversalPowerUtils.INSTANCE.new IC2Pack(ElectricItem.manager.discharge(toDischarge, pack.toEU(), 3, false, false), 1);
            }
            catch (Throwable e)
            {
                return UniversalPowerUtils.INSTANCE.new EmptyPack();
            }
        }
        
        @Override
        public boolean isFull(ItemStack toCheck)
        {
            return ElectricItem.manager.charge(toCheck, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true) == 0;
        }
        
        @Override
        public boolean isEmpty(ItemStack toCheck)
        {
            return ElectricItem.manager.discharge(toCheck, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true) == 0;
        }
    }
}
