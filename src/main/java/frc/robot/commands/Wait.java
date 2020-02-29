package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Wait extends CommandBase {

    private int seconds;
    private Timer timer;

    public Wait(int seconds) {
        timer = new Timer();
        timer.start();
    }

    @Override
    public void execute() {
        return;
    }

    @Override
    public boolean isFinished() {
        return timer.hasPeriodPassed(seconds);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }
}