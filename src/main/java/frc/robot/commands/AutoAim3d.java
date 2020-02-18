package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.LimeLightSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class AutoAim3d extends SequentialCommandGroup{
    private LimeLightSubsystem limeLightSubsystem;
    private SwerveDriveSubsystem swerveDriveSubsystem;
    private ShooterSubsystem shooterSubsystem;

    public AutoAim3d(LimeLightSubsystem limeLightSubsystem, ShooterSubsystem shooterSubsystem, SwerveDriveSubsystem swerveDriveSubsystem, int position){
        this.shooterSubsystem = shooterSubsystem;
        this.limeLightSubsystem = limeLightSubsystem;
        this.swerveDriveSubsystem = swerveDriveSubsystem;
        
        double distance=-56;
        double rpm=8500;

        if(position==0){
            distance=-56;
            rpm=8500;
        }
        else if(position==1){
            distance=-170;
            rpm=8850;
        }
        
        
        super.addCommands(
            new MoveTo3d(swerveDriveSubsystem, limeLightSubsystem, 0, distance),
            new Shoot(shooterSubsystem, rpm)
        );
    }
    public void setPosition(int position){
        super.clearGroupedCommands();
        double distance=-56;
        double rpm=8500;

        if(position==0){
            distance=-56;
            rpm=8500;
        }
        else if(position==1){
            distance=-170;
            rpm=8850;
        }
        
        
        super.addCommands(
            new MoveTo3d(swerveDriveSubsystem, limeLightSubsystem, 0, distance),
            new Shoot(shooterSubsystem, rpm)
        );
    }
}