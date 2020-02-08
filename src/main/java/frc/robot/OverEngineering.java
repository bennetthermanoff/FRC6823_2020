package frc.robot;

public class OverEngineering extends Thread {
    private int dot = 200;
    private int dash = 500;
    private RGB RGB;

    public OverEngineering(RGB RGB) {
        this.RGB = RGB;
    }

    public void run() {
        try { // the compiler whines about sleep
            //gracious profesionalism
            int[] message = { dash, dash, dot, dot, dash, dot, dot, dash, dash, dot, dash, dot, dot, dot, dash, dash, dash,
                dot, dot, dash, dot, dot, dot, dot, dash, dash, dot, dot, dash, dot, dash, dash, dash, dot, dot, dash,
                dot, dot, dot, dot, dot, dot, dot, dot, dot, dot, dash, dash, dash, dash, dot, dot, dash, dot, dash,
                dot, dot, dot, dot, dot, dot, dot, dash, dash };
            for (int i = 0; i < message.length; i++) {
            // on peroid
            RGB.setRed();
            sleep(message[i]);
            // off peroid
            RGB.setOff();
            sleep(100);
            }
        } catch (Exception error) {
            // Its rgb. Does it matter if it crashes?
        }
    }
}