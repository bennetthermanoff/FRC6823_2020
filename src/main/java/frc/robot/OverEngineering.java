package frc.robot;
import edu.wpi.first.wpilibj.Spark;

public class OverEngineering extends Thread {
    private int dot = 200;
    private int dash = 500;
    private long startTime;
    private Spark RGB;

    public OverEngineering(Spark RGB) {
        this.RGB = RGB;
   }

    public void run() {
        startTime = System.currentTimeMillis();
          //..- .-. -- --- -- --. .- -.--
        int[] message = { dash, dash, dot, dot, dash, dot, dot, dash, dash, dot, dash, dot, dot, dot, dash, dash, dash,
                dot, dot, dash, dot, dot, dot, dot, dash, dash, dot, dot, dash, dot, dash, dash, dash, dot, dot, dash,
                dot, dot, dot, dot, dot, dot, dot, dot, dot, dot, dash, dash, dash, dash, dot, dot, dash, dot, dash,
                dot, dot, dot, dot, dot, dot, dot, dash, dash};
        for (int i = 0; i < message.length; i++) {
            // on peroid 
            while (System.currentTimeMillis() < startTime + message[i]) {
                RGB.set(0.61);
            }
            startTime += message[i];
            //off peroid
            while (System.currentTimeMillis() < startTime + 100) {
                RGB.set(0.99);
            }
            startTime += 100;
        }
        
    }
}