package es.uco.WOW.Utils;

import java.io.Serializable;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: StudentTest
 * FUNCTION: This class contains all the data to manage the information 
 *  about one test and a student.
 * LAST MODIFICATION: 06-02-2008 
 *
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class StudentTest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Student's login
	 */
	private String login = "";
	
	/**
	 * Number of times the student has done the test
	 */
	private int make = 0; 
	
	/**
	 * Indicates if the student can repeat the test or not
	 */
	private boolean canRepeat = true;
	
	/**
	 * Score of the student on the last execution of the test
	 */
	private double lastScore = -1;
	
	/**
	 * Indicates if the student has already finish the test
	 */
	private boolean finish = false;
	
	/**
	 * Knowledge of the student in the test
	 */
	private double theta = 99999;
	
	/**
	 * Standar error used to calculate the knowledge of the student
	 */
	private double standardError = 99999; 

	
	/**
	 * Returns a boolean value indicating if the student
	 * can repeat the test or not.
	 * @return boolean
	 */
	public boolean getCanRepeat() {
		return canRepeat;
	}

	/**
	 * Returns a boolean value indicating if the student
	 * has finished the test or not.
	 * @return boolean
	 */
	public boolean getFinish() {
		return finish;
	}

	/**
	 * Returns the score obtained by the student
	 * in his last execution of the test.
	 * @return double
	 */
	public double getLastScore() {
		return lastScore;
	}

	/**
	 * Returns the student's login
	 * @return the string of student's login
	 */
	public String getLogin() {
		return login.trim();
	}

	/**
	 * Returns the number of times that 
	 * the student has done the test
	 * @return int
	 */
	public int getMake() {
		return make;
	}

	/**
	 * Returns the standar error used to calculate 
	 * the knowledge of the student in the test
	 * @return double
	 */
	public double getStandardError() {
		return standardError;
	}

	/**
	 * Returns the knowledge of the studen
	 * in this test.
	 * @return double
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Sets if the student can repeat the test
	 * or not.
	 * @param b boolean
	 */
	public void setCanRepeat(final boolean b) {
		canRepeat = b;
	}

	/**
	 * Sets if the student has finished
	 * already the test or not.
	 * @param b boolean
	 */
	public void setFinish(final boolean b) {
		finish = b;
	}

	/**
	 * Sets the last score obtained by the student
	 * in his last execution of the test.
	 * @param d double
	 */
	public void setLastScore(final double d) {
		lastScore = d;
	}

	/**
	 * Sets the login of the student
	 * @param s String
	 */
	public void setLogin(final String s) {
		login = s;
	}

	/**
	 * Sets the number of times that 
	 * the student has done the test.
	 * @param i int
	 */
	public void setMake(final int i) {
		make = i;
	}

	/**
	 * Sets the standar error used to calculate
	 * the knowledge of the student.
	 * @param d double
	 */
	public void setStandardError(final double d) {
		standardError = d;
	}

	/**
	 * Sets the knowledge of the student in the test
	 * @param d double
	 */
	public void setTheta(final double d) {
		theta = d;
	}

} // End of StudentTest class  