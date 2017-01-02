package edu.missdaisy.autonomous;

/**
 * State to wait for a specified number of milliseconds.
 * 
 * @author Jared341
 */
public class WaitForTime extends State
{
    long mTimeout;

    public WaitForTime(int aMilliseconds)
    {
        super("WaitForTime");

        mTimeout = aMilliseconds;
    }

    public void enter()
    {
        mTimeout += System.currentTimeMillis();
    }

    public void running()
    {
    }

    public void exit()
    {
    }

    public boolean isDone()
    {
        return mTimeout < System.currentTimeMillis();
    }

}
