package edu.missdaisy.subsystems;

import edu.missdaisy.Constants;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 * @author Pratik
 */
public class Intake extends DaisySubsystem
{
    private Talon motor;
    private DoubleSolenoid pickup;
    
    private boolean isDown = false;

    
    private static Intake instance = null;

    private Intake()
    {
        motor = new Talon(Constants.IntakePWM);
        pickup = new DoubleSolenoid(Constants.IntakeSolenoid1, Constants.IntakeSolenoid2);
        
        LiveWindow.addActuator("Intake", "Motor", motor);
        LiveWindow.addActuator("Intake", "Piston", pickup);

    }
    
    public static Intake getInstance()
    {
        if( instance == null )
        {
            instance = new Intake();
        }
        return instance;
    }
    
    public void set(double power)
    {
        motor.set(power);
    }
    
    public void lowerIntake()
    {
        pickup.set(DoubleSolenoid.Value.kForward);
        isDown = true;
    }

    public void raiseIntake()
    {
        pickup.set(DoubleSolenoid.Value.kReverse);
        isDown = false;
    }
    
    public boolean isDown()
    {
        return isDown;
    }
}
