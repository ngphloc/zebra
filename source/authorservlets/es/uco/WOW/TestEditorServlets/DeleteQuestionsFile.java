package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.TestEditor.TestEditor;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: DeleteQuestionsFile 
 * FUNCTION: This servlet receives the name of a course and a name
 * of a question file of that course that will be deleted from the
 * course.This servlet also deletes all the associated images to the 
 * question file that will be deleted.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DeleteQuestionsFile extends HttpServlet
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
		 * Name of the course which the the deleted question
		 * file belongs to.
		 */
		String courseName; 
		
		/**
		 * Name of the question file that will be deleted
		 */
		String questionsFileName; 
		
		/**
		 * Login of the author that makes the job
		 */
		String authorName;

		// Get the parameters
		courseName = req.getParameter("courseName");
		questionsFileName = req.getParameter("questionsFileName");
		authorName = req.getParameter("authorName");

		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		if (!courseName.trim().equals("") && !questionsFileName.trim().equals(""))
		{
			// Checks wether the question file that will be deleted belongs
			// to a Test not finished
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			boolean checkQuestionsFile = questionFile.checkQuestionsFileInTest();
			if (checkQuestionsFile == true)
			{
				// Deletes the question file
				if (questionFile.deleteQuestionsFile(authorName))
					out.print(TestEditor.TEXT_FILE_DELETED);
				else
				{
					out.println("ERROR: The file has not been deleted.");
					out.println("There was a problem with the server or with the own question file.");
					out.println("Try it later.");
				}
			}
			else
			{
				out.println("ERROR: The question file is in a test not finished for the students.");
				out.println("You cannot delete the question file.");
				out.println("If you want to delete the question file,");
				out.println("all the students must finish this test");
			}
		}
		else
		{
			out.println("ERROR: The file has not been deleted.");
			out.println("There was a problem with the server or with the own question file.");

		}
		out.flush();
		out.close();
	}
}