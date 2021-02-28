package frc.robot.commands;

//import edu.wpi.first.wpilibj.Preferences;
//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj2.command.InstantCommand;
//import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.NavXHandler;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class AutoCommandGroup extends SequentialCommandGroup {

    private LimeLightSubsystem limeLightSubsystem;
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private ShooterSubsystem shooterSubsystem;

    private ScanFieldForLemons scanCommand;
    ////private LimeLightPickupBall eatBallCommand;

    private NavXHandler navXHandler;

    private double margin = 3;

    private double path; // path : 1= red A, 2 = red B, 3 = blue B, 4 = blue A

    private static double angleToLemon;

    public static void setAngleToLemon(double angle) {
        angleToLemon = angle;
    }

    public static double getAngleToLemon() {
        return angleToLemon;

    }

    public AutoCommandGroup(RobotContainer robotContainer) {
        //boolean leftRight = Preferences.getInstance().getBoolean("leftRight", false);
        //boolean back = Preferences.getInstance().getBoolean("backShoot", false);
        //boolean sideShoot = Preferences.getInstance().getBoolean("sideShoot", false);
        //int waitSeconds = (int) Preferences.getInstance().getDouble("waitTime", 1);

        limeLightSubsystem = robotContainer.getLimeLightSubsystem();
        swerveDriveSubsystem = robotContainer.getSwervedriveSubsystem();

        navXHandler = robotContainer.getNavXHandler();
        // navXHandler.zeroYaw();
        shooterSubsystem = robotContainer.getShooterSubsystem();

        double initialAngle = navXHandler.getAngle();

        RotateToZero.setInitialAngle(navXHandler.getAngleRad());

        GoRight.zero(navXHandler.getAngleRad());

        limeLightSubsystem.setServoAngle(15);
        limeLightSubsystem.setPipeline(1);

        addCommands(new WaitCommand(0.5));

        // addCommands(new ScanFieldForLemons(limeLightSubsystem));

        // addCommands(new WaitCommand(0.5));

        // if (Math.abs(angleToLemon - 0.0) < margin) { // if it sees a ball directly
        // ahead C3
        // path = 1;
        // } else if (Math.abs(angleToLemon - -11.3) < margin) {
        // path = 3;
        // } else {

        // addCommands(new RotateDegrees(swerveDriveSubsystem, navXHandler, 27.0));

        // addCommands(new ScanFieldForLemons(limeLightSubsystem));

        // addCommands(new WaitCommand(0.5));

        // angleToLemon = AutoCommandGroup.getAngleToLemon();
        // if (Math.abs(angleToLemon - 26.5) < margin) {
        // path = 2;
        // } else {
        // addCommands(new RotateDegrees(swerveDriveSubsystem, navXHandler, 0));
        // path = 4;
        // }

        // }

        // SmartDashboard.putNumber("Auto Path", path);
        // SmartDashboard.putNumber("Auto Angle To Lemon", angleToLemon);

        // // path plans
        // if (path == 1) {
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // first ball

        // addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // second ball

        // addCommands(new RotateLeftUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // third ball

        // addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // addCommands(new RotateToZero(swerveDriveSubsystem, navXHandler));
        // addCommands(new GoForward(swerveDriveSubsystem, 2));
        // } else if (path == 2) {// already facing ball
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // first ball

        // addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // second ball

        // addCommands(new RotateLeftUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // third ball

        // addCommands(new RotateToZero(swerveDriveSubsystem, navXHandler));
        // addCommands(new GoForward(swerveDriveSubsystem, 2));

        // } else if (path == 3) {
        // addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // first ball

        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new RotateLeftUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // second ball

        // addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem,
        // limeLightSubsystem));
        // // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // // limeLightSubsystem));
        // addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
        // limeLightSubsystem, 0));
        // // third ball

        // addCommands(new RotateToZero(swerveDriveSubsystem, navXHandler));
        // addCommands(new GoForward(swerveDriveSubsystem, 2));

        // } else {
        addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 1));
        // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // limeLightSubsystem));
        addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0));
        // first ball

        addCommands(new RotateLeftUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 1));
        // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // limeLightSubsystem));
        addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0));
        // second ball

        addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 1));
        // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // limeLightSubsystem));
        addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0));
        // third ball

        addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 1));
        // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // limeLightSubsystem));
        addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0));
        // 4th ball

        addCommands(new RotateRightUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 1));
        // addCommands(new GoForwardUntilSeeLemon(swerveDriveSubsystem,
        // limeLightSubsystem));
        addCommands(new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0));
        // 5th ball

        addCommands(new SwitchPipelineCommand(limeLightSubsystem, 0));
        addCommands(new RotateToZero(swerveDriveSubsystem, navXHandler));
        addCommands(new RotateLeftUntillSeeBall(swerveDriveSubsystem, limeLightSubsystem, 0));// ball means target
        addCommands(new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, -1));

        // addCommands(new GoRight(swerveDriveSubsystem, 2, navXHandler));
        // }
        // addCommands(new Wait(waitSeconds));

        // if (leftRight && !back) {
        // addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem,
        // robotContainer.limeLightSubsystem.getX(), -56));
        // } else if (leftRight && back) {
        // addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem,
        // robotContainer.limeLightSubsystem.getX(), -130));
        // }

        // if (!sideShoot) {
        // double conveyorSpeed = .5;
        // int rpm = 7250;
        // if (back) {
        // addCommands(
        // new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem, 0, -140));
        // rpm = 6700;
        // conveyorSpeed = .6;

        // } else {
        // addCommands(
        // new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem, 0, -56));
        // rpm = 5925;
        // conveyorSpeed = .5;
        // }
        // addCommands(new ParallelRaceGroup(new Shoot(robotContainer.shooterSubsystem,
        // rpm, conveyorSpeed, 5),
        // new JustAim(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem)));
        // } else {
        // addCommands(new LongRange2d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem,
        // robotContainer.shooterSubsystem, null, 0));
        // }

        // addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem, 0, -56),
        // new InstantCommand(robotContainer.fieldSpaceDriveCommand::zero),
        // new Shoot(robotContainer.shooterSubsystem, 8500, .65, 5));
    }

}