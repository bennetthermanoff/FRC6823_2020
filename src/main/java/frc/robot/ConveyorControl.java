package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.controller.PIDController;

class ConveyorControl {

    private DigitalInput intake, shooter;
    private PIDController pidController;
    private CANSparkMax motor;

    public ConveyorControl(int intakePort, int shooterPort, int motorCANID) {
        this.intake = new DigitalInput(intakePort);
        this.shooter = new DigitalInput(shooterPort);
        this.pidController = new PIDController(1, 0, 0);
        this.motor = new CANSparkMax(motorCANID, MotorType.kBrushed);
    }

    double getSpeed(boolean shoot, boolean override) {
        if (shoot) {
            return Robot.prefs.getDouble("shootConveyorSpeed", 0);
        } else if (shooter.get() && !Robot.prefs.getBoolean("conveyorOverride", false)) {
            return 0;
        } else if (intake.get()) {
            return Robot.prefs.getDouble("intakeConveyorSpeed", 0);
        } else {
            return 0;
        }
    }

    public void setMotor(boolean shoot, boolean override) {
        pidController.setSetpoint(this.getSpeed(shoot, override));
        motor.set(pidController.calculate(motor.getEncoder().getVelocity()));
    }
}