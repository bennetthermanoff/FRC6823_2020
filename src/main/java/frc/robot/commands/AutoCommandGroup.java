package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class AutoCommandGroup extends SequentialCommandGroup {

    public AutoCommandGroup(RobotContainer robotContainer, Boolean leftRight, boolean back, boolean sideShoot,
            int waitSeconds) {
        addCommands(new Wait(waitSeconds));

        if (leftRight && !back) {
            addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem,
                    robotContainer.limeLightSubsystem.getX(), -56));
        } else if (leftRight && back) {
            addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem,
                    robotContainer.limeLightSubsystem.getX(), -130));
        }

        if (!sideShoot) {
            double conveyorSpeed = .5;
            int rpm = 7250;
            if (back) {
                addCommands(
                        new MoveTo3d(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem, 0, -130));
                rpm = 7800;
                conveyorSpeed = .35;

            } else {
                addCommands(
                        new MoveTo3d(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem, 0, -56));
                rpm = 7000;
                conveyorSpeed = .5;
            }
            addCommands(new ParallelRaceGroup(new Shoot(robotContainer.shooterSubsystem, rpm, conveyorSpeed, 5),
                    new JustAim(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem)));
        } else {
            addCommands(new LongRange2d(robotContainer.swerveDriveSubsystem, robotContainer.limeLightSubsystem,
                    robotContainer.shooterSubsystem, null));
        }

        // addCommands(new MoveTo3d(robotContainer.swerveDriveSubsystem,
        // robotContainer.limeLightSubsystem, 0, -56),
        // new InstantCommand(robotContainer.fieldSpaceDriveCommand::zero),
        // new Shoot(robotContainer.shooterSubsystem, 8500, .65, 5));
    }

}