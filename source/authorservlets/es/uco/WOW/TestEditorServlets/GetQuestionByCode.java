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
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetQuestionByCode
 * FUNCTION: Receives a course name, a name of a question file with questions 
 * of that course, and a question code. This servlet looks for the question with
 * that question code, obtains its data and returns a Vector object with it.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetQuestionByCode extends HttpServlet
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
		 * Name of the question file where the question is stored
		 */
		String questionsFileName;
		
		/**
		 * Code of the question that is searched
		 */
		String codeQuestion; 
		
		/**
		 * Data of the searched question
		 */
		Question question = null;

		// Gets the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");
		codeQuestion = req.getParameter("codeQuestion");

		if (!courseName.trim().equals("") && !questionsFileName.trim().equals("")
				&& !codeQuestion.trim().equals(""))
		{
			// Checks if the question file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists()) {
				// Gets the data of the searched question
				question = questionFile.getQuestionByCode(codeQuestion);
			}
		}

		// Returns the Question object with the searched question or null
		// in error case
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