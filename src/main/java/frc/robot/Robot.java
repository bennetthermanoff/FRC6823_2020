/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//hey look it's some code! Incredible
package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
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
  private double joyStickAxis1, joyStickAxis0, joyStickAxis2;
  private boolean isBackward;
  private LimeLightAutoAimAutoSteer limeLightAutoAimAutoSteer;
  private double[] voltages;
  boolean isCalibrate;

  // swerve drive!

  @Override
  public void robotInit() {
    prefs = Preferences.getInstance();
    isBackward = false;
    joystick = new Joystick(0); // creates new joystick

    encoder0 = new AnalogInput(0); // encoders from swervedrive 0,1,2,3 = FL,FR,BL,BR
    encoder1 = new AnalogInput(1);
    encoder2 = new AnalogInput(2);
    encoder3 = new AnalogInput(3);

    backRight = new WheelDrive(5, 1, encoder3, -4.70);// These are the motors and encoder ports for swerve drive,
    backLeft = new WheelDrive(6, 2, encoder2, .884);
    frontRight = new WheelDrive(7, 3, encoder1, .697);
    frontLeft = new WheelDrive(8, 4, encoder0, .374);// angle,speed,encoder,offset (offset gets changed by
                                                     // smartdashboard in calibration.)

    swerveDrive = new SwerveDrive(backRight, backLeft, frontRight, frontLeft);
    frontLeft.setZero(prefs.getDouble("FLOffset", 0) + 1.25);
    frontRight.setZero(prefs.getDouble("FROffset", 0) + 1.25);
    backLeft.setZero(prefs.getDouble("BLOffset", 0) + 1.25);
    backRight.setZero(prefs.getDouble("BROffset", 0) + 1.25); // This creates a swervedrive object, use
    // it to interact with the swervedrive
    limeLightAutoAimAutoSteer = new LimeLightAutoAimAutoSteer(prefs);
    isCalibrate=false;

  }

  @Override
  public void teleopPeriodic() {
    frontLeft.setZero(prefs.getDouble("FLOffset", 0) + 1.25);
    frontRight.setZero(prefs.getDouble("FROffset", 0) + 1.25);
    backLeft.setZero(prefs.getDouble("BLOffset", 0) + 1.25);
    backRight.setZero(prefs.getDouble("BROffset", 0) + 1.25);                  

    limeLightAutoAimAutoSteer.updatePrefs();

    // Joystick deadzone code
    
    //  frontLeft.setZero(prefs.getDouble("FLOffset", 0));
    //frontRight.setZero(prefs.getDouble("FROffset", 0));
    //backLeft.setZero(prefs.getDouble("BLOffset", 0));
    //backRight.setZero(prefs.getDouble("BROffset", 0));
    if (joystick.getRawButton(3)) {
      double[] autoAim = limeLightAutoAimAutoSteer.aimAndSteer();
      swerveDrive.drive( autoAim[1] * .2,0, autoAim[0] * .3, 1);
    
     if (joystick.getRawButton(4)){
      swerveDrive.drive(0, limeLightAutoAimAutoSteer.strafe()*.15, 0, 1);
      
    }
  }
    else  {

      limeLightAutoAimAutoSteer.strafeDebug();

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

      if (!joystick.getRawButton(2)) {
        joyStickAxis0 *= -1;
        joyStickAxis1 *= -1;
        // joyStickAxis2*=-1;
      }

      if (joystick.getRawButton(12)) {
        swerveDrive.drive(0.2, 0, 0, 1);// press button 12 to set the swerve just forward, this is for calibration
                                       // purposes.

      } else if (joystick.getRawButton(9))
        swerveDrive.drive(0, 0, 0, 0); // This is the swerve "STOP" button, it will tell the swerve to immidiately stop
                                       // and set all motors to brake.

      else
        joystickDrive();
    }
   
    
  
    backLeft.getVoltages();
    backRight.getVoltages();
    frontLeft.getVoltages();
    frontRight.getVoltages();// outputs voltages to SmartDashBoard from each swerve module.

    // sets encoder offsets from smartdashboard. Also rotates
    // 90 degrees to account for which side is "forward".

  
}

  public void getValues(){
    frontLeft.setZero(0);
    frontRight.setZero(0);
    backLeft.setZero(0);
    backRight.setZero(0);
    swerveDrive.drive(.2, 0, 0, 1);
    voltages = new double[] {
      backLeft.getVoltages(),
      backRight.getVoltages(),
      frontLeft.getVoltages(),
      frontRight.getVoltages()};
  }
  public void calibrate(double[] array){
    frontLeft.setZero(frontLeft.getVoltages());
    frontRight.setZero(frontRight.getVoltages());
    backLeft.setZero(backLeft.getVoltages());
    backRight.setZero(backRight.getVoltages());
    prefs.putDouble("FLOffset", frontLeft.getVoltages()-2.5);
    prefs.putDouble("FROffset", frontRight.getVoltages()-2.);
    prefs.putDouble("BLOffset", backLeft.getVoltages());
    prefs.putDouble("BROffset", backRight.getVoltages());
    isCalibrate = false;
    frontRight.restart();
    frontLeft.restart();
    backLeft.restart();
    backRight.restart();
    
  }
  

  public void joystickDrive() { // drives the swervedrive, also gets speed rates from smartdashboard.
    double speedRate = prefs.getDouble("SpeedRate", 1);
    double turnRate = prefs.getDouble("TurnRate", 1);// rates are broken rn. Keep at 1 until marked as fixed or
                                                     // calculations will go bad.
    swerveDrive.drive(joyStickAxis1 * speedRate, joyStickAxis0 * speedRate, joyStickAxis2 * turnRate, 1);

  }

}
