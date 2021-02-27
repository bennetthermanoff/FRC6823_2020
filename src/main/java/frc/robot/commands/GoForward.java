package frc.robot.commands;

//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class GoForward extends CommandBase{
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private boolean isFinished = false;
    private double direction;
    private double startTime;
    private boolean timerStarted = false;
    private double time;

    public GoForward(SwerveDriveSubsystem swerveDriveSubsystem, double time) {
        this.swerveDriveSubsystem = swerveDriveSubsystem;
        //direction is always forward in radians (right is 0, left is PI, forward is Pi/2. back is 3PI/2)
        this.direction = Math.PI / 2.0; 
        this.time = time;

        addRequirements(swerveDriveSubsystem);
    }

    @Override
    public void execute(){
        double amplitude = 0.2;
        double xDirection =  Math.cos(direction) * amplitude;
        double yDirection = Math.sin(direction) * amplitude;
        swerveDriveSubsystem.drive(yDirection, xDirection, 0);

        if(!timerStarted){
            startTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - startTime > time * 1000.0){
            isFinished = true;

        }



    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
    @Override
    public void initialize() {

    }

    @Override
    public void end(boolean interrupted){
        swerveDriveSubsystem.drive(0, 0, 0);
    }
}
