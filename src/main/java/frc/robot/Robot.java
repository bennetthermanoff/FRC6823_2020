/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//hey look it's some code! Incredible
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * This is a small bit of code for using tank drive with 4 NEO motors with
 * SparkMAX's in CAN mode. (What a great jumping off point for some limelight
 * testing!) - Bennett H. zucc
 */
public class Robot extends TimedRobot {
  private DifferentialDrive driveTrain;
  private Joystick driveStick;
  private Talon left, right;
  private double speedRate, turnRate;
  private Preferences prefs;
  private NetworkTable table;
  private NetworkTableEntry tx, ty, ta, ts;
  LimelightAutoAimAutoSteer autoSteer;

  @Override
  public void robotInit() {
    left = new Talon(1);

    right = new Talon(0);

    /**
     * The numbers 1-4 are the CAN id's for the SparkMAX's, configure their ID's via
     * plugging the SparkMAX via USB and using their software.
     **/

    driveTrain = new DifferentialDrive(left, right);
    driveStick = new Joystick(0);
    prefs = Preferences.getInstance();
    // setsup limelight table values

    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
    ts = table.getEntry("ts");
  }

  @Override
  public void teleopInit() {
    speedRate = prefs.getDouble("SpeedRate", 1);
    turnRate = prefs.getDouble("TurnRate", 1);

    double KpDistance = prefs.getDouble("KpDistance", .18); // speed in which distance is adjusted when autoaiming
    double KpAim = prefs.getDouble("KpAim", -.036); // speed in which aiming is adjusted when autoaiming
    double min_aim_command = prefs.getDouble("min_aim_command", .1); // minimum command sent to motors when coarse
                                                                     // autoaiming
    double low_min_aim_command = prefs.getDouble("low_min_aim_command", .3); // minimum command sent to motors when fine
    double angleCutoff = prefs.getDouble("angleCutoff", 10); // cutoff angle where fine autoaim ceases to activate.

    autoSteer = new LimelightAutoAimAutoSteer(KpDistance, KpAim, min_aim_command, low_min_aim_command, angleCutoff);
  }

  @Override
  public void teleopPeriodic() {
    // get limelight values and output to smartdashboard
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double skew = ts.getDouble(0.0);

    SmartDashboard.putNumber("x", x);
    SmartDashboard.putNumber("y", y);
    SmartDashboard.putNumber("area", area);
    SmartDashboard.putNumber("skew", skew);

    // Start of autoaim/autodistance code
    driveTrain.arcadeDrive((-driveStick.getRawAxis(1)) * speedRate, driveStick.getTwist() * turnRate); // drives

    if (driveStick.getRawButton(3)) {
      double[] coarseControl = autoSteer.coarseControl(x, y); // gets values from autosteer Coarse adjustment
      driveTrain.tankDrive(coarseControl[0], coarseControl[1]);
    } // activates coarse autosteer
    if (driveStick.getRawButton(5)) {
      double[] fineControl = autoSteer.fineControl(x, y); // gets values from autosteer fine adjustment
      driveTrain.tankDrive(fineControl[0], fineControl[1]);
    } // activates fine autosteer

    if (driveStick.getRawButton(5)) {
      double[] fineControl = autoSteer.fineControl(x, y); // gets values from autosteer fine adjustment
      driveTrain.tankDrive(fineControl[0], fineControl[1]);
    } // activates fine autosteer

  }
}
