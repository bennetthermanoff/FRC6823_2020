package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.JoystickHandler;
import frc.robot.NavXHandler;
import frc.robot.Robot;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class FieldSpaceDrive extends CommandBase {
    // The subsystem the command runs on
    private final SwerveDriveSubsystem swerveDrive;
    private JoystickHandler joystickHandler;
    private NavXHandler navXHandler;

    private double fieldAngle = 0;

    public FieldSpaceDrive(SwerveDriveSubsystem subsystem, JoystickHandler joystickHandler, NavXHandler navXHandler) {
        this.swerveDrive = subsystem;
        this.joystickHandler = joystickHandler;
        this.navXHandler = navXHandler;

        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        double speedRate = Robot.PREFS.getDouble("SpeedRate", 1);
        double turnRate = Robot.PREFS.getDouble("TurnRate", 1);

        double xval = joystickHandler.getAxis1() * speedRate;
        double yval = joystickHandler.getAxis0() * speedRate;
        double spinval = joystickHandler.getAxis5() * turnRate;

        double robotAngle = navXHandler.getAngleRad() - fieldAngle;

        // mapping field space to robot space
        double txval = getTransX(xval, yval, robotAngle);
        double tyval = getTransY(xval, yval, robotAngle);

        swerveDrive.drive(txval, tyval, spinval);// zoooooom
    }

    private double getTransX(double x, double y, double angle) {
        return x * Math.cos(angle) + -y * Math.sin(angle);
    }

    private double getTransY(double x, double y, double angle) {
        return x * Math.sin(angle) + y * Math.cos(angle);
    }

    public void zero() {
        this.fieldAngle = navXHandler.getAngleRad();
    }
}
