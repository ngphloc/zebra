package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.TestFile.TestFile;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: AddQuestionToTest 
 * FUNCTION: Servlet that receives the necessary data to
 * add a question of a file to a test, so the question take part of the test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class AddQuestionToTest extends HttpServlet
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
		 * Stores the name of the course that is on the question file
		 */
		String courseName; 
		/**
		 * Contains the name of the file that contains the question 
		 * that will be added to the test
		 */
		String questionsFileName; 
		/**
		 * Stores the code of the question that will be added to the test
		 */
		String codeQuestion;
		/**
		 * Stores the name of the test file that will be added a question
		 */
		String testFileName; 
		/**
		 * Stores a value that indicates if the test is a classic test or an
		 * adaptative one. Possible values are "classic" and "adaptive"
		 */
		String executionType; 
		
		// Get the parameters
		courseName = req.getParameter("courseName").trim();
		questionsFileName = req.getParameter("questionsFileName").trim();
		codeQuestion = req.getParameter("codeQuestion").trim();
		testFileName = req.getParameter("testFileName").trim();
		executionType = req.getParameter("executionType").trim();

		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		if (courseName != null
		 && questionsFileName != null
		 && codeQuestion != null
		 && testFileName != null
		 && executionType != null) {
			// Adds the question to the test file
			TestFile testFile = new TestFile(courseName, testFileName);
			String addQuestion = testFile.addQuestionToTest(questionsFileName, codeQuestion, executionType);
			
			if (addQuestion == null) {
				out.println("ERROR: The question has NOT been ADDED.");
				out.println("There was a problem with the server. Attempt it later.");
			} else {
				StringTokenizer token = new StringTokenizer(addQuestion, " ");
				String returnString = "";
				int i = 0;
				while (token.hasMoreElements()) {
					returnString = returnString.concat(token.nextElement() + " ");
					i++;
					if (i >= 9) {
						out.println(returnString);
						i = 0;
						returnString = "";
					}
				}
				if (returnString.length() > 0) {
					out.println(returnString);
				}
			}
		} else {
			out.println("ERROR: The question has NOT been ADDED.");
			out.println("There was a problem with the server.");
		}
		out.flush();
		out.close();
	}
}