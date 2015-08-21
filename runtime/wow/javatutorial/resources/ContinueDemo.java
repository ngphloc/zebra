/** 
 * ContinueDemo.java is an application that compiles and runs
 * under J2SE 5.0. It requires no other files.
 */
public class ContinueDemo {
    public static void main(String[] args) {

        StringBuffer searchMe = new StringBuffer(
                  "peter piper picked a peck of pickled peppers");
        int max = searchMe.length();
        int numPs = 0;

	//NOTE: We can't use foreach because StringBuffer
	//isn't a Collection.
        for (int i = 0; i < max; i++) {
	    //interested only in p's
            if (searchMe.charAt(i) != 'p')
	        continue;

	    //process p's
	    numPs++;
            searchMe.setCharAt(i, 'P');
        }
        System.out.println("Found " + numPs + " p's in the string.");
        System.out.println(searchMe);
    }
}
