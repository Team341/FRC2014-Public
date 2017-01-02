package edu.missdaisy.loops;

import edu.missdaisy.Constants;
import edu.missdaisy.subsystems.DaisySubsystem;
import edu.missdaisy.subsystems.Drive;
import java.util.TimerTask;

/**
 * Synchronous control loop to call filters and controllers at a fixed rate.
 * 
 * @author Jared341
 */
public class FastLoopTimer extends TimerTask
{
    private final java.util.Timer timer;
    private boolean mActive;

    private final DaisySubsystem[] subsystems = { Drive.getInstance() };

    private static FastLoopTimer instance = null;

    public static FastLoopTimer getInstance()
    {
        if( instance == null )
        {
            instance = new FastLoopTimer();
            instance.start();
        }
        return instance;
    }

    public FastLoopTimer()
    {
        timer = new java.util.Timer();
        mActive = true;
    }

    private void start()
    {
        timer.scheduleAtFixedRate(this, 0L, Constants.LoopPeriodMs);
    }
    
    public synchronized void setActive(boolean aActive)
    {
        mActive = aActive;
    }

    public synchronized void run()
    {
        if( mActive )
        {
            for( int i = 0; i < subsystems.length; i++ )
            {
                subsystems[i].runInputFilters();
                subsystems[i].runCurrentController();
                subsystems[i].runOutputFilters();
            }
        }
    }
}