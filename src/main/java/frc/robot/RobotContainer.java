package frc.robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutoAim3d;
import frc.robot.commands.AutoCommandGroup;
import frc.robot.commands.FieldSpaceDrive;
import frc.robot.commands.RobotSpaceDrive;
import frc.robot.subsystems.SwerveDriveSubsystem;
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
    public LimeLightHandler limeLight;
    private LimeLightSubsystem limeLightSubsystem;

    public RobotContainer() {
        swerveDriveSubsystem = new SwerveDriveSubsystem();
        shooterSubsystem = new ShooterSubsystem();

        joystickHandler = new JoystickHandler(); // joystick input
        limeLight = new LimeLightHandler(0, shooterSubsystem); // limelight input
        limeLightSubsystem = new LimeLightSubsystem(0);
        navX = new NavXHandler(); // navx input
        autoAim3d = new AutoAim3d(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem, positionSelect());

        // field space also uses navx to get its angle
        fieldSpaceDriveCommand = new FieldSpaceDrive(swerveDriveSubsystem, joystickHandler, navX);
        robotSpaceDriveCommand = new RobotSpaceDrive(swerveDriveSubsystem, joystickHandler);
        autoCommandGroup = new AutoCommandGroup(limeLightSubsystem, shooterSubsystem, swerveDriveSubsystem);
        CommandScheduler.getInstance().setDefaultCommand(swerveDriveSubsystem, robotSpaceDriveCommand);

        configureButtonBindings();
    }

    public AutoCommandGroup getAutoCommandGroup() {
        return autoCommandGroup;
    }

    private void configureButtonBindings() {

        // press button 12 to set the swerve just forward, this is for calibration
        // purposes
        // joystickHandler.button(12).whileHeld(() -> swerveDriveSubsystem.drive(0.2, 0,
        // 0), swerveDriveSubsystem);

        // this will set the current orientation to be "forward" for field drive
        joystickHandler.button(14).whenPressed(() -> fieldSpaceDriveCommand.zero());

        // holding 10 will enable field space drive, instead of robot space
        joystickHandler.button(7).whenHeld(fieldSpaceDriveCommand);

        joystickHandler.button(15).whileActiveContinuous(shooterSubsystem::shooterPID, shooterSubsystem)
                .whenInactive(shooterSubsystem::stopShooterSpin);

        joystickHandler.button(5).whenPressed(limeLight::aimReset);
    
        joystickHandler.button(5).whenPressed(() -> {
                autoAim3d.setPosition(this.positionSelect());
                autoAim3d.schedule();
        });

        joystickHandler.button(11).whenPressed(shooterSubsystem::startConveyorSpin)
                .whenReleased(shooterSubsystem::stopConveyorSpin);

        joystickHandler.button(12).whenPressed(shooterSubsystem::startReverseConveyor)
                .whenReleased(shooterSubsystem::stopConveyorSpin);

        joystickHandler.button(1).whenPressed(shooterSubsystem::startIntakeSpin)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        joystickHandler.button(15).whenPressed(shooterSubsystem::startTimer).whenReleased(shooterSubsystem::stopTimer);
        joystickHandler.button(4).whenPressed(limeLight::pipeLineSwitch);

        joystickHandler.button(9).whenPressed(shooterSubsystem::startIntakeSpin)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        joystickHandler.button(10).whenPressed(shooterSubsystem::startReverseIntake)
                .whenReleased(shooterSubsystem::stopIntakeSpin);
        joystickHandler.button(16).whenPressed(shooterSubsystem::coolShooter)
                .whenReleased(shooterSubsystem::stopShooterSpin);

    }
    private int positionSelect(){
        if(joystickHandler.getRawAxis6()<.33){
                return 0;
        } 
        else
                return 1;
    }
}
