/*package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LiftSubsystem extends SubsystemBase {

    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;

    public LiftSubsystem(int leftMotor, int rightMotor) {
        this.leftMotor = new CANSparkMax(leftMotor, MotorType.kBrushless);
        this.rightMotor = new CANSparkMax(rightMotor, MotorType.kBrushless);
    }

    public void startUp() {
        leftMotor.set(Robot.PREFS.getDouble("LiftPower", 0)*-1);
        rightMotor.set(Robot.PREFS.getDouble("LiftPower", 0));
    }

    public void startReverse() {
        leftMotor.set(Robot.PREFS.getDouble("LiftPower", 0));
        rightMotor.set(Robot.PREFS.getDouble("LiftPower", 0)*-1);
    }

    public void stop() {
        leftMotor.set(0);
        rightMotor.set(0);
    }

}
*/