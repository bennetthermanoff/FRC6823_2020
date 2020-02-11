/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//hey look it's some code! Incredible
package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    public static Preferences prefs;

    private Joystick joystick;
    private WheelDrive backRight, backLeft, frontLeft, frontRight;
    private SwerveDrive swerveDrive;
    private AnalogInput encoder0, encoder1, encoder2, encoder3;
    private double joyStickAxis1, joyStickAxis0, joyStickAxis2;
    private NavXHandler navX;
    private LimeLight limeLight;
    private CANSparkMax intake, conveyor, leftShoot, rightShoot;
    private SpeedControllerGroup shooter;
    public static RGB rgb;

    private double fieldAngle = 0;

    private CANSparkMax launchBoi;

    // swerve drive!

    @Override
    public void robotInit() {
        prefs = Preferences.getInstance();

        joystick = new Joystick(0); // creates new joystick
        encoder0 = new AnalogInput(0); // encoders from swervedrive 0,1,2,3 = FL,FR,BL,BR
        encoder1 = new AnalogInput(1);
        encoder2 = new AnalogInput(2);
        encoder3 = new AnalogInput(3);
        backRight = new WheelDrive(7, 6, encoder3, -4.70);// These are the motors and encoder ports for swerve drive,
        backLeft = new WheelDrive(5, 4, encoder2, .884);
        frontRight = new WheelDrive(3, 2, encoder1, .697);
        frontLeft = new WheelDrive(1, 8, encoder0, .374);// angle,speed,encoder,offset (offset gets changed by
                                                         // smartdashboard in calibration.)
        rgb = new RGB(9);

        swerveDrive = new SwerveDrive(backRight, backLeft, frontRight, frontLeft);
        /*
         * frontLeft.setZero(prefs.getDouble("FLOffset", 0) + 1.25);
         * frontRight.setZero(prefs.getDouble("FROffset", 0) + 1.25);
         * backLeft.setZero(prefs.getDouble("BLOffset", 0) + 1.25);
         * backRight.setZero(prefs.getDouble("BROffset", 0) + 1.25);
         */ // This creates a swervedrive object, use it to
            // interact
            // with the swervedrive4
        if (!prefs.getBoolean("PracticeBot", false)) {
            frontLeft.setZero(prefs.getDouble("FLOffset", 0) + 1.25);
            frontRight.setZero(prefs.getDouble("FROffset", 0) + 1.25);
            backLeft.setZero(prefs.getDouble("BLOffset", 0) + 1.25);
            backRight.setZero(prefs.getDouble("BROffset", 0) + 1.25);
        } else {
            frontLeft.setZero(prefs.getDouble("FLOffsetPractice", 0) + 1.25);
            frontRight.setZero(prefs.getDouble("FROffsetPractice", 0) + 1.25);
            backLeft.setZero(prefs.getDouble("BLOffsetPractice", 0) + 1.25);
            backRight.setZero(prefs.getDouble("BROffsetPractice", 0) + 1.25);
        }

        // launchBoi = new CANSparkMax(0, MotorType.kBrushless);

        intake = new CANSparkMax(9, MotorType.kBrushed);
        conveyor = new CANSparkMax(10, MotorType.kBrushed);
        leftShoot = new CANSparkMax(11, MotorType.kBrushed);
        rightShoot = new CANSparkMax(13, MotorType.kBrushed);
        shooter = new SpeedControllerGroup(leftShoot, rightShoot);

        limeLight = new LimeLight(prefs); // limelight class
        prefs.putBoolean("DEBUG_MODE", false);
        navX = new NavXHandler();

        SmartDashboard.putBoolean("LemonPipeline", false);

    }

    @Override
    public void teleopPeriodic() {

        navX.printEverythingDammit(prefs);
        if (!prefs.getBoolean("PracticeBot", false)) {
            frontLeft.setZero(prefs.getDouble("FLOffset", 0) + 1.25);
            frontRight.setZero(prefs.getDouble("FROffset", 0) + 1.25);
            backLeft.setZero(prefs.getDouble("BLOffset", 0) + 1.25);
            backRight.setZero(prefs.getDouble("BROffset", 0) + 1.25);
        } else {
            frontLeft.setZero(prefs.getDouble("FLOffsetPractice", 0) + 1.25);
            frontRight.setZero(prefs.getDouble("FROffsetPractice", 0) + 1.25);
            backLeft.setZero(prefs.getDouble("BLOffsetPractice", 0) + 1.25);
            backRight.setZero(prefs.getDouble("BROffsetPractice", 0) + 1.25);
        }
        limeLight.updatePrefs(); // updates values to limelight class from SmartDashBoard

        // Joystick deadzone code
        deadZoneCalculate();

        // the following if statements are for "overide" modes. For example, when
        // running limelight code, normal joystick input is made unavailable.

        if (joystick.getRawButton(3)) { // limelight goTo Polar command, gets values from smartdashboard for testing
            double[] autoAim = limeLight.goToPolar(prefs.getDouble("PolarDistance", 50),
                    prefs.getDouble("PolarTheta", 0));
            swerveDrive.drive(autoAim[2] * .65, autoAim[1] * .1, autoAim[0] * .3);

        } else if (joystick.getRawButton(4)) { // limelight goTo coordinate command, gets values from smartdashboard for
                                               // testing
            double[] autoAim = limeLight.goTo(prefs.getDouble("CY", 50), prefs.getDouble("CX", 0));
            swerveDrive.drive(autoAim[2] * .65, autoAim[1] * .1, autoAim[0] * .3);

        } else if (joystick.getRawButton(12)) {
            swerveDrive.drive(0.2, 0, 0);// press button 12 to set the swerve just forward, this is for calibration
                                         // purposes

        } else if (joystick.getRawButton(6)) {
            double[] autoAim = limeLight.aimSteerAndStrafe();
            swerveDrive.drive(autoAim[2] * .4, autoAim[1] * .15, autoAim[0] * .1);

        } else if (joystick.getRawButton(10)) {
            fieldDrive();
        } else if (joystick.getRawButton(7)) {
            fieldDriveTargetLimelight();
        } else {
            joystickDrive();
            // fieldDrive();
            SmartDashboard.putBoolean("Shoot", false);
            rgb.setOff();
        }

        if (joystick.getRawButton(1))
            intake.set(prefs.getDouble("intakeSpeed", .05));
        else
            intake.set(0);

        if (joystick.getRawButton(8))
            fieldAngle = navX.getAngleRad();

        if (joystick.getRawButton(11)) {
            leftShoot.set(1);
            rightShoot.set(1);

        } else {
            leftShoot.set(0);
            rightShoot.set(0);

        }
        if (joystick.getRawButton(9))
            conveyor.set(prefs.getDouble("ConveyorSpeed", 0));
        else
            conveyor.set(0);

        /**
         * if (joystick.getTrigger()) { launchBoi.set(.5); } else { launchBoi.set(0); }
         */

        backLeft.getVoltages();
        backRight.getVoltages();
        frontLeft.getVoltages();
        frontRight.getVoltages();// outputs voltages to SmartDashBoard from each swerve module.

        // sets encoder offsets from smartdashboard. Also rotates
        // 90 degrees to account for which side is "forward".

    }

    // this method creates a deadzone on the joystick, so that minute movements near
    // the center of the joystick do not start robot movement.
    public void deadZoneCalculate() {
        double deadZone = prefs.getDouble("DeadZone", .1);
        if (Math.abs(joystick.getRawAxis(1)) < deadZone)
            joyStickAxis1 = 0;
        else
            joyStickAxis1 = joystick.getRawAxis(1);

        if (Math.abs(joystick.getRawAxis(2)) < deadZone)
            joyStickAxis2 = 0;
        else
            joyStickAxis2 = joystick.getRawAxis(2);

        if (Math.abs(joystick.getRawAxis(0)) < deadZone)
            joyStickAxis0 = 0;
        else
            joyStickAxis0 = joystick.getRawAxis(0);
    }

    // next two methods are for abondoned auto calibrate code for swerve drive.
    public void getValues() {
        frontLeft.setZero(0);
        frontRight.setZero(0);
        backLeft.setZero(0);
        backRight.setZero(0);
        swerveDrive.drive(.2, 0, 0);
        // voltages = new double[] { backLeft.getVoltages(), backRight.getVoltages(),
        // frontLeft.getVoltages(),
        // frontRight.getVoltages() };
    }

    // abandoned. maybe someone wants to make this work?
    public void calibrate(double[] array) {
        frontLeft.setZero(frontLeft.getVoltages());
        frontRight.setZero(frontRight.getVoltages());
        backLeft.setZero(backLeft.getVoltages());
        backRight.setZero(backRight.getVoltages());
        prefs.putDouble("FLOffset", frontLeft.getVoltages() - 2.5);
        prefs.putDouble("FROffset", frontRight.getVoltages() - 2.);
        prefs.putDouble("BLOffset", backLeft.getVoltages());
        prefs.putDouble("BROffset", backRight.getVoltages());
        // isCalibrate = false;
        frontRight.restart();
        frontLeft.restart();
        backLeft.restart();
        backRight.restart();

    }

    public void joystickDrive() { // drives the swervedrive, also gets speed rates from smartdashboard.
        double speedRate = prefs.getDouble("SpeedRate", 1);
        double turnRate = prefs.getDouble("TurnRate", 1);// rates are broken rn. Keep at 1 until marked as fixed or
                                                         // calculations will go bad.
        if (!joystick.getRawButton(2)) { // this is for reversing drive direction
            joyStickAxis0 *= -1;
            joyStickAxis1 *= -1;
        }

        swerveDrive.drive(joyStickAxis1 * speedRate, joyStickAxis0 * speedRate, joyStickAxis2 * turnRate);// zoooooom

    }

    public void fieldDrive() { // drives the swervedrive, also gets speed rates from smartdashboard.
        double speedRate = prefs.getDouble("SpeedRate", 1);
        double turnRate = prefs.getDouble("TurnRate", 1);// rates are broken rn. Keep at 1 until marked as fixed or
                                                         // calculations will go bad.
        if (!joystick.getRawButton(2)) { // this is for reversing drive direction
            joyStickAxis0 *= -1;
            joyStickAxis1 *= -1;
        }

        double xval = joyStickAxis1 * speedRate;
        double yval = joyStickAxis0 * speedRate;
        double spinval = joyStickAxis2 * turnRate;

        double txval = swerveDrive.getTransX(xval, yval, navX.getAngleRad() - fieldAngle);
        double tyval = swerveDrive.getTransY(xval, yval, navX.getAngleRad() - fieldAngle);

        swerveDrive.drive(txval, tyval, spinval);// zoooooom
    }

    public void fieldDriveTargetLimelight() { // drives the swervedrive, also gets speed rates from smartdashboard.
        double speedRate = prefs.getDouble("SpeedRate", 1);
        double turnRate = prefs.getDouble("TurnRate", 1);// rates are broken rn. Keep at 1 until marked as fixed or
                                                         // calculations will go bad.
        if (!joystick.getRawButton(2)) { // this is for reversing drive direction
            joyStickAxis0 *= -1;
            joyStickAxis1 *= -1;
        }

        double xval = joyStickAxis1 * speedRate;
        double yval = joyStickAxis0 * speedRate;

        double spinval = joyStickAxis2 * turnRate;

        double txval = swerveDrive.getTransX(xval, yval, navX.getAngleRad() - fieldAngle);
        double tyval = swerveDrive.getTransY(xval, yval, navX.getAngleRad() - fieldAngle);

        swerveDrive.drive(txval, tyval, limeLight.aim() * .25);// zoooooom
    }
}
