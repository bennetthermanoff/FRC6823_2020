package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.NavXHandler;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class NewAutoAim extends SequentialCommandGroup {

    public NewAutoAim(LimeLightSubsystem limeLightSubsystem, ShooterSubsystem shooterSubsystem,
            SwerveDriveSubsystem swerveDriveSubsystem, int position, NavXHandler navX) {

        double distance = 56;
        double rpm = 8500;

        if (position == 0) {
            // distance = -56;
            // rpm = 5925;
            distance = 56;
            rpm = 5900;
        } else if (position == 1) {
            // distance = -105;
            // rpm = 6575;
            distance = 105;
            rpm = 6700;

        } else if (position == -1) {
            // distance = -37
            // distance = 6000
            distance = 43;
            rpm = 8750;
        }

        super.addCommands(new LineUpWithTargetAt(swerveDriveSubsystem, limeLightSubsystem, 0, navX, distance));
        super.addCommands(new WaitCommand(0.5));
        super.addCommands(new Shoot(shooterSubsystem, rpm, .4, 8, swerveDriveSubsystem));
    }
}
