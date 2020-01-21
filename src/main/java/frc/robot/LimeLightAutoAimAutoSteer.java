package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class LimeLightAutoAimAutoSteer {
    private double kPAim, kPDistance, thetaX, thetaY;
    private NetworkTable table;
    private NetworkTableEntry tx, ty, ta, ts;
    private Preferences prefs;
    private PIDController aimPidController, distancePidController;

    public LimeLightAutoAimAutoSteer(Preferences prefs) {
        this.prefs = prefs;
        double KpDistance = this.prefs.getDouble("KpDistance", .18); // speed in which distance is adjusted when
                                                                     // autoaiming
        double KpAim = this.prefs.getDouble("KpAim", -.036); // speed in which aiming is adjusted when autoaiming
        aimPidController = new PIDController(KpAim, 0, 0);
        distancePidController = new PIDController(KpDistance, 0, 0);
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");

    }

    public void updatePrefs() {
        double KpDistance = this.prefs.getDouble("KpDistance", .18); // speed in which distance is adjusted when
                                                                     // autoaiming
        double KpAim = this.prefs.getDouble("KpAim", -.036); // speed in which aiming is adjusted when autoaiming
        aimPidController.setP(KpAim);
        distancePidController.setP(KpDistance);

    }

    public double[] aimAndSteer() {
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        double skew = ts.getDouble(0.0);
        prefs.putDouble("x", x);
        prefs.putDouble("y", y);
        
        double aimCommand = aimPidController.calculate(x, 0);
        double distanceCommand = distancePidController.calculate(y, 0);
        prefs.putDouble("aimCommand", aimCommand);
        prefs.putDouble("distanceCommand", distanceCommand);
        


        return new double[] { aimCommand, distanceCommand};//

    }
}