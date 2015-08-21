package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Test;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetTest 
 * FUNCTION: Receives a course name, a name of a test file and
 * returns the information about that test in a Test object. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetTest extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * Name of the course which the test file belongs to
		 */
		String courseName;
		
		/**
		 * Name of the test file
		 */
		String testFileName; 

		/**
		 * Test data
		 */
		Test test = null;

		// Gets the parameters
		courseName = req.getParameter("courseName");
		testFileName = req.getParameter("testFileName");

		if (courseName != null && testFileName != null) {
			// Checks if the test file exists
			TestFile testFile = new TestFile(courseName, testFileName);
			if (testFile.exists()) {
				// Gets the test data
				test = testFile.getTest();
			}
		}

		// Returns the Test object or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(test);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}