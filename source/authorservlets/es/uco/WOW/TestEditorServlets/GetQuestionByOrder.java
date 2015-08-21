package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
 * NAME: GetQuestionByOrder 
 * FUNCTION: Receives a course name, the name of a question file of that
 * course, and a order number that indicates where a question is located 
 * in the file. Returns the data relative to that question or null in 
 * other case
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */

public class GetQuestionByOrder extends HttpServlet
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
		 * Name of the file of questions
		 */
		String questionsFileName; 
		
		/**
		 * Position that the question has in the question file
		 */
		String numQuestion; 

		/**
		 * Data of the searched question 
		 */
		Question question = null; 

		// Gets the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");
		numQuestion = req.getParameter("numQuestion");

		if (!courseName.trim().equals("") && !questionsFileName.trim().equals("")
				&& !numQuestion.trim().equals(""))
		{
			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists()) {
				// Gets the data of the searched question
				question = questionFile.getQuestionByOrder(Integer.valueOf(numQuestion).intValue());
			}
		}

		// Returns the Question object with the data of the searched question
		// or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(question);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}