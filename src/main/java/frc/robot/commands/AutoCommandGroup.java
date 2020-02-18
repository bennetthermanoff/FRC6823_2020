package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class AutoCommandGroup extends SequentialCommandGroup{

    public AutoCommandGroup(LimeLightSubsystem limeLightSubsystem, ShooterSubsystem shooterSubsystem, SwerveDriveSubsystem swerveDriveSubsystem){
        addCommands(
            new MoveTo3d(swerveDriveSubsystem, limeLightSubsystem, 0, -56),
            new Shoot(shooterSubsystem, 8500)
        );
    }

}