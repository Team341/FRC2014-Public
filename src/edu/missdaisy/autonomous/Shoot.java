/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.missdaisy.autonomous;

/**
 *
 * @author Team341
 */
public class Shoot extends State
{
    private double mTimeout;
    private boolean isReady;
    private int shotCounter;
    
    public Shoot() 
    {
        super("Shoot");
        //mTimeout = timeout;
    }
    
    public void enter()
    {
        mIntake.lowerIntake();
        mShooter.raiseEars();
        shotCounter = 0;
    }

    public void running()
    {
        if (!mShooter.isReadyToFire())
            {
                mShooter.winch(mShooter.isReadyToFire());
            } 
        else
        {
            mShooter.runMotor(0.0);
            mShooter.shoot();
            shotCounter++;
        }
    }

    public void exit()
    {
    }

    public boolean isDone()
    {
        return shotCounter == 1;
    }

}