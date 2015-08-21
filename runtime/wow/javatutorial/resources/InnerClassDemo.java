//1.2, 1.3, and compatible releases.  Works in 1.1 if you add
//JFC/Swing 1.1.

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

public class InnerClassDemo {
    public Component createComponents() {
        JButton button = new JButton("Quit");
        //button.addActionListener(new MyActionListener());

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(
                                        30, //top
                                        30, //left
                                        30, //bottom
                                        30) //right
                                        );
        pane.setLayout(new GridLayout(0, 1));
        pane.add(button);

        return pane;
    }

    protected void quit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        //Create the top-level container and add contents to it.
        JFrame frame = new JFrame("Inner Class Demo");
        final InnerClassDemo app = new InnerClassDemo();
        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        //Finish setting up the frame, and show it.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                app.quit();
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}
