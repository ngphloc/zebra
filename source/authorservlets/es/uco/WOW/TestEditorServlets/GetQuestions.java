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
 * NAME: GetQuestions. 
 * FUNCTION: Receives the name of a course and the name of a question file
 * of that course and returns a Vector object containing all the questions
 * stored in that question file. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetQuestions extends HttpServlet
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
		 * Name of the course which the question file belongs to
		 */
		String courseName; 
		
		/**
		 * Name of a question file 
		 */
		String questionsFileName; 
		
		/**
		 * Ouput vector of Question objects
		 */
		Vector questionsVector = null;

		// Gets the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");

		if (!courseName.equals("") && !questionsFileName.equals(""))
		{
			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists())
				// Gets the data of all the question on the file
				questionsVector = questionFile.getQuestions();
		}

		// Returns the vector with the data of all the questions
		// or null in other case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(questionsVector);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}