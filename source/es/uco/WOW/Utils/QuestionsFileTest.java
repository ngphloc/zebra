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
 * NAME: QuestionsFileTest
 * FUNCTION: This class encapsulates all the information about the 
 *  necessary data to manage a file of questions that take part of 
 *  a test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen 
 */
public class QuestionsFileTest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Relative path to the question file
	 */
	private String pathQuestionsFile = "";
	
	/**
	 * Name of the question file
	 */
	private String questionsFileName = "";
	
	/**
	 * Name of the concept associated to this question file
	 */
	private String concept = ""; 
	
	/**
	 * Vector object where in each position contains 
	 * a question code of this file that belongs to the test.
	 */
	private Vector codeQuestions = null; 
	
	/**
	 * This is a Vector object where in each position stores
	 * the answer to the question that is located in the same
	 * position in the 'codeQuestions' Vector. Possible values
	 * for each position are "correct", "incorrect", or
	 * "withoutAnswer".
	 */
	private Vector answersValues = null;
	
	/**
	 * This is a Vector object where in each position stores
	 * the time in seconds that the student has taken to answer 
	 * the question that is located in the same position in the 
	 * 'codeQuestions' Vector.
	 */
	private Vector times = null; 
	
	/**
	 * This is a Vector object where in each position stores
	 * a String value indicating if the question that is located 
	 * in the same position in the 'codeQuestions' Vector has 
	 * been showed in the test or not. Possible values are "true"
	 * and "false".
	 */
	private Vector done = null;
	
	/**
	 * This is a Vector object that stores in each position
	 * an WOWTestAnswers object that contains a String object 
	 * with a question code and a Vector object with the answers
	 * codes of the answers that the student has choose for that
	 * question.
	 */
	private Vector answers = null; 
	
	
	/**
	 * Return the relative path to this question file.
	 * @return String
	 */
	public String getPathQuestionsFile() {
		return pathQuestionsFile.trim();
	}

	/**
	 * Returns the name of the question file
	 * @return String
	 */
	public String getQuestionsFileName() {
		return questionsFileName.trim();
	}

	/**
	 * Returns the name of the concept associated to this question file
	 * @return String
	 */
	public String getConcept() {
		return concept.trim();
	}

	/**
	 * Returns the 'codeQuestions' Vector object with the question
	 * codes for this file.
	 * @return Vector
	 */
	public Vector getCodeQuestions() {
		return codeQuestions;
	}

	/**
	 * Returns the 'answersValues' Vector object with the codes of the
	 * answers for the questions of this file.
	 * @return Vector
	 */
	public Vector getAnswersValues() {
		return answersValues;
	}

	/**
	 * Returns a Vector object with the time in seconds that a student
	 * has taken to answer a question of the test.
	 * @return Vector
	 */
	public Vector getTimes() {
		return times;
	}

	/**
	 * Returns the 'done' Vector object with the questions that have
	 * been showed in the test.
	 * @return Vector
	 */
	public Vector getDone() {
		return done;
	}

	/**
	 * Returns the 'answers' Vector object that contains the
	 * WOWTestAnswers objects with the answers to the questions of this
	 * file.
	 * @return Vector
	 */
	public Vector getAnswers() {
		return answers;
	}

	/**
	 * Sets the relative path to this question file.
	 * @param pathQuestionsFile String
	 */
	public void setPathQuestionsFile(final String pathQuestionsFile) {
		this.pathQuestionsFile = pathQuestionsFile;
	}

	/**
	 * Sets the name of this question file.
	 * @param questionsFileName String
	 */
	public void setQuestionsFileName(final String questionsFileName) {
		this.questionsFileName = questionsFileName;
	}

	/**
	 * Sets the name of the concept that is associated to this question
	 * file.
	 * @param concept String
	 */
	public void setConcept(final String concept) {
		this.concept = concept;
	}

	/**
	 * Sets a Vector object with the code of the questions associated
	 * to this file.
	 * @param codeQuestions Vector
	 */
	public void setCodeQuestions(final Vector codeQuestions) {
		this.codeQuestions = codeQuestions;
	}

	/**
	 * Sets a Vector object with the code of the answers to the
	 * questions associated to this file.
	 * @param answersValues Vector
	 */
	public void setAnswersValues(final Vector answersValues) {
		this.answersValues = answersValues;
	}

	/**
	 * Sets a Vector object with the time in seconds that the student
	 * has taken to answer the questions associated to this file.
	 * @param timeVector Vector
	 */
	public void setTimes(final Vector timeVector) {
		this.times = timeVector;
	}

	/**
	 * Sets a Vector object with a value indicating if a question has
	 * been showed to a student or not
	 * @param doneVector Vector
	 */
	public void setDone(final Vector doneVector) {
		this.done = doneVector;
	}

	/**
	 * Sets a Vector of WOWTestAnswers with the answers to the
	 * questions of this file that the student has choose
	 * @param answers
	 */
	public void setAnswers(final Vector answers) {
		this.answers = answers;
	}

} // End of QuestionsFileTest class
