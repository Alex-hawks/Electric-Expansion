package electricexpansion.languages;

import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import electricexpansion.ElectricExpansion;

public class en_us 
{
	public static void registerLanguageTerms()
	{
		//Set the Uninsulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Copper.name", "en_us", "Uninsulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Tin.name", "en_us", "Uninsulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Silver.name", "en_us", "Uninsulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.HV.name", "en_us", "Uninsulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.RawWire.Endium.name", "en_us", "Uninsulated Endium Wire");
		//Set the Insulated Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Copper.name", "en_us", "Insulated Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Tin.name", "en_us", "Insulated Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Silver.name", "en_us", "Insulated Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.HV.name", "en_us", "Insulated HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.InsulatedWire.Endium.name", "en_us", "Insulated Endium Wire");
		//Set the Hidden Cable Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Copper.name", "en_us", "Hidden Copper Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Tin.name", "en_us", "Hidden Tin Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Silver.name", "en_us", "Hidden Silver Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.HV.name", "en_us", "Hidden HV Wire");
		LanguageRegistry.instance().addStringLocalization("tile.HiddenWire.Endium.name", "en_us", "Hidden Endium Wire");
		//Set the Switch Cable (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Copper.name", "en_us", "Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Tin.name", "en_us", "Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Silver.name", "en_us", "Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.HV.name", "en_us", "HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWire.Endium.name", "en_us", "Endium Switch Wire");
		//Set the Switch Cable Block (On/Crafted) Name(s)
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Copper.name", "en_us", "Hidden Copper Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Tin.name", "en_us", "Hidden Tin Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Silver.name", "en_us", "Hidden Silver Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.HV.name", "en_us", "Hidden HV Switch Wire");
		LanguageRegistry.instance().addStringLocalization("tile.SwitchWireBlock.Endium.name", "en_us", "Hidden Endium Switch Wire");

		//Machines
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockUPTransformer, "en_us", "Up Transformer");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockBigBatteryBox, "en_us", "Advanced Battery Box");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockDOWNTransformer, "en_us", "Down Transformer");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockVoltDet, "en_us", "Voltage Detector");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockWireMill, "en_us", "Wire Mill");
		//LanguageRegistry.instance().addNameForObject(blockFuse, "en_us", "120 Volt Relay");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 0), "en_us", "Quantum Battery Box");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 4), "en_us", "Induction Power Sender");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.blockWPT, 1, 8), "en_us", "Induction Power Reciever");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLeadGear, "en_us", "Lead Gear");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.blockLead, "en_us", "Lead Block");
		
		//Upgrades
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 0), "en_us", "Tier 1 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 1), "en_us", "Tier 2 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 2), "en_us", "Tier 3 Storage Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 3), "en_us", "BC Compatibility Upgrade");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemUpgrade, 1, 4), "en_us", "IC2 Compatibility Upgrade");
		
		//Resources
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLead, "en_us", "Lead Ingot");
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemLeadTearBat, "en_us", "Lead-Tear Battery");
		LanguageRegistry.instance().addNameForObject(new ItemStack(ElectricExpansion.itemParts, 1, 0), "en_us", "Draw Plates");
		
		LanguageRegistry.instance().addNameForObject(ElectricExpansion.itemConnectorAlloy, "en_us", "Connector Alloy");
	}
}
