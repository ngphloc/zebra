package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.Utils.Student;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetStudentAndFinishTest 
 * FUNCTION: Receives a course name, the name of a test file, a student login
 * and the type of test and returns the informacion about that student and
 * that test in a Student object.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez
 */
public class GetStudentAndFinishTest extends HttpServlet
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
		 * Name of the course
		 */
		String courseName;
		
		/**
		 * Name of a test file
		 */
		String testFileName; 
		
		/**
		 * Student's login
		 */
		String login;

		/**
		 * Student data that will be returned
		 */
		Student student = null;

		// Gets the parameters
		courseName = req.getParameter("courseName");
		testFileName = req.getParameter("testFileName");
		login = req.getParameter("login");

		if (!courseName.trim().equals("") && !testFileName.trim().equals("")
				&& !login.trim().equals(""))
		{
			// Checks if the test file exists
			StudentFile studentFile = new StudentFile(courseName, login);
			if (studentFile.exists()) {
				// Gets the student's data
				student = studentFile.getStudentFinishedTest(testFileName);
			}
		}

		// Returns the Student object or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(student);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}
