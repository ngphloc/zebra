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
 * NAME: CreateQuestionsFile. 
 * FUNCTION: This servlet receives the following parameters:
 * - a course name,
 * - a concept 
 * - a question file name,  
 * - the number of responses for all the questions of this file,
 * - a boolean value indicating if it must create a new file if it exists
 * 
 * This servlet creates an empty question file in the course
 * with the name passed as argument, and with the name of response
 * for each question of that file.
 * 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class CreateQuestionsFile extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * Name of the course where the 
		 * question file will be created
		 */
		String courseName; 
		
		/**
		 * Name of the concept which the questions
		 * file will be associated to
		 */
		String concept;
		
		/**
		 * Name of the question file that will be created
		 */
		String questionsFileName; 
		
		/**
		 * Number of questions which the questions of the
		 * new file created will contain
		 */
		int numAnswers; 
		
		/**
		 * Indicates if the servlet must replace an existing
		 * file with the same name or not.
		 */
		boolean replace;
		
		/**
		 * Login of the author that makes the job
		 */
		String authorName;
		
		// Get the parameters
		courseName = req.getParameter("courseName");
		concept = req.getParameter("concept");
		questionsFileName = req.getParameter("questionsFileName");
		numAnswers = Integer.valueOf(req.getParameter("numAnswers")).intValue();
		replace = Boolean.valueOf(req.getParameter("replace")).booleanValue();
		authorName = req.getParameter("authorName");

//		try {
//			ProfileDB pdb = WOWStatic.DB().getProfileDB();
//			long id = pdb.findProfile(authorName);
//	      if (id == 0) {
//	      	UtilLog.toLog("Profile not found de " + authorName);
//	      } else {
//	      	Profile profile = pdb.getProfile(id);
//	      	String value = profile.getAttributeValue("personal", "course");
//	      	UtilLog.toLog("Curso=" + value);
//	      	value = profile.getAttributeValue("personal", "directory");
//	      	UtilLog.toLog("Directorio=" + value);
//	      }
//		} catch (Exception e) {
//			UtilLog.writeException(e);
//		}
		
		
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		// Checks wether a file with the same name exists or not
		QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
		boolean createFile = questionFile.exists();

		if (createFile == false || replace == true)
		{
			if (replace == true)
			{
				// Checks if the question file that will be deleted
				// takes part of another not finished test
				boolean checkQuestionsFile = questionFile.checkQuestionsFileInTest();
				if (checkQuestionsFile == true) {
					// Deletes the file and the associated images
					// for creating it again
					questionFile.deleteImageDirectory();
					questionFile.deleteQuestionsFile(authorName);
				}
				else
				{
					out.println("ERROR: The question file is in a test not "
									+ "finished for the students.");
					out.println("You cannot replace the question File.");
					out.println("If you want to replace it, all the students");
					out.println("must finish the test.");

					out.flush();
					out.close();

					return;
				}
			}
			createFile = questionFile.createQuestionsFile(concept, numAnswers, authorName);
			if (createFile)
				out.println(TestEditor.TEXT_FILE_CREATED);
			else
				out.println("ERROR: File has NOT been created");
		}
		else
		{
			out.println(TestEditor.TEXT_ANOTHER_FILE_EXISTS);
			out.println("Do you want to replace it?");
		}
		out.flush();
		out.close();
	}
}