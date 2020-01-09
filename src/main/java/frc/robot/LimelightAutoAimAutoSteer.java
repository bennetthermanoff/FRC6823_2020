package frc.robot;

public class LimelightAutoAimAutoSteer {

    private double KpDistance, min_aim_command, low_min_aim_command, angleCutoff, KpAim, left_command, right_command;

    public LimelightAutoAimAutoSteer(double KpDistance, double KpAim, double min_aim_command,
            double low_min_aim_command, double angleCutoff) {
        this.KpDistance = KpDistance;
        this.angleCutoff = angleCutoff;
        this.low_min_aim_command = low_min_aim_command;
        this.min_aim_command = min_aim_command;
        this.KpAim = KpAim;
    }

    public double[] coarseControl(double x, double y) {

        // Start of autoaim/autodistance code
        double steering_adjust = 0.0;

        double heading_error = -1 * x;
        double distance_error = -1 * y;

        if (x > 1.0) {
            steering_adjust = KpAim * heading_error + min_aim_command; // this gets just the steering adjustment. the if
                                                                       // statement is for postive or negative error
        } else if (x < 1) {
            steering_adjust = KpAim * heading_error - min_aim_command;
        }

        double distance_adjust = KpDistance * distance_error; // adds distance adjust
        left_command = steering_adjust - distance_adjust;
        right_command = steering_adjust * -1 - distance_adjust;

        return new double[] { left_command, right_command };

    }

    public double[] fineControl(double x, double y) {
        // the x and y are degrees

        // This is the same as the coarseControl method, but the difference is that it
        // allows angle cutoffs (doesnt trigger at large angles) and also has a
        // different minimum aim command.
        double steering_adjust = 0.0;

        if (x > 1 && x < angleCutoff) {
            steering_adjust = KpAim * -x + low_min_aim_command;
        } else if (x < 1 && x > -angleCutoff) {
            steering_adjust = KpAim * -x - low_min_aim_command;
        }

        // we add y to change the distance. robot will move forward/backward until y=0
        left_command = steering_adjust + y;
        right_command = -steering_adjust + y;

        return new double[] { left_command, right_command };

    }

    public void beginStrafe() {

    }
}