package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.MovingAverage;

public class LimeLight {

    private NetworkTable table;
    private NetworkTableEntry tx, ty, threeD;
    private Preferences prefs;
    private PIDController aimPidController, distancePidController, strafePidController;

    MovingAverage xFilter;

    public LimeLight(Preferences prefs) {
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

        if (SmartDashboard.getBoolean("LemonPipeline", false)) {
            table.getEntry("pipeline").setNumber(1);
        } else {
            table.getEntry("pipeline").setNumber(0);
        }

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

    public double[] aimSteerAndStrafe() { // this is the big boy method, it do all the things
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

    public double[] goToPolar(double distance, double theta) { // this is the big boy method, it do all the things, but
                                                               // it also takes command in radians and inches for
                                                               // getting a polar point from target, Negative is left,
                                                               // Postitive Right.
                                                               //
                                                               // NOTE: Currently this Method computes distance using
                                                               // the Limelight 3D pipeline, this is experimental, and
                                                               // if unstable, we may want to use the standard limelight
                                                               // pipeline instead.
        double skewOffset = threeD.getDoubleArray(new double[] { 0 })[0]; // (x,y,z,pitch,yaw,roll)
        double skew = distance * Math.sin(theta);
        double ySetpoint = distance * Math.cos(theta);
        strafePidController.setSetpoint(skew);

        double skewCommand = strafePidController.calculate(skewOffset);
        double x = tx.getDouble(0.0);
        double r = threeD.getDoubleArray(new double[] { 0 })[2];
        xFilter.nextVal(skewOffset);
        skewOffset = xFilter.get();

        prefs.putDouble("x", x);

        double aimCommand = aimPidController.calculate(x, 0);
        double distanceCommand = distancePidController.calculate(r, ySetpoint);

        prefs.putDouble("aimCommand", aimCommand);

        if (Math.abs(x) < 3 && Math.abs(r) < 3 && Math.abs(skewOffset) < 5) {
            SmartDashboard.putBoolean("Shoot", true);
        } else {
            SmartDashboard.putBoolean("Shoot", false);
        }

        return new double[] { aimCommand, skewCommand * -1, distanceCommand };//
    }

    public double[] goTo(double x, double z) { // this is the big boy method, it do all the things, but
                                               // it also takes command in radians and inches for
                                               // getting a coordinate point from target, Negative is left,
                                               // Postitive Right.
                                               //
                                               // NOTE: Currently this Method computes distance using
                                               // the Limelight 3D pipeline, this is experimental, and
                                               // if unstable, we may want to use the standard limelight
                                               // pipeline instead.
        double skewOffset = threeD.getDoubleArray(new double[] { 0 })[0]; // (x,y,z,pitch,yaw,roll)
        double xtheta = tx.getDouble(0.0);
        double r = threeD.getDoubleArray(new double[] { 0 })[2];
        // xFilter.nextVal(skewOffset);
        // skewOffset = xFilter.get();

        // prefs.putDouble("x", xtheta);

        double aimCommand = aimPidController.calculate(xtheta, 0);
        double distanceCommand = distancePidController.calculate(r, z);
        double skewCommand = strafePidController.calculate(skewOffset, x);

        // prefs.putDouble("aimCommand", aimCommand);

        if (Math.abs(xtheta) < 3 && Math.abs(r) < 3 && Math.abs(skewOffset) < 5) { // for SmartDashBoard Green light
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