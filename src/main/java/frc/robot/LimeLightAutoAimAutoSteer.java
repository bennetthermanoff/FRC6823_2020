package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.MovingAverage;

import com.kauailabs.navx.frc.AHRS;

public class LimeLightAutoAimAutoSteer {

    private NetworkTable table;
    private NetworkTableEntry tx, ty, threeD;
    private Preferences prefs;
    private PIDController aimPidController, distancePidController, strafePidController;

    MovingAverage xFilter;

    public LimeLightAutoAimAutoSteer(Preferences prefs) {
        this.prefs = prefs;
        double KpDistance = this.prefs.getDouble("KpDistance", .18); // speed in which distance is adjusted when
                                                                     // autoaiming
        double KpAim = this.prefs.getDouble("KpAim", -.036); // speed in which aiming is adjusted when autoaiming
        double KpStrafe = this.prefs.getDouble("KpStrafe", .1);
        aimPidController = new PIDController(KpAim, 0, 0);
        distancePidController = new PIDController(KpDistance, 0, 0);
        strafePidController = new PIDController(KpStrafe, 0, 0);
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        threeD = table.getEntry("camtran");

        xFilter = new MovingAverage(1);

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
        xFilter.nextVal(x);
        x = xFilter.get();
        double y = ty.getDouble(0.0);

        prefs.putDouble("x", x);
        prefs.putDouble("y", y);

        double aimCommand = aimPidController.calculate(x, 0);
        double distanceCommand = distancePidController.calculate(y, 0);
        prefs.putDouble("aimCommand", aimCommand);
        prefs.putDouble("distanceCommand", distanceCommand);

        return new double[] { aimCommand, distanceCommand };//

    }

    public double[] aimSteerAndStrafe() {
        double skew = threeD.getDoubleArray(new double[] { 0 })[0]; // (x,y,z,pitch,yaw,roll)
        // if(tx.getDouble(0)>0){
        // strafePidController.setSetpoint(-90);
        // }
        // else
        strafePidController.setSetpoint(0);

        double skewCommand = strafePidController.calculate(skew);
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0);
        xFilter.nextVal(skew);
        skew = xFilter.get();

        prefs.putDouble("x", x);

        double aimCommand = aimPidController.calculate(x, 0);
        double distanceCommand = distancePidController.calculate(y, 0);

        prefs.putDouble("aimCommand", aimCommand);

        if (Math.abs(x) < 3 && Math.abs(y) < 3 && Math.abs(skew) < 5) {
            SmartDashboard.putBoolean("Shoot", true);
        } else {
            SmartDashboard.putBoolean("Shoot", false);
        }

        return new double[] { aimCommand, skewCommand * -1, distanceCommand };//
    }

    public double[] strafeAndAim() {
        double skew = threeD.getDoubleArray(new double[] { 0 })[0]; // (x,y,z,pitch,yaw,roll)
        // if(tx.getDouble(0)>0){
        // strafePidController.setSetpoint(-90);
        // }
        // else
        strafePidController.setSetpoint(0);

        double skewCommand = strafePidController.calculate(skew);
        double x = tx.getDouble(0.0);
        xFilter.nextVal(skew);
        skew = xFilter.get();

        prefs.putDouble("x", x);

        double aimCommand = aimPidController.calculate(x, 0);

        prefs.putDouble("aimCommand", aimCommand);

        return new double[] { aimCommand, skewCommand * -1 };//

    }
}