package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class Shoot extends CommandBase{

    private ShooterSubsystem shooterSubsystem;
    private double rpm;
    private Timer timer;

    public Shoot(ShooterSubsystem shooterSubsystem,double rpm){
        this.shooterSubsystem = shooterSubsystem;
        this.rpm=rpm;
    }

    @Override
    public void execute() {
        shooterSubsystem.shooterPID();
    }

    @Override
    public void initialize(){
        shooterSubsystem.startTimer();
        timer = new Timer();
        timer.start();
    }
    
    @Override
    public boolean isFinished() {
        
        if (timer.hasPeriodPassed(3)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override 
    public void end(boolean inturrupted){
        shooterSubsystem.stopTimer();
        timer.stop();
        shooterSubsystem.stopShooterSpin();
    }
}