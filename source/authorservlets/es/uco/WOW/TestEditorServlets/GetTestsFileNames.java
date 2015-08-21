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
import es.uco.WOW.TestFile.TestFile;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetTestsFileNames 
 * FUNCTION: Receives the login of an author and analyses the authorlistfile.xml
 * file of the author to obtain the courses that belongs to that author. Then
 * analyses the folders of each obtained course and returns a Vector object. This
 * ouput object contains in each position a Course object with the name of the
 * course and the name of the requested tests according to its type. 
 * If this servlet receives as parameter only the name of a course, then only 
 * will analyse that course searching for the name of the question files, and 
 * won't search for all the courses that belongs to the author. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetTestsFileNames extends HttpServlet
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
		 * Type of test ("classic" or "adaptive")
		 */
		String testType;
		
		/**
		 * Name of a course
		 */
		String courseName; 
		
		/**
		 * List with the name of the courses of the author
		 */
		Vector courseNameVector; 

		/**
		 * List of the courses of the user and names of the
		 * test files for each course
		 */
		Vector courseVector; 

		// Gets the parameters
		login = req.getParameter("userLogin").trim();
		testType = req.getParameter("testType").trim();
		courseName = req.getParameter("courseName");

		if (courseName == null || courseName.equals(""))
			// Gets the names of all the courses of the author 
			courseNameVector = getCourseNames(login);
		else
		{
			// The petition only treats the course name
			// passed as argument
			courseNameVector = new Vector();
			courseNameVector.add(courseName);
		}

		// Gets the list of courses of the author with the name
		// of the test files for each course
		courseVector = TestFile.getTestsFileNames(courseNameVector, testType);

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
	 * Analyses the authorlistfile.xml file of the author
	 * and returns a list with the name of all the courses that belongs
	 * to the author passed as argument
	 * @param login Author's login
	 * @return Vector object
	 */
	public Vector getCourseNames(String login)
	{
		AuthorsConfig authorConfig = new AuthorsConfig();
		WowAuthor wowAuthor = new WowAuthor();

		wowAuthor = authorConfig.GetAuthor(login);

		return wowAuthor.getCourseList();
	}
}