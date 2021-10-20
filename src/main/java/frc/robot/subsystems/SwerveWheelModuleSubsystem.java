package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.util.MathUtil;

public class SwerveWheelModuleSubsystem extends SubsystemBase {
    private final double MAX_VOLTS = 4.95; // Voltage for the Andymark Absolute Encoders used in the SDS kit.
    private final double P = .5;

    private CANSparkMax angleMotor;
    private CANSparkMax speedMotor;
    private PIDController pidController;
    private AnalogInput angleEncoder;

    private double encoderOffset;

    public SwerveWheelModuleSubsystem(int angleMotorChannel, int speedMotorChannel, int angleEncoderChannel,
            double encoderOffset) {
        // We're using CANSparkMax controllers, but not their encoders.
        this.angleMotor = new CANSparkMax(angleMotorChannel, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.speedMotor = new CANSparkMax(speedMotorChannel, CANSparkMaxLowLevel.MotorType.kBrushless);
        this.angleEncoder = new AnalogInput(angleEncoderChannel);
        this.encoderOffset = encoderOffset;

        pidController = new PIDController(P, 0, 0); // This is the PID constant, we're not using any
        // Integral/Derivative control but changing the P value will make
        // the motors more aggressive to changing to angles.

        // pidController.setTolerance(20); //sets tolerance, shouldn't be needed.

        pidController.enableContinuousInput(0, MAX_VOLTS); // This makes the PID controller understand the fact that for
        // our setup, 4.95V is the same as 0 since the wheel loops.

        SendableRegistry.addChild(this, angleMotor);
        SendableRegistry.addChild(this, speedMotor);
        SendableRegistry.addChild(this, angleEncoder);
        SendableRegistry.addLW(this, "Swerve Wheel Module");

    }

    public void setZero(double offset) {
        encoderOffset = offset;
    }

    // angle is a value between -1 to 1
    public void drive(double speed, double angle) {

        double currentEncoderValue = (angleEncoder.getVoltage() + encoderOffset) % MAX_VOLTS; // Combines reading from
        // encoder

        // Optimization offset can be calculated here.
        double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
        setpoint += MAX_VOLTS;
        setpoint %= MAX_VOLTS; // ensure setpoint is on scale 0-4.95

        // if the setpoint is more than pi/4 rad away form the current position, then
        // just reverse the speed
        if (MathUtil.getCyclicalDistance(currentEncoderValue, setpoint, MAX_VOLTS) > MAX_VOLTS / 4) {
            speed *= -1;
            setpoint = (setpoint + MAX_VOLTS / 2) % MAX_VOLTS;
        }

        speedMotor.set(speed); // sets motor speed.
        pidController.setSetpoint(setpoint);

        double pidOut = pidController.calculate(currentEncoderValue, setpoint);

        angleMotor.set(-pidOut);

        if (Robot.PREFS.getBoolean("DEBUG_MODE", false)) {
            Robot.PREFS.putDouble("Encoder [" + angleEncoder.getChannel() + "] currentEncoderValue",
                    currentEncoderValue);
            SmartDashboard.putNumber("Encoder [" + angleEncoder.getChannel() + "] currentEncoderValue",
                    currentEncoderValue);
            Robot.PREFS.putDouble("Encoder [" + angleEncoder.getChannel() + "] setpoint", setpoint);
            Robot.PREFS.putDouble("Encoder [" + angleEncoder.getChannel() + "] pidOut", pidOut);
        }

        // This is for testing to find the max value of an encoder. The encoders we use
        // (and most encoders) give values from 0 - 4.95.
        /*
         * if (angleEncoder.getChannel() == 0) { if (angleEncoder.getVoltage() > maxVal)
         * { maxVal = angleEncoder.getVoltage();
         * Robot.prefs.putDouble("Encoder ["+angleEncoder.getChannel()+"] getVoltageMax"
         * , maxVal); } }
         */

    }

    // this method outputs voltages of the encoder to the smartDashBoard, useful for
    // calibrating the encoder offsets
    public double getVoltages() {
        return angleEncoder.getVoltage();
    }

    public void stop() {
        pidController.setP(0);
        speedMotor.set(0);
    }

    public void restart() {
        pidController.setP(P);
    }

    @Override
    public void periodic() {
        // Robot.PREFS.putDouble("Encoder [" + angleEncoder.getChannel() + "]
        // getVoltage", angleEncoder.getVoltage());
    }
}
