package edu.missdaisy;

/**
 * This class contains constants used throughout the code.  
 * 
 * Putting them here means all the other code you write should never care what
 * PWM a motor plugs into, or how large your wheels are.  If there is a case to
 * be made that the constant might need to be changed on the quicker than it
 * takes to build and download the code (ex. PID control loop gain), you should
 * instead use a Property.
 * 
 * @author Jared341
 */
public class Constants
{
    /***** ROBOT OUTPUTS *****/

    // PWM outputs
    public static final int LeftDrivePWM = 1; // [10] both the left and right drives are going to have 2 Y-splitter cables for PWMs each
    public static final int RightDrivePWM = 10 ;
    public static final int IntakePWM = 5;
    public static final int ShooterPWM = 6;
    public static final int LeftServoPWM = 2;
    public static final int RightServoPWM = 9;

    // Relay outputs
    public static final int CompressorRelay = 3;
    public static final int LightRelay = 2;
 
    // Solenoid outputs
    public static final int DriveSolenoid = 1; //Shifter for drive
    public static final int IntakeSolenoid1 = 3; //intake up/down
    public static final int IntakeSolenoid2 = 4;
    public static final int DogEars = 6;//wings in/out
    public static final int WingsSolenoid = 8;
    

    /***** ROBOT INPUTS *****/

    // Digital Inputs
    public static final int PressureSwitchDI = 1;
    public static final int ShooterDI = 2; // limit switch for choo choo
    public static final int VisionDI = 3; // Banner Sensor [retroreflective] 
    public static final int LeftDriveDI1 = 10; // left Encoder A signal
    public static final int LeftDriveDI2 = 11;// Left encoder B signal
    public static final int RightDriveDI1 = 5;// right Encoder A signal
    public static final int RightDriveDI2 = 6;// right Encoder A signal
    
// Analog Inputs
    public static final int GyroAI = 1;
    
    

    /***** DRIVER INPUTS *****/

    // Joysticks
    public static final int DriverGamepadPort = 1;
    public static final int OperatorGamepadPort = 2;
    
    
    /***** OTHER CONSTANTS *****/

    // Loop timer
    public static final long LoopPeriodMs = 10L;
    public static final double LoopPeriodS = (double)LoopPeriodMs/1000.0;
    
    // Misc quantities
    public static final double WheelSizeIn = 4.0;
    public static final double GamepadDeadband = 0.1;
}
