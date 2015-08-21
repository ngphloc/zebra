/**
 * ClickMeApp.java is an application that compiles and runs
 * under J2SE v5.0.  It requires two additional files:
 *   ClickMe.java
 *   Spot.java
 */
 
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class ClickMeApp implements Runnable {
    
    /**
     * This constructor creates an instance of ClickMeApp,
     * which creates and shows a window containing a
     * ClickMe component.
     */
    public ClickMeApp() {
        /*
         * Tells the event-dispatching thread (used to
         * display and handle events of a Swing GUI) to
         * call the run method of "this" (the ClickMeApp
         * object this constructor created).  The
         * argument to invokeLater must implement the
         * Runnable interface, which guarantees that
         * it defines the run method.
         */
        SwingUtilities.invokeLater(this);
    }
    
    /**
     * Creates and shows the GUI.  This method should be
     * invoked on the event-dispatching thread.
     */
    public void run() {
        createAndShowGUI();
    }
    
    /**
     * Brings up a window that contains a ClickMe component.
     * For thread safety, this method should be invoked from
     * the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("ClickMeApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the layout manager.
        frame.setLayout(new BorderLayout());
        
        //Add the ClickMe component.
        ClickMe component = new ClickMe();
        frame.add(component, BorderLayout.CENTER);
        
        //Add an explanatory label.
        frame.add(new JLabel("Click within the rectangle."),
                             BorderLayout.SOUTH);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    //This method is automatically executed.
    public static void main(String[] args) {
        //Create an instance of ClickMeApp.
        new ClickMeApp();
    }
}
