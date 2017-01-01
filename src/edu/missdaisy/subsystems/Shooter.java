package edu.missdaisy.subsystems;

import edu.missdaisy.Constants;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.TimerTask;
import java.util.Timer;


/**
 *
 * @author Pratik341
 */
public class Shooter extends DaisySubsystem
{
   //actuators
   private final Talon winch;
   private final DigitalInput limitSwitch;
   private final Solenoid ears;
   //private final Servo rightServo;
   
   private final Timer mTimer;
   
   private boolean isEngaged = true;
   private boolean readyToFire = false;
   private boolean earsUp;
   
   private static Shooter instance = null;

   public Shooter()
   {
        winch = new Talon(Constants.ShooterPWM);
        limitSwitch = new DigitalInput(Constants.ShooterDI);
        //leftServo = new Servo(Constants.LeftServoPWM);
        //rightServo = new Servo(Constants.RightServoPWM);
        ears = new Solenoid(Constants.DogEars);
        
        mTimer = new Timer();
        
        LiveWindow.addActuator("Shooter", "Winch", winch);
        LiveWindow.addActuator("Shooter", "Dog Ears", ears);
        //LiveWindow.addActuator("Shooter", "Left Servo", leftServo);
        //LiveWindow.addActuator("Shooter", "Right Servo", rightServo);
        LiveWindow.addSensor("Shooter", "Limit Switch", limitSwitch);
   }
   
   public static Shooter getInstance()
    {
        if( instance == null )
        {
            instance = new Shooter();
        }
        return instance;
    }
   
   public void winch(boolean stop)
   {
       if (!stop)
       {
           winch.set(-1.0);
       }
       else
           winch.set(0.0);
   }
   
   /**
    * Method for manually overriding the sensor if necessary
    */
   public void runMotor(double value)
   {
       winch.set(value);
   }
   
   public boolean isReadyToFire()
   {
       if (!limitSwitch.get())
           return true;
       else
           return false;
       //return limitSwitch.get();
   }
   
   /**
    * Method to shoot, wait a given amount of time (in milliseconds) and then continue to winch
    * @param timeOut- the amount of time to wait after the shot
    */
   public void shoot()
   {
      runMotor(-1.0);
       
   }
   
   public void raiseEars()
   {
       ears.set(false);
       earsUp = true;
   }
   
   public void lowerEars()
   {
       ears.set(true);
       earsUp = false;
   }
   

   public void moveLeftServo(double value)
   {
      // leftServo.set(leftServo.get() + value);
   }
   
   public void moveRightServo(double value)
   {
      // rightServo.set(rightServo.get() + value);
   }
   
   public boolean sensorReturn()
   {
       return !limitSwitch.get();
   }
}
