package es.uco.WOW.Utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: Course
 * FUNCTION: This class contains all the data to manage the information 
 *  about a course. Contains the name of the course and the names of the
 *  concept files and question files associated to it.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the course 
	 */
	private String name = "";
	
	/**
	 * This is a list with the names of *all* the not abstract concepts
	 * of this course.
	 */
	private Vector conceptVector = null;
	
	/**
	 * This is a list with the names of the not abstract concepts
	 * of this course that are from type *test*
	 */
	//private Vector testConceptVector = null; 
	
	/**
	 * This is a list with the names of the abstracts concepts
	 * of this course.
	 */
	private Vector abstractConceptVector = null; 
	
	/**
	 * This is a list with the name of the question files
	 * of this course.
	 */
	private Vector questionsFileVector = null; 
	
	/**
	 * This is a list with the default number of answers that
	 * each question file has.
	 */
	private Vector numAnswersOfQuestionsFile = null;
	
	/**
	 * This is a list where in each position stores a String value
	 * with the number of questions that each question file has. 
	 */
	private Vector numQuestionsOfFile = null; 
	
	/**
	 * This is a list where in each position stores the name
	 * of a test file.
	 */
	private Vector testFileVector = null;

//	/**
//	 * @return Returns the testConceptVector.
//	 */
//	public final Vector getTestConceptNames() {
//		return testConceptVector;
//	}
//
//	/**
//	 * @param testConceptVector The testConceptVector to set.
//	 */
//	public final void setTestConceptNames(final Vector list) {
//		this.testConceptVector = list;
//	}

	/**
	 * Returns the name of the course.
	 * @return String
	 */
	public String getName() {
		return name.trim();
	}

	/**
	 * Returns a list with the names of the concepts of this course.
	 * @return Vector
	 */
	public Vector getConceptNames() {
		return conceptVector;
	}

	/**
	 * Returns a list with the names of the abstracts concepts of this course.
	 * @return Vector
	 */
	public Vector getAbstractConceptNames() {
		return abstractConceptVector;
	}

	/**
	 * Returns a list with the name of the question files of this course.
	 * @return Vector
	 */
	public Vector getQuestionsFileNames() {
		return questionsFileVector;
	}

	/**
	 * Returns a list with the default number of answers that each question file has.
	 * @return Vector
	 */
	public Vector getNumAnswersOfQuestionsFile() {
		return numAnswersOfQuestionsFile;
	}

	/**
	 * Returns a list where in each position stores a String value with the number of questions that
	 * each question file has.
	 * @return Vector
	 */
	public Vector getNumQuestionsOfFile() {
		return numQuestionsOfFile;
	}

	/**
	 * Returns a list where in each position stores the name of a test file.
	 * @return Vector
	 */
	public Vector getTestsFileNames() {
		return testFileVector;
	}

	/**
	 * Sets the name of the course.
	 * @param theName String
	 */
	public void setName(final String theName) {
		this.name = theName;
	}

	/**
	 * Sets a list with the names of the concepts of this course.
	 * @param aConceptVector Vector
	 */
	public void setConceptNames(final Vector aConceptVector) {
		this.conceptVector = aConceptVector;
	}

	/**
	 * Sets a list with the names of the abstract concepts
	 * of this course.
	 * @param list Vector
	 */
	public void setAbstractConceptNames(final Vector list) {
		this.abstractConceptVector = list;
	}

	/**
	 * Sets a list with the name of the question files
	 * of this course.
	 * @param list Vector
	 */
	public void setQuestionsFileNames(final Vector list) {
		this.questionsFileVector = list;
	}

	/**
	 * Sets a list with the default number of answers that each question file has.
	 * @param list Vector
	 */
	public void setNumAnswersOfQuestionsFile(final Vector list) {
		this.numAnswersOfQuestionsFile = list;
	}

	/**
	 * Sets a list where in each position stores a String value with the number of questions that
	 * each question file has.
	 * @param list Vector
	 */
	public void setNumQuestionsOfFile(final Vector list) {
		this.numQuestionsOfFile = list;
	}

	/**
	 * Sets a list where in each position stores the name of a test file.
	 * @param list Vector
	 */
	public void setTestsFileNames(final Vector list) {
		this.testFileVector = list;
	}

} // End of Course class