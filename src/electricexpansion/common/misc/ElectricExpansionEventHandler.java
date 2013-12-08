package electricexpansion.common.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import universalelectricity.core.electricity.ElectricalEvent;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
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
        if (event.entityLiving != null && !(event.entityLiving instanceof EntityPlayer) 
            && event.source instanceof EntityDamageSource && ((EntityDamageSource) event.source).getEntity() instanceof EntityPlayer)
        {
            Random rand = new Random();
            int numberOfDrops = 0;
            boolean rare = rand.nextInt(400) < 5 + event.lootingLevel;
            
            if (event.entityLiving.dimension == -1)
            {
                numberOfDrops = rand.nextInt((int) event.entityLiving.getMaxHealth() / 5 + event.lootingLevel);
                ItemStack leadIS = new ItemStack(ElectricExpansionItems.itemParts, Math.max(2, numberOfDrops), 10);
                
                event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, leadIS.copy()));
                
                if (rare)
                    event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(ElectricExpansionItems.itemParts, 1, 7)));
            }
            else if (event.entityLiving.dimension == 1)
            {
                numberOfDrops = rand.nextInt((int) event.entityLiving.getMaxHealth() / 20 + event.lootingLevel);
                ItemStack silverIS = new ItemStack(ElectricExpansionItems.itemParts, Math.max(2, numberOfDrops), 11);
                
                event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, silverIS.copy()));
                
                if (rare)
                    event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, new ItemStack(ElectricExpansionItems.itemParts, 1, 7)));
            }
            
            if (rand.nextInt(50_000) <= event.entityLiving.getMaxHealth() * (numberOfDrops + (rare ? 9 : 0)))
            {
                event.entityLiving.worldObj.createExplosion(event.entityLiving, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, 3, false);
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
        ElectricExpansion.DistributionNetworksInstance = DistributionNetworks.onWorldLoad(event);
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
