package electricexpansion.api.tile;

public class EnergyCoordinates
{
    public final float x;
    public final float y;
    public final float z;
    
    public EnergyCoordinates(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        else if (!(o instanceof EnergyCoordinates))
            return false;
        else
        {
            EnergyCoordinates that = (EnergyCoordinates) o;
            return this.x == that.x && this.y == that.y && this.z == that.z;
        }
    }
}
