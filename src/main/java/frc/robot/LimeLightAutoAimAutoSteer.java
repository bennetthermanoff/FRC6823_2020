package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;

public class LimeLightAutoAimAutoSteer{
    private double kPAim, kPDistance, thetaX, thetaY;
    private NetworkTable networkTable;
    private NetworkTableEntry tx, ty, ta, ts;
    private Preferences prefs

    public LimeLightAutoAimAutoSteer(Preferences prefs){
        this.prefs = prefs;
        double KpDistance = this.prefs.getDouble("KpDistance", .18); // speed in which distance is adjusted when autoaiming
        double KpAim = this.prefs.getDouble("KpAim", -.036); // speed in which aiming is adjusted when autoaiming
        
    }
    public double[] aimAndSteer(){

    }
}