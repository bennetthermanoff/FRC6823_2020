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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

  private Joystick joystick = new Joystick(0); // creates new joystick

  private WheelDrive backRight = new WheelDrive(0, 1, 0);// These are the motors and encoder ports for swerve drive,
                                                         // CHANGE DEM

  private WheelDrive backLeft = new WheelDrive(2, 3, 1); // angle,speed,encoder
  private WheelDrive frontRight = new WheelDrive(4, 5, 2);
  private WheelDrive frontLeft = new WheelDrive(6, 7, 3);

  private SwerveDrive swerveDrive = new SwerveDrive(backRight, backLeft, frontRight, frontLeft); // This creates a
                                                                                                 // swerve drive!

  @Override
  public void robotInit() {

  }

  @Override
  public void teleopPeriodic() {
    joystickDrive(); // drives the swervedrive!

  }

  public void joystickDrive() {
    double speedRate = SmartDashboard.getNumber("SpeedRate", 1);
    double turnRate = SmartDashboard.getNumber("TurnRate", 1);
    swerveDrive.drive(joystick.getRawAxis(1), joystick.getRawAxis(0), joystick.getRawAxis(4) * turnRate, speedRate);
  }
}
