/** 
 * ContinueWithLabelDemo.java is an application that compiles
 * and runs under J2SE 5.0. It requires no other files.
 * 
 * The purpose of this example is to demonstrate the continue
 * statement, NOT to show how to search. In a real program,
 * you'd probably use the String method indexof(String, int). 
 */

public class ContinueWithLabelDemo {
    public static void main(String[] args) {

        String searchMe = "Look for a substring in me";
        String substring = "sub";
        boolean foundIt = false;

        int max = searchMe.length() - substring.length();  

    test:
        for (int i = 0; i <= max; i++) {
            int n = substring.length();
            int j = i;
            int k = 0;
            while (n-- != 0) {
                if (searchMe.charAt(j++) != substring.charAt(k++)) {
                    continue test;
                }
            }    
            foundIt = true;
            break test;
        }
        System.out.println(foundIt ? "Found it" : "Didn't find it");
    }
}
