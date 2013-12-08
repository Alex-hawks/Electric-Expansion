package electricexpansion.common.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import electricexpansion.client.gui.GuiInsulationMachine;
import electricexpansion.client.gui.GuiWireMill;
import electricexpansion.common.ElectricExpansion;

public class NEIElextricExpansionConfig implements IConfigureNEI
{
    
    @Override
    public void loadConfig()
    {
        
        API.registerRecipeHandler(new InsulatingMachineRecipeHandler());
        API.registerUsageHandler(new InsulatingMachineRecipeHandler());
        API.setGuiOffset(GuiInsulationMachine.class, 0, 0);
        
        API.registerRecipeHandler(new WireMillRecipeHandler());
        API.registerUsageHandler(new WireMillRecipeHandler());
        API.setGuiOffset(GuiWireMill.class, 0, 0);
    }
    
    @Override
    public String getName()
    {
        return "Electric Expansion plugin";
    }
    
    @Override
    public String getVersion()
    {
        return ElectricExpansion.VERSION;
    }
    
}
