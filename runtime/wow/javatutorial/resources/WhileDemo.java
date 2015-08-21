/** 
 * WhileDemo.java is an application that compiles and runs
 * under J2SE 5.0. It requires no other files.
 */

public class WhileDemo {
    public static void main(String[] args) {

        String copyFromMe = "Copy this string until you encounter the letter 'g'.";
        StringBuffer copyToMe = new StringBuffer();

        int i = 0;
        char c = copyFromMe.charAt(i);

        while (c != 'g') {
            copyToMe.append(c);
            c = copyFromMe.charAt(++i);
        }
        System.out.println(copyToMe);
    }
}
