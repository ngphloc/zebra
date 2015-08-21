package es.uco.WOW.Utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: Student
 * FUNCTION: This class contains the necessary data to manage
 *  all the information about the log file of a student.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Student's login.
	 */
	private String login = "";
	
	/**
	 * Name or login of the student.
	 */
	private String name = ""; 
	
	/**
	 * Name of the student's log file
	 */
	private String fileName = ""; 
	
	/**
	 * Relative path to the student's log file
	 */
	private String path = ""; 
	
	/**
	 * Name of the course where this file is located
	 */
	private String course = ""; 
	
	/**
	 * Total number of classic tests done by the student 
	 */
	private int totalClassicTest = 0; 
	
	/**
	* Total number of adaptive tests done by the student 
	*/
	private int totalAdaptiveTest = 0; 
	
	/**
	 * This is a list where each position stores a TestLogStudent
	 * object with information about all the tests done by the student
	 */
	private Vector test = null;
	
	
	/**
	 * Returns the name of the course where this file is located
	 * @return String
	 */
	public String getCourse() {
		return course.trim();
	}

	/**
	 * Returns the name of the file of the student 
	 * @return String
	 */
	public String getFileName() {
		return fileName.trim();
	}

	/**
	 * Returns the login of the student
	 * @return String
	 */
	public String getLogin() {
		return login.trim();
	}

	/**
	 * Returns the name of the student
	 * @return String
	 */
	public String getName() {
		return name.trim();
	}

	/**
	 * Returns the relative path to this file
	 * @return String
	 */
	public String getPath() {
		return path.trim();
	}

	/**
	 * Returns a Vector object with a list 
	 * of TestLogStudent objects that contains which
	 * test the student has done. 
	 * @return Vector
	 */
	public Vector getTest() {
		return test;
	}

	/**
	 * Returns the total number of adaptive
	 * test that the student has done 
	 * @return int
	 */
	public int getTotalAdaptiveTest() {
		return totalAdaptiveTest;
	}

	/**
	 * Returns the total number of classic
	 * test that the student has done
	 * @return int
	 */
	public int getTotalClassicTest() {
		return totalClassicTest;
	}

	/**
	 * Sets the course where this file is located
	 * @param string
	 */
	public void setCourse(final String string) {
		course = string;
	}

	/**
	 * Sets the name of the file where this
	 * information will be stored
	 * @param string
	 */
	public void setFileName(final String string) {
		fileName = string;
	}

	/**
	 * Sets the student's login
	 * @param string
	 */
	public void setLogin(final String string) {
		login = string;
	}

	/**
	 * Sets the student's name
	 * @param string
	 */
	public void setName(final String string) {
		name = string;
	}

	/**
	 * Sets the relative path to this file
	 * @param string
	 */
	public void setPath(final String string) {
		path = string;
	}

	/**
	 * Sets a Vector with information about
	 * the test that this student has done
	 * @param vector
	 */
	public void setTest(final Vector vector) {
		test = vector;
	}

	/**
	 * Sets the total number of adaptive
	 * test that this student has done 
	 * @param i
	 */
	public void setTotalAdaptiveTest(final int i) {
		totalAdaptiveTest = i;
	}

	/**
	 * Sets the total number of classic
	 * test that this student has done
	 * @param i
	 */
	public void setTotalClassicTest(final int i) {
		totalClassicTest = i;
	}

} // End of Student class