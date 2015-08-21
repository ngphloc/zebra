package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.config.WowAuthor;
import vn.spring.WOW.config.AuthorsConfig;
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
 * NAME: GetQuestionsFileNames 
 * FUNCTION: Analyses the authorlistfile.xml to obtain all the courses
 * that belongs to the user received as parameter. Then analyses the folders
 * of each course and returns a Vector object. This one contains in each position
 * a Course object with a name of a course and the list of names of the question
 * files for this course. If this servlet only receives as parameter
 * the name of a course, then only analyses that course looking for the name
 * of the question files and doesn't analyses all the courses that belongs 
 * to the author. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */

public class GetQuestionsFileNames extends HttpServlet
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
		 * Author's login
		 */
		String login; 
		
		/**
		 * Name of a course. Optional parameter
		 */
		String courseName; 
		
		/**
		 * List with the name of the courses of the author
		 */
		Vector courseNameVector; 
		
		/**
		 * Output vector
		 */
		Vector courseVector = null;

		// Gets the parameters
		login = req.getParameter("userLogin").trim();
		courseName = req.getParameter("courseName");

		if (courseName == null || courseName.trim().equals(""))
			// Gets the name of the courses of the author
			courseNameVector = getCoursesNames(login);
		else
		{
			// Creates the list with only the course received as argument
			courseNameVector = new Vector();
			courseNameVector.add(courseName);
		}

		// Gets the list of the courses of the author with the name
		// of the question files for each course
		courseVector = QuestionFile.getQuestionsFileNames(courseNameVector);

		// Returns the data or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(courseVector);
		} catch (java.io.IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}

	
	/**
	 * Get the name of the courses that belongs to 
	 * the author passed as argument. Analyses the
	 * authorlistfile.xml file of the author to obtain
	 * this list.
	 * @param login Login of the author
	 * @return a Vector object
	 */
	private Vector getCoursesNames(String login)
	{
		AuthorsConfig authorConfig = new AuthorsConfig();
		WowAuthor wowAuthor = new WowAuthor();

		wowAuthor = authorConfig.GetAuthor(login);

		return wowAuthor.getCourseList();
	}
}