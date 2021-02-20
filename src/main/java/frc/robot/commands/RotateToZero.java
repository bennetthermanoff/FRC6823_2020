package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.NavXHandler;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class RotateToZero extends CommandBase {
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private NavXHandler navXHandler;
    private boolean isFinished = false;
    private double margin = 0.5; // margin of degrees
    private PIDController angleController;

    private static double initialDegrees;

    public RotateToZero(SwerveDriveSubsystem swerveDriveSubsystem, NavXHandler navXHandler) {

        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.navXHandler = navXHandler;
        addRequirements(swerveDriveSubsystem);

    }

    public static void setInitialDegrees(double angle) {
        RotateToZero.initialDegrees = angle;
    }

    @Override
    public void execute() {
        double currentAngle = navXHandler.getAngle();
        double rotateCommand = angleController.calculate(currentAngle);

        if (rotateCommand > 0.2) {
            rotateCommand = 0.2;
        } else if (rotateCommand < -0.2) {
            rotateCommand = -0.2;
        }

        swerveDriveSubsystem.drive(0, 0, rotateCommand * -1);
        if (Math.abs(currentAngle - initialDegrees) < margin) {
            isFinished = true;
        }

    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void initialize() {
        angleController = new PIDController(.016, 0, 0);
        angleController.setSetpoint(initialDegrees);
    }

    @Override
    public void end(boolean interrupted) {
        swerveDriveSubsystem.drive(0, 0, 0);
    }

}
