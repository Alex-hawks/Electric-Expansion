package electricexpansion.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.gui.GuiAdvancedBatteryBox;
import electricexpansion.client.gui.GuiFuseBox;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.client.gui.GuiLogisticsWire;
import electricexpansion.client.gui.GuiQuantumBatteryBox;
import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.client.render.RenderHandler;
import electricexpansion.client.render.RenderInsulatedWire;
import electricexpansion.client.render.RenderMultimeter;
import electricexpansion.client.render.RenderRawWire;
import electricexpansion.client.render.RenderTransformer;
import electricexpansion.client.render.RenderWireMill;
import electricexpansion.common.CommonProxy;
import electricexpansion.common.cables.TileEntityInsulatedWire;
import electricexpansion.common.cables.TileEntityLogisticsWire;
import electricexpansion.common.cables.TileEntityRawWire;
import electricexpansion.common.cables.TileEntityRedstonePaintedWire;
import electricexpansion.common.cables.TileEntitySwitchWire;
import electricexpansion.common.cables.TileEntitySwitchWireBlock;
import electricexpansion.common.cables.TileEntityWireBlock;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityFuseBox;
import electricexpansion.common.tile.TileEntityHiveCore;
import electricexpansion.common.tile.TileEntityInsulatingMachine;
import electricexpansion.common.tile.TileEntityMultimeter;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;
import electricexpansion.common.tile.TileEntityTransformer;
import electricexpansion.common.tile.TileEntityWireMill;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public static int RENDER_ID;
    
    @Override
    public void init()
    {
        RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        RenderInsulatedWire insulatedWireRenderer = new RenderInsulatedWire();
        
        ClientRegistry.registerTileEntity(TileEntityWireMill.class, "TileEntityWireMill", new RenderWireMill());
        ClientRegistry.registerTileEntity(TileEntityRawWire.class, "TileEntityRawWire", new RenderRawWire());
        ClientRegistry.registerTileEntity(TileEntityInsulatedWire.class, "TileEntityInsulatedWire", insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntitySwitchWire.class, "TileEntitySwitchWire", insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntityLogisticsWire.class, "TileEntityLogisticsWire", insulatedWireRenderer);
        ClientRegistry.registerTileEntity(TileEntityRedstonePaintedWire.class, "TileEntityRedstonePaintedWire", insulatedWireRenderer);
        GameRegistry.registerTileEntity(TileEntityWireBlock.class, "TileEntityWireBlock");
        GameRegistry.registerTileEntity(TileEntitySwitchWireBlock.class, "TileEntitySwitchWireBlock");
        GameRegistry.registerTileEntity(TileEntityQuantumBatteryBox.class, "TileEntityDistribution");
        GameRegistry.registerTileEntity(TileEntityInsulatingMachine.class, "TileEntityInsulatingMachine");
        GameRegistry.registerTileEntity(TileEntityHiveCore.class, "TileEntityRedstoneNetworkCore");
        
        ClientRegistry.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer", new RenderTransformer());
        ClientRegistry.registerTileEntity(TileEntityMultimeter.class, "TileEntityMultimeter", new RenderMultimeter());
        GameRegistry.registerTileEntity(TileEntityAdvancedBatteryBox.class, "TileEntityAdvBox");
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null)
        {
            switch (ID)
            {
                case 0:
                    return new GuiAdvancedBatteryBox(player.inventory, (TileEntityAdvancedBatteryBox) tileEntity);
                case 1:
                    break; // What's this used for?
                case 2:
                    return new GuiWireMill(player.inventory, (TileEntityWireMill) tileEntity);
                case 3:
                    return new GuiLogisticsWire((TileEntityLogisticsWire) tileEntity);
                case 4:
                    return new GuiQuantumBatteryBox(player.inventory, (TileEntityQuantumBatteryBox) tileEntity);
                case 5:
                    return new GuiInsulationMachine(player.inventory, (TileEntityInsulatingMachine) tileEntity);
                case 6:
                    return new GuiFuseBox(player.inventory, (TileEntityFuseBox) tileEntity);
                    
            }
        }
        return null;
    }
}
