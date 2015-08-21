/**
 * ForDemo.java is an application that compiles and runs
 * under J2SE v5.0.  It requires no other files.
 *
 * See ForEachDemo.java for an equivalent version that takes
 * advantage of the for-each feature introduced in 5.0.
 */

public class ForDemo {
    public static void main(String[] args) {
        int[] arrayOfInts = { 32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127 };

        for (int i = 0; i < arrayOfInts.length; i++) {
            System.out.print(arrayOfInts[i] + " ");
        }
        System.out.println();
    }
}
