package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: BeginTest 
 * FUNCTION: Servlet that receives the necessary data to create
 * a new student log file if it doesn't exist already. This servlet
 * also obtains the necessary data of the questions to make the test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class BeginTest extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException 
	{
		/** 
		 * Contains the name of the course
		 */
		String courseName = "";
		/** 
		 * Contains the name of the test
		 */								
		String testFileName = "";
		/**
		 * Login of the student who makes the test
		 */
		String login = "";
		/**
		 * Indicates the type of test. Possible values:
		 * "classic" and "adaptative"
		 */
		String executionType = "";
		
		double theta = 99999;
		double standardError = 99999;
		ObjectOutputStream objectOutputStream = null;

		Student student = null;

		// Get the parameters
		courseName = req.getParameter("courseName").trim();
		testFileName = req.getParameter("testFileName").trim();
		login = req.getParameter("login").trim();
		executionType = req.getParameter("executionType").trim();
		theta = Double.valueOf(req.getParameter("theta").trim()).doubleValue();
		standardError = Double.valueOf(req.getParameter("standardError").trim()).doubleValue();

		// Obtains the data of the test
		if (courseName != null && testFileName != null && login != null && executionType != null) {
			student = configureTest(
								courseName, testFileName, login,
								executionType, theta, standardError);
		}

		// Return of the WOWStudent objet with the student and test data
		// or null in case of error.
		try {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(student);
		} catch (IOException e) {
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}

	/**
	 * Make all the necessary operations to prepare the execution of the test
	 * and obtain the result of it.
	 * @param courseName
	 * @param testFileName
	 * @param login
	 * @param executionType
	 * @param theta
	 * @param standardError
	 * @return Student
	 */
	private Student configureTest(
			final String courseName,
			final String testFileName,
			final String login,
			final String executionType,
			final double theta,
			final double standardError) 
	{
		StudentTest studentTest = new StudentTest();
		Student student = null;

		// Obtains the student data of the test file and the data of the test
		TestFile testFile = new TestFile(courseName, testFileName);
		Test test = testFile.getTest();
		studentTest = testFile.getStudentTest(login);

		if (studentTest == null) {
			// Adds a reference to the student in the test file
			if (!addStudentToTest(login, theta, standardError, testFileName, courseName)) {
				return null;
			} else {
				// Obtains the student data from the test file
				studentTest = testFile.getStudentTest(login);
			}
		}

		// Checks if the student log file exists
		// Create the file if not
		StudentFile studentFile = new StudentFile(courseName, login);
		if (!studentFile.exists()) {
			if (!createStudentFile(studentFile)) 
				return null;
		}

		// Checks the finish of the test
		if (studentTest.getFinish() == true) {
			// Checks if the test can be repeated
			if (studentTest.getCanRepeat() == true) {
				// Add or update the new test to be done in the student log file
				TestLogStudent testLogStudent = createTestLogStudent(test, executionType, theta, standardError);

				if (studentFile.addTest(testLogStudent)) {
					// Get the student and test data to be done
					student = studentFile.getStudentNotFinishedTest(testFileName, executionType);

					// Update the reference in the test to the student who makes it
					if (!addStudentToTest(login, theta, standardError, testFileName, courseName))
						return null;
				} else
					student = null;
			} else {
				return null;
			}
		} else {
			// Add or update the student log file with the new test execution
			TestLogStudent testLogStudent = createTestLogStudent(test, executionType, theta, standardError);

			if (studentFile.addTest(testLogStudent))
				// Obtains the student and test data to be done
				student = studentFile.getStudentNotFinishedTest(testFileName, executionType);
			else
				return null;
		}
		return student;
	}

	
	/**
	 * Adds a StudentTest object to the test that is going to be done, in case it doesn't exist. If
	 * it exists and it has finished the test, but the Student can repeat the test, then update its
	 * values, to allow that. Invocates the addStudentToTest method of the TestFile class.
	 * @param testFile
	 * @param login
	 * @param theta
	 * @param standardError
	 * @param testFileName
	 * @param courseName
	 * @return true if the process has been well done, other cases false
	 */
	private boolean addStudentToTest(
			final String login,
			final double theta,
			final double standardError,
			final String testFileName,
			final String courseName) 
	{
		StudentTest studentTest = new StudentTest();
		studentTest.setCanRepeat(false);
		studentTest.setFinish(false);
		studentTest.setLastScore(0.0);
		studentTest.setLogin(login);
		studentTest.setMake(0);
		studentTest.setTheta(theta);
		studentTest.setStandardError(standardError);
		return TestFile.addStudentToTest(courseName, testFileName, studentTest);
	}

	
	/**
	 * Creates a Student log file for the test
	 * @param studentFile
	 * @return true if the process has been well done, other cases false
	 */
	private boolean createStudentFile(final StudentFile studentFile) {
		// Creates the new Student log file
		Student student = new Student();
		student.setCourse(studentFile.getCourseName());
		student.setFileName(studentFile.getStudentFileName());
		student.setLogin(studentFile.getStudentFileName());
		student.setName(studentFile.getStudentFileName());
		return studentFile.createStudentFile(student);
	}

	
	/**
	 * Creates a TestLogStudent object and returns it
	 * @param test
	 * @param executionType
	 * @param theta
	 * @param standardError
	 * @return The new created TestLogStudent object
	 */
	private TestLogStudent createTestLogStudent(Test test, String executionType,
			double theta, double standardError) 
	{
		// Allocates memory for the TestLogStudent object
		TestLogStudent testLogStudent = new TestLogStudent();
		testLogStudent.setFileName(test.getTestFileName());
		testLogStudent.setFinish(false);
		testLogStudent.setLastScore(0.0);
		testLogStudent.setMake(0);
		testLogStudent.setName(test.getTestName());
		testLogStudent.setPath(test.getPath());
		testLogStudent.setExecutionType(executionType);
		testLogStudent.setTheta(theta);
		testLogStudent.setStandardError(standardError);

		// Allocates memory for the EvalTestLogStudent object
		EvalTestLogStudent evalTestLogStudent = new EvalTestLogStudent();

		Calendar calendar = Calendar.getInstance();
		evalTestLogStudent.setDate(calendar.get(Calendar.DAY_OF_MONTH) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.YEAR));

		evalTestLogStudent.setCorrect(0);
		evalTestLogStudent.setIncorrect(0);
		evalTestLogStudent.setWithoutAnswer(0);
		evalTestLogStudent.setScore(0.0);
		evalTestLogStudent.setTime(0);
		evalTestLogStudent.setTotalQuestion(test.getTotalQuestion());
		evalTestLogStudent.setTestVector(test.getTestVector());
		evalTestLogStudent.setTheta(theta);
		evalTestLogStudent.setStandardError(standardError);

		// Adds the EvalTestLogStudent object to the TestLogStudent object
		Vector evalTestLogStudentVector = new Vector();
		evalTestLogStudentVector.add(evalTestLogStudent);
		testLogStudent.setEvaluatedTest(evalTestLogStudentVector);

		return testLogStudent;
	}
}
