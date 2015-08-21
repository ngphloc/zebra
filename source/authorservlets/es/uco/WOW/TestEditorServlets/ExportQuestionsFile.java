package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
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
 * NAME: ExportQuestionsFile. 
 * FUNCTION: Receives the name of a course, the name of a question file
 * used by the TestEditor tool and the name of a new question file.
 * This servlet converts the question file used by TestEditor to a new
 * question file used in older versions of WOW!
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class ExportQuestionsFile extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		try {
			
			/**
			 * Name of the course which the exported question file belongs to
			 */
			String courseName; 
			
			/**
			 * Name of the question file that will be exported to an older version
			 */
			String questionsFileName; 
			
			/**
			 * Stores the name of the output file
			 */
			String newFileName;
			
			/**
			 * Stores the format of the output file
			 */
			short theFormat;
			
			// Get the parameters
			courseName = req.getParameter("courseName");
			questionsFileName = req.getParameter("questionsFileName");
			newFileName = req.getParameter("newFileName");
			theFormat = Short.parseShort(req.getParameter("theFormat"));
	
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
	
			// Checks if the source file exists
			String result = "";
			
			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (questionFile.exists()) {
				// Creates a new temporal file as output
				result = questionFile.exportQuestionFile(newFileName, theFormat);
			}
			
			out.print(result);
			out.flush();
			out.close();

		} catch (Exception e) {
			UtilLog.writeException(e);
		}
	}
}