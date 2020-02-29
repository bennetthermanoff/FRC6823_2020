package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Robot;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class LongRange2d extends CommandBase {

    private SwerveDriveSubsystem swerveDriveSubsystem;
    private LimeLightSubsystem limeLightSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private PIDController aimController;

    public LongRange2d(SwerveDriveSubsystem swerveDriveSubsystem, LimeLightSubsystem limeLightSubsystem,
            ShooterSubsystem shooterSubsystem) {
        this.limeLightSubsystem = limeLightSubsystem;
        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.addRequirements(limeLightSubsystem, swerveDriveSubsystem);
    }

    @Override
    public void execute() {
        // if (!limeLightSubsystem.hasTarget())
        // return;

        double aimCommand = aimController.calculate(limeLightSubsystem.getTx());

        swerveDriveSubsystem.drive(0, 0, aimCommand * -1);

        limeLightSubsystem.setServoAngle(limeLightSubsystem.getServoAngle() + .1 * limeLightSubsystem.getTy());
    }

    @Override
    public void initialize() {
        limeLightSubsystem.setPipeline(2);
        aimController = new PIDController(.01, 0, 0);
        aimController.setSetpoint(0);
    }

    @Override
    public boolean isFinished() {
        return !limeLightSubsystem.hasTarget()
                && (Math.abs(aimController.getPositionError()) < .2 && Math.abs(limeLightSubsystem.getTy()) < .2);
    }

    @Override
    public void end(boolean interrupted) {
        swerveDriveSubsystem.drive(0, 0, 0);
        int rpm = (int) Robot.PREFS.getDouble("RPMControl", 0);
        double t = limeLightSubsystem.getServoAngle();
        // int rpm = (int) (.0233 * Math.pow(t, 4) - 189.39 * Math.pow(t, 3) + 16304 * t
        // * t - 622601 * t
        // + 9 * Math.pow(10, 6));// (int) limeLightSubsystem.getServoAngle(); // PUT
        // FUNCTION
        // HERE
        CommandScheduler.getInstance().schedule(new Shoot(shooterSubsystem, rpm, .35, 5));// ,
        // new JustAim(swerveDriveSubsystem, limeLightSubsystem)));
    }
}