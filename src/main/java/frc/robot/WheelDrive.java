package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.controller.PIDController;

public class WheelDrive {
    private final double MAX_VOLTS = 4.95; // Voltage for the Andymark Absolute Encoders used in the SDS kit.
    private CANSparkMax angleMotor;
    private CANSparkMax speedMotor;
    private PIDController pidController;
    private AnalogInput angleEncoder;
    private double encoderOffset;

    private boolean invertSpeed;

    public WheelDrive(int angleMotor, int speedMotor, AnalogInput angleEncoder, double encoderOffset) {
        this.angleMotor = new CANSparkMax(angleMotor, MotorType.kBrushless);
        this.speedMotor = new CANSparkMax(speedMotor, MotorType.kBrushless); // We're using CANSparkMax controllers, but
                                                                             // not their encoders.
        this.angleEncoder = angleEncoder;
        this.encoderOffset = encoderOffset;

        // pidController = new PIDController(1, 0, 0, new AnalogInput(encoder),
        // this.angleMotor);
        pidController = new PIDController(.5, 0, 0); // This is the PID constant, we're not using any
                                                     // Integral/Derivative control but changing the P value will make
                                                     // the motors more aggressive to changing to angles.

        // pidController.setTolerance(20); //sets tolerance, shouldn't be needed.

        pidController.enableContinuousInput(0, MAX_VOLTS); // This makes the PID controller understand the fact that for
                                                           // our setup, 4.95V is the same as 0 since the wheel loops.

        invertSpeed = false;
    }

    public void setZero(double offset) {
        encoderOffset = offset;
    }

    // angle is a value between -1 to 1
    public void drive(double speed, double angle) {

        double currentEncoderValue = (angleEncoder.getVoltage() + encoderOffset) % MAX_VOLTS; // Combines reading from encoder
        double currentDirection = invertSpeed ? (currentEncoderValue+MAX_VOLTS/2)% MAX_VOLTS : currentEncoderValue;

        double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
        if (setpoint < 0) {
            setpoint = MAX_VOLTS + setpoint;
        }
        if (setpoint > MAX_VOLTS) {
            setpoint = setpoint - MAX_VOLTS;
        } // converts angle into the same scale that the encoder uses.

        if (Math.abs(currentDirection - setpoint) > MAX_VOLTS/2) {
            invertSpeed = !invertSpeed;
            currentDirection = invertSpeed ? (currentEncoderValue+MAX_VOLTS/2)% MAX_VOLTS : currentEncoderValue;
        }

        speedMotor.set(invertSpeed ? -speed : speed); // sets motor speed. google ternary operator


        pidController.setSetpoint(setpoint); // sets setpoint from PID controller
                                                                                        // with the encoderOffset that
                                                                                        // can be changed in
                                                                                        // SmartDashboard.

        double pidOut = pidController.calculate(currentEncoderValue, setpoint);// calculates using PID the angle motor
                                                                         // command.

        angleMotor.set(-pidOut); // colten is god for figuring out our problem on 1/14/20 was this not being
                                 // negative.

        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] boundedOffset", currentEncoderValue);
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
    public double getVoltages() {
        Robot.prefs.putDouble("Encoder [" + angleEncoder.getChannel() + "] getVoltage", angleEncoder.getVoltage());
        return angleEncoder.getVoltage();
    }

    public void stop() {
        pidController.setP(0);
        speedMotor.set(0);
    }

    public void restart() {
        pidController.setP(.5);
    }

    double maxVal = 0; // This for the Encoder max value testcode above.

}
