/**
 * Spot.java is used by ClickMe.java.
 */

public class Spot {
    //instance variables
    private int size;
    public int x, y;
    
    //constructor
    public Spot() {
        x = -1;
        y = -1;
        size = 1;
    }

    //methods for access to the size instance variable    
    public void setSize(int newSize) {
        if (newSize >= 0) {
            size = newSize;
        }
    }
    public int getSize() {
        return size;
    }
}
