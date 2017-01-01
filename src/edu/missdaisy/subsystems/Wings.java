package edu.missdaisy.subsystems;

import edu.missdaisy.Constants;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 * @author Pratik
 */
public class Wings extends DaisySubsystem
{
    //actuators
    private Solenoid wings;
    
    //state
    private boolean isOpen = false;
    private static Wings instance = null;
    
    private Wings()
    {
        wings = new Solenoid(Constants.WingsSolenoid);
        
        LiveWindow.addActuator("Wings", "Wings", wings);
    }
    
    public static Wings getInstance()
    {
        if (instance == null)
            instance = new Wings();
        return instance;
    }
    
    public void open()
    {
        wings.set(true);
        isOpen = true;
    }
    
    public void close()
    {
        wings.set(false);
        isOpen = false;
    }
    
    public boolean isOpen()
    {
        return isOpen;
    }
}
