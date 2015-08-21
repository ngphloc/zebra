package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Test;

/**
 * <p>Title: Wow! TestEditor</p> 
 *	<p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 *	<p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: CreateTestFile 
 * FUNCTION: This servlet receives the following mandatory parameters:
 * - a Test object 
 * - a login of the author who makes the job
 * Creates a file with the configuration for a test execution.
 * Receives the confirmation of delete an existing file with the
 * same name in case it exists. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class CreateTestFile extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/**
		 * Indicates if the servlet must replace an existing file with
		 * the same name in case it exists
		 */
		boolean replace = true;
		
		/** 
		 * Indicates if all the Students contains in the existing
		 * file has already finish the test.
		 */
		boolean allStudentsFinished = true;
		
		/**
		 * Must the servlet checks if the test has Students
		 * that has not finished it?
		 */
		boolean checkFinishStudents = true;
		
		/**
		 * This students has a reference to the old test on their 
		 * log file and it has to be deleted.
		 */
		Vector deleteStudents = null;
		
		/**
		 * Type of the image file
		 */
		String fileType = "";
		
		/**
		 * Contains the image file
		 */
		byte[] data = null;
			
		/**
		 * Contains the login of the author that makes the job
		 */
		String authorName;
		
		/**
		 * The Test object that is received as parameter
		 */
		Test test;
		
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		try
		{
			// Get the parameters
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
			
			// Mandatory parameters
			test = (Test) bufferIn.readObject();	
			authorName = (String) bufferIn.readObject();

			try
			{
				// Optional parameters
				replace = bufferIn.readBoolean();
				checkFinishStudents = bufferIn.readBoolean();
				deleteStudents = (Vector) bufferIn.readObject();
			}
			catch (java.io.IOException ioe)
			{
				// Default values
				replace = true;
				checkFinishStudents = true;
				deleteStudents = null;
			}

			try
			{
				// Optional parameters
				fileType = ((String) bufferIn.readObject()).toLowerCase();

				// Get the image file bytes
				data = new byte[bufferIn.readInt()];
				if (data.length > 0) 
					bufferIn.readFully(data);
			}
			catch (java.io.IOException ioe)
			{
				// Default values
				fileType = null;
				data = null;
			}
			catch (java.lang.NullPointerException npe)
			{
				fileType = null;
				data = null;
			}

			// Checks wether a file with the same name exists
			TestFile testFile = new TestFile(test.getCourse(), test.getTestFileName());
			boolean createFile = testFile.exists();
			
			if (createFile == false || replace == true)
			{
				String pathBackgroundOld = test.getBackground();

				if (replace == true)
				{
					Vector studentTestVector = test.getStudentVector();
					if (studentTestVector == null || studentTestVector.isEmpty())
					{
						if (checkFinishStudents == true)
						{
							// Checks if there are some students to finish the test
							// In affirmative case, the existing test file won't be delete
							allStudentsFinished = testFile.checkStudentFinishTest();
						}
					}

					if (allStudentsFinished == true)
					{
						// Deletes the old Test file
						testFile.deleteTestFile(test.getBackground().trim().equals(""), deleteStudents, authorName);

						// Creates the new one
						createFile = testFile.createTestFileA(test, authorName);

						if (createFile == true)
						{
							// Saves the associated image to the test, in case
							// the servlet has received it as parameter
							if (fileType != null && fileType.trim().equals("") == false
									&& data != null && data.length > 0)
							{
								TestFile.deleteBackgroundTest(pathBackgroundOld);
								createFile = testFile.saveBackground(fileType, data);
							}
							else
							{
								if (test.getBackground().equals("") == false)
								{
									createFile = TestFile.copyBackground(pathBackgroundOld, test.getBackground());
								}
							}

							if (createFile == true)
								out.println(TestEditor.TEXT_TESTFILE_CREATED);
							else
							{
								out.println(TestEditor.TEXT_TESTFILE_CREATED);
								out.println("but the test background HAS NOT BEEN STORED correctly.");
							}
						}
						else
							out.println("ERROR: Test file has NOT been created");
					}
					else
					{
						out.println("ERROR: There are students that has not finished the test.");
						out.println("You cannot replace the test.");
						out.println("If you want to replace the test,");
						out.println("all the students must finish this test");
					}
				}
				else
				{
					// Creates the new file
					createFile = testFile.createTestFileA(test, authorName);

					if (createFile == true)
					{
						// Saves the associated image to the test, in case it exists	
						if (fileType != null && data != null && data.length > 0
								&& fileType != null && fileType.trim().equals("") == false)
						{
							TestFile.deleteBackgroundTest(pathBackgroundOld);
							createFile = testFile.saveBackground(fileType, data);
						}
						else
						{
							if (test.getBackground().equals("") == false)
							{
								createFile = TestFile.copyBackground(pathBackgroundOld,test.getBackground());
							}
						}

						if (createFile == true)
							out.println(TestEditor.TEXT_TESTFILE_CREATED);
						else
						{
							out.println(TestEditor.TEXT_TESTFILE_CREATED);
							out.println("but the test background HAS NOT BEEN STORED correctly.");
						}
					}
					else
						out.println("ERROR: Test file has NOT been created");
				}
			} else {
				out.println(TestEditor.TEXT_ANOTHER_FILE_EXISTS);
				out.println("Do you want to replace it?");
			}
		}
		catch (Exception e)
		{
			out.println("ERROR: The test file has NOT been created. An exception has occur in the server.");
		}
		
		out.flush();
		out.close();
	}
}