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
    //setsup limelight table values
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

    //get limelight values
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double skew = ts.getDouble(0.0);
    SmartDashboard.putNumber("x",x);
    SmartDashboard.putNumber("y", y);
    SmartDashboard.putNumber("area", area);
    SmartDashboard.putNumber("skew", skew);

    float KpAim = -0.1f;
float KpDistance = -0.03f;
float min_aim_command = 0.01f;
float xfloat =(float) (x/360.0*60);
float yfloat = (float) (y/360.0*60);
if (driveStick.getRawButton(3)) 
{
        double heading_error = -1*xfloat;
        double distance_error = -1*xfloat;
        double steering_adjust = 0.0;

        if (xfloat > 1.0)
        {
                steering_adjust = KpAim*heading_error - min_aim_command;
        }
        else if (xfloat < 1.0)
        {
                steering_adjust = KpAim*heading_error + min_aim_command;
        }

        double distance_adjust = KpDistance * distance_error;

        float left_command = 0.0f;
        float right_command = 0.0f;

        left_command += (float)steering_adjust + (float)distance_adjust;
        right_command -= (float)steering_adjust + (float)distance_adjust;
        System.out.println(left_command);
        System.out.println(right_command);
       // driveTrain.tankDrive(left_command*2, right_command*2);
}


  }
}