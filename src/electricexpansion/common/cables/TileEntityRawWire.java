package electricexpansion.common.cables;

import electricexpansion.api.ElectricExpansionItems;
import electricexpansion.common.helpers.TileEntityConductorBase;

public class TileEntityRawWire extends TileEntityConductorBase
{
    /**
     * Values will NOT be actual values or precise relative values. But if x is
     * meant to be greater than y, it will be. Maybe by 10^10 or 10^-10. But the
     * one meant to be greater, will be.
     */
    @Override
    public float getResistance()
    {
        return super.getResistance() * 2;
    }

    @Override
    protected int getID()
    {
        return ElectricExpansionItems.blockRawWire.blockID;
    }
}