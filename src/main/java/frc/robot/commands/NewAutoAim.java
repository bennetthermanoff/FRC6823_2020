package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.NavXHandler;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NewAutoAim extends SequentialCommandGroup {

    public NewAutoAim(LimeLightSubsystem limeLightSubsystem, ShooterSubsystem shooterSubsystem,
            SwerveDriveSubsystem swerveDriveSubsystem, int position, NavXHandler navX) {

        double distance = 56;
        double rpm = 8500;

        // SmartDashboard.putNumber("Position From the target", position);
        if (position == 0) {
            // distance = -56;
            // rpm = 5925;
            distance = 102;
            rpm = 6650;
        } else if (position == 1) {
            // distance = -105;
            // rpm = 6575;
            distance = 186;
            rpm = 9000;

        } else if (position == -1) {
            // distance = -37
            // distance = 6000
            distance = 43; // inches
            rpm = 8750;
        }

        super.addCommands(new LineUpWithTargetAt(swerveDriveSubsystem, limeLightSubsystem, 0, navX, distance));
        super.addCommands(new MoveTo3d(swerveDriveSubsystem, limeLightSubsystem, 0, distance * -1));
        super.addCommands(new Shoot(shooterSubsystem, rpm, .4, 8, swerveDriveSubsystem));
    }
}
