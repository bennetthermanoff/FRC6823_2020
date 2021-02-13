package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class Shoot extends CommandBase {
    private ShooterSubsystem shooterSubsystem;
    private double conveyorPower;
    private Timer timer;
    private int time;

    private LongRange2dAutoShoot.DoubleContainer rpm;

    public Shoot(ShooterSubsystem shooterSubsystem, double rpm, double conveyorPower, int time) {
        this(shooterSubsystem, new LongRange2dAutoShoot.DoubleContainer(rpm), conveyorPower, time);
    }

    public Shoot(ShooterSubsystem shooterSubsystem, LongRange2dAutoShoot.DoubleContainer rpm, double conveyorPower,
            int time) {
        this.shooterSubsystem = shooterSubsystem;
        this.rpm = rpm;
        this.conveyorPower = conveyorPower;
        this.time = time;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        // shooterSubsystem.shooterPID(rpm, 20);
        shooterSubsystem.shooterPID(rpm.value, 30, conveyorPower);
    }

    @Override
    public void initialize() {
        shooterSubsystem.startTimer();
        timer = new Timer();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        return timer.hasPeriodPassed(time);
    }

    @Override
    public void end(boolean inturrupted) {
        shooterSubsystem.stopTimer();
        timer.stop();
        shooterSubsystem.stopShooterSpin();
    }
}