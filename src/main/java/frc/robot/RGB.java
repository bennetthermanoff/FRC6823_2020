/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.Spark;

public class RGB extends Spark{
  // you could control it like a spark motor, but why would you want to?
  /**
   * Makes a new RGB
   * @param channel the pwm the rgb is on
   */
  public RGB(int channel) {
    super(channel);
  }

  // based of https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf (BLINKIN
  // LED DRIVER USER'S MANUAL) pages 14 - 17
  public void setRed(){
      set(0.61);
  }
    public void setBlue(){
      set(0.87);
  }
  public void setGreen(){
      set(0.77);
  }
  public void setYellow() {
    set(0.69);
  }
  public void setRainbowPartyPallet() {
    set(-0.97);
  }
  public void setRainbowForestPallet(){
      set(-0.91);
  }
  public void setRainbowOceanPallet() {
    set(-0.95);
  }
  public void setHeartbeatRed() {
    set(-0.25);
  }

  /**
   * Set the rgb to black (off)
   */
  public void setOff() {
    set(0.99);
  }
  /**
   * Set the rgb to color1 color2 waves (look at the rev blinkin and they should be blue and yellow)
   */
  public void normalMode(){
    set(0.41);
  }

  public void setLimeLight(boolean locked) {
    if (!locked)
      setRainbowForestPallet();
    else
      setHeartbeatRed();
  }
  private OverEngineering thread = new OverEngineering(this);
  /**
   * a message in morse code
   */
  public void graciousProfesionalism() {
    if(!thread.isAlive())
      thread.start();
  }
  /**
   * Sets the rgb to a color put in
   * @param color the color it should become
   */
  public void setRGBtoColor(String color) {
    if (!color.equals("unknown"))
      if (color.equals("yellow"))
        setYellow();
      else if (color.equals("blue"))
        setBlue();
      else if (color.equals("green"))
        setGreen();
      else
        setRed();
  }
}