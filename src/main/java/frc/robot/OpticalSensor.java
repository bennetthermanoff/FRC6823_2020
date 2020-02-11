package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class OpticalSensor {
    private int channel;
    private DigitalInput ai;

    public OpticalSensor(int channel) {
        this.channel = channel;
        this.ai = new DigitalInput(channel);
    }

    public boolean seen() {
        return ai.get();
    }
}
