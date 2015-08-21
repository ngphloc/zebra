package es.uco.WOW.Utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: WOW! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: TestLogStudent
 * FUNCTION: This is an class that stores some data about tests
 *  when they have been done by the student. These data are stored
 *  in the WOWStudent class.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez
 */
public class TestLogStudent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the test
	 */
	private String name = ""; 
	
	/**
	 * Name of the file who stores the test
	 */
	private String fileName = ""; 
	
	/**
	 * Relative path to the file who stores the test
	 */
	private String path = ""; 
	
	/**
	 * Number of times that the student has done this test
	 */
	private int make = 0; 
	
	/**
	 * Indicates if the test is finished or not
	 */
	private boolean finish = false; 
	
	/**
	 * Last score obtained the last time the test was done
	 */
	private double lastScore = 0; 
	
	/**
	 * Type of test ("classic" or "adaptive")
	 */
	private String executionType = "";
	
	/**
	 * Knowledge of the student in the test
	 */
	private double theta = 99999; 
	
	/**
	 * Standar error to calculate the knowledge of the student
	 */
	private double standardError = 99999; 
	
	/**
	 * Some data relative to the evaluation of the test
	 * Is a Vector object and in each position of that
	 * there is a EvalTestLogStudent object with information
	 * about a evaluation of the test.
	 */
	private Vector evaluatedTest = null;


	/**
	 * Sets the name of the test
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the file name who stores the test
	 * @param fileName String
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the relative path to the test file
	 * @param path String
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Sets the number of times the test has been done by the student 
	 * @param make int
	 */
	public void setMake(final int make) {
		this.make = make;
	}

	/**
	 * Set a value that indicates if the test is already finished
	 * by the student
	 * @param finish boolean
	 */
	public void setFinish(final boolean finish) {
		this.finish = finish;
	}

	/**
	 * Sets the score obtained by the student in his last execution
	 * of the test. 
	 * @param lastScore double
	 */
	public void setLastScore(final double lastScore) {
		this.lastScore = lastScore;
	}

	/**
	 * Sets the type of test. Possible values are
	 * "classic" for classic tests and "adaptive"
	 * for adaptive tests.
	 * @param executionType String
	 */
	public void setExecutionType(final String executionType) {
		this.executionType = executionType;
	}

	/**
	 * Sets the knowledge of the student for the test
	 * @param theta double
	 */
	public void setTheta(final double theta) {
		this.theta = theta;
	}

	/**
	 * Sets the value of the standar error
	 * to calculate the knowledge of the student 
	 * @param standardError double
	 */
	public void setStandardError(final double standardError) {
		this.standardError = standardError;
	}

	/**
	 * Sets a Vector object. In each position this object 
	 * contains a EvalTestLogStudent object with information
	 * about an evaluation of the test.
	 * @param evaluatedTest Vector
	 */
	public void setEvaluatedTest(final Vector evaluatedTest) {
		this.evaluatedTest = evaluatedTest;
	}

	/**
	 * Returns the name of the test
	 * @return String
	 */
	public String getName() {
		return name.trim();
	}

	/**
	 * Returns the name of the file who stores the test
	 * @return String
	 */
	public String getFileName() {
		return fileName.trim();
	}

	/**
	 * Returns the relative path to the file who stores 
	 * the test into the WOW! system
	 * @return String
	 */
	public String getPath() {
		return path.trim();
	}

	/**
	 * Returns the number of times that the test
	 * has been done by the student 
	 * @return int
	 */
	public int getMake() {
		return make;
	}

	/**
	 * Returns a boolean value indicating if the test
	 * has been done by the student or not
	 * @return boolean
	 */
	public boolean getFinish() {
		return finish;
	}

	/**
	 * Returns the score obtained by the student in the
	 * last execution of the test  
	 * @return double
	 */
	public double getLastScore() {
		return lastScore;
	}

	/**
	 * Returns the type of test 
	 * ("classic" or "adaptive")
	 * @return String
	 */
	public String getExecutionType() {
		return executionType.trim();
	}

	/**
	 * Returns the calculated knowledge for the
	 * student in this test
	 * @return double
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Returns the value of standar error used to calculate
	 * the knowledge of the student in the test 
	 * @return double
	 */
	public double getStandardError() {
		return standardError;
	}

	/**
	 * Returns a Vector object with information about
	 * all the executions and evaluation of the test by the student 
	 * @return vector
	 */
	public Vector getEvaluatedTest() {
		return evaluatedTest;
	}

} // End of TestLogStudent class