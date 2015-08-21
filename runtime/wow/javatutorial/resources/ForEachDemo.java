/**
 * ForEachDemo.java is an application that compiles and runs
 * under J2SE v5.0 (and *not* under earlier versions).
 * It requires no other files.
 *
 * See ForDemo.java for an equivalent version that doesn't
 * use the enhanced for language feature introduced in 5.0.
 */
public class ForEachDemo {
    public static void main(String[] args) {
        int[] arrayOfInts = { 32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127 };

        for (int element : arrayOfInts) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}
