package main.java.frc.robot;
public class WheelDrive{


    private Jaguar angleMotor;
    private Jaguar speedMotor;
    private PIDController pidController;
    private final double MAX_VOLTS = 4.95; //CHANGE THIS VOLTAGE I HAVE NO IDEA WHAT OUR KIT USES

    public WheelDrive (int angleMotor, int speedMotor, int encoder) {

        this.angleMotor = new Jaguar (angleMotor); //configured for Jaguar controllers, can be changed.
        this.speedMotor = new Jaguar (speedMotor);
        pidController = new PIDController (1, 0, 0, new AnalogInput (encoder), this.angleMotor);
    
        pidController.setOutputRange (-1, 1);
        pidController.setContinuous ();
        pidController.enable ();
    }
    public void drive (double speed, double angle) {
        speedMotor.set (speed);
    
        double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
        if (setpoint < 0) {
            setpoint = MAX_VOLTS + setpoint;
        }
        if (setpoint > MAX_VOLTS) {
            setpoint = setpoint - MAX_VOLTS;
        }
    
        pidController.setSetpoint (setpoint);
    }

}
