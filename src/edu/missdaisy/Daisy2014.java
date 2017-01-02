package edu.missdaisy;

import edu.missdaisy.autonomous.AutonomousParser;
import edu.missdaisy.autonomous.StateMachine;
import edu.missdaisy.fileio.PropertyReader;
import edu.missdaisy.fileio.PropertySet;
import edu.missdaisy.loops.FastLoopTimer;
import edu.missdaisy.subsystems.*;
import edu.missdaisy.utilities.CheesyVisionServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Miss Daisy 2014 Robot
 * 
 * @author Jared341
 */
public class Daisy2014 extends IterativeRobot 
{
    OI mOI;
    Drive mDrive;
    Shooter mShooter;
    Intake mIntake;
    Vision mVision;
    Wings mWings;
    
    FastLoopTimer mTimer;
    Timer timer;
    Timer autonTimer;
    DriverStation ds;
    
    CheesyVisionServer mServer;
    final int listenPort = 1180;
    
    int stepCounter;
    boolean isHot;
    
    double intakeSpeed;
    double intakeTime;
    double spitSpeed;

    PropertySet mProperties;
    PropertyReader mPropertyReader;
    StateMachine mAutonomousStateMachine;
    int mAutoMode = 0;
    boolean mLastIterationButtonState = false;
    boolean mLastIterationButton2State = false;
    
    private static Daisy2014 instance = null;
    
    public static Daisy2014 getInstance()
    {
        return instance;
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        instance = this;
        mProperties = PropertySet.getInstance();

        mPropertyReader = new PropertyReader();
        mPropertyReader.parseFile("file://properties.txt");
        
        mOI = OI.getInstance();
        mDrive = Drive.getInstance();
        mTimer = FastLoopTimer.getInstance();
        mShooter = Shooter.getInstance();
        mIntake = Intake.getInstance();
        mWings = Wings.getInstance();
        mServer = CheesyVisionServer.getInstance();
        timer = new Timer();
        autonTimer = new Timer();
        ds = DriverStation.getInstance();
        isHot = false;
        
        mServer.setPort(listenPort);
        mServer.start();
        
        readAutoMode(mAutoMode);
        
        

        loadAllProperties();
    }
    
    private void readAutoMode(int lWhich)
    {/*
        switch(lWhich)
        {
            case 1:
                mPropertyReader.parseAutonomousFile("file://autoShoot1.txt");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Single Ball     ");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
                break;
                
            case 2:
                mPropertyReader.parseAutonomousFile("file://autoShoot2.txt");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Double Ball     ");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
                break;
            case 3:
                mPropertyReader.parseAutonomousFile("file://autoShoot3.txt");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Tripple Ball    ");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
                break;
            default:
                mPropertyReader.parseAutonomousFile("file://auto0.txt");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Mode Default    ");
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
                break;
        }
        DriverStationLCD.getInstance().updateLCD();*/
    }
    
    public void disabledInit()
    {
        mServer.stopSamplingCounts();
        resetAllSubsystems();
    }
    
