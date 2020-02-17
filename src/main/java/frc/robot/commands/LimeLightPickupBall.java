package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.LimeLightHandler;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class LimeLightPickupBall extends CommandBase {
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private LimeLightSubsystem limelightSubsystem;

    private PIDController distance;
    private PIDController twist;

    public LimeLightPickupBall(SwerveDriveSubsystem swerveDriveSubsystem, ShooterSubsystem shooterSubsystem,
            LimeLightSubsystem limelightSubsystem) {

        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.limelightSubsystem = limelightSubsystem;

        addRequirements(swerveDriveSubsystem, shooterSubsystem, limelightSubsystem);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void execute() {
        double x = limelightSubsystem.getTx();
        double y = limelightSubsystem.getTy();

    }
}