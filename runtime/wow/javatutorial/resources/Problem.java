//1.3

import java.util.*;

public class Problem {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("Exiting.");
                    timer.cancel();
                }
            },
            5000);

        System.out.println("In 5 seconds this application will exit. ");
    }
}
