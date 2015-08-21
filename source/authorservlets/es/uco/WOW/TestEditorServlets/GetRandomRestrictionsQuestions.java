package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.Utils.Question;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetRandomRestrictionsQuestions 
 * FUNCTION: Receives the name of a course, the name of a question file
 * of that course, a integer number that indicates the number of questions
 * requested from that file and a Question object with several parameters.
 * The servlet looks for the questions according to that parameters and 
 * returns it in a Vector object
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetRandomRestrictionsQuestions extends HttpServlet
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
		 * Number of requested questions
		 */
		int numQuestionsToGet = -1; 
		
		/**
		 * Question with the parameters to do the search
		 */
		Question question;

		try
		{
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
			
			// Receives the name of course, question file, number of questions and
			// the Question object
			// DON'T MODIFY THE ORDER OF THE PARAMETERS
			courseName = (String) bufferIn.readObject();
			questionsFileName = (String) bufferIn.readObject();
			numQuestionsToGet = bufferIn.readInt();
			question = (Question) bufferIn.readObject();

			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists())
				// Gets the data of the questions from the file
				questionsVector = questionFile.getRandomRestrictionsQuestions(numQuestionsToGet, question);
		}
		catch (java.lang.ClassNotFoundException e)
		{
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.println("ERROR: The question data have not been received.");
		}

		// Returns the Vector object with the questions or null in 
		// error case
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