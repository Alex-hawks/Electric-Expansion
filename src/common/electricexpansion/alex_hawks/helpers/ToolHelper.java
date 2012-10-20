package electricexpansion.alex_hawks.helpers;

import net.minecraft.src.Item;

public abstract class ToolHelper extends Item 
{
	protected int stoneDurability = 131;
	protected int ironDurability = 250;
	protected int diamondDurability = 1561;
	
	protected ToolHelper(int ID, int meta) 
	{
		super(ID);
		this.setMaxStackSize(1);
		this.setItemName("ElectricExpansionTool");
	}

}
