/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.missdaisy.autonomous;

/**
 *
 * @author Pratik341
 */
public class MixedIntake extends State
{
    private double mTimeout;
    private double mIntakeSpeed;
    
    public MixedIntake (double speed, double timeout) 
    {
        super("MixedIntake");
        mTimeout = timeout;
        mIntakeSpeed = speed;
    }
    
    public void enter()
    {
        mTimeout += System.currentTimeMillis();
        mIntake.lowerIntake();
    }

    public void running()
    {
        mShooter.lowerEars();
        mIntake.set(mIntakeSpeed);
    }

    public void exit()
    {
        mIntake.set(0.0);
    }

    public boolean isDone()
    {
        return mTimeout < System.currentTimeMillis() ;
    }

}
