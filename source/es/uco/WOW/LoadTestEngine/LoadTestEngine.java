package es.uco.WOW.LoadTestEngine;

import java.util.StringTokenizer;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.WOWException;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Test;
/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: LoadTestEngine.
 * FUNCTION: This class creates and shows a web page that contains the applet
 *   where the test is showed to the student.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class LoadTestEngine {

    /**
     * Name of the root folder of the WOW! system.
     */
    private String wowPath;

    /**
     * Relative path in the WOW! system to the XML files.
     */
    private String xmlRootPath;

    /**
     * Name of the folder where the items files are stored.
     */
    private String itemsPath;

    /**
     * URL of the applet.
     */
    private String codebase;

    /**
     * URL to redirect when the test if finished.
     */
    private String urlGoAfterEndTest;

    /**
     * Login of the student that makes the test.
     */
    private String login;

    /**
     * Name of the course that the test is associated to.
     */
    private String courseName;

    /**
     * Profile of the student.
     */
    private Profile profile;

    /**
     * Output of the applet.
     */
    private StringBuffer sb;

    /**
     * Name of the test file.
     */
    private String testFileName = "";

    /**
     * Test object that will be represented in the applet.
     */
    private Test test = null;

    /**
     * Public Constructor.
     * @param theLogin Student's login.
     * @param theProfile Student's profile.
     * @param smallDocName Contains the path to the name of the test.
     * @param buffer Reference to the output.
     * @throws WOWException Exception during processing
     */
    public LoadTestEngine(
   		 final String theLogin, final Profile theProfile, String smallDocName,
   		 final StringBuffer buffer, final String contextpath, final String pathinfo) throws WOWException {
        // Gets the login and profile of the student
        this.login = theLogin;
        this.profile = theProfile;

        // Gets the system's and items path
        this.xmlRootPath = WOWStatic.config.Get("XMLROOT");
        this.wowPath = WOWStatic.config.Get("CONTEXTPATH");
        this.wowPath = wowPath + xmlRootPath.substring(xmlRootPath.lastIndexOf(wowPath) + wowPath.length());
        itemsPath = WOWStatic.config.Get("itemspath");
        wowPath = wowPath + itemsPath;

        // Checks the start protocol of the reference to the course
        if (smallDocName.startsWith("/file:")) {
      	  smallDocName = smallDocName.substring("/file:".length());
        }

        // Reads the name of the course
        this.courseName = smallDocName.substring(1);
        StringTokenizer token = new StringTokenizer(courseName, "/");
        this.courseName = token.nextElement().toString();
        //token.nextToken();
        this.testFileName = token.nextToken().toString();
        this.testFileName = this.testFileName.substring(0, this.testFileName.lastIndexOf("."));
        this.sb = buffer;

        // Creates the path to load the applet that exams the student
        this.codebase = contextpath + "/lib";
        this.urlGoAfterEndTest = contextpath + "/ViewGet" + pathinfo;

        /**
         * Reads the test configuration to check if is a activity test
         * or an exam test. In function of this, the background colour
         * of the page.
         */
        TestFile testFile = new TestFile(courseName, testFileName);
        test = testFile.getTest();

        if (test == null) {
      	  throw new WOWException("The test has not been found");
        }
    }


    /**
     * This method creates dinamically a web page that loads the applet in
     * which the students will be evaluated.
     */
    public void PrintApplet() {

   	  String appletClass = "es.uco.WOW.AppletTestEngine.AppletTestEngine.class";

        // Deletes the previous content of the output
        sb.delete(0, sb.length());

        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        sb.append(" \"DTD/xhtml1-strict.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sb.append("<head></head>\n");
        sb.append("<body bgcolor=\"").append(test.getBgColor());
        sb.append("\" leftmargin=\"0\" topmargin=\"0\" ");
        sb.append("marginwidth=\"0\" marginheight=\"0\" ");

        if (test.getBackground().trim().equals("") == false)
            sb.append("background=\"").append(wowPath + test.getBackground()).append("\">\n");
        else
            sb.append("background=\"\">\n");

        sb.append("<center>\n\n");
        sb.append("<table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" ");
        sb.append("cellspacing=\"0\">\n");
        sb.append("<tr>\n");
        sb.append("<td>\n");
        sb.append("<center>\n");
        sb.append("<strong><font size=\"6\" color=\"").append(test.getTitleColor()).append("\">");
        sb.append(test.getTestName());
        sb.append("</font><strong>\n");
        sb.append("<p></p>\n");
        sb.append("</center>\n");
        sb.append("</td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>\n");
        sb.append("<td>\n");

        if (test.getEnable() == true) {
      	  sb.append("<applet ");
     	  	  sb.append("	codebase=\"").append(codebase).append("\" \n");
           sb.append("	code=\"" + appletClass + "\" \n");
           sb.append("	archive=\"TestEngine.jar\" \n");
           sb.append("	width=\"560\" \n");
           sb.append("	height=\"550\" \n");
           sb.append("	name=\"applet\" mayscript=\"true\" > \n");
           sb.append(" 		<param name=\"urlGoAfterEndTest\" value=\"").append(urlGoAfterEndTest).append("\">\n");
           sb.append("		<param name=\"courseName\" value=\"").append(courseName).append("\">\n");
           sb.append("		<param name=\"testFileName\" value=\"").append(testFileName).append("\">\n");
           sb.append("		<param name=\"login\" value=\"").append(login).append("\">\n");
           sb.append("		<param name=\"profileID\" value=\"").append(profile.id).append("\">\n");
           sb.append("</applet>\n");
        } else {
            sb.append("<strong><font size=\"7\" color=\"").append(test.getTitleColor()).append("\">THE TEST IS DISABLED.").append("</font><strong>\n");
        }

        sb.append("</tr>\n</td>\n</table>\n</center>\n</body>\n</html>\n");
    }

} // End of LoadTestEngine class