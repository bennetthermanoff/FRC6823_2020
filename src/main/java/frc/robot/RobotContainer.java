package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoAim3d;
import frc.robot.commands.AutoCommandGroup;
import frc.robot.commands.FieldSpaceDrive;
import frc.robot.commands.LimeLightPickupBall;
import frc.robot.commands.RobotSpaceDrive;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;;

public class RobotContainer {
        public SwerveDriveSubsystem swerveDriveSubsystem;
        public ShooterSubsystem shooterSubsystem;

        private FieldSpaceDrive fieldSpaceDriveCommand;
        private RobotSpaceDrive robotSpaceDriveCommand;
        private AutoCommandGroup autoCommandGroup; // gotta construct auto by giving it the swerve bas
        private AutoAim3d autoAim3d;
        private JoystickHandler joystickHandler;
        private NavXHandler navX;
        private LimeLightSubsystem limeLightSubsystem;
        private LiftSubsystem liftSubsystem;
        private LimeLightPickupBall pickupBallCommand;

        public RobotContainer() {
                swerveDriveSubsystem = new SwerveDriveSubsystem();
                shooterSubsystem = new ShooterSubsystem();

                joystickHandler = new JoystickHandler(); // joystick input
                limeLightSubsystem = new LimeLightSubsystem(0);
                liftSubsystem = new LiftSubsystem(14, 15); // enter CAN Id's for the lift motors.
                navX = new NavXHandler(); // navx input
                autoAim3d = new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, positionSelect());
                this.pickupBallCommand = new LimeLightPickupBall(swerveDriveSubsystem, shooterSubsystem,
                                limeLightSubsystem, 0);

                // field space also uses navx to get its angle
                fieldSpaceDriveCommand = new FieldSpaceDrive(swerveDriveSubsystem, joystickHandler, navX);
                robotSpaceDriveCommand = new RobotSpaceDrive(swerveDriveSubsystem, joystickHandler);
                autoCommandGroup = new AutoCommandGroup(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem);
                CommandScheduler.getInstance().setDefaultCommand(swerveDriveSubsystem, fieldSpaceDriveCommand);

                configureButtonBindings();
        }

        public AutoCommandGroup getAutoCommandGroup() {
                return autoCommandGroup;
        }

        private void configureButtonBindings() {

                // press button 12 to set the swerve just forward, this is for calibration
                // purposes
                // joystickHandler.button(13).whileHeld(() -> swerveDriveSubsystem.drive(0.1, 0,
                // 0), swerveDriveSubsystem);

                // this will set the current orientation to be "forward" for field drive
                // joystickHandler.button(14).whenPressed(() -> fieldSpaceDriveCommand.zero());
                joystickHandler.button(14).whenPressed(liftSubsystem::startUp).whenReleased(liftSubsystem::stop);
                joystickHandler.button(13).whenPressed(liftSubsystem::startReverse).whenReleased(liftSubsystem::stop);

                // holding 10 will enable field space drive, instead of robot space
                joystickHandler.button(7).whenHeld(fieldSpaceDriveCommand);

                joystickHandler.button(15).whileActiveContinuous(shooterSubsystem::shooterPID, shooterSubsystem)
                                .whenInactive(shooterSubsystem::stopShooterSpin);

                joystickHandler.button(5).whenPressed(() -> {
                        autoAim3d.setPosition(this.positionSelect());
                        autoAim3d.schedule();
                }).whenReleased(autoAim3d::cancel);

                joystickHandler.button(11).whenPressed(shooterSubsystem::startConveyorSpin)
                                .whenReleased(shooterSubsystem::stopConveyorSpin);

                joystickHandler.button(12).whenPressed(shooterSubsystem::startReverseConveyor)
                                .whenReleased(shooterSubsystem::stopConveyorSpin);

                joystickHandler.button(1).whenPressed(shooterSubsystem::startIntakeSpin)
                                .whenReleased(shooterSubsystem::stopIntakeSpin);
                joystickHandler.button(15).whenPressed(shooterSubsystem::startTimer)
                                .whenReleased(shooterSubsystem::stopTimer);

                joystickHandler.button(9).whenPressed(shooterSubsystem::startIntakeSpin)
                                .whenReleased(shooterSubsystem::stopIntakeSpin);
                joystickHandler.button(10).whenPressed(shooterSubsystem::startReverseIntake)
                                .whenReleased(shooterSubsystem::stopIntakeSpin);
                joystickHandler.button(16).whenPressed(shooterSubsystem::coolShooter)
                                .whenReleased(shooterSubsystem::stopShooterSpin);

                // joystickHandler.button(14).whileActiveOnce(pickupBallCommand);

                // joystickHandler.button(3).whenPressed(new MoveTo3d(swerveDriveSubsystem,
                // limeLightSubsystem, 0, 100));

        }

        private int positionSelect() {
                if (joystickHandler.getRawAxis6() < .33) {
                        return 0;
                } else
                        return 1;
        }
}
