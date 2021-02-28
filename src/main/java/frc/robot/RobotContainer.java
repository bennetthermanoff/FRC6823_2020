package frc.robot;

//import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
//import edu.wpi.first.wpilibj2.command.InstantCommand;
//import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
//import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.commands.AutoAim3d;
import frc.robot.commands.AutoCommandGroup;
import frc.robot.commands.ChangePipeline;
// import frc.robot.commands.DeterminePathandDoItCommand;
import frc.robot.commands.FieldSpaceDrive;
import frc.robot.commands.LimeLightPickupBall;
//import frc.robot.commands.LimeLightSeek;
//import frc.robot.commands.LongRange2d;
import frc.robot.commands.LongRange2dAutoShoot;
import frc.robot.commands.LooptyLoop;
import frc.robot.commands.RobotSpaceDrive;
import frc.robot.commands.RotateToZero;
import frc.robot.commands.Wait;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;;

public class RobotContainer {
    public SwerveDriveSubsystem swerveDriveSubsystem;
    public ShooterSubsystem shooterSubsystem;
    public NavXHandler navX;

    public FieldSpaceDrive fieldSpaceDriveCommand;
    private RobotSpaceDrive robotSpaceDriveCommand;
    private AutoCommandGroup auton; // gotta construct auto by giving it the swerve bas
    private AutoAim3d autoAim3dClose, autoAim3dSuperClose;
    private AutoAim3d autoAim3dFar;
    private JoystickHandler joystickHandler;
    public LimeLightSubsystem limeLightSubsystem;
    private LiftSubsystem liftSubsystem;
    private LimeLightPickupBall pickupBallCommand;
    // private LooptyLoop loop;

    public LimeLightSubsystem getLimeLightSubsystem() {
        return limeLightSubsystem;
    }

    public SwerveDriveSubsystem getSwervedriveSubsystem() {
        return swerveDriveSubsystem;
    }

    public ShooterSubsystem getShooterSubsystem() {
        return shooterSubsystem;
    }

    public NavXHandler getNavXHandler() {
        return navX;
    }

