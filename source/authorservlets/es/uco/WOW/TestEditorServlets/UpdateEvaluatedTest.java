package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.TestLogStudent;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: UpdateEvaluatedTest 
 * FUNCTION: Receives a Student object and calls the 
 * upgradeDataEvaluatedTest method of the StudentFile object
 * to update the values of the test that is being done.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class UpdateEvaluatedTest extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
		throws ServletException, IOException {
	//{
		/**
		 * Current score of the Student during the test
		 */
		String score = null;

		// Gets the parameters
		ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
		ObjectOutputStream objectOutputStream = null;

		try {
			// Receives the parameters in Object format
			// DON'T MODIFY THE RECEPTION ORDER
			Student student = (Student) bufferIn.readObject();

			// Checks if the log file of the student exists
			StudentFile studentFile = new StudentFile(student.getCourse(), student.getFileName());
			if (studentFile.exists()) {
				TestLogStudent testLogStudent = null;
				EvalTestLogStudent evalTestLogStudent = null;
				testLogStudent = (TestLogStudent) student.getTest().firstElement();
				evalTestLogStudent = (EvalTestLogStudent) testLogStudent.getEvaluatedTest().firstElement();
				
				TestFile testFile = new TestFile(student.getCourse(), testLogStudent.getFileName());
				testFile.updateStudentTest(student.getLogin(), evalTestLogStudent.getScore(),
													evalTestLogStudent.getTheta(),
													evalTestLogStudent.getStandardError());

				score = studentFile.updateEvaluatedTest(student);
			}

			// Returns the score calculated by the engine or null in error case
			// DON'T MODIFY THE SENT ORDER
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(score);

		} catch (Exception e) {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(null);
			UtilLog.writeException(e);

		} finally {
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}
		}
	}
}