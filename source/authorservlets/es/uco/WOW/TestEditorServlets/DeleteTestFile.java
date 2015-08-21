package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.TestFile.TestFile;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: DeleteTestFile. 
 * FUNCTION: This servlet receives the necessary data to delete a Test file.
 * It also deletes the references to this file in the question files that could
 * belong to this test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DeleteTestFile extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * Stores the name of the course which
		 * the deleted test belongs to
		 */
		String courseName; 
		
		/**
		 * Stores the name of the file test that will be deleted
		 */
		String testFileName; 
		
		/**
		 * Login of the author that makes this job.
		 */
		String authorName;

		// Get the parameters
		courseName = req.getParameter("courseName");
		testFileName = req.getParameter("testFileName");
		authorName = req.getParameter("authorName");

		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		if (courseName != null && testFileName != null) {
			// Checks wether the test is finished for all the students or no
			TestFile testFile = new TestFile(courseName, testFileName);
			boolean remove = testFile.checkStudentFinishTest();
			if (remove == false) {
				out.println("ERROR: There are students that don't finished the test.");
				out.println("You cannot delete the test.");
				out.println("If you want to delete the test,");
				out.println("all the students must finish the test");
			} else {
				// Deletes the test file
				if (testFile.deleteTestFile(true, null, authorName)) {
					out.println(TestEditor.TEXT_FILE_DELETED);
				} else {
					out.println("ERROR: The file HAS NOT BEEN deleted.");
					out.println("There was a problem with the server or with the own question file.");
					out.println("Please try it later.");
				}
			}
		} else {
			out.println("ERROR: The file HAS NOT BEEN deleted.");
			out.println("There was a problem with the server or with the own question file.");
		}
		out.flush();
		out.close();
	}
}