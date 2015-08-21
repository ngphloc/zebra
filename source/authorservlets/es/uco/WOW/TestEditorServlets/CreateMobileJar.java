package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.TestFile.TestFile;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: CreateMobileJar
 * FUNCTION: Servlet that receives the necessary data to
 *   create a JAR to be used in a mobile phone.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class CreateMobileJar extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	//{
		/** Name of the course */
   	String courseName;
   	/** Name of the test file */
   	String testFileName;
		
		// Get the parameters
		courseName = req.getParameter("courseName");
		testFileName = req.getParameter("testFileName");
		if (courseName != null) {
			courseName = courseName.trim();
		} else {
			courseName = "";
		}		
		if (testFileName != null) {
			testFileName = testFileName.trim();
		} else {
			testFileName = "";
		}
		
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		if (!courseName.equals("") && !testFileName.equals("")) {
			// Creates the mobile jar
			TestFile testFile = new TestFile(courseName, testFileName);
			String createJar = testFile.createMobileJar();
			
			if (createJar == null) {
				out.println("ERROR: The mobile JAR has not been created.");
				out.println("There was a problem with the server. Try it later.");
			} else {
				out.println(createJar);
			}
		} else {
			out.println("ERROR: The mobile JAR has not been created.");
			out.println("You must specify a correct course name and test file name");
		}
		out.flush();
		out.close();
	}
}