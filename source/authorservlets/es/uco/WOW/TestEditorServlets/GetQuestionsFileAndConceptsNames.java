package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.Utils.Course;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetQuestionsFileAndConceptsNames 
 * FUNCTION: Receives a Course object that contains the name of a course.
 * Analyses the folder of that course and returns a Course object
 * that contains the name of that course and a list of question files 
 * of that course that are associated to the concept files which name are 
 * the same as the course name parameter.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetQuestionsFileAndConceptsNames extends HttpServlet
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
		 * The course received as argumen
		 */
		Course course = null;
		
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
		
		try {
			// Gets the parameters
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
			course = (Course) bufferIn.readObject();

			// Gets the list of courses of the author and the names of the
			// question files for each course
			course = QuestionFile.getQuestionsFileNames(course);

			// Returns the data
			objectOutputStream.writeObject(course);
		} catch (Exception e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}