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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;

/**
 * This is a small bit of code for using tank drive with 4 NEO motors with SparkMAX's in CAN mode. 
 * (What a great jumping off point for some limelight testing!) - Bennett H. zucc
 */
public class Robot extends TimedRobot {
  private DifferentialDrive driveTrain;
  private Joystick driveStick;
  private CANSparkMax left1CAN, left2CAN, right1CAN, right2CAN;
  private double speedRate, turnRate;

  @Override
  public void robotInit() {
    left1CAN  = new Talon();
    left2CAN  = new Talon();
    right1CAN = new Talon();
    right2CAN = new Talon();   
    /**  The numbers 1-4 are the CAN id's for the SparkMAX's, configure their ID's via 
     *   plugging the SparkMAX via USB and using their software.**/

    SpeedControllerGroup driveTrainLeft  = new SpeedControllerGroup(left1CAN,  left2CAN);
    SpeedControllerGroup driveTrainRight = new SpeedControllerGroup(right1CAN, right2CAN); //groups motors into one virtual ESC for each side.

    driveTrain = new DifferentialDrive(driveTrainLeft, driveTrainRight);
    
    

  }

  @Override
  public void teleopPeriodic() {
    speedRate = SmartDashboard.getNumber("SpeedRate", 1);
    turnRate  = SmartDashboard.getNumber("TurnRate",  1);
    driveTrain.arcadeDrive((-driveStick.getRawAxis(1))*speedRate, driveStick.getTwist()*turnRate);
  }
}