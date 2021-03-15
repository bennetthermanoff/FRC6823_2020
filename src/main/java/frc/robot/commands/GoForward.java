package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.NavXHandler;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;

public class GoForward extends CommandBase {
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private boolean isFinished = false;
    private double direction;
    private Timer timer;

    private NavXHandler navX;

    private double initialAngle = 0;

    public GoForward(SwerveDriveSubsystem swerveDriveSubsystem, NavXHandler navx) {
        this.swerveDriveSubsystem = swerveDriveSubsystem;
        // direction is always forward in radians (right is 0, left is PI, forward is
        // Pi/2. back is 3PI/2)
        this.direction = Math.PI / 2.0;
        addRequirements(swerveDriveSubsystem);
        timer = new Timer();

        this.navX = navx;

    }

    @Override
    public void execute() {

        swerveDriveSubsystem.weirdDrive(-1, 0, navX.getAngleRad());
        if (timer.hasPeriodPassed(1.5))
            isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void initialize() {
        this.swerveDriveSubsystem.setFieldAngle(this.navX.getAngleRad());

        timer.reset();
        timer.start();
    }

    @Override
    public void end(boolean interrupted) {
        isFinished = false;
        swerveDriveSubsystem.drive(0, 0, 0);
    }
}
