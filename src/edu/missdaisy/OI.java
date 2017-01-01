package edu.missdaisy;

import edu.missdaisy.subsystems.*;
import edu.missdaisy.utilities.DaisyMath;
import edu.missdaisy.utilities.XboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * Operator interface.
 * 
 * @author Jared341
 */
public class OI
{
    private final Daisy2014 mRobot;
    private final Drive mDrive;
    private final Shooter mShooter;
    private final Intake mIntake;
    private final Wings mWings;

    private final Timer timer;
    
    final XboxController mDriverGamepad;
    final XboxController mOperatorGamepad;
    final Joystick mTestGamepad;

    private static OI instance = null;
    
    private long mLoopCounter = 0;

    public static OI getInstance()
    {
        if( instance == null )
        {
            instance = new OI();
        }
        return instance;
    }

    private OI()
    {
        mRobot = Daisy2014.getInstance();
        mDrive = Drive.getInstance();
        mShooter = Shooter.getInstance();
        mIntake = Intake.getInstance();
        mWings = Wings.getInstance();

        timer = new Timer();
        timer.start();
        // Set up OI
        mDriverGamepad = new XboxController(Constants.DriverGamepadPort);
        mOperatorGamepad = new XboxController(Constants.OperatorGamepadPort);
        mTestGamepad = new Joystick(3);
    }

    public void resetState()
    {
        
    }

    public void processDriverInputs()
    {
        /***** DRIVER *****/
        /*
         * Driver functions:
         * 1. Manual driving (Uses both analog sticks)
         * 2. Shifting from low to high gear (LB/RB)
         * 3. Open/Close wings (LT/RT)
         * 4. Stop the robot from moving [Brake] (B)
         */ 
        if (mDriverGamepad.getBButton())
        {
            // brake
            mDrive.setOpenLoop();
            //mDrive.driveLeftRight(0.0, 0.0);
            mDrive.brake();
        }
        else
        {
            mDrive.setOpenLoop();
            
            //mDrive.unBrake();
            // Manual driving
            double speedCommand = DaisyMath.applyDeadband(-mDriverGamepad.getLeftYAxis(), Constants.GamepadDeadband);
            double turnCommand = DaisyMath.applyDeadband(mDriverGamepad.getRightXAxis(), Constants.GamepadDeadband);
            mDrive.driveSpeedTurn(speedCommand, turnCommand);

            // shift for low/high gear
            if (mDriverGamepad.getRB())
                mDrive.highGear();
            else if ( mDriverGamepad.getLB())
                mDrive.lowGear();     
        }
        
        //mShooter.winch(mDriverGamepad.getAButton());
        
        
        
        /***** OPERATOR *****/
        /*
         * Operator functions:
         * 1. Run intake (Lb in, RB out)
         * 2. Move intake up/down (X/Y)
         * 3. Select between close shot and far shot (A/B)
         * 4. Shoot ball (RT)
         * 
         */
        
        
        //Maunual overtride for shooter
       /*if (mOperatorGamepad.getLeftYAxis() > .1)
       {
            if (mOperatorGamepad.getLeftTriggerHeld())
            {
                mShooter.runMotor(-1.0);
            }
            else
            {
                mShooter.winch(mShooter.isReadyToFire());
            }
            
            if (mOperatorGamepad.getRightTriggerHeld())
            {
                mShooter.shoot();
            }
            else
                mShooter.runMotor(0.0);
        }
        else // not manual override; 
        {*/
            //shooter
            if (!mShooter.isReadyToFire() && mOperatorGamepad.getRightTriggerHeld())
            {
                mShooter.winch(mShooter.isReadyToFire());
                timer.reset();
            }
            else if (mDriverGamepad.getRightTriggerHeld())
            {
                // Set flag = true
                mShooter.raiseEars();
                
                if(timer.get() > .5)
                    mShooter.shoot();
            }
            else if (mDriverGamepad.getLeftTriggerHeld())
            {
                mShooter.lowerEars();
                if(timer.get() > .25)
                    mShooter.shoot();
            }
            else
            {
                mShooter.runMotor(0.0);
                timer.reset();
            
        //}
            
        // If flag == true, then reset timer, set flag false
        // if timer > delay, shoot
        
        if (mOperatorGamepad.getAButton())
        {
            mWings.open();
        }
        else if (mOperatorGamepad.getBButton())
        {
            mWings.close();
        }
        
        //intake piston
        if(mOperatorGamepad.getXButton())
        {
            mIntake.lowerIntake();
            //mShooter.lowerEars();
        }
        else if (mOperatorGamepad.getYButton())
        {
            mIntake.raiseIntake();
            //mShooter.raiseEars();
        }
        
        //intake motor
        if (mOperatorGamepad.getRB())
        {
            mIntake.set(1.0);
            mShooter.lowerEars();
        }
        else if (mOperatorGamepad.getLB())
        {
            mIntake.set(-1.0);
            mShooter.lowerEars();
        }
        
        else 
        {
            mIntake.set(0);
            mShooter.raiseEars(); 
        }
        
        if(mDriverGamepad.getLeftStickClick())
        {
            mShooter.lowerEars();
        }
        else if (mDriverGamepad.getRightStickClick())
        {
            mShooter.raiseEars();
        }
            }
            
        /**
         * RAZER ORBWEAVER CODE
         * only for use when shit hits the fan
         * 
         * 1) Intake Up [1]
         * 2) Intake Down [2]
         * 3) Intake Suck [4]
         * 4) Intake Spit [5]
         * 5) Winch Shooter [6]
         * 
         */
            /*
            
        if(mTestGamepad.getRawButton(1))
        {
            mIntake.lowerIntake();
        }
        else if(mTestGamepad.getRawButton(2))
        {
            mIntake.raiseIntake();
        }
        
        if(mTestGamepad.getRawButton(3))
        {
            mIntake.set(1.0);
            mShooter.lowerEars();
        }
        else if(mTestGamepad.getRawButton(4))
        {
            mIntake.set(-1.0);
            mShooter.lowerEars();
        }
        else
        {
            mIntake.set(0.0);
            mShooter.raiseEars();
        }
        
        if(mTestGamepad.getRawButton(6))
        {
            if (!mShooter.isReadyToFire())
            {
                mShooter.winch(mShooter.isReadyToFire());
            }
        }
         
            */
        /*
        if(mOperatorGamepad.getAButton())
        {
            mShooter.lowerEars();
        }
        else if (mOperatorGamepad.getBButton())
        {
            mShooter.raiseEars();
        }*/
        
        //Shooter Servos
        //mShooter.moveLeftServo(DaisyMath.applyDeadband(-mOperatorGamepad.getLeftYAxis(), Constants.GamepadDeadband));
        //mShooter.moveRightServo(DaisyMath.applyDeadband(-mOperatorGamepad.getRightYAxis(), Constants.GamepadDeadband));

        mLoopCounter++;
    }
    
}
