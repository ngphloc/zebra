package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.Utils.Adaptive;
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
 * NAME: GetInformationValues 
 * FUNCTION: Receives the theta value and a Vector object with
 * the main data of the questions used in the test. Calculates the value of
 * the information function for each question and stores it in a Vector that
 * will be returned as the result of the servlet
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class GetInformationValues extends HttpServlet
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
		 * Knowledge of the student
		 */
		double theta = 99999;
		
		/**
		 * Question vector
		 */
		Vector questionVector = null;
		
		/**
		 * Name of parameters of the adaptative test. 
		 * It is received as argument
		 */
		int irtModel = 3;
		
		/**
		 * Information Vector that will be the output of the servlet
		 */
		Vector questionInformationVector = null;

		try
		{
			// Get the parameters
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
			
			// Important: Dont modify the order
			theta = bufferIn.readDouble();
			questionVector = (Vector) bufferIn.readObject();
			irtModel = bufferIn.readInt();
		}
		catch (java.lang.ClassNotFoundException e)
		{
			theta = 99999;
			questionVector = null;
		}

		if (theta != 99999 && questionVector != null && !questionVector.isEmpty())
		{
			Adaptive adaptive = new Adaptive(irtModel);
				
			questionInformationVector = new Vector();
			
			// Calculates the information value for each question
			for (int i = 0; i < questionVector.size(); i++)
			{
				// Reads the main data of the current question
				Question question = (Question) questionVector.get(i);

				// Checks if the question has been correct or not, if this 
				// question has been used in the test.
				boolean correct = question.isCorrect();

				// Looks for the question
				QuestionFile questionFile = new QuestionFile(question.getCourse(), question.getQuestionsFileName());
				question = questionFile.getQuestionByCode(question.getCodeQuestion());

				question.setCorrect(correct);

				// calculates the information function value
				double info = adaptive.information(question.getDiscrimination(),
						question.getDifficulty(), question.getGuessing(), theta);
				question.setInformationValue(info);
				questionInformationVector.add(question);
			}
		}
		else
			questionInformationVector = null;

		// Returns the list of the searched question in a Vector object or 
		// null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(questionInformationVector);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
}