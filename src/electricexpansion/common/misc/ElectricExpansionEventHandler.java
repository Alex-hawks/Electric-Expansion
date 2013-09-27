package electricexpansion.common.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import universalelectricity.core.electricity.ElectricalEvent;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.ElectricExpansion;

public class ElectricExpansionEventHandler
{
    private Map<IElectricityNetwork, ElectricityPack> electricityMap = new HashMap<IElectricityNetwork, ElectricityPack>();
    
    public static ElectricExpansionEventHandler INSTANCE = new ElectricExpansionEventHandler();
    
    private ElectricExpansionEventHandler() { }
    
    @ForgeSubscribe
    public void onEntityDropItems(LivingDropsEvent event)
    {
        if (event.entity != null)
        {
            if (event.entity instanceof EntitySkeleton && ((EntitySkeleton) event.entity).getSkeletonType() == 1)
            {
                {
                    Random dropNumber = new Random();
                    int numberOfDrops = dropNumber.nextInt(4) + 1;
                    ItemStack leadIS = new ItemStack(ElectricExpansionItems.itemParts, numberOfDrops, 7);
                    
                    event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, leadIS.copy()));
                }
            }
        }
    }
    
    @ForgeSubscribe
    @SideOnly(Side.SERVER)
    public void onWorldSave(WorldEvent.Save event)
    {
        ElectricExpansion.DistributionNetworksInstance.onWorldSave(event);
    }
    
    @ForgeSubscribe
    @SideOnly(Side.SERVER)
    public void onWorldLoad(WorldEvent.Load event)
    {
        ElectricExpansion.DistributionNetworksInstance = new DistributionNetworks();
        ElectricExpansion.DistributionNetworksInstance.onWorldLoad();
    }
    
    @ForgeSubscribe
    @SideOnly(Side.SERVER)
    public void onWorldUnload(WorldEvent.Unload event)
    {
        ElectricExpansion.DistributionNetworksInstance.onWorldSave(event);
    }
    
    @ForgeSubscribe
    @SideOnly(Side.SERVER)
    public void handleElectricityPacket(ElectricalEvent.NetworkEvent e)
    {
        this.electricityMap.get(e.network);
        this.electricityMap.put(e.network, ElectricityPack.merge(this.electricityMap.get(e.network), e.electricityPack));
    }
    
    public void cleanNetworkStat(IElectricityNetwork net)
    {
        this.electricityMap.remove(net);
    }
    
    public ElectricityPack getNetworkStat(IElectricityNetwork net)
    {
        return this.electricityMap.get(net);
    }
}
