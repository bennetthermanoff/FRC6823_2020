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

    private PIDController distController, aimController;
    private double y;

    private long whenStartedGorging;
    private int stage; // 0=noteating, 1=spinningwithoutball, 2=chomping

    public LimeLightPickupBall(SwerveDriveSubsystem swerveDriveSubsystem, ShooterSubsystem shooterSubsystem,
            LimeLightSubsystem limeLightSubsystem, double y) {

        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.limeLightSubsystem = limeLightSubsystem;

        distController = new PIDController(.008, 0, 0);
        aimController = new PIDController(.008, 0, 0);

        this.y = y;
        stage = 0;

        addRequirements(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem);
    }

    @Override
    public void execute() {
        double distanceCommand = distController.calculate(limeLightSubsystem.getTy());
        double aimCommand = aimController.calculate(limeLightSubsystem.getTx());

        if (stage == 0) {
            // far from ball, need to move towards it using limelight
            swerveDriveSubsystem.drive(distanceCommand, 0, aimCommand * -1);

            if (Math.abs(distController.getPositionError()) < 1) {
                stage = 1;
                whenStartedGorging = System.currentTimeMillis();
                shooterSubsystem.startIntakeSpin();
            }
        } else if (stage == 1) {
            // close to ball, move towards it despite not seeing it
            swerveDriveSubsystem.drive(-.1, 0, 0);

            if (shooterSubsystem.doesSenseBall() == true) {
                stage = 2;
            }

            // stop after 2 seconds
            if (System.currentTimeMillis() - whenStartedGorging > 1000) {
                stage = 0;
                shooterSubsystem.stopIntakeSpin();
            }
        } else if (stage == 2) {
            // sensor has ball, eating it
            if (shooterSubsystem.doesSenseBall() == false) {
                stage = 0;
                shooterSubsystem.stopIntakeSpin();
            }
        }

    }

    @Override
    public void initialize() {
        limeLightSubsystem.setPipeline(1);

        distController.setSetpoint(y);
        aimController.setSetpoint(0);

        stage = 0;
    }

    @Override
    public boolean isFinished() {
        return false;
        // if (Math.abs(distController.getPositionError()) < 5 &&
        // Math.abs(aimController.getPositionError()) < 2) {
        // return true;
        // } else {
        // return false;
        // }
    }

    @Override
    public void end(boolean interrupted) {
        swerveDriveSubsystem.drive(0, 0, 0);
        shooterSubsystem.stopIntakeSpin();
    }
}