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
    
    @Override
    public String toString()
    {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    @Override
    public int hashCode()
    {
        return ((Float) this.x).hashCode() & ((Float) this.y).hashCode() & ((Float) this.z).hashCode();
    }
    
}
