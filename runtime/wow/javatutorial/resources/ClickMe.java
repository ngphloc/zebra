/**
 * ClickMe.java is used by ClickMeApp.java.  ClickMe is a
 * component that puts a spot wherever you click.  It requires
 * another file: Spot.java.
 */
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.*;

public class ClickMe extends JComponent
                     implements MouseListener {
    private Spot spot = null;
    private static final int RADIUS = 7;
    private Color spotColor = new Color(107, 116, 2); //olive

    /** Creates and initializes the ClickMe component. */
    public ClickMe() {
        addMouseListener(this);
        
        //Hint at good sizes for this component.
        setPreferredSize(new Dimension(RADIUS * 30, RADIUS * 15));
        setMinimumSize(new Dimension(RADIUS * 4, RADIUS * 4));
        
        //Request a black line around this component.
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    /**
     * Paints the ClickMe component.  This method is
     * invoked by the Swing component-painting system.
     */
    public void paintComponent(Graphics g) {
        /**
         * Copy the graphics context so we can change it.
         * Cast it to Graphics2D so we can use antialiasing.
         */
        Graphics2D g2d = (Graphics2D)g.create();
        
        //Turn on antialiasing, so painting is smooth.
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Paint the background.
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        //Paint the spot.
        if (spot != null) {
            int radius = spot.getSize();
            g2d.setColor(spotColor);
            g2d.fillOval(spot.x - radius, spot.y - radius,
                         radius * 2, radius * 2);
        }
    }
    
    //Methods required by the MouseListener interface.
    public void mousePressed(MouseEvent event) {
        if (spot == null) {
            spot = new Spot();
            spot.setSize(RADIUS);
        }
        spot.x = event.getX();
        spot.y = event.getY();
        repaint();
    }
    public void mouseClicked(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
}
