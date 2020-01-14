package frc.robot;

import com.revrobotics.CANSparkMax;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.controller.PIDController;

public class WheelDrive {
    private final double MAX_VOLTS = 4.95; // CHANGE THIS VOLTAGE I HAVE NO IDEA WHAT OUR KIT USES.

    private CANSparkMax angleMotor;
    private CANSparkMax speedMotor;
    private PIDController pidController;

    private AnalogInput angleEncoder;
    private double encoderOffset;

    public WheelDrive(int angleMotor, int speedMotor, AnalogInput angleEncoder, double encoderOffset) {
        this.angleMotor = new CANSparkMax(angleMotor, MotorType.kBrushless);
        this.speedMotor = new CANSparkMax(speedMotor, MotorType.kBrushless);
        this.angleEncoder = angleEncoder;
        this.encoderOffset = encoderOffset;

        // pidController = new PIDController(1, 0, 0, new AnalogInput(encoder),
        // this.angleMotor);
        pidController = new PIDController(.5, 0, 0);
        // pidController.setTolerance(20);
        pidController.enableContinuousInput(0, MAX_VOLTS);

    }

    public void setZero(double offset) {
        encoderOffset = offset;
    }

    // angle is a value between -1 to 1
    public void drive(double speed, double angle, double rate) {

        speedMotor.set(speed * rate); // sets motor speed

        double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
        if (setpoint < 0) {
            setpoint = MAX_VOLTS + setpoint;
        }
        if (setpoint > MAX_VOLTS) {
            setpoint = setpoint - MAX_VOLTS;
        } // converts angle into the same scale that the encoder uses.

        pidController.setSetpoint(setpoint); // sets setpoint from PID controller
        double boundedOffset = (angleEncoder.getVoltage() + encoderOffset) % MAX_VOLTS;

        double pidOut = pidController.calculate(boundedOffset, setpoint);
        angleMotor.set(-pidOut);

        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] boundedOffset", boundedOffset);
        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] setpoint", setpoint);
        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] pidOut", pidOut);// These values are output
                                                                                            // to the smartDashBoard for
                                                                                            // checking if PID workin.

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
    public void getVoltages() {
        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] getVoltage", angleEncoder.getVoltage());
    }

    // double maxVal = 0; //This for the Encoder max value testcode above.

}
