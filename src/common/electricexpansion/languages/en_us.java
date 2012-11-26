package electricexpansion.languages;

import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import electricexpansion.ElectricExpansion;

public class en_US 
{
	public static void registerLanguageTerms()
	{
		//Set the Uninsulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Copper.name", "en_US", "Uninsulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Tin.name", "en_US", "Uninsulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Silver.name", "en_US", "Uninsulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.HV.name", "en_US", "Uninsulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Endium.name", "en_US", "Uninsulated Endium Wire");
		//Set the Insulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Copper.name", "en_US", "Insulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Tin.name", "en_US", "Insulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Silver.name", "en_US", "Insulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.HV.name", "en_US", "Insulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Endium.name", "en_US", "Insulated Endium Wire");
		//Set the Hidden Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Copper.name", "en_US", "Hidden Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Tin.name", "en_US", "Hidden Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Silver.name", "en_US", "Hidden Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.HV.name", "en_US", "Hidden HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Endium.name", "en_US", "Hidden Endium Wire");
		//Set the Switch Cable (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Copper.name", "en_US", "Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Tin.name", "en_US", "Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Silver.name", "en_US", "Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.HV.name", "en_US", "HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Endium.name", "en_US", "Endium Switch Wire");
		//Set the Switch Cable Block (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Copper.name", "en_US", "Hidden Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Tin.name", "en_US", "Hidden Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Silver.name", "en_US", "Hidden Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.HV.name", "en_US", "Hidden HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Endium.name", "en_US", "Hidden Endium Switch Wire");

		//Machines
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockUPTransformer, "en_US", "Up Transformer");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockBatteryBox, "en_US", "Battery Box");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockDOWNTransformer, "en_US", "Down Transformer");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockVoltDet, "en_US", "Voltage Detector");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockWireMill, "en_US", "Wire Mill");
		//LanguageRegistry.instance().addNameForObject(blockFuse, "en_US", "120 Volt Relay");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 0), "en_US", "Quantum Battery Box");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 4), "en_US", "Induction Power Sender");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 8), "en_US", "Induction Power Reciever");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLeadGear, "en_US", "Lead Gear");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockLead, "en_US", "Lead Block");
		
		//Upgrades
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 0), "en_US", "Tier 1 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 1), "en_US", "Tier 2 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 2), "en_US", "Tier 3 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 3), "en_US", "BC Compatibility Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 4), "en_US", "IC2 Compatibility Upgrade");
		
		//Resources
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLead, "en_US", "Lead Ingot");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLeadTearBat, "en_US", "Lead-Tear Battery");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemParts, 1, 0), "en_US", "Draw Plates");
		
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemConnectorAlloy, "en_US", "Connector Alloy");
	}
}
