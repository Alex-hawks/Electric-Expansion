package electricexpansion.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import universalelectricity.core.item.ItemElectric;

/**
 * This class contains the variables for all of the Electric Expansion Items and
 * Blocks. <br>
 * Before referencing these, check if they are not null, and you should check if <br>
 * Electric Expansion is loaded as well, particularly if recipes are involved.
 * 
 * @author Alex_hawks
 * 
 */
public class ElectricExpansionItems
{
    
    // Blocks
    public static Block blockRawWire;
    public static Block blockInsulatedWire;
    public static Block blockWireBlock;
    public static Block blockSwitchWire;
    public static Block blockSwitchWireBlock;
    public static Block blockLogisticsWire;
    public static Block blockRedstonePaintedWire;
    // public static final Block blockRedstoneWireBlock = new BlockRedstoneWireBlock(redstoneWireBlock, 0);
    
    public static Block blockAdvBatteryBox;
    public static Block blockMultimeter;
    public static Block blockWireMill;
    public static Block blockTransformer;
    public static Block blockDistribution;
    public static Block blockLead;
    //public static Block blockSilverOre;
    public static Block blockInsulationMachine;
    // public static Block blockFuseBox;
    public static Block blockRedstoneNetworkCore;
    
    // Items
    public static Item itemParts;
    public static Item itemUpgrade;
    public static ItemElectric itemEliteBat;
    public static ItemElectric itemAdvBat;
    public static ItemElectric itemUltimateBat;
    public static Item itemMultimeter;
    public static Item itemFuse;
    public static Item itemLinkCard;
}
