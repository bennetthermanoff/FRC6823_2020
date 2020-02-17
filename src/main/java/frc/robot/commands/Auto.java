package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.LimeLightHandler;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class Auto extends CommandBase {

    private SwerveDriveSubsystem swerveDriveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private LimeLightHandler limeLightHandler;
    private Timer timer;

    public Auto(SwerveDriveSubsystem swerveDriveSubsystem, ShooterSubsystem shooterSubsystem,
            LimeLightHandler limeLightHandler) {
        this.limeLightHandler = limeLightHandler;
        this.shooterSubsystem = shooterSubsystem;
        this.swerveDriveSubsystem = swerveDriveSubsystem;
    }

    @Override
    public void execute() {
        double[] autoAim = limeLightHandler.programmedDistances(0);
        swerveDriveSubsystem.drive(autoAim[2] * .15, autoAim[1] * .1, autoAim[0] * .25);
        shooterSubsystem.shooterPID();
    }

    @Override
    public void initialize() {
        timer = new Timer();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasPeriodPassed(5))
            return true;
        else
            return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stopShooterSpin();
        swerveDriveSubsystem.drive(0, 0, 0);
    }
}