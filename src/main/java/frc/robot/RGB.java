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
 
  /**
   * a constructer for the rgb which takes in a spark motor as a paramater
   * @param lights a spark motor
   */
  public RGB(Spark lights) {
    RGB = lights;
  }

  /** This adjusts the RGB to the color selected (two ahead of the color seen)
   * @param colorsensor A Colors Sensor (constructed in the ColorSensor.java class [ not ColorSensorV3 or anything included in wpilib {this is a really weird custom solution and will not work if you are copying and pasting only this part of the code}] )
   */
  public void lightsBasedOffColors(ColorSensor colorsensor){
    RGB.set(colorToRGB(colorsensor.colorSelected()));
  }

  private double colorToRGB(String color) {
    if (color.equals("yellow")) {
      return 0.69;
    } else if (color.equals("blue")) {
      return 0.87;
    } else if (color.equals("green")) {
      return 0.77;
    } else if (color.equals("red")) {
      return 0.61;
    } else {
      // this is color waves color 1 and 2 (chosen on the blinkin)
      return 0.53;
    }
  }
}