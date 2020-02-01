/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.Spark;

public class RGB {

  private Spark RGB;
// for testing purposes only remove this later

  public double test;

  /**
   * a constructer for the rgb which takes in a spark motor as a paramater
   * @param lights a spark motor
   */
  public RGB(Spark lights) {
    RGB = lights;
  }
// for testing purposes only remove this later
  public void nextStep(){
    if (test == 0.99)
      test = -0.99;
    else
      test += 0.02;
    RGB.set(test);
  }
  public double getTest(){
      return test; 
  }
  /** This adjusts the RGB to the color selected (two ahead of the color seen)
   * @param colorsensor A Colors Sensor (constructed in the ColorSensor.java class [ not ColorSensorV3 or anything included in wpilib {this is a really weird custom solution and will not work if you are copying and pasting only this part of the code}] )
   */
  public void lightsBasedOffColors(ColorSensor colorsensor){
    RGB.set(colorToRGB(colorsensor.colorSelected()));
  }
  /**
  * this takes a string and returns what you should the RGB "motor" at to make the same. It assumes ththat the color isn't unknown
  * @param color the color that the rgb should be (yellow, red, blue, green)
  *  @return a double which is what you should the RGB "motor" at
  */
  private double colorToRGB(String color) {
    if (color.equals("yellow"))
      return 0.69;
    else if (color.equals("blue"))
      return 0.87;
    else if (color.equals("green"))
      return 0.77;
    else 
      return 0.61;
  }
  /**
   * Set the rgb to color1 color2 waves (look at the rev blinkin and they should be blue and yellow)
   */
  public void normalMode(){
    RGB.set(0.53);
  }

}