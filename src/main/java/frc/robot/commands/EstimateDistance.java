package frc.robot.commands;

public class EstimateDistance {
    private static double cameraHeight = 33.5;

    public static double getDistance(double targetHeight, double cameraAngleFromForward, double cameraAngleToTarget) {
        return ((targetHeight - cameraHeight) / Math.tan(cameraAngleFromForward + cameraAngleToTarget))
                * Math.cos(cameraAngleFromForward);

    }

}