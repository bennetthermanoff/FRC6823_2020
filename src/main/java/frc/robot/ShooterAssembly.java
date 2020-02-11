package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;

class ShooterAssembly {
    private ConveyorControl conveyorControl;
    private CANSparkMax intakeMotor, leftMotor, rightMotor;
    private SpeedControllerGroup shooter;
    private PIDController rpmController;

    ShooterAssembly(int intakePort, int shooterPort, int intakeMotor, int conveyorMotor, int leftMotor,
            int rightMotor) {
        this.conveyorControl = new ConveyorControl(intakePort, shooterPort, conveyorMotor);
        this.intakeMotor = new CANSparkMax(intakeMotor, MotorType.kBrushed);
        this.leftMotor = new CANSparkMax(leftMotor, MotorType.kBrushed);
        this.rightMotor = new CANSparkMax(rightMotor, MotorType.kBrushed);
        this.rpmController = new PIDController(1, 0, 0);
    }

    public void intakeMotor(boolean on) {
        if (on)
            intakeMotor.set(Robot.prefs.getDouble("intakeSpeed", 0));
        else
            intakeMotor.set(0);
    }

    public void update(boolean shoot) {
        conveyorControl.setMotor(shoot, Robot.prefs.getBoolean("conveyorShutoffOveride", false));
        rpmController.setSetpoint(Robot.prefs.getDouble("shooterTargetRPM", 0));
        shooter.set(rpmController.calculate(rightMotor.getEncoder().getVelocity()));
    }
}