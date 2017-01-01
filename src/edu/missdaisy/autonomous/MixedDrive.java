/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.missdaisy.autonomous;

import edu.missdaisy.Constants;
import edu.missdaisy.loops.DriveDistance;
import edu.missdaisy.utilities.Trajectory;

/**
 *
 * @author Pratik341
 */
public class MixedDrive extends State
{
    private double mDistance;
    private double mArmPosition;
    private double mHeading;
    
    public MixedDrive(double distance, double heading, double armDown)
    {
        super("MixedDrive");
        
        mDistance = distance;
        mHeading = heading;
        mArmPosition = armDown;
    }

    public void enter()
    {
        mDrive.resetEncoders();
        
        double maxVel = mProperties.getDoubleValue("DriveDistanceMaxVel", 120.0);
        double maxAcc = mProperties.getDoubleValue("DriveDistanceMaxAcc", 250.0);
        double maxJerk = mProperties.getDoubleValue("DriveDistanceMaxJerk", 1250.0);
        
        System.out.println("Generating trajectory...");
        Trajectory.getInstance().generate(Math.abs(mDistance), maxVel, maxAcc, maxJerk, Constants.LoopPeriodS);
        System.out.println("Finished");
        DriveDistance.getInstance().loadProfile(Trajectory.getInstance(), (mDistance > 0.0 ? 1.0 : -1.0), mHeading);
        mDrive.setCurrentController(DriveDistance.getInstance());
        //mUtilityArm.setOpenLoop();
        if (mArmPosition > 0.0)
            mIntake.lowerIntake();
        else
            mIntake.raiseIntake();
         
        //mShooter.setDesiredSpeedRPM(0.0,0.0);
    }
    
    public void exit()
    {
        mDrive.setOpenLoop();
        mDrive.driveLeftRight(0.0,0.0);
    }
    
    public void running() 
    {
    }

    public boolean isDone() 
    {
        return DriveDistance.getInstance().onTarget();// && mUtilityArm.getShoulderOnTarget();
    }
}
