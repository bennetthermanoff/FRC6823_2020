package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;

import com.kauailabs.navx.frc.AHRS;

public class NavXHandler {
    private AHRS ahrs;

    public NavXHandler() {
        try {
            /***********************************************************************
             * navX-MXP: - Communication via RoboRIO MXP (SPI, I2C) and USB. - See
             * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
             ***********************************************************************/
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }
    }

    public void printEverythingDammit(Preferences prefs) {

        prefs.putDouble("getAngle()", ahrs.getAngle());

        prefs.putDouble("getDisplacementX()", ahrs.getDisplacementX());
        prefs.putDouble("getDisplacementY()", ahrs.getDisplacementY());
        prefs.putDouble("getDisplacementZ()", ahrs.getDisplacementZ());

        prefs.putDouble("getVelocityX()", ahrs.getVelocityX());
        prefs.putDouble("getVelocityY()", ahrs.getVelocityY());
        prefs.putDouble("getVelocityZ()", ahrs.getVelocityZ());
    }

    public double getAngleRad() {
        return ahrs.getAngle() * 2 * Math.PI / 360d;
    }

    public double yeetPerSecond() {
        return Math.sqrt(Math.pow(ahrs.getVelocityX(), 2) + Math.pow(ahrs.getVelocityY(), 2));
    }
}