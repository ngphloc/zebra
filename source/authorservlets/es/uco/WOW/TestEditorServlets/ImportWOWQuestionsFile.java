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
import es.uco.WOW.Utils.UtilLog;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: ImportWOWQuestionsFile 
 * FUNCTION: Receives a course name, the relative path to a question file
 * that belongs to older versions of WOW!, and the name of a new question file.
 * This servlet converts the question file from an older versi�n of WOW! to a
 * question file of the current version of the TestEditor tool.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class ImportWOWQuestionsFile extends HttpServlet
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
		 * Concept which the question file are associated to
		 */
		String concept; 

		/**
		 * Relative path to the question file of an older version of WOW!
		 */
		String testOldPath; 
		
		/**
		 * Name of the question file that will be the result of the 
		 * import to the current version
		 */
		String newQuestionsFileName;
		
		/**
		 * Indicates if the system must replace a existing file with the
		 * same name as the new created file
		 */
		boolean replace; 

		/**
		 * Login of the author
		 */
		String authorName;

		// Gets the parameters
		try {
			courseName = req.getParameter("courseName");
			concept = req.getParameter("concept");
			testOldPath = req.getParameter("testOldPath");
			newQuestionsFileName = req.getParameter("newQuestionsFileName");
			replace = Boolean.valueOf(req.getParameter("replace")).booleanValue();
			authorName = req.getParameter("authorName");
	
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
	
			// Checks if the source file exists
			if (QuestionFile.checkQuestionsFileOld(testOldPath))
			{
				// Checks if a question file with the same name exists already
				QuestionFile questionFile = new QuestionFile(courseName, newQuestionsFileName);
				boolean fileExists = questionFile.exists();
	
				if ((fileExists && replace) || !fileExists) {
					if (replace) {
						// Checks if the question file that will be deleted take part of 
						// another test not finished
						if (questionFile.checkQuestionsFileInTest()) {
							// Deletes the existing file and the associated images to it
							questionFile.deleteImageDirectory();
							questionFile.deleteQuestionsFile(authorName);

						} else {
							// The file cannot be replaced
							out.println("ERROR: The question File is in a test not finished for the students.");
							out.println("You cannot replace the question file.");
							out.println("If you want to replace the question file,");
							out.println("all the students must finish the test.");

							out.flush();
							out.close();
	
							return;
						}
					}
	
					// Creates the new question file of the current version of TestEditor
					questionFile.createDirectory();
					if (questionFile.importWOWQuestionsFile(concept, testOldPath, authorName))
						out.println("The file has been imported.");
					else
						out.println("ERROR: The file has NOT been imported.");
				} else {
					out.println(TestEditor.TEXT_ANOTHER_FILE_EXISTS);
					out.println("Do you want to replace it?");
				}
			} else {
				out.println("ERROR: The original file is not available to be imported.");
			}

			out.flush();
			out.close();

		} catch (Exception e) {
			UtilLog.writeException(e);
		}
	}
}