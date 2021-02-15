package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.WheelDrive;

public class SwerveDriveSubsystem extends SubsystemBase {
    /**
     * What does this class do? some weird math to take controller inputs and
     * convert them into rotation and speed values for each motor (which is
     * controlled via the WheelDrive class)
     * <p>
     * This code heavily attributed from Jacob Misirian of FIRST Robotics Team 2506
     * of Franklin, WI.
     */
    public final double L = 1; //
    public final double W = 1; // These are from the Length and Width between wheels. CHANGE THESE IF YOUR
    // ROBOT IS NOT A SQUARE

    private SwerveWheelModuleSubsystem backRight;
    private SwerveWheelModuleSubsystem backLeft;
    private SwerveWheelModuleSubsystem frontRight;
    private SwerveWheelModuleSubsystem frontLeft;

    public SwerveDriveSubsystem() {
        backRight = new SwerveWheelModuleSubsystem(7, 6, 3, -4.70);// These are the motors and encoder ports for swerve
                                                                   // drive,
        backLeft = new SwerveWheelModuleSubsystem(5, 4, 2, .884);
        frontRight = new SwerveWheelModuleSubsystem(3, 2, 1, .697);
        frontLeft = new SwerveWheelModuleSubsystem(1, 8, 0, .800);// angle,speed,encoder,offset (offset gets changed by
        // smartdashboard in calibration.)

        SendableRegistry.addChild(this, backRight);
        SendableRegistry.addChild(this, backLeft);
        SendableRegistry.addChild(this, frontRight);
        SendableRegistry.addChild(this, frontLeft);

        SendableRegistry.addLW(this, "Swerve Drive Subsystem");

    }

    public void drive(double x1, double y1, double x2) {
        // x1, y1 are from the position of the joystick, x2 is from the rotation

        double r = Math.sqrt((L * L) + (W * W));
        // y1 *= -1;
        // x1 *= -1;

        double a = x1 - x2 * (L / r);
        double b = x1 + x2 * (L / r);
        double c = y1 - x2 * (W / r);
        double d = y1 + x2 * (W / r);

        double backRightSpeed = Math.sqrt((b * b) + (c * c));//
        double backLeftSpeed = Math.sqrt((a * a) + (c * c));
        double frontRightSpeed = Math.sqrt((b * b) + (d * d));
        double frontLeftSpeed = Math.sqrt((a * a) + (d * d));//

        double backRightAngle = Math.atan2(b, c) / Math.PI;
        double backLeftAngle = Math.atan2(a, c) / Math.PI;
        double frontRightAngle = Math.atan2(b, d) / Math.PI;
        double frontLeftAngle = Math.atan2(a, d) / Math.PI;

        backRight.drive(backRightSpeed, backRightAngle);
        backLeft.drive(backLeftSpeed, backLeftAngle);
        frontRight.drive(frontRightSpeed, frontRightAngle);
        frontLeft.drive(frontLeftSpeed, frontLeftAngle);
    }

    @Override
    public void periodic() {
        if (Robot.PREFS.getBoolean("PracticeBot", false)) {
            backRight.setZero(Robot.PREFS.getDouble("BROffsetPractice", 0) + 1.25);
            backLeft.setZero(Robot.PREFS.getDouble("BLOffsetPractice", 0) + 1.25);
            frontRight.setZero(Robot.PREFS.getDouble("FROffsetPractice", 0) + 1.25);
            frontLeft.setZero(Robot.PREFS.getDouble("FLOffsetPractice", 0) + 1.25);
        } else {
            backRight.setZero(Robot.PREFS.getDouble("BROffset", 0) + 1.25);
            backLeft.setZero(Robot.PREFS.getDouble("BLOffset", 0) + 1.25);
            frontRight.setZero(Robot.PREFS.getDouble("FROffset", 0) + 1.25);
            frontLeft.setZero(Robot.PREFS.getDouble("FLOffset", 0) + 1.25);
        }
    }
}
