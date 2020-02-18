package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLightSubsystem extends SubsystemBase {

    private Servo servo;
    private int pipeline;
    private NetworkTable table;

    public LimeLightSubsystem(int servo) {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        this.servo = new Servo(servo);
        this.setPipeline(0);
    }

    public void setPipeline(int pipeline) {
        table.getEntry("pipeline").setNumber(pipeline);
        if (pipeline == 0) {
            this.setServo(75);
        } else if (pipeline == 1) {
            this.setServo(0);
        }
        // else if..... (for other pipeline based servo angles)
    }

    public int getPipeline() {
        return (int) table.getEntry("getpipe").getDouble(0);
    }

    public void setServo(int degrees) {
        servo.setAngle(degrees);
    }

    // "T"x is for theta, aka this is from 2d pipelines, non "T" methods get 3d
    // pipline functions.
    public double getTx() {
        return table.getEntry("tx").getDouble(0);
    }

    public double getTy() {
        return table.getEntry("ty").getDouble(0);
    }

    // this is the 3d Distance, should always be negative
    public double getZ() {
        return table.getEntry("camtran").getDoubleArray(new double[] { 0 })[2];
    }

    // this is the 3d strafe
    public double getX() {
        return table.getEntry("camtran").getDoubleArray(new double[] { 0 })[0];
    }

}