package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.util.MathUtil;

public class JoystickHandler {
    private Joystick joystick;
    private double deadZone;

    public JoystickHandler() {
        this.joystick = new Joystick(0);
        this.deadZone = Preferences.getInstance().getDouble("DeadZone", .1);
    }

    public double getRawAxis0() {
        return joystick.getRawAxis(0);
    }

    public double getRawAxis1() {
        return joystick.getRawAxis(1);
    }

    public double getRawAxis2() {
        return joystick.getRawAxis(2);
    }

    public double getAxis0() {
        return MathUtil.clipToZero(getRawAxis0(), deadZone);
    }

    public double getAxis1() {
        return MathUtil.clipToZero(getRawAxis1(), deadZone);
    }

    public double getAxis2() {
        return MathUtil.clipToZero(getRawAxis2(), deadZone);
    }

    public Joystick joy() {
        return joystick;
    }

    public JoystickButton button(int buttonNumber) {
        return new JoystickButton(joystick, buttonNumber);
    }
}