    public RobotContainer() {
        swerveDriveSubsystem = new SwerveDriveSubsystem();
        shooterSubsystem = new ShooterSubsystem();

        joystickHandler = new JoystickHandler(); // joystick input
        limeLightSubsystem = new LimeLightSubsystem(0);
        liftSubsystem = new LiftSubsystem(14, 15); // enter CAN Id's for the lift motors.
        navX = new NavXHandler(); // navx input
        autoAim3dClose = new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, 0);
        autoAim3dFar = new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, 1);
        autoAim3dSuperClose = new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, -1);

        // field space also uses navx to get its angle
        fieldSpaceDriveCommand = new FieldSpaceDrive(swerveDriveSubsystem, joystickHandler, navX);
        robotSpaceDriveCommand = new RobotSpaceDrive(swerveDriveSubsystem, joystickHandler);
        swerveDriveSubsystem.setDefaultCommand(fieldSpaceDriveCommand);
        // limeLightSubsystem.setServoAngle(70);

        this.pickupBallCommand = new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem, limeLightSubsystem, 0);

        // this.loop = new LooptyLoop(swerveDriveSubsystem, limeLightSubsystem, 1);
        limeLightSubsystem.setServoAngle(65);
        limeLightSubsystem.setPipeline(0);
        RotateToZero.setInitialAngle(navX.getAngleRad());
        configureButtonBindings();

    }

    public AutoCommandGroup getAutoCommandGroup() {
        auton = new AutoCommandGroup(this);
        return auton;
        // return new AutoCommandGroup(this, Robot.PREFS.getBoolean("leftRight", true),
        // Robot.PREFS.getBoolean("backShoot", false),
        // Robot.PREFS.getBoolean("sideShoot", false),
        // (int) Robot.PREFS.getDouble("waitTime", 0));
    }

    // public DeterminePathandDoItCommand getMoreCommands() {
    // return new DeterminePathandDoItCommand(swerveDriveSubsystem, navX,
    // limeLightSubsystem, shooterSubsystem);
    // }

    private void configureButtonBindings() {

        // press button 12 to set the swerve just forward, this is for calibration
        // purposes
        // joystickHandler.button(13).whileHeld(() -> swerveDriveSubsystem.drive(0.1, 0,
        // 0), swerveDriveSubsystem);

        // this will set the current orientation to be "forward" for field drive
        joystickHandler.button(3).whenPressed(fieldSpaceDriveCommand::zero);

        joystickHandler.button(14).whenPressed(liftSubsystem::startUp).whenReleased(liftSubsystem::stop);
        joystickHandler.button(13).whenPressed(liftSubsystem::startReverse).whenReleased(liftSubsystem::stop);

        // holding 10 will enable field space drive, instead of robot space
        joystickHandler.button(7).whenHeld(robotSpaceDriveCommand);

        joystickHandler.button(15).whileActiveContinuous(shooterSubsystem::shooterPID, shooterSubsystem)
                .whenInactive(shooterSubsystem::stopShooterSpin);

        joystickHandler.button(5)
                .whileActiveOnce(new ConditionalCommand(autoAim3dSuperClose,
                        new ConditionalCommand(autoAim3dClose, autoAim3dFar, () -> joystickHandler.getRawAxis6() < .85),
                        () -> joystickHandler.getRawAxis6() < -.75));

        joystickHandler.button(11).whenPressed(shooterSubsystem::startConveyorSpin)
                .whenReleased(shooterSubsystem::stopConveyorSpin);

        joystickHandler.button(12).whenPressed(shooterSubsystem::startReverseConveyor)
                .whenReleased(shooterSubsystem::stopConveyorSpin);

        joystickHandler.button(1).whenPressed(shooterSubsystem::startIntakeSpin)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        joystickHandler.button(15).whenPressed(shooterSubsystem::startTimer).whenReleased(shooterSubsystem::stopTimer);

        joystickHandler.button(9).whenPressed(shooterSubsystem::startIntakeSpin)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        joystickHandler.button(10).whenPressed(shooterSubsystem::startReverseIntake)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        // joystickHandler.button(16).toggleWhenPressed(
        // new StartEndCommand(shooterSubsystem::coolShooter,
        // shooterSubsystem::stopShooterSpin));
        joystickHandler.button(16).whenPressed(shooterSubsystem::raiseIntake);

        joystickHandler.button(2).whileActiveContinuous(() -> shooterSubsystem.shooterPID(10000, 30), shooterSubsystem)
                .whenInactive(shooterSubsystem::stopShooterSpin);
        joystickHandler.button(2).whenPressed(shooterSubsystem::startTimer).whenReleased(shooterSubsystem::stopTimer);

        // joystickHandler.button(14).whileActiveOnce(pickupBallCommand);

        // joystickHandler.button(JoystickHandler.T5)
        // .whileActiveOnce(new SequentialCommandGroup(new
        // ChangePipeline(limeLightSubsystem, 2),
        // new LongRange2d(swerveDriveSubsystem, limeLightSubsystem,
        // shooterSubsystem)));

        joystickHandler.button(4)
                .whileActiveOnce(new LongRange2dAutoShoot(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem));

        // joystickHandler.button(3).whenPressed(new MoveTo3d(swerveDriveSubsystem,
        // limeLightSubsystem, 0, 100));
        // joystickHandler.button(8).whileActiveOnce(loop);
        // joystickHandler.button(8)
        // .whenPressed(() -> SmartDashboard.putNumber("PickupBallCommand stage",
        // pickupBallCommand.getStage()));
        // joystickHandler.button(8).whileActiveOnce(new
        // RotateToZero(swerveDriveSubsystem, navX));
    }

    // private int positionSelect() {
    // if (joystickHandler.getRawAxis6() < .33) {
    // return -1;
    // } else if (joystickHandler.getRawAxis6() < .85) {
    // return 0;
    // } else {
    // return 1;
    // }
    // // cooleo
    // }
}
