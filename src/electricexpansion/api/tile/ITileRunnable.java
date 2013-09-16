package electricexpansion.api.tile;

public interface ITileRunnable
{
    public int getProcessTime();
    
    public int getTimeRemaining();
    
    public int getCurrentProgress();
}