    public void disabledPeriodic()
    {
        stepCounter = 0;
        /*
        // Re-load the properties file
        if( mOI.mDriverGamepad.getAButton() && !mLastIterationButtonState )
        {
            if( mAutoMode > 3) // depends on how many Auto modes we have
            {
                mAutoMode = 0;
            }
            
            resetAllSubsystems();
            //loadAllProperties();
            
            //mAutonomousStateMachine = new StateMachine((new AutonomousParser()).parseStates());
            
            //System.gc(); // Suggest garbage collection
        }
        if( mOI.mDriverGamepad.getBButton() && !mLastIterationButton2State )
        {
            mDrive.zeroGyro();
        }
        mLastIterationButtonState = mOI.mDriverGamepad.getAButton();
        mLastIterationButton2State = mOI.mDriverGamepad.getBButton();
           */
        
        mDrive.lowGear();
        if (ds.getDigitalIn(1))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Single Ball     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 1;
        }
        else if (ds.getDigitalIn(2))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Two Ball     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 2;
        }
           
        else if (ds.getDigitalIn(3))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Three Ball     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 3;
        }
        else if (ds.getDigitalIn(4))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Drive Foreward     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 4;
        }
        else if (ds.getDigitalIn(5))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Two Ball Adjustible    ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 5;
        }
        else if (ds.getDigitalIn(6))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: One Ball From Line     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 6;
        }
        else if (ds.getDigitalIn(7))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: One Ball 1.0     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 7;
        }
        else if (ds.getDigitalIn(8))
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: One Ball .85     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 8;
        }
        
        else
        {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "AUTO: Do Nothing     ");
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                      ");
            mAutoMode = 0;
        }
            
        intakeSpeed = ds.getAnalogIn(1);
        intakeTime = ds.getAnalogIn(2);
        spitSpeed = ds.getAnalogIn(3);
        
        DriverStationLCD.getInstance().updateLCD();
        logToDashboard();
    }

    public void autonomousInit() 
    {
        timer.start();
        
        autonTimer.start();
        autonTimer.reset();
        isHot = false;
        
        stepCounter = 0;
        
        mServer.reset();
        mServer.startSamplingCounts();
        /* mTimer.setActive(true);
        LiveWindow.setEnabled(false);
        resetAllSubsystems();*/
    }
    
    public void autonomousPeriodic()
    {
        System.out.println("Auto step: " + stepCounter);
        System.out.println("Auton Timer: " + autonTimer.get());
        
        isHot = mServer.getRightStatus();
        
        /*
        if (autonTimer.get() < 0.6) {
            isHot = mDrive.seesTarget();
        }
        else if (autonTimer.get() > 6.0) {
            isHot = true;
        }
        
        if (mAutoMode == 1) {
            if (autonTimer.get() > 0.7) {
                oneBall(-1.0, isHot);
            }
        }*/
        
        switch (mAutoMode)
        {
            case 1: 
                oneBall(-1.0, isHot);
                break;
            case 2:
                twoBall(.6, 2.0, .22); 
                break;
            case 3:
                break;
            case 4:
                driveForward();
                break;
            case 5: // .6
                twoBall(intakeSpeed, intakeTime, spitSpeed);
                break;
            case 6: // .7
                oneBallFromLine();
                break;
            case 7: // .8
                oneBall(-1.0, false);
                break;
            case 8: // .4 
                oneBall(-.95, false); 
                break;
            default:
                break;
        }
        
        logToDashboard();
        /*
        // LOW GOAL AUTO
        if (stepCounter == 0)
        {
            timer.start();
            timer.reset();
            stepCounter++;
        }
        else if (stepCounter == 1)
        {
            if (timer.get() < 6)
            //if (Math.abs(mDrive.getAverageDistance()) < 60)
            {
                mDrive.driveSpeedTurn(.5, 0.0);
            }
            else
            {
                mDrive.driveSpeedTurn(0.0, 0.0);
                stepCounter++;       
                timer.reset();
            }
        }
        
        else
        {
            mDrive.driveSpeedTurn(0.0,0.0);
        }*/
        
        
        //HIGH GOAL AUTO
        
        
        
        
        
        /*else if(stepCounter == 2)
        {
            mShooter.shoot();
        }
       
          
        /* mAutonomousStateMachine.run();

        logToDashboard();
        
        // Autonomous
        SmartDashboard.putString("AutoState", mAutonomousStateMachine.getCurrentState());*/
    }
    
    public void driveForward()
    {
        if (stepCounter == 0)
        {
            timer.start();
            timer.reset();
            stepCounter++;
        }
        else if (stepCounter == 1)
        {
            if (timer.get() < 2.1)
            //if (Math.abs(mDrive.getAverageDistance()) < 60)
            {
                mDrive.driveSpeedTurn(-.5, 0.0);
            }
            else
                stepCounter++;
        }
        else
        {
            mDrive.driveSpeedTurn(0.0, 0.0);
            timer.reset();
        }
    }
    
    public void twoBall(double speedForIntake, double timeForIntake, double speedSpit) // .6, 2.0, .22
    {
        if (stepCounter == 0)
        {
            timer.start();
            timer.reset();
            stepCounter++;
        }
        else if (stepCounter == 1)
        {
            if (timer.get() < .4)
            //if (Math.abs(mDrive.getAverageDistance()) < 60)
            {
                mShooter.raiseEars();
                mIntake.lowerIntake();
                mIntake.set(speedForIntake);
                
            }
            else
            {
                mDrive.driveSpeedTurn(0.0, 0.0);
                stepCounter++;       
                timer.reset();
            }
        }
        
        else if (stepCounter == 2)
        {
            if (timer.get() < 2.6)
            {
                if (timer.get() > 2.4)
                {
                    mIntake.set(-speedSpit);
                }
                mDrive.driveSpeedTurn(-1.0, 0.0);
            }
            else
            {
                mShooter.lowerEars();
                mIntake.set(0.0);
                mDrive.driveSpeedTurn(0.0,0.0);
                stepCounter++;
                timer.reset();
            }
            
        }
        else if (stepCounter == 3)
        {
            if (timer.get() < .5)
            {
                mShooter.lowerEars();
            }
            else
            {
                stepCounter++;
                timer.reset();
            }
        }
        else if(stepCounter == 4)
        {
            mShooter.lowerEars();
            if(timer.get() < .5)
            {
                mShooter.shoot();
            }
            else
            {
                mShooter.runMotor(0.0);
                stepCounter++;
                timer.reset();
            }
                
        }
        
        else if(stepCounter == 5)
        {
            if(!mShooter.isReadyToFire())
            {
                mShooter.winch(mShooter.isReadyToFire());
                if (timer.get() > .25)
                {
                    mShooter.lowerEars();
                }
                
            }
            else
            {
                mShooter.runMotor(0.0);
                timer.reset();
                stepCounter++;
                timer.reset();
                
            }
            
        }
        else if (stepCounter == 6)
        {
            System.out.println("Just Intaking");
            if(timer.get() < timeForIntake)
            {
                //mShooter.runMotor(0.0);
                mShooter.lowerEars();
                mIntake.set(1.0);
                //mDrive.driveSpeedTurn(.2, 0.0);
            }
            else
            {
                mShooter.runMotor(0.0);
                stepCounter++;
                timer.reset(); 
            }
        }
        
        
        else if (stepCounter == 7)
        {
            if (timer.get() < .7)
            {
                mShooter.lowerEars();
            }
            else
            {
                stepCounter++;
                timer.reset();
            }
        }
        
        else if(stepCounter == 8)
        {
            mShooter.lowerEars();
            if(timer.get() < .5)
            {
                
                mShooter.shoot();
            }
            else
            {
               
                mShooter.runMotor(0.0);
                stepCounter++; 
                timer.reset();
            }
                
        }
        else
        {
            mDrive.driveSpeedTurn(0.0,0.0);
        }
    }
    
    public void oneBall(double driveSpeed, boolean targetIsHot)
    {
        if (stepCounter == 0)
        {
            timer.start();
            timer.reset();
            stepCounter++;
        }
        else if (stepCounter == 1)
        {
            if (timer.get() < 2.6)
            //if (Math.abs(mDrive.getAverageDistance()) < 60)
            {
                mDrive.driveSpeedTurn(driveSpeed, 0.0);
                mShooter.raiseEars();
            }
            else
            {
                mDrive.driveSpeedTurn(0.0, 0.0);
                stepCounter++;       
                timer.reset();
            }
        }
        
        else if (stepCounter == 2)
        {
            if(timer.get() < 0.4)
            {
             //   mShooter.lowerEars();
                if (timer.get() > 0.3) {
                    mIntake.lowerIntake();
                }
            }
            else
            {
                timer.reset();
                stepCounter++;
            }
        }
        else if (stepCounter == 3) {
            if (timer.get() < 0.8) {
                // wait for intake to fall
                if (timer.get() > 0.3) {
                    mShooter.lowerEars();
                }
            } else {
                timer.reset();
                stepCounter++;
            }
        }
        else if(stepCounter == 4)
        {
            if (targetIsHot) {
                if(timer.get() < .6)
                {
                    System.out.println("I shot the sheriff.");
                    mShooter.lowerEars();
                    mShooter.shoot();
                }
                else
                { 
                    timer.reset();
                    mShooter.runMotor(0.0);
                    stepCounter++;
                }
            } else {
                System.out.println("Waiting for signal to shoot");
                timer.reset();
            }              
        }
        else if(stepCounter == 5)
        {
            if(!mShooter.isReadyToFire())
            {
                mShooter.winch(mShooter.isReadyToFire());
                mIntake.raiseIntake();
            }
            else
            {
                mShooter.runMotor(0.0);
                mIntake.raiseIntake();
                timer.reset();
                stepCounter++;
                timer.reset();
                
            }
        }
        else if(stepCounter == 6)
        {
            if(timer.get() < 2.5)
            {
                mDrive.driveSpeedTurn(-driveSpeed, 0.0);
            }
            else
            {
                timer.reset();
                stepCounter++;
            }
        }
        else
        {
            mDrive.driveSpeedTurn(0.0,0.0);
            
        }
    }
    public void oneBallFromLine()
    {
        if (stepCounter == 0)
        {
            timer.start();
            timer.reset();
            stepCounter++;
        }
        
        
        else if (stepCounter == 1)
        {
            if(timer.get() < 0.4)
            {
             //   mShooter.lowerEars();
                if (timer.get() > 0.3) {
                    mIntake.lowerIntake();
                }
            }
            else
            {
                timer.reset();
                stepCounter++;
            }
        }
        else if (stepCounter == 2) {
            if (timer.get() < 0.8) {
                // wait for intake to fall
                if (timer.get() > 0.3) {
                    mShooter.raiseEars();
                }
            } else {
                timer.reset();
                stepCounter++;
            }
        }
        else if (stepCounter == 3)
        {
            if (timer.get() < 0.6)
            {
                mShooter.shoot();
            }
            else
            {
                mShooter.runMotor(0.0);
                timer.reset();
                stepCounter++;
            }
        }
        
        else if(stepCounter == 3)
        {
            if(!mShooter.isReadyToFire())
            {
                mShooter.winch(mShooter.isReadyToFire());
                mIntake.raiseIntake();
            }
            else
            {
                mShooter.runMotor(0.0);
                mIntake.raiseIntake();
                timer.reset();
                stepCounter++;
                timer.reset();
                
            }
        }
        else if(stepCounter == 4)
        {
            if(timer.get() < 1.5)
            {
                mDrive.driveSpeedTurn(-1.0, 0.0);
            }
            else
            {
                timer.reset();
                stepCounter++;
            }
        }
        else
        {
            mDrive.driveSpeedTurn(0.0,0.0);
            
        }
    }
    
    public void teleopInit()
    {
        resetAllSubsystems();
        mTimer.setActive(false);
        LiveWindow.setEnabled(false);
    }

    public void teleopPeriodic()
    {
        mOI.processDriverInputs();
        
        logToDashboard();
    }
    
    public void testInit()
    {
        mTimer.setActive(false);
        LiveWindow.setEnabled(true);
    }
    
    public void testPeriodic() 
    {
        LiveWindow.run();
        logToDashboard();
    }
    
    private void resetAllSubsystems()
    {
        mDrive.reset();

        mDrive.setOpenLoop();
    }
    
    private void logToDashboard()
    {
        
        // Drive
        SmartDashboard.putNumber("LeftDriveDistance",mDrive.getLeftDistance());
        SmartDashboard.putNumber("LeftDriveSpeed",mDrive.getLeftSpeed());
        SmartDashboard.putNumber("RightDriveDistance",mDrive.getRightDistance());
        SmartDashboard.putNumber("RightDriveSpeed",mDrive.getRightSpeed());
        SmartDashboard.putNumber("GyroAngle",mDrive.getGyroAngle());
        SmartDashboard.putBoolean("HighGear",mDrive.isHighGear());
        SmartDashboard.putBoolean("Cheesy Vision Target", mServer.getRightStatus());
        SmartDashboard.putBoolean("isHot", isHot);
        
        // Intake
        SmartDashboard.putBoolean("IntakeDown", mIntake.isDown());
        
        // Shooter
        SmartDashboard.putBoolean("Shooter Ready",mShooter.isReadyToFire());
        SmartDashboard.putBoolean("Shooter Sensor", mShooter.sensorReturn());
        
        //Wings
        SmartDashboard.putBoolean("Wings Open", mWings.isOpen());
        
                
    }
    
    private void loadAllProperties()
    {
        mDrive.loadProperties();
        mIntake.loadProperties();
        mShooter.loadProperties();
        mWings.loadProperties();
    }
}
