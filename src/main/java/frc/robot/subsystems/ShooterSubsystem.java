package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.WheelDrive;

public class ShooterSubsystem extends SubsystemBase {
    private CANSparkMax intake, conveyor, leftShoot, rightShoot;
    private DigitalInput bottomSensor, secondSensor, topSensor;
    private boolean manualControl;

    public ShooterSubsystem() {
        intake = new CANSparkMax(9, MotorType.kBrushed);
        conveyor = new CANSparkMax(10, MotorType.kBrushed);
        leftShoot = new CANSparkMax(11, MotorType.kBrushed);
        rightShoot = new CANSparkMax(13, MotorType.kBrushed);
        bottomSensor = new DigitalInput(0);
        secondSensor = new DigitalInput(1);
        topSensor = new DigitalInput(2);
    }

    public void startShooterSpin() {
        leftShoot.set(Preferences.getInstance().getDouble("ShootSpeed", 1));
        rightShoot.set(Preferences.getInstance().getDouble("ShootSpeed", 1));
        conveyor.set(Preferences.getInstance().getDouble("ConveyorShootSpeed", 0));
    }

    public void stopShooterSpin() {
        leftShoot.set(0);
        rightShoot.set(0);
        conveyor.set(0);
    }

    public void startConveyorSpin() {
        conveyor.set(Preferences.getInstance().getDouble("ConveyorSpeed", 0));
        manualControl = true;
    }

    public void stopConveyorSpin() {
        conveyor.set(0);
        manualControl = false;
    }

    public void startIntakeSpin() {
        intake.set(Preferences.getInstance().getDouble("IntakeSpeed", .05));
    }

    public void stopIntakeSpin() {
        intake.set(0);
    }

    @Override
    public void periodic() {
        if ((!bottomSensor.get() || !secondSensor.get()) && !manualControl && topSensor.get()) {
            conveyor.set(Preferences.getInstance().getDouble("ConveyorSpeed", 0));
        } else if (!manualControl) {
            conveyor.set(0);
        }
        Preferences.getInstance().putDouble("shootRPM", leftShoot.getAlternateEncoder(EncoderType.kQuadrature, 1024).getVelocity();
    }
}
