package edu.missdaisy;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Interface to laptop-based computer vision application.
 * 
 * No vision processing is actually done here; rather, this is just a thin
 * interface to SmartDashboard to pull values that are set by an offboard
 * program.
 * 
 * @author Jared341
 */
public class Vision
{
    private static Vision instance = null;
    
    public static Vision getInstance()
    {
        if( instance == null )
        {
            instance = new Vision();
        }
        return instance;
    }
    
    private Vision()
    {
        
    }

    /*
    // Example usage:
    
    public boolean seesTarget()
    {
        return SmartDashboard.getBoolean("found", false);
    }

    public double getElevation()
    {
        return SmartDashboard.getNumber("elevation", 0.0);
    }

    public double getAzimuth()
    {
        return SmartDashboard.getNumber("azimuth", 0.0);
    }
    
    public boolean getOnTarget()
    {
        return SmartDashboard.getBoolean("ontarget",false);
    }
    */
}
