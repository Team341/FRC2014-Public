package edu.missdaisy.autonomous;

/**
 *
 * @author Pratik
 */
public class WaitIfNecessary extends WaitForTime
{
    public WaitIfNecessary(int aMilliseconds) 
    {
        super(aMilliseconds);
    }
    
    public void enter()
    {
        if(!mDrive.seesTarget())
        {
            super.enter();
        }
        else
            return;
    }
    
    public boolean isDone()
    {
        return mDrive.seesTarget();
    }
}
