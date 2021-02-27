package frc.robot.commands;

//import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.NavXHandler;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class RotateLeftUntillSeeBall extends CommandBase {
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private LimeLightSubsystem limeLightSubsystem;
    //private NavXHandler navXHandler;
    private boolean isFinished = false;
    //private double newDirection;
    //private double margin = 0.5; // margin of degrees
    //private PIDController angleController;

    //private static double initialDegrees;

    public RotateLeftUntillSeeBall(SwerveDriveSubsystem swerveDriveSubsystem, LimeLightSubsystem limeLightSubsystem) {

        this.swerveDriveSubsystem = swerveDriveSubsystem;
        this.limeLightSubsystem = limeLightSubsystem;
        addRequirements(swerveDriveSubsystem, limeLightSubsystem);

    }

    @Override
    public void execute() {
        double rotateCommand = 0.2;

        swerveDriveSubsystem.drive(0, 0, rotateCommand * -1);
        if (limeLightSubsystem.hasTarget()) {
            isFinished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void initialize() {
        limeLightSubsystem.setPipeline(1);
    }

    @Override
    public void end(boolean interrupted) {
        swerveDriveSubsystem.drive(0, 0, 0);
    }
}