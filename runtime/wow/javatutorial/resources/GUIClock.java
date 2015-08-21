import java.applet.Applet;
import java.awt.Graphics;
import java.util.*;
import java.text.DateFormat;

public class GUIClock extends Applet implements Sleeper {

    private AlarmClock clock;

    public void init() {
        clock = new AlarmClock();
    }
    public void start() {
        clock.letMeSleepFor(this, ONE_SECOND);
    }
    public void paint(Graphics g) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormatter = DateFormat.getTimeInstance();
        g.drawString(dateFormatter.format(date), 5, 10);
    }
    public void wakeUp() {
        repaint();
        clock.letMeSleepFor(this, ONE_SECOND);
    }
}

