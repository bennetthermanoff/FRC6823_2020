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
import edu.wpi.first.wpilibj.controller.PIDController;

import edu.wpi.first.wpilibj.util.Color;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.PWMVictorSPX;

import com.revrobotics.ColorSensorV3; // Color sensor

/**
 * This is a small bit of code for using tank drive with 4 NEO motors with
 * SparkMAX's in CAN mode. (What a great jumping off point for some limelight
 * testing!) - Bennett H. zucc
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
  private double red;
  private double green;
  private double blue;
  // wheel of fortune
  private PWMVictorSPX spinner;

  private double error = 0.04;
  private String[] colors = { "blue", "green", "red", "yellow" };
  private int colorSelection = 0;
  private int clockwise = 1;
  private double distanceMotorSpins = 0;

  // PID Controller Related Shit
  private PIDController pidcontroller = new PIDController(0.2, 0, 0);

  @Override
  public void robotInit() {
    spinner = new PWMVictorSPX(5); // placeholder

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
    pidcontroller.enableContinuousInput(0, 1);

  }

  @Override
  public void teleopPeriodic() {
    speedRate = SmartDashboard.getNumber("SpeedRate", 1);
    turnRate = SmartDashboard.getNumber("TurnRate", 1);
    driveTrain.arcadeDrive((-driveStick.getRawAxis(1)) * speedRate, driveStick.getTwist() * turnRate);

    // Color Sensor

    Color detectedColor = colorSensor.getColor();
    red = detectedColor.red;
    green = detectedColor.green;
    blue = detectedColor.blue;
    // cycles through the colors
    if (driveStick.getRawButtonPressed(10)) {
      if (colorSelection == colors.length - 1) {
        colorSelection = 0;
      } else {
        colorSelection++;
      }

    }

    if (driveStick.getRawButtonPressed(11)) {
      distanceMotorSpins = NextDistanceSpun();
      spinner.set(distanceMotorSpins / 2);// you can tell I have no idea what I am doing
    }
    SmartDashboard.putString("Looking for", colors[colorSelection]);
    SmartDashboard.putString("Color I see", colorSeen());
    SmartDashboard.putString("Color selected", colorSelected());
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Distance motor spins", distanceMotorSpins);

    // PID Color Controller
    if (driveStick.getRawButtonPressed(12)) {

    }
  }

  // returns if the rgb of two colors is within the errorValue
  public boolean closeEnough(String color) {
    if (color.equals("red")) {
      return Math.abs(red - 0.507568) <= error && Math.abs(green - 0.355225) <= error
          && Math.abs(blue - 0.136963) <= error;
    } else if (color.equals("green")) {
      return Math.abs(red - 0.163574) <= error && Math.abs(green - 0.584473) <= error
          && Math.abs(blue - 0.251953) <= error;
    } else if (color.equals("blue")) {
      return Math.abs(red - 0.118164) <= error && Math.abs(green - 0.426758) <= error
          && Math.abs(blue - 0.455078) <= error;
    } else {
      return Math.abs(red - 0.312256) <= error && Math.abs(green - 0.566162) <= error
          && Math.abs(blue - 0.121338) <= error;
    }
  }

  // returns the color the color sensor see
  public String colorSeen() {
    for (int i = 0; i < colors.length; i++) {
      if (closeEnough(colors[i])) {
        return colors[i];
      }
    }
    return "idk";
  }

  public String colorSelected() {
    if (colorSeen().equals("red")) {
      return "blue";
    } else if (colorSeen().equals("blue")) {
      return "red";
    } else if (colorSeen().equals("green")) {
      return "yellow";
    } else {
      return "green";
    }
  }

  public double convertToNumber(String color) {
    if (color.equals("yellow")) {
      return 0;
    } else if (color.equals("blue")) {
      return 0.25;
    } else if (color.equals("green")) {
      return 0.5;
    } else {
      return 0.75;
    }
  }

  public double NextDistanceSpun() {
    double setpoint = convertToNumber(colors[colorSelection]);
    pidcontroller.setSetpoint(setpoint);
    return pidcontroller.calculate(convertToNumber(colorSelected()), setpoint) * 12.5;

    /**
     * if (colors[colorSelection].equals("blue")) { if
     * (colorSelected().equals("blue")) { distance += 50; } else if
     * (colorSelected().equals("green")) { distance += 37.5; } else if
     * (colorSelected().equals("red")) { distance += 25; } else if
     * (colorSelected().equals("yellow")) { distance += 12.5;
     * 
     * } } else if (colors[colorSelection].equals("green")) { if
     * (colorSelected().equals("blue")) { distance += 12.5; } else if
     * (colorSelected().equals("green")) { distance += 50; } else if
     * (colorSelected().equals("red")) { distance += 37.5; } else if
     * (colorSelected().equals("yellow")) { distance += 25; } } else if
     * (colors[colorSelection].equals("red")) { if (colorSelected().equals("blue"))
     * { distance += 25; } else if (colorSelected().equals("green")) { distance +=
     * 12.5; } else if (colorSelected().equals("red")) { distance += 50; } else if
     * (colorSelected().equals("yellow")) { distance += 37.5; } } else { if
     * (colorSelected().equals("blue")) { distance += 37.5; } else if
     * (colorSelected().equals("green")) { distance += 25; } else if
     * (colorSelected().equals("red")) { distance += 12.5; } else { distance += 50;
     * } } return distance * clockwise;
     **/

  }
}