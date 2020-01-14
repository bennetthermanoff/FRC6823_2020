/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//hey look it's some code! Incredible
package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Preferences;

public class Robot extends TimedRobot {
  public static Preferences prefs;


  private Joystick joystick;
  private WheelDrive backRight, backLeft, frontLeft, frontRight;
  private SwerveDrive swerveDrive;
  private AnalogInput encoder0, encoder1, encoder2, encoder3;

  // swerve drive!

  @Override
  public void robotInit() {
    prefs = Preferences.getInstance();

    joystick = new Joystick(0); // creates new joystick


    encoder0 = new AnalogInput(0);
    encoder1 = new AnalogInput(1);
    encoder2 = new AnalogInput(2);
    encoder3 = new AnalogInput(3);

    backRight = new WheelDrive(5, 1, encoder3, -4.70);// These are the motors and encoder ports for swerve drive,
    backLeft = new WheelDrive(9, 2, encoder2, .884); // angle,speed,encoder
    frontRight = new WheelDrive(7, 3, encoder1, .697);
    frontLeft = new WheelDrive(8, 4, encoder0, .374);

    swerveDrive = new SwerveDrive(backRight, backLeft, frontRight, frontLeft); // This creates a
  }

  @Override
  public void teleopPeriodic() {
   // if(joystick.getRawButton(3)){
    //}
    if(joystick.getRawButton(12)){
      swerveDrive.drive(0,.2,0,1);
    }
    else if(joystick.getRawButton(9))
    swerveDrive.drive(0,0,0,0);
    else joystickDrive();
    
    if(joystick.getRawButton(11)){
      backLeft.getVoltages();
      backRight.getVoltages();
      frontLeft.getVoltages();
      frontRight.getVoltages();
    }
    
      frontLeft.setZero(prefs.getDouble("FLOffset", 0)+1.25);
      frontRight.setZero(prefs.getDouble("FROffset", 0)+1.25);
      backLeft.setZero(prefs.getDouble("BLOffset", 0)+1.25);
      backRight.setZero(prefs.getDouble("BROffset", 0)+1.25);
    
    
   
    
   // if(joystick.getRawButton(12)){
    //swerveDrive.drive(0, .2, 0, 1);}
    // drives the swervedrive!

    /**
     * prefs.putDouble("Encoder 0", encoder0.getVoltage()); prefs.putDouble("Encoder
     * 1", encoder1.getVoltage()); prefs.putDouble("Encoder 2",
     * encoder2.getVoltage()); prefs.putDouble("Encoder 3", encoder3.getVoltage());
     */
    // System.out.println(encoder0.getVoltage());

  }

  public void joystickDrive() {
    double speedRate = prefs.getDouble("SpeedRate", 1);
    double turnRate = prefs.getDouble("TurnRate", 1);
    swerveDrive.drive(joystick.getRawAxis(1), joystick.getRawAxis(0), joystick.getRawAxis(2) * turnRate, speedRate);

  }

}
