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
 * NAME: GetRandomQuestions 
 * FUNCTION: Receives a course name and a name of a question file
 * of that course, and a integer number that indicates the number of 
 * questions that will be requested from that file and returns a Vector
 * object that contains all the data of the questions requested from
 * the question file.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetRandomQuestions extends HttpServlet
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
		 * Name of the question file
		 */ 
		String questionsFileName; 
		
		/**
		 * Output vector
		 */
		Vector questionsVector = null; 
		
		/**
		 * Number of questions to return
		 */
		int numQuestionsToGet = -1; 
		
		// Gets the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");

		try
		{
			numQuestionsToGet = Integer.valueOf(req.getParameter("numQuestionsToGet")).intValue();
		}
		catch (java.lang.NumberFormatException e)
		{
			numQuestionsToGet = -1;
		}

		if (!courseName.equals("") && !questionsFileName.equals("")
				&& numQuestionsToGet != -1)
		{
			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists())
				// Gets the data from the file
				questionsVector = questionFile.getRandomQuestions(numQuestionsToGet);
		}

		// Returns the vector object with the data of the questions
		// or null in error case
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
