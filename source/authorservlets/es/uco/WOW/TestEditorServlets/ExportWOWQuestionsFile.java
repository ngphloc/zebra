package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: ExportWOWQuestionsFile. 
 * FUNCTION: Receives the name of a course, the name of a question file
 * used by the TestEditor tool and the name of a new question file.
 * This servlet converts the question file used by TestEditor to a new
 * question file used in older versions of WOW!
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class ExportWOWQuestionsFile extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * Name of the course which the exported question file belongs to
		 */
		String courseName; 
		
		/**
		 * Name of the question file that will be exported to an older version
		 */
		String questionsFileName; 
		
		/**
		 * Stores the relative path of the output file
		 */
		String testOldPath; 
		
		/**
		 * Checks wether the servlet must replace an existing file
		 * with the same name or not
		 */
		boolean replace; 
		
		// Get the parameterss
		try {
			courseName = req.getParameter("courseName");
			questionsFileName = req.getParameter("questionsFileName");
			testOldPath = req.getParameter("testOldPath");
			replace = Boolean.valueOf(req.getParameter("replace")).booleanValue();
	
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
	
			// Checks if the source file exists
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists()) {
				// Checks if exists a file with the same name of the exported one
				boolean exportFile = QuestionFile.checkQuestionsFileOld(testOldPath);
	
				if ((exportFile == true && replace == true) || exportFile == false)
				{
					// Creates a new temporal file as output
					if (questionFile.exportWOWQuestionFile(testOldPath))
						out.println("The file has been exported.");
					else
						out.println("ERROR: The file has NOT been exported.");
				} else {
					out.println(TestEditor.TEXT_ANOTHER_FILE_EXISTS);
					out.println("Do you want to replace it?");
				}
			}
			else
				out.println("ERROR: The source file is not available to be exported.");
	
			out.flush();
			out.close();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
	}
}