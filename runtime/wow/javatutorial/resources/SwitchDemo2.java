/** 
 * SwitchDemo2.java is an application that compiles and runs
 * under J2SE 5.0. It requires no other files.
 *
 * In a real program, you'd probably use the date API
 * instead of calculating days per month.
 */

public class SwitchDemo2 {
    public static void main(String[] args) {

        int month = 2;
        int year = 2000;
        int numDays = 0;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numDays = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numDays = 30;
                break;
            case 2:
                if ( ((year % 4 == 0) && !(year % 100 == 0))
                     || (year % 400 == 0) )
                    numDays = 29;
                else
                    numDays = 28;
                break;
            default:
                numDays = 0;
                break;
        }
        System.out.println("Number of Days = " + numDays);
    }
}
