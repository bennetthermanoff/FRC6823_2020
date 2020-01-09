/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//hey look it's some code! Incredible
package frc.robot;

//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3; // Color sensor

/**
 * This is a small bit of code for using tank drive with 4 NEO motors with SparkMAX's in CAN mode. 
 * (What a great jumping off point for some limelight testing!) - Bennett H. zucc
 */
public class Robot extends TimedRobot {
  private DifferentialDrive driveTrain;
  private Joystick driveStick;
  private Talon left, right;
 // private CANSparkMax left1CAN, left2CAN, right1CAN, right2CAN;
  private double speedRate, turnRate;
  private I2C.Port i2cPort = I2C.Port.kOnboard;
  private ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  private NetworkTableEntry tx, ty, ta, ts;
  private NetworkTable table;
  private Preferences prefs;



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
    speedRate = SmartDashboard.getNumber("SpeedRate", 1);
    turnRate  = SmartDashboard.getNumber("TurnRate",  1);
    driveTrain.arcadeDrive((-driveStick.getRawAxis(1))*speedRate, driveStick.getTwist()*turnRate);
    
    // Color Sensor

    Color detectedColor = colorSensor.getColor();

    double IR = colorSensor.getIR();

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    System.out.println("Color: " + IR);
  }
  //returns if the rgb of two colors is within the errorValue
  public boolean closeEnough(Color colorSensor, Color targetColor){
    int errorValue = 10;
    if (Math.abs(colorSensor.red - targetColor.red) <= errorValue && Math.abs(colorSensor.blue - targetColor.blue) <= errorValue && Math.abs(colorSensor.green - targetColor.green) <= errorValue){
      return true;
    }
    else{
      return false;
    }
  }
}  