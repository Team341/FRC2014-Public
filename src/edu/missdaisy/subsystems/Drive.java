package edu.missdaisy.subsystems;

import edu.missdaisy.Constants;
import edu.missdaisy.utilities.MA3Encoder;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The robot drive base.
 * 
 * This includes motors and sensors related to the drive and chassis base.
 * 
 * @author Jared341
 */
public class Drive extends DaisySubsystem
{
    // Actuators
    private final Talon leftMotor1;
    private final Talon rightMotor1;
    private final Solenoid transmission;
    //private final Solenoid brake;
    private final Compressor compressor;
    //private final Relay testCompressor;
    
    // Sensors
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private BetterGyro gyro;
    private DigitalInput vision; // optical retroreflective banner sensor 
    //private DigitalInput testPressure;
    
    // State
    private boolean isHighGear = false;
    private boolean isBrake = false;
    private double leftDistance = 0;
    private double rightDistance = 0;
    private double lastLeftDistance = 0;
    private double lastRightDistance = 0;
    private double leftRaw = 0;
    private double rightRaw =0;

    private static Drive instance = null;

    private Drive()
    {
        leftMotor1 = new Talon(Constants.LeftDrivePWM);
        rightMotor1 = new Talon(Constants.RightDrivePWM);
        transmission = new Solenoid (Constants.DriveSolenoid);
        //brake = new Solenoid (Constants.BrakeSolenoid);
        compressor = new Compressor (Constants.PressureSwitchDI, Constants.CompressorRelay);
        compressor.start();
        //testCompressor = new Relay(Constants.CompressorRelay, Relay.Direction.kForward);
        //testPressure = new DigitalInput(Constants.PressureSwitchDI);
        
        
        

        double encoderScalingFactor = Constants.WheelSizeIn*Math.PI/360.0; // inches
        leftEncoder = new Encoder(Constants.LeftDriveDI1, Constants.LeftDriveDI2);
        leftEncoder.setDistancePerPulse(encoderScalingFactor);
        rightEncoder = new Encoder(Constants.RightDriveDI1, Constants.RightDriveDI2);
        rightEncoder.setDistancePerPulse(-encoderScalingFactor);
        
        leftEncoder.start();
        rightEncoder.start();
        gyro = new BetterGyro(Constants.GyroAI);
        
        vision = new DigitalInput(Constants.VisionDI);
        
        lowGear();
        
        LiveWindow.addActuator("Drive", "LeftMotor1", leftMotor1);
        LiveWindow.addActuator("Drive", "RightMotor1", rightMotor1);
        LiveWindow.addSensor("Drive", "LeftEncoder", leftEncoder);
        LiveWindow.addSensor("Drive", "RightEncoder", rightEncoder);
        LiveWindow.addSensor("Drive", "Gyro", gyro);
    }
    
    public static Drive getInstance()
    {
        if( instance == null )
        {
            instance = new Drive();
        }
        return instance;
    }
    
    /*
    public void runCompressor()
    {
        if (!testPressure.get())
        {
            testCompressor.set(Relay.Value.kOn);
            
        }
        else
            testCompressor.set(Relay.Value.kOff);
    }
    */
    private void set(double left, double right)
    {
        
        leftMotor1.set(left);
        rightMotor1.set(-right);
    }

    public void driveSpeedTurn(double speed, double turn)
    {
        double left = speed + turn;
        double right = speed - turn;
        set(left, right);
    }

    public void driveLeftRight(double left, double right)
    {
        set(left, right);
    }
    
    public void lowGear()
    {
        transmission.set(false);
        isHighGear = false;
    }
    
    public void highGear()
    {
        transmission.set(true);
        isHighGear = true;
    }
    
    public void brake()
    {
        //brake.set(true);
        driveLeftRight(0.0, 0.0);
        isBrake = true;
    }
    
    public void unBrake()
    {
        //brake.set(false);
        isBrake = false;
    }
    
    public boolean isHighGear()
    {
        return isHighGear;
    }
    
    public boolean seesTarget()
    {
        return vision.get();
    }
    
    public double getLeftDistance()
    {
        return leftDistance;
    }
    
    public double getRightDistance()
    {
        return rightDistance;
    }
    
    public synchronized double getAverageDistance()
    {
        return (leftDistance+rightDistance)/2.0;
    }
    
    public synchronized double getLargestDistance()
    {
        if( Math.abs(leftDistance) > Math.abs(rightDistance))
        {
            return leftDistance;
        }
        else
        {
            return rightDistance;
        }
    }
    
    public double getLeftSpeed()
    {
        //return (leftDistance-lastLeftDistance)/Constants.LoopPeriodS;
        return leftEncoder.getRate();
    }
    
    public double getRightSpeed()
    {
        //return (rightDistance-lastRightDistance)/Constants.LoopPeriodS;
        return leftEncoder.getRate();
    }
    
    public synchronized double getAverageSpeed()
    {
        return (getLeftSpeed()+getRightSpeed())/2.0;
    }
    
    public double getGyroAngle()
    {
        return gyro.getAngle();
    }
    
     public void driveArc(double speed, double arc)
    {
        double theta = (arc * (Math.PI/2));
        
        double ySpeed =  (speed*Math.cos(theta));
        double xSpeed = (speed*Math.sin(theta));
        
        double rPlusL = (1-Math.abs(xSpeed)) * (ySpeed) + ySpeed;
        double rMinusL = (1-Math.abs(ySpeed)) * (xSpeed) + xSpeed;
        
        set((rPlusL + rMinusL)/2, (rPlusL - rMinusL)/2);
    }
    
    public synchronized void runInputFilters()
    {
        lastLeftDistance = leftDistance;
        lastRightDistance = rightDistance;
        leftDistance = leftEncoder.getDistance();
        rightDistance = rightEncoder.getDistance();
        leftRaw = leftEncoder.get();
        rightRaw = rightEncoder.get();
    }
    
    public double getLeftRaw()
    {
        return leftRaw;
    }
    
    public double getRightRaw()
    {
        return rightRaw;
    }
    
    public void resetEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
        leftDistance =  0;
        rightDistance = 0;
        lastLeftDistance = 0; 
        lastRightDistance = 0;
    }
    
    public synchronized void runOutputFilters()
    {
    }

    public synchronized void reset()
    {
        this.driveSpeedTurn(0.0,0.0);
        lowGear();
        gyro.reset();
        resetEncoders();
    }
    
    public synchronized void zeroGyro()
    {
        gyro.initGyro();
    }
}