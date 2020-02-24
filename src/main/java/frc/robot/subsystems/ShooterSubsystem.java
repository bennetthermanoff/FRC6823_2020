package frc.robot.subsystems;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.WheelDrive;

public class ShooterSubsystem extends SubsystemBase {
    private CANSparkMax intake, conveyor, leftShoot, rightShoot;
    private DigitalInput bottomSensor, secondSensor, topSensor;
    private boolean manualControl;
    private Encoder encoder;
    private PIDController speedController;
    private Timer timer;
    private int count;

    public ShooterSubsystem() {
        intake = new CANSparkMax(9, MotorType.kBrushless);
        conveyor = new CANSparkMax(10, MotorType.kBrushless);
        leftShoot = new CANSparkMax(11, MotorType.kBrushed);
        rightShoot = new CANSparkMax(13, MotorType.kBrushed);
        bottomSensor = new DigitalInput(0);
        secondSensor = new DigitalInput(1);
        topSensor = new DigitalInput(2);
        encoder = new Encoder(8, 9, false, Encoder.EncodingType.k1X);
        encoder.setDistancePerPulse(1);
        speedController = new PIDController(Robot.PREFS.getDouble("rpmk", .0001), 0, 0);

    }

    public void startShooterSpin() {
        leftShoot.set(Robot.PREFS.getDouble("ShootSpeed", 1));
        rightShoot.set(Robot.PREFS.getDouble("ShootSpeed", 1));
        conveyor.set(Robot.PREFS.getDouble("ConveyorShootSpeed", 0));
        manualControl = true;
    }

    public void coolShooter() {

        leftShoot.set(.1);
        rightShoot.set(.1);

    }

    public void stopShooterSpin() {
        leftShoot.set(0);
        rightShoot.set(0);
        conveyor.set(0);
        manualControl = false;
    }

    public void shooterPID() {
        speedController.setP((Robot.PREFS.getDouble("rpmk", .0001)));
        speedController.setI(Robot.PREFS.getDouble("rpmi", 0));
        speedController.setD(Robot.PREFS.getDouble("rpmd", 0));
        speedController.setSetpoint(Robot.PREFS.getDouble("RPMControl", 0));
        double out = speedController.calculate(encoder.getRate() * 60 / 1024);
        leftShoot.set(out);
        rightShoot.set(out);
        count++;
        if (count > 30) {
            conveyor.set(Robot.PREFS.getDouble("ConveyorShootSpeed", 0));
            manualControl = true;
        }
    }

    public void shooterPID(double rpm) {

        speedController.setP((Robot.PREFS.getDouble("rpmk", .0001)));
        speedController.setI(Robot.PREFS.getDouble("rpmi", 0));
        speedController.setD(Robot.PREFS.getDouble("rpmd", 0));
        speedController.setSetpoint(rpm);
        double out = speedController.calculate(encoder.getRate() * 60 / 1024);
        leftShoot.set(out);
        rightShoot.set(out);
        count++;
        if (count > 20) {
            conveyor.set(Robot.PREFS.getDouble("ConveyorShootSpeed", 0));
            manualControl = true;
        }
    }

    public void startConveyorSpin() {
        conveyor.set(Robot.PREFS.getDouble("ConveyorSpeed", 0));
        manualControl = true;
    }

    public void startTimer() {
        count = 0;
    }

    public void stopTimer() {
        count = 0;
    }

    public void startReverseConveyor() {
        conveyor.set(-1 * Robot.PREFS.getDouble("ConveyorSpeed", 0));
        manualControl = true;
    }

    public void stopConveyorSpin() {
        conveyor.set(0);
        manualControl = false;
    }

    public void startIntakeSpin() {
        intake.set(Robot.PREFS.getDouble("IntakeSpeed", .05));
    }

    public void stopIntakeSpin() {
        intake.set(0);
    }

    public void startReverseIntake() {
        intake.set(-1 * Robot.PREFS.getDouble("IntakeSpeed", .05));
    }

    public boolean doesSenseBall() {
        return (!bottomSensor.get() || !secondSensor.get());
    }

    @Override
    public void periodic() {
        // get returns true when nothing is there
        if (doesSenseBall() && !manualControl && topSensor.get()) {
            conveyor.set(Robot.PREFS.getDouble("ConveyorSpeed", 0));
        } else if (!manualControl) {
            conveyor.set(0);
        }
        Robot.PREFS.putDouble("shootRPM", encoder.getRate() * 60 / 1024);
    }
}
