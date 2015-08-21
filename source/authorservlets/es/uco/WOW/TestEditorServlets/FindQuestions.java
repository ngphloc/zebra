package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

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
 * @version 1.0
 */

/**
 * NAME: FindQuestions
 * FUNCTION: Receives a Question object that contains some data
 * about questions. This servlet look for the questions that matchs
 * with the parameters, and returns a Vector object
 * with their data 
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class FindQuestions extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * A list of Question objects that contains the data
		 * of the searched questions. This object will be the
		 * output of the servlet
		 */
		Vector questionsVector = null; 

		try
		{
			// Get the parameters
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
			
			// Get the question object 
			Question question = (Question) bufferIn.readObject();
				
			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(question.getCourse(),question.getQuestionsFileName());
			if (questionFile.exists()) {
				// Gets the data of the searched questions
				questionsVector = questionFile.findQuestions(question);
			}
		}
		catch (java.lang.ClassNotFoundException e)
		{
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.println("ERROR: The question data have not been received.");
		}

		// Returns the list of questions with the searched questions data
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