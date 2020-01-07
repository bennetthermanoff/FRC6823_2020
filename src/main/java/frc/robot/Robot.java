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
  public void teleopPeriodic() {
    speedRate = prefs.getDouble("SpeedRate", 1);
    turnRate = prefs.getDouble("TurnRate", 1);
    driveTrain.arcadeDrive((-driveStick.getRawAxis(1)) * speedRate, driveStick.getTwist() * turnRate);

    // get limelight values
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double skew = ts.getDouble(0.0);
    SmartDashboard.putNumber("x", x);
    SmartDashboard.putNumber("y", y);
    SmartDashboard.putNumber("area", area);
    SmartDashboard.putNumber("skew", skew);

    // Start of autoaim/autodistance code
    double steering_adjust = 0.0;
    double KpAim = prefs.getDouble("KpAim", -.2); // Speed constant for aiming
    double KpDistance = prefs.getDouble("KpDistance", -.35);// speed constant for distance
    double min_aim_command = prefs.getDouble("min_aim_command", .05);// If aim command is less than minAngle, robot will purposefully overshoot so it can
    // come in hotter, creating more accuracy. If too high robot will tictac, if too
    // low it will be inaccurate.
    double low_min_aim_command = prefs.getDouble("low_min_aim_command", 3);

    //double KpAim = -.5;
    //double KpDistance = -0.6; 
    //double min_aim_command = 0.1; 

    if (driveStick.getRawButton(3)) {
      double heading_error = -1 * x;
      double distance_error = -1 * y;
    
      if (x > 1.0) {
        steering_adjust = KpAim * heading_error + min_aim_command; // this gets just the steering adjustment. If the
                                                                   // angle is larger than 1 then it removes the
                                                                   // minimum.
      } 
      else if (x < 1) {
        steering_adjust = KpAim * heading_error - min_aim_command; // If angle is less than 1 it adds the min aim
                                                                   // command.
      }

      double distance_adjust = KpDistance * distance_error;

      double left_command = 0.0;
      double right_command = 0.0;// check if these are needed. Are you supposed to let these just keep going? Or
                                 // reset them to 0 every time? If so, consider moving these before the if to
                                 // condense code.

      left_command = steering_adjust - distance_adjust;
      right_command = steering_adjust*-1 - distance_adjust;
      // System.out.println(left_command); //For debugging purposes
      // System.out.println(right_command);
      driveTrain.tankDrive(left_command, right_command);
    }
    if (driveStick.getRawButton(5)) {
      double heading_error = -1 * x;
      double distance_error = -1 * y;
    
      if (x > 1.0 && x<10) {
        steering_adjust = KpAim * heading_error + low_min_aim_command; // this gets just the steering adjustment. If the
                                                                   // angle is larger than 1 then it removes the
                                                                   // minimum.
      } 
      else if (x < 1 && x > -10) {
        steering_adjust = KpAim * heading_error - low_min_aim_command; // If angle is less than 1 it adds the min aim
                                                                   // command.
      }

      double distance_adjust = KpDistance * distance_error;

      double left_command = 0.0;
      double right_command = 0.0;// check if these are needed. Are you supposed to let these just keep going? Or
                                 // reset them to 0 every time? If so, consider moving these before the if to
                                 // condense code.

      left_command = steering_adjust - distance_adjust;
      right_command = steering_adjust*-1 - distance_adjust;
      // System.out.println(left_command); //For debugging purposes
      // System.out.println(right_command);
      driveTrain.tankDrive(left_command, right_command);
    }

  }
}