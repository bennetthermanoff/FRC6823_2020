package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.util.MathUtil;

public class JoystickHandler {
    private Joystick joystick;
    private double deadZone;

    public JoystickHandler() {
        this.joystick = new Joystick(1);
        this.deadZone = Preferences.getInstance().getDouble("DeadZone", .05);
    }

    public double getRawAxis0() {
        return joystick.getRawAxis(0);
    }

    public double getRawAxis1() {
        return joystick.getRawAxis(1);
    }

    public double getRawAxis5() {
        return joystick.getRawAxis(5);
    }

    public double getRawAxis6() {
        return joystick.getRawAxis(6);
    }

    public double getAxis0() {
        return MathUtil.clipToZero(getRawAxis0(), deadZone);
    }

    public double getAxis1() {
        return MathUtil.clipToZero(getRawAxis1(), deadZone);
    }

    public double getAxis5() {
        return MathUtil.clipToZero(getRawAxis5(), deadZone);
    }

    public Joystick joy() {
        return joystick;
    }

    public JoystickButton button(int buttonNumber) {
        return new JoystickButton(joystick, buttonNumber);
    }
}
