package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class LimeLightPickupBall extends CommandBase {
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private LimeLightSubsystem limeLightSubsystem;

    private PIDController strafeController, distController, aimController;
    private double x, z;

    public LimeLightPickupBall(SwerveDriveSubsystem swerveDriveSubsystem, ShooterSubsystem shooterSubsystem,
            LimeLightSubsystem limeLightSubsystem) {

        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.limeLightSubsystem = limeLightSubsystem;

        addRequirements(swerveDriveSubsystem, shooterSubsystem, limelightSubsystem);
    }

    @Override
    public void execute() {
        double strafeCommand = strafeController.calculate(limeLightSubsystem.getX());
        double distanceCommand = distController.calculate(limeLightSubsystem.getZ());
        double aimCommand = aimController.calculate(limeLightSubsystem.getTx());

        swerveDriveSubsystem.drive(distanceCommand * -1, strafeCommand, aimCommand * -1);
    }

    @Override
    public void initialize() {
        limeLightSubsystem.setPipeline(1);

        strafeController = new PIDController(.01, 0, 0);
        distController = new PIDController(.015, 0, 0);
        aimController = new PIDController(.008, 0, 0);

        strafeController.setSetpoint(x);
        distController.setSetpoint(z);
        aimController.setSetpoint(0);

    }

    @Override
    public boolean isFinished() {
        if (Math.abs(strafeController.getPositionError()) < 5 && Math.abs(distController.getPositionError()) < 5
                && Math.abs(aimController.getPositionError()) < 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        swerveDriveSubsystem.drive(0, 0, 0);
    }
}