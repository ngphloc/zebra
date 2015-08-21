/** 
 * RelationalDemo.java is an application that compiles and runs
 * under J2SE 5.0. It requires no other files.
 */

public class RelationalDemo {
    public static void main(String[] args) {
        //a few numbers
        int i = 37;
        int j = 42;
        int k = 42;
        System.out.println("Variable values...");
        System.out.println("    i = " + i);
        System.out.println("    j = " + j);
        System.out.println("    k = " + k);

	//greater than
        System.out.println("Greater than...");
        System.out.println("    i > j is " + (i > j));     //false
        System.out.println("    j > i is " + (j > i));     //true
        System.out.println("    k > j is " + (k > j));     //false, they are equal

	//greater than or equal to
        System.out.println("Greater than or equal to...");
        System.out.println("    i >= j is " + (i >= j));   //false
        System.out.println("    j >= i is " + (j >= i));   //true
        System.out.println("    k >= j is " + (k >= j));   //true

	//less than
        System.out.println("Less than...");
        System.out.println("    i < j is " + (i < j));     //true
        System.out.println("    j < i is " + (j < i));     //false
        System.out.println("    k < j is " + (k < j));     //false

	//less than or equal to
        System.out.println("Less than or equal to...");
        System.out.println("    i <= j is " + (i <= j));   //true
        System.out.println("    j <= i is " + (j <= i));   //false
        System.out.println("    k <= j is " + (k <= j));   //true

	//equal to
        System.out.println("Equal to...");
        System.out.println("    i == j is " + (i == j));   //false
        System.out.println("    k == j is " + (k == j));   //true

	//not equal to
        System.out.println("Not equal to...");
        System.out.println("    i != j is " + (i != j));   //true
        System.out.println("    k != j is " + (k != j));   //false
    }
}
