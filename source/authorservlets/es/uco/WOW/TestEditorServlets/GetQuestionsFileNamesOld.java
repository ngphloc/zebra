package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetQuestionsFileNamesOld 
 * FUNCTION: Receives the path where the old question files are located.
 * Returns a list of courses of the author with the names of the old 
 * question file names associated to that course.
 * This is done to import this old question files to the current version
 * of the TestEditor tool.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */

public class GetQuestionsFileNamesOld extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		/**
		 * Path where the old question file are located
		 */
		String testOldPath; 
		
		/**
		 * Ouput Vector object of the servlet
		 */
		Vector questionsFileNameOldVector = null;

		// Gets the parameters
		testOldPath = req.getParameter("testOldPath").trim();

		// Gets the course list of the author with the name
		// of the question files for each course
		try {
			questionsFileNameOldVector = QuestionFile.getQuestionsFileNamesOld(testOldPath);
		} catch (Exception e) {}

		// Returns the data or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(questionsFileNameOldVector);
		} catch (java.io.IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}