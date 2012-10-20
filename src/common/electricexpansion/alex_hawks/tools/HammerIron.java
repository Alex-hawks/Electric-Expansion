package electricexpansion.alex_hawks.tools;

import electricexpansion.alex_hawks.helpers.ToolHelper;

public class HammerIron extends ToolHelper
{
	public HammerIron(int ID, int meta) 
	{
		super(ID, meta);
		this.setIconIndex(meta);	//change
	    this.setMaxDamage(this.stoneDurability);
	}

}
