package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;
import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.TempFile;
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
 * NAME: ImportQuestionsFile 
 * FUNCTION: Receives a course name, the relative path to a question file
 * that belongs to older versions of WOW!, and the name of a new question file.
 * This servlet converts the question file from an older versi�n of WOW! to a
 * question file of the current version of the TestEditor tool.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class ImportQuestionsFile extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 * Root of the WOW system
	 */
	private static String wowRoot = WOWStatic.config.Get("WOWROOT");

	
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

		/**
		 * Test format to import from
		 */
		short theFormat;
		
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		try {
			// Gets the parameters
			courseName = req.getParameter("courseName");
			concept = req.getParameter("concept");
			testOldPath = wowRoot + req.getParameter("testOldPath");
			newQuestionsFileName = req.getParameter("newQuestionsFileName");
			replace = Boolean.valueOf(req.getParameter("replace")).booleanValue();
			authorName = req.getParameter("authorName");
			theFormat = Short.parseShort(req.getParameter("theFormat"));
	
			// Checks if the source file exists
			if (new File(testOldPath).exists()) {
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
					boolean result = questionFile.importQuestionsFile(concept, testOldPath, theFormat, authorName);
					if (result) {
						// Deletes the folder
						try {
							TempFile.deleteFolder(new File(testOldPath).getParentFile());
						} catch (Exception e)  {
							UtilLog.writeException(e);
						}
						out.println("The file has been imported");
					} else {
						out.println("ERROR: The file has NOT been imported.");
					}
				} else {
					out.println(TestEditor.TEXT_ANOTHER_FILE_EXISTS);
					out.println("Do you want to replace it?");
				}
			} else {
				out.println("ERROR: The source file is not available to be imported.");
			}

		} catch (Exception e) {
			UtilLog.writeException(e);
			out.println(e.getMessage());

		} finally {
			out.flush();
			out.close();
		}
	}
}