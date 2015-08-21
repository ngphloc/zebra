package es.uco.WOW.LoadTestEngine;

import java.io.ByteArrayInputStream;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.datacomponents.Resource;
import vn.spring.WOW.datacomponents.ResourceType;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 *
 * @version 1.0
 */

/**
 * NAME: DoAppletTest
 * FUNCTION: This class activates a filter which generates a HTML-form
 *   with a test that a user can fill in.
 *   The results of the test will be checked by EvaluateTest.
 * LAST MODIFICATION: 06-02-2008
 *
 * @author Isaac R�der Jim�nez, changed by Phuoc-Loc Nguyen
 */
public class DoAppletTest {
 
	/**
     * Output of the applet. It is passed by
     * reference to the LoadTestEngine class.
     */
    private StringBuffer sb;

    /**
     * Calls the LoadTestEngine class that returns the necessary code to
     * show the test.
     * @param profile Student's profile.
     * @param login Student's login.
     * @param contextpath URL to the WOW! server
     * @param pathinfo Info about the path
     * @return The 'sb' StringBuffer as a Resource object
     */
    public Resource getOutput(
   	 final Profile profile, final String login, final String contextpath, final String pathinfo) {
    //{
        try {
      	  sb = new StringBuffer();
           String test = pathinfo;
           test = test.substring(0, test.indexOf("|"));
           LoadTestEngine loadTestEngine = new LoadTestEngine(login, profile, test, sb, contextpath, pathinfo);

           // Dump the test to out.
           loadTestEngine.PrintApplet();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Returns the StringBuffer as a Byte Array.
        // This is because StringBufferInputStream class is deprecated
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
        return new Resource(inputStream, new ResourceType("text/html"));
        // Before we returned this
        //return new Resource(new StringBufferInputStream(sb.toString()), new ResourceType("text/html"));
    }

} // End of DoAppletTest class