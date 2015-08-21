package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: DeleteQuestion 
 * FUNCTION: This servlet receives the name of a course, the name 
 * of a question file of that course and the code of a question that 
 * belongs to that file. This question will be deleted of that file
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DeleteQuestion extends HttpServlet
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
		 * Name of the course that the question file belongs to.
		 */
		String courseName; 
		
		/**
		 * Name of the question file which will be deleted a question
		 */
		String questionsFileName;
		
		/** 
		 * Code of the questions that will be deleted.
		 */
		String codeQuestion; 
		
		/**
		 * Login of the author that makes this job.
		 */
		String authorName;

		// Get the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");
		codeQuestion = req.getParameter("codeQuestion");
		authorName = req.getParameter("authorName");

		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		if (!courseName.trim().equals("") && !questionsFileName.trim().equals("")
				&& !codeQuestion.trim().equals(""))
		{
			// Checks wether the question that will be deleted take part
			// of another test not finished
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			boolean checkQuestion = questionFile.checkQuestionInTest(codeQuestion);

			if (checkQuestion == true) {
				// Deletes the question.
				String delete = questionFile.deleteQuestion(codeQuestion, authorName);
				out.println(delete);
			} else {
				out.println("ERROR: The question is in a test not finished by the students.");
				out.println("You cannot delete the question.");
				out.println("If you want to delete the question,");
				out.println("all the students must finish this test");
			}
		} else {
			out.println("Question not deleted.");
			out.println("There is some problem with the server");
			out.println("or with the own question file.");
			out.println("Possibly you don't have permission to delete the question.");
		}

		out.flush();
		out.close();
	}
}